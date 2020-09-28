package com.whitedwaft.devservice.controllers;

import com.whitedwaft.devservice.dao.LogConfigDao;
import com.whitedwaft.devservice.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

@RestController
public class TestController {

//    @Autowired
//    private LogService logService;

    @GetMapping("/log")
    public ResponseEntity makeNewLog() throws IOException, JAXBException {
//        LogConfigDao config = logService.readConfig("C:\\Users\\akors\\Desktop\\Dev-Service\\config.properties");
//        File logFile = logService.createLogFile("C:\\Users\\akors\\Desktop\\Dev-Service",config);
        return  new ResponseEntity(HttpStatus.OK);
    }
}
