package com.whitedwaft.devservice.services;

import com.google.gson.Gson;
import com.whitedwaft.devservice.dao.LogConfigDao;
import com.whitedwaft.devservice.dao.LogPatternDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;


@Aspect
@Component
public class LogService {

    LogConfigDao config = new LogConfigDao();

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    public LogConfigDao readConfig(String configFileLocation)
    {
        try (InputStream input = new FileInputStream(configFileLocation)) {

            Properties prop = new Properties();
            prop.load(input);
            config.pattern = prop.getProperty("log.pattern");
            config.size = prop.getProperty("log.size");
            config.format = config.fromStringToFormat(prop.getProperty("log.format"));
            config.level = config.fromStringToLevel(prop.getProperty("log.level"));
            return config;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public File createLogFile(String logDirectory, LogConfigDao config) throws IOException
    {
        File logDir = new File(logDirectory);
        if (!logDir.exists())
            logDir.mkdir();
        int count = (int) Arrays.asList(logDir.list()).stream().filter(x -> x.contains(config.pattern)).count();
        if (count == 0) {
            File log = new File(logDirectory + File.separator + config.pattern + 1);
            log.createNewFile();
            return log;
        }
        else
        {
            if (new File(logDirectory+File.separator+Arrays.asList(logDir.list()).stream().filter(x -> x.contains(config.pattern)).max((String x1,String x2) -> (x1.compareTo(x2))).get()).length() / 1024 > Integer.parseInt(config.size))
            {
                File log = new File(logDirectory + File.separator + config.pattern + (count + 1));
                log.createNewFile();
                return log;
            }
            else
            {
                File log = new File(logDirectory + File.separator + config.pattern + count);
                log.createNewFile();
                return log;
            }
        }
    }
    @AfterReturning(pointcut = "springBeanPointcut()", returning = "result")
    @AfterThrowing(pointcut = "springBeanPointcut()",throwing = "result")
    public String createLog(JoinPoint jp, Object result) throws Throwable {
        LogConfigDao config = readConfig("C:\\Users\\akors\\Desktop\\Dev-Service\\config.properties");
        File logFile = createLogFile("C:\\Users\\akors\\Desktop\\Dev-Service",config);
        String input = "";
        String src = jp.getSignature().toLongString();

        LogPatternDao log = new LogPatternDao(config.level,"result.toString()", LocalDateTime.now().toString(),src);
        FileWriter fw1 = new FileWriter(logFile,true);

        switch (config.format)
        {
            case XML:
                input = createXML(log)+"\n";
                break;
            case JSON:
                input = createJson(log)+"\n";
                break;
        }
        fw1.append(input);
        fw1.close();
        return input;
    }

    public String createJson(LogPatternDao logPattern)
    {
        Gson gson= new Gson();
        return gson.toJson(logPattern);
    }
    public String createXML(LogPatternDao logPattern) throws JAXBException
    {
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(LogPatternDao.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(logPattern,writer);
        return writer.toString();
    }
}
