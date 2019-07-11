package com.study.rabbit.controller;

import com.study.rabbit.componet.MQSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @Autowired
    MQSend mqSend;

    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public String getDepartment(@PathVariable String id){

        mqSend.send("goods."+id);
        return "sucess";
    }



}
