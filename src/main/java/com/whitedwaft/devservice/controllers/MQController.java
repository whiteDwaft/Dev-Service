package com.whitedwaft.devservice.controllers;

import com.whitedwaft.devservice.dao.MQConfigDao;
import com.whitedwaft.devservice.repositories.MQConfigRepository;
import com.whitedwaft.devservice.services.MQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.io.IOException;
import java.net.SocketTimeoutException;

@RestController
public class MQController {

    @Autowired
    private MQService mqService;

    @Autowired
    private MQConfigRepository mqConfigRepository;

    @PostMapping("/send_message")
    public ResponseEntity sendMessage(@RequestBody MQConfigDao configs)
    {
        mqService.setUp(configs);
        mqService.sendNewMesssage(configs);
        configs.setTime(String.valueOf(System.currentTimeMillis()));
        mqConfigRepository.save(configs);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/consume_message")
    public ResponseEntity consumeMessage(@RequestBody MQConfigDao configs) throws JMSException {
        mqService.setUp(configs);
        return new ResponseEntity(mqService.consumeMessages(),HttpStatus.OK);
    }
    @GetMapping("/get_history")
    public ResponseEntity getHistory()
    {
        try {
            return new ResponseEntity(mqConfigRepository.findTop5ByOrderByTimeAsc(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
