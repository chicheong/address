package com.wongs.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/state")
public class StateWebController {

	private static final Logger log = LoggerFactory.getLogger(StateWebController.class);
	
	@Autowired StateService stateService;
	
	@RequestMapping(value={"/"})
	ModelAndView state(){
		log.info("State home");
		
		ModelAndView modelAndView = new ModelAndView("web/state/home", "object", null);
		
		return modelAndView;
	}
	
    @RequestMapping("*")
    @ResponseBody
    public String fallbackMethod(){
        return "fallback method"; //return custom 404
    }
	
}
