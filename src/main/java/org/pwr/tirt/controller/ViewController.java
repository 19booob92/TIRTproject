package org.pwr.tirt.controller;

import org.pwr.tirt.model.Schedule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String mainPage(Model model) {
            model.addAttribute("schedule", new Schedule());
        return "main";
    }
    
}
