package com.wongs.address;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/country")
public class CountryRestController {

	private static final Logger log = LoggerFactory.getLogger(CountryRestController.class);
	
	@Autowired CountryService countryService;
    
    //------------------- Handlers for Web AJAX starts ----------------------
    /**
     * Display Country list
     * @param rows
     * @param page
     * @return
     */
	@RequestMapping(value={"/itemList"}, params={"rows","page"})
	public Map<String, Object> itemList(
			@RequestParam(value = "rows") int rows, @RequestParam(value = "page") int page, HttpServletRequest request
		){
		log.debug("rows: " + rows);
		log.debug("page: " + page);
		
		/**
		 * filtering
		 */
		Map<String, Object> returnList = new HashMap<String, Object>();
		List<Map<String, Object>> returnRows = new ArrayList<Map<String, Object>>();
		
		Page<Country> pageCountry = null;
		
		String filterRules = request.getParameter("filterRules");
		if (!(StringUtils.isEmpty(filterRules) || "[]".equalsIgnoreCase(filterRules))){
			log.debug("filterRules: " + filterRules);
			Country country = new Country();
			//Replace square brackets []
			filterRules = filterRules.replaceAll("^\\[|\\]$", "");
			//Split into objects
			String[] objectList = filterRules.split("(?=.*\\}),(?=\\{.*)", -1);
			
	        for (int i=0; i<objectList.length; i++){
	        	String object = objectList[i];
	        	object = object.replaceAll("^\\{|\\}$", "");
	        	String[] itemList = object.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
	        	
	        	String field = "", op = "", fieldValue = "";
	        	for (int j=0; j<itemList.length; j++){
	        		String item = itemList[j];
	        		
		        	String[] valueList = item.split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		        	log.debug(valueList[0] + ": " + valueList[1]);
		        	String paramName = valueList[0].replaceAll("^\"|\"$", "");
		        	String paramValue = valueList[1].replaceAll("^\"|\"$", "");
		        	
					
		        	if ("field".equalsIgnoreCase(paramName)){
		        		field = paramValue;
		        	} else if ("op".equalsIgnoreCase(paramName)){
		        		op = paramValue;
		        	} else if ("value".equalsIgnoreCase(paramName)){
		        		fieldValue = paramValue;
		        	}
	        	}
		        if ("countryId".equalsIgnoreCase(field)){
		        	country.setCountryId(fieldValue);
		        } else if ("code".equalsIgnoreCase(field)){
		        	country.setCode(fieldValue);
		        } else if ("num".equalsIgnoreCase(field)){
		        	country.setNum(fieldValue);
		        } else if ("name".equalsIgnoreCase(field)){
		        	country.setName(fieldValue);
		        }
		        
	        }
	        Pageable pageable = new PageRequest(page-1, rows);
	        pageCountry = this.countryService.findByAllANDLike(country, pageable);
		} else {

			Pageable pageable = new PageRequest(page-1, rows);
			pageCountry = this.countryService.findAll(pageable);

		}

		if (pageCountry != null){
			List<Country> listCountry = pageCountry.getContent();
			
			for (int i = 0; i < listCountry.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Country country = listCountry.get(i);
				
				map.put("countryId", country.getCountryId());
				map.put("code", country.getCode());
				map.put("num", country.getNum());
				map.put("name", country.getName());
				returnRows.add(map);
			}
			returnList.put("total", pageCountry.getTotalElements());
			returnList.put("rows", returnRows);
		}else{
			returnList.put("total", 0);
			returnList.put("rows", returnRows);
		}
		
		return returnList;
	}
	
	
	/**
	 * Add, update, delete Country
	 * @param type
	 * @return
	 */
	@RequestMapping(value={"/itemList"}, params={"data"})
	public Map<String, Object> itemList(
			@RequestParam(value = "data") String data, HttpServletRequest request
		){
		Map<String, Object> output = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer();
		
		log.debug("data: " + data);
		Pattern objectPattern = Pattern.compile("\\{([^}]*)\\}");
		
		Matcher matcher = objectPattern.matcher(data);
	    while (matcher.find()) {
	        String objectList = matcher.group(1);
	        String[] parameterList = objectList.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
	        
	        String type = "", countryId = "", code = "", num = "", name = "";
	        for (int i=0; i<parameterList.length; i++){
	        	String parameter = parameterList[i];
	        	String[] valueList = parameter.split(":(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
	        	log.debug(valueList[0] + " :" + valueList[1]);
	        	
	        	String paramName = valueList[0].replaceAll("^\"|\"$", "");
	        	String paramValue = valueList[1].replaceAll("^\"|\"$", "");
	        	//Four backslashes will be deducted to two
	        	paramValue = paramValue.replaceAll("\\\\{4}", "\\\\\\\\");
	        	if ("type".equalsIgnoreCase(paramName)){
	        		type = paramValue;
	        	} else if ("countryId".equalsIgnoreCase(paramName)){
	        		countryId = paramValue;
	        	} else if ("code".equalsIgnoreCase(paramName)){
	        		code = paramValue;
	        	} else if ("num".equalsIgnoreCase(paramName)){
	        		num = paramValue;
	        	} else if ("name".equalsIgnoreCase(paramName)){
	        		name = paramValue;
	        	} 
	        }
	        
	        if ("add".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(countryId)){
					Country country = new Country();
					try {
						if (this.countryService.get(countryId) == null){
							country.setCountryId(countryId);
							country.setCode(code);
							country.setNum(num);
							country.setName(name);
							this.countryService.add(country);
							message.append(MessageFormat.format("Add Success. Country with countryId: {0} is added.<br />", countryId));
						} else {
							message.append(MessageFormat.format("Add failure. Country with countryId: {0} already existed.<br />", countryId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Add failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Add failure. CountryId should not be empty.<br />");
				}
	        	
	        } else if ("update".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(countryId)){
	        		Country country = null;
					try {
						country = this.countryService.get(countryId);
						if (country != null){
							country.setCode(code);
							country.setNum(num);
							country.setName(name);
							this.countryService.update(country);
							message.append(MessageFormat.format("Update Success. Country with countryId: {0} is updated.<br />", countryId));
						} else {
							message.append(MessageFormat.format("Update failure. Country with countryId: {0} does not exist.<br />", countryId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Update failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Update failure. CountryId should not be empty.<br />");
				}
	        	
	        } else if ("delete".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(countryId)){
					try {
						if (this.countryService.get(countryId) != null){
							this.countryService.delete(countryId);
							message.append(MessageFormat.format("Delete Success. Country with countryId: {0} is deleted.<br />", countryId));
						} else {
							message.append(MessageFormat.format("Delete failure. Country with countryId: {0} does not exist.<br />", countryId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Delete failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Delete failure. CountryId should not be empty.<br />");
				}
	        }
	    }
	    
	    output.put("msg", message.toString());
		return output;
	}
	

    /**
     * Display Country list by query q
     * @param q
     * @return
     */
	@RequestMapping(value={"/countryLikeList"}, params={"q"})
	public Map<String, Object> countryLikeList(
			@RequestParam(value = "q") String q, HttpServletRequest request
		){
		log.debug("q: " + q);
		
		Map<String, Object> returnList = new HashMap<String, Object>();
		List<Map<String, Object>> returnRows = new ArrayList<Map<String, Object>>();
		
		Country searchCountry = new Country();
		searchCountry.setCountryId(q);
		searchCountry.setCode(q);
		searchCountry.setNum(q);
		searchCountry.setName(q);
		
		Pageable pageable = new PageRequest(0, 20);
		Page<Country> pageCountry = this.countryService.findByAllORLike(searchCountry, pageable);

		if (pageCountry != null){
			List<Country> listCountry = pageCountry.getContent();
			
			for (int i = 0; i < listCountry.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Country country = listCountry.get(i);
				
				map.put("countryId", country.getCountryId());
				map.put("code", country.getCode());
				map.put("num", country.getNum());
				map.put("name", country.getName());
				returnRows.add(map);
			}
			returnList.put("total", pageCountry.getTotalElements());
			returnList.put("rows", returnRows);
		}else{
			returnList.put("total", 0);
			returnList.put("rows", returnRows);
		}
		
		return returnList;
	}
    //------------------- Handlers for Web AJAX ends ----------------------
    
    @RequestMapping("*")
    @ResponseBody
    public String fallbackMethod(){
        return "fallback method"; //return custom 404
    }
	
}
