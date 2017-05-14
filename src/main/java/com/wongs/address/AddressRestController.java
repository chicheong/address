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
@RequestMapping("/rest/address")
public class AddressRestController {

	private static final Logger log = LoggerFactory.getLogger(AddressRestController.class);
	
	@Autowired AddressService addressService;
	@Autowired CountryService countryService;
	@Autowired StateService stateService;
	
	@RequestMapping(value={"/", "/addresss"})
	Iterable<Address> addresss(){
		return this.addressService.findAll(new PageRequest(0, 10));
	}
    
    //------------------- Handlers for Web AJAX starts ----------------------
    /**
     * Display Address list
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
		
		Page<Address> pageAddress = null;
		
		String filterRules = request.getParameter("filterRules");
		if (!(StringUtils.isEmpty(filterRules) || "[]".equalsIgnoreCase(filterRules))){
			log.debug("filterRules: " + filterRules);
			Address address = new Address();
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
		        if ("line1".equalsIgnoreCase(field)){
		        	address.setLine1(fieldValue);
		        } else if ("line2".equalsIgnoreCase(field)){
		        	address.setLine2(fieldValue);
		        } else if ("line3".equalsIgnoreCase(field)){
		        	address.setLine3(fieldValue);
		        } else if ("line4".equalsIgnoreCase(field)){
		        	address.setLine4(fieldValue);
		        } else if ("city".equalsIgnoreCase(field)){
		        	address.setCity(fieldValue);
		        } else if ("postalCode".equalsIgnoreCase(field)){
		        	address.setPostalCode(fieldValue);
		        } else if ("countryId".equalsIgnoreCase(field)){
		        	Country country = new Country();
		        	country.setCountryId(fieldValue);
		        	address.setCountry(country);
		        } else if ("stateId".equalsIgnoreCase(field)){
		        	State state = new State();
		        	state.setStateId(fieldValue);
		        	address.setState(state);
		        }
		        
	        }
	        pageAddress = this.addressService.findByAllLike(address, null);
		} else {

			Pageable pageable = new PageRequest(page-1, rows);
			pageAddress = this.addressService.findAll(pageable);

		}

		if (pageAddress != null){
			List<Address> listAddress = pageAddress.getContent();
			
			for (int i = 0; i < listAddress.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Address address = listAddress.get(i);
				
				map.put("addressId", address.getAddressId());
				map.put("line1", address.getLine1());
				map.put("line2", address.getLine2());
				map.put("line3", address.getLine3());
				map.put("line4", address.getLine4());
				map.put("city", address.getCity());
				map.put("postalCode", address.getPostalCode());
				map.put("countryId", address.getCountry()!=null?address.getCountry().getCountryId():"");
				map.put("stateId", address.getState()!=null?address.getState().getStateId():"");
				returnRows.add(map);
			}
			returnList.put("total", pageAddress.getTotalElements());
			returnList.put("rows", returnRows);
		}else{
			returnList.put("total", 0);
			returnList.put("rows", returnRows);
		}
		
		return returnList;
	}
	
	
	/**
	 * Add, update, delete Address
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
	        
	        String type = "", addressId = "", line1 = "", line2 = "", line3 = "", line4 = "", city = "", postalCode = "",
	        		countryId = "", stateId = "";
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
	        	} else if ("addressId".equalsIgnoreCase(paramName)){
	        		addressId = paramValue;
	        	} else if ("line1".equalsIgnoreCase(paramName)){
	        		line1 = paramValue;
	        	} else if ("line2".equalsIgnoreCase(paramName)){
	        		line2 = paramValue;
	        	} else if ("line3".equalsIgnoreCase(paramName)){
	        		line3 = paramValue;
	        	} else if ("line4".equalsIgnoreCase(paramName)){
	        		line4 = paramValue;
	        	} else if ("city".equalsIgnoreCase(paramName)){
	        		city = paramValue;
	        	} else if ("postalCode".equalsIgnoreCase(paramName)){
	        		postalCode = paramValue;
	        	} else if ("countryId".equalsIgnoreCase(paramName)){
	        		countryId = paramValue;
	        	} else if ("stateId".equalsIgnoreCase(paramName)){
	        		stateId = paramValue;
	        	} 
	        }
	        
	        if ("add".equalsIgnoreCase(type)){
	        	if (StringUtils.isEmpty(addressId)){
	        		Address address = new Address();
					try {
						Country country = this.countryService.get(countryId);
						if (country != null || !StringUtils.isEmpty(city)){
							State state = null;
							if (!StringUtils.isEmpty(stateId))
								state = this.stateService.get(stateId);
							address.setLine1(line1);
							address.setLine2(line2);
							address.setLine3(line3);
							address.setLine4(line4);
							address.setCity(city);
							address.setPostalCode(postalCode);
							address.setCountry(country);
							address.setState(state);
							this.addressService.add(address);
							message.append(MessageFormat.format("Add Success. Address with addressId: {0} is added.<br />", addressId));
						} else {
							message.append("Add failure. Address must have either City or Country.<br />");
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Add failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Add failure. AddressId should be empty.<br />");
				}
	        } else if ("update".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(addressId)){
	        		Address address = null;
					try {
						address = this.addressService.get(addressId);
						if (address != null){
							Country country = this.countryService.get(countryId);
							if (country != null || !StringUtils.isEmpty(city)){
								State state = null;
								if (!StringUtils.isEmpty(stateId))
									state = this.stateService.get(stateId);
								address.setLine1(line1);
								address.setLine2(line2);
								address.setLine3(line3);
								address.setLine4(line4);
								address.setCity(city);
								address.setPostalCode(postalCode);
								address.setCountry(country);
								address.setState(state);
								this.addressService.update(address);
								message.append(MessageFormat.format("Update Success. Address with addressId: {0} is updated.<br />", addressId));
							} else {
								message.append("Update failure. Address must have either City or Country.<br />");
							}
						} else {
							message.append(MessageFormat.format("Update failure. Address with addressId: {0} does not exist.<br />", addressId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Update failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Update failure. AddressId should not be empty.<br />");
				}
	        	
	        } else if ("delete".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(addressId)){
					try {
						if (this.addressService.get(addressId) != null){
							this.addressService.delete(addressId);
							message.append(MessageFormat.format("Delete Success. Address with addressId: {0} is deleted.<br />", addressId));
						} else {
							message.append(MessageFormat.format("Delete failure. Address with addressId: {0} does not exist.<br />", addressId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Delete failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Delete failure. AddressId should not be empty.<br />");
				}
	        }
	    }
	    
	    output.put("msg", message.toString());
		return output;
	}	
	
	@RequestMapping (value = "/itemList")
	public Iterable<Address> debug (HttpServletRequest request) {
	    Map<String, String[]> parameters = request.getParameterMap();

	    for(String key : parameters.keySet()) {
	    	log.debug(key);
	        String[] vals = parameters.get(key);
	        for(String val : vals)
	        	log.debug(" -> " + val);
	    }
	    return this.addressService.findAll(new PageRequest(0, 10));
	}
    //------------------- Handlers for Web AJAX ends ----------------------
    
    @RequestMapping("*")
    @ResponseBody
    public String fallbackMethod(){
        return "fallback method"; //return custom 404
    }
	
}
