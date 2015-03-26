package org.pwr.tirt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	@RequestMapping(value = "/")
	public String mainPage() {
		return "redirect:schedule.html";
	}
}
