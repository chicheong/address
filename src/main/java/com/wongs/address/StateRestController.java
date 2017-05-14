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
@RequestMapping("/rest/state")
public class StateRestController {

	private static final Logger log = LoggerFactory.getLogger(StateRestController.class);
	
	@Autowired StateService stateService;
	@Autowired CountryService countryService;
    
    //------------------- Handlers for Web AJAX starts ----------------------
    /**
     * Display State list
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
		
		Page<State> pageState = null;
		
		String filterRules = request.getParameter("filterRules");
		if (!(StringUtils.isEmpty(filterRules) || "[]".equalsIgnoreCase(filterRules))){
			log.debug("filterRules: " + filterRules);
			State state = new State();
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
		        if ("stateId".equalsIgnoreCase(field)){
		        	state.setStateId(fieldValue);
		        } else if ("countryId".equalsIgnoreCase(field)){
		        	state.setCountryId(fieldValue);
		        } else if ("code".equalsIgnoreCase(field)){
		        	state.setCode(fieldValue);
		        } else if ("name".equalsIgnoreCase(field)){
		        	state.setName(fieldValue);
		        }
		        
	        }
	        pageState = this.stateService.findByAllANDLike(state, null);
		} else {

			Pageable pageable = new PageRequest(page-1, rows);
			pageState = this.stateService.findAll(pageable);

		}

		if (pageState != null){
			List<State> listState = pageState.getContent();
			
			for (int i = 0; i < listState.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				State state = listState.get(i);
				
				map.put("stateId", state.getStateId());
				map.put("countryId", state.getCountryId());
				map.put("code", state.getCode());
				map.put("name", state.getName());
				returnRows.add(map);
			}
			returnList.put("total", pageState.getTotalElements());
			returnList.put("rows", returnRows);
		}else{
			returnList.put("total", 0);
			returnList.put("rows", returnRows);
		}
		
		return returnList;
	}
	
	
	/**
	 * Add, update, delete State
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
	        
	        String type = "", stateId = "", countryId = "", code = "", name = "";
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
	        	} else if ("stateId".equalsIgnoreCase(paramName)){
	        		stateId = paramValue;
	        	} else if ("countryId".equalsIgnoreCase(paramName)){
	        		countryId = paramValue;
	        	} else if ("code".equalsIgnoreCase(paramName)){
	        		code = paramValue;
	        	} else if ("name".equalsIgnoreCase(paramName)){
	        		name = paramValue;
	        	} 
	        }
	        
	        if ("add".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(stateId)){
	        		State state = new State();
					try {
						if (this.stateService.get(stateId) == null){
							Country country = this.countryService.get(countryId);
							if (country != null){
								state.setStateId(stateId);
								state.setCountryId(countryId);
								state.setCode(code);
								state.setName(name);
								this.stateService.add(state);
								message.append(MessageFormat.format("Add Success. State with stateId: {0} is added.<br />", stateId));
							} else {
								message.append(MessageFormat.format("Add failure. State {0} with not existing countryId {1}.<br />", stateId, countryId));
							}
						} else {
							message.append(MessageFormat.format("Add failure. State with stateId: {0} already existed.<br />", stateId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Add failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Add failure. StateId should not be empty.<br />");
				}
	        	
	        } else if ("update".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(stateId)){
	        		State state = null;
					try {
						state = this.stateService.get(stateId);
						if (state != null){
							Country country = this.countryService.get(countryId);
							if (country != null){
								state.setCountryId(countryId);
								state.setCode(code);
								state.setName(name);
								this.stateService.update(state);
								message.append(MessageFormat.format("Update Success. State with stateId: {0} is updated.<br />", stateId));
							} else {
								message.append(MessageFormat.format("Update failure. State {0} with not existing countryId {1}.<br />", stateId, countryId));
							}
						} else {
							message.append(MessageFormat.format("Update failure. State with stateId: {0} does not exist.<br />", stateId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Update failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Update failure. StateId should not be empty.<br />");
				}
	        	
	        } else if ("delete".equalsIgnoreCase(type)){
	        	if (!StringUtils.isEmpty(stateId)){
					try {
						if (this.stateService.get(stateId) != null){
							this.stateService.delete(stateId);
							message.append(MessageFormat.format("Delete Success. State with stateId: {0} is deleted.<br />", stateId));
						} else {
							message.append(MessageFormat.format("Delete failure. State with stateId: {0} does not exist.<br />", stateId));
						}
					} catch (Exception e) {
						log.error(e.toString());
						message.append(MessageFormat.format("Delete failure. {0}<br />", e.toString()));
					}
				} else {
					message.append("Delete failure. StateId should not be empty.<br />");
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
	@RequestMapping(value={"/stateLikeList"}, params={"q"})
	public Map<String, Object> stateLikeList(
			@RequestParam(value = "q") String q, HttpServletRequest request
		){
		log.debug("q: " + q);
		
		Map<String, Object> returnList = new HashMap<String, Object>();
		List<Map<String, Object>> returnRows = new ArrayList<Map<String, Object>>();
		
		State searchState = new State();
		searchState.setStateId(q);
		searchState.setCountryId(q);
		searchState.setCode(q);
		searchState.setName(q);

		Pageable pageable = new PageRequest(0, 20);
		Page<State> pageState = this.stateService.findByAllORLike(searchState, pageable);
		
		if (pageState != null){
			List<State> listState = pageState.getContent();
			
			for (int i = 0; i < listState.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				State state = listState.get(i);
				
				map.put("stateId", state.getStateId());
				map.put("countryId", state.getCountryId());
				map.put("code", state.getCode());
				map.put("name", state.getName());
				returnRows.add(map);
			}
			returnList.put("total", pageState.getTotalElements());
			returnList.put("rows", returnRows);
		} else {
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
