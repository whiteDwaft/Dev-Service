package com.whitedwaft.devservice.services;

import javax.jms.*;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.whitedwaft.devservice.dao.MQConfigDao;
import com.whitedwaft.devservice.repositories.MQConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQService {

    // System exit status value (assume unset value to be 1)
    private static int status = 1;

    // Create variables for the connection to MQ
    private static final String HOST = "qm1-c7ac.qm.eu-gb.mq.appdomain.cloud"; // Host name or IP address
    private static final int PORT = 30869; // Listener port for your queue manager
    private static final String CHANNEL = "CLOUD.APP.SVRCONN"; // Channel name
    private static final String QMGR = "QM1"; // Queue manager name
    private static final String APP_USER = "demoapp"; // User name that application uses to connect to MQ
    private static final String APP_PASSWORD = "QM9Na-ueyp2k2lcJYKQNkmtDpT49m61pUcy8phHdnJ4t"; // Password that the application uses to connect to MQ
    private static final String QUEUE_NAME = "DEV.QUEUE.4"; // Queue that the application uses to put and get messages to and from
        // Variables
        JMSContext context = null;
        Destination destination = null;
        JMSProducer producer = null;
        JMSConsumer consumer = null;

    @Autowired
    private MQConfigRepository mqConfigRepository;

    public void setUp(MQConfigDao configs)
    {
        try
        {
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, configs.getHost());
            cf.setIntProperty(WMQConstants.WMQ_PORT, Integer.parseInt(configs.getPort()));
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, configs.getChannel());
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, configs.getQmgr());
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(WMQConstants.USERID, configs.getApp_user());
            cf.setStringProperty(WMQConstants.PASSWORD, configs.getApp_password());

            context = cf.createContext();
            destination = context.createQueue("queue:///" + configs.getQueue_name());
        }
        catch (JMSException jmsex)
        {
        recordFailure(jmsex);
        }

    }

    public void sendNewMesssage(MQConfigDao configs) {

        TextMessage message = context.createTextMessage(configs.getMessage());
        producer = context.createProducer();
        producer.send(destination, message);
    }
    public String consumeMessages() throws JMSException {
        consumer = context.createConsumer(destination);
        Message mes = consumer.receive();
        return mes.getBody(String.class);

    }

    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                System.out.println(ex);
            }
        }
        System.out.println("FAILURE");
        status = -1;
        return;
    }
    private static void processJMSException(JMSException jmsex) {
        System.out.println(jmsex);
        Throwable innerException = jmsex.getLinkedException();
        if (innerException != null) {
            System.out.println("Inner exception(s):");
        }
        while (innerException != null) {
            System.out.println(innerException);
            innerException = innerException.getCause();
        }
        return;
    }

}

