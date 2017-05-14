package com.wongs.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/address")
public class AddressWebController {

	private static final Logger log = LoggerFactory.getLogger(AddressWebController.class);
	
	@Autowired AddressService addressService;
	
	@RequestMapping(value={"/"})
	ModelAndView home(){
		log.info("Address home");
		
		ModelAndView modelAndView = new ModelAndView("web/address/home", "object", null);
		
		return modelAndView;
	}
	
    @RequestMapping("*")
    @ResponseBody
    public String fallbackMethod(){
        return "fallback method"; //return custom 404
    }
	
}
