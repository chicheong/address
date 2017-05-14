package com.wongs.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/country")
public class CountryWebController {

	private static final Logger log = LoggerFactory.getLogger(CountryWebController.class);
	
	@Autowired CountryService countryService;
	
	@RequestMapping(value={"/"})
	ModelAndView country(){
		log.info("Country home");
		
		ModelAndView modelAndView = new ModelAndView("web/country/home", "object", null);
		
		return modelAndView;
	}
	
    @RequestMapping("*")
    @ResponseBody
    public String fallbackMethod(){
        return "fallback method"; //return custom 404
    }
	
}
