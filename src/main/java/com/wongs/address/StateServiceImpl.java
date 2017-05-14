package com.wongs.address;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
//import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.orm.hibernate4.HibernateTransactionManager;

@Component("stateService")
@Transactional (rollbackFor = {Exception.class} )
public class StateServiceImpl implements StateService {
	
	private static final Logger log = LoggerFactory.getLogger(StateService.class);
	private static final String PERCENT = "%";
	
	private StateRepository stateRepository;

	@Autowired
	public StateServiceImpl(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	@Override
	public State add(State instance) {
		return this.stateRepository.save(instance);
		
	}

	@Override
	public State update(State instance) {
		return this.stateRepository.save(instance);
	}

	@Override
	public void delete(String id) {
		this.stateRepository.delete(id);
	}
	
	@Override
	public State get(String id) {
		return this.stateRepository.findOne(id);
	}

	@Override
	public Page<State> findAll(Pageable pageable) {
		return this.stateRepository.findAll(pageable);
	}
	
	@Override
	public Page<State> findByAllANDLike(State obj, Pageable pageable) {
		if(obj.getStateId()==null) obj.setStateId("");
		if(obj.getCountryId()==null) obj.setCountryId("");
		if(obj.getCode()==null) obj.setCode("");
		if(obj.getName()==null) obj.setName("");
		
		Page<State> objs = this.stateRepository.findByStateIdContainingAndCountryIdContainingAndCodeContainingAndNameContainingAllIgnoreCase(
				obj.getStateId(), obj.getCountryId(), obj.getCode(), obj.getName(), new PageRequest(0, 10));
		return objs;
	}
	
	@Override
	public Page<State> findByAllORLike(State obj, Pageable pageable) {
		if(obj.getStateId()==null) obj.setStateId("");
		if(obj.getCountryId()==null) obj.setCountryId("");
		if(obj.getCode()==null) obj.setCode("");
		if(obj.getName()==null) obj.setName("");
		
		Page<State> objs = this.stateRepository.findByStateIdContainingOrCountryIdContainingOrCodeContainingOrNameContainingAllIgnoreCase(
				obj.getStateId(), obj.getCountryId(), obj.getCode(), obj.getName(), pageable);
		return objs;
	}

	@Override
	public List<State> findByCountryId(String countryId) {
		return this.stateRepository.findByCountryId(countryId);
	}

	
}
