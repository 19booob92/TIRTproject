package org.pwr.tirt.controller;

import org.pwr.tirt.service.ConnectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchController {

    @Autowired
    ConnectionProcessor connectionProcessor;
    
    @RequestMapping("/fetchHtml")
    @ResponseBody
    public String getHtml() {
        return connectionProcessor.fetchData();
    }
    
}
