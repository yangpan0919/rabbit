package com.study.rabbit.controller;

import com.study.rabbit.componet.MQSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @Autowired
    MQSend mqSend;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getDepartment(){

        mqSend.send("First");
        return "sucess";
    }



}
