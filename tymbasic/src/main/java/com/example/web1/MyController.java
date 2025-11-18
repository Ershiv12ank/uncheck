package com.example.web1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
	
	@GetMapping("/home")
	public String welcome(Model m) {
		m.addAttribute("Student","This is welocme page injoy your home man");
		return "kodnest";
		
	}

}
