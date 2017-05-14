package com.wongs.address;

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

@Component("countryService")
@Transactional (rollbackFor = {Exception.class} )
public class CountryServiceImpl implements CountryService {
	
	private static final Logger log = LoggerFactory.getLogger(CountryService.class);
		
	private CountryRepository countryRepository;

	@Autowired
	public CountryServiceImpl(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@Override
	public Country add(Country instance) {
		return this.countryRepository.save(instance);
	}

	@Override
	public Country update(Country instance) {
		return this.countryRepository.save(instance);
	}

	@Override
	public void delete(String id) {
		this.countryRepository.delete(id);
	}

	@Override
	public Country get(String id) {
		return this.countryRepository.findOne(id);
	}

	@Override
	public Page<Country> findAll(Pageable pageable) {
		return this.countryRepository.findAll(pageable);
	}

	@Override
	public Page<Country> findByAllANDLike(Country obj, Pageable pageable) {
		if(obj.getCountryId() == null) obj.setCountryId("");
		if(obj.getCode() == null) obj.setCode("");
		if(obj.getNum() == null) obj.setNum("");
		if(obj.getName() == null) obj.setName("");
		
		Page<Country> objs = this.countryRepository.findByCountryIdContainingAndCodeContainingAndNumContainingAndNameContainingAllIgnoreCase(
				obj.getCountryId(), obj.getCode(), obj.getNum(), obj.getName(), pageable);
		return objs;
		
	}
	
	@Override
	public Page<Country> findByAllORLike(Country obj, Pageable pageable) {
		if(obj.getCountryId() == null) obj.setCountryId("");
		if(obj.getCode() == null) obj.setCode("");
		if(obj.getNum() == null) obj.setNum("");
		if(obj.getName() == null) obj.setName("");
		
		Page<Country> objs = this.countryRepository.findByCountryIdContainingOrCodeContainingOrNumContainingOrNameContainingAllIgnoreCase(
				obj.getCountryId(), obj.getCode(), obj.getNum(), obj.getName(), pageable);
		return objs;
	}
	
}
