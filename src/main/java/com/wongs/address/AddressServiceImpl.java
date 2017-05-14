package com.wongs.address;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
//import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.orm.hibernate4.HibernateTransactionManager;

@Component("addressService")
@Transactional (rollbackFor = {Exception.class} )
public class AddressServiceImpl implements AddressService {
	
	private static final Logger log = LoggerFactory.getLogger(AddressService.class);
	private static final String PERCENT = "%";
	
	private AddressRepository addressRepository;
	private StateRepository stateRepository;

	@Autowired
	public AddressServiceImpl(AddressRepository addressRepository, StateRepository stateRepository) {
		this.addressRepository = addressRepository;
		this.stateRepository = stateRepository;
	}

	@Override
	public Address add(Address instance) {
		UUID id = UUID.randomUUID();
		instance.setAddressId(Address.ID_PREFIX + id.toString().toUpperCase());
		 return this.addressRepository.save(instance);
	}

	@Override
	public Address update(Address instance) {
		return this.addressRepository.save(instance);
	}

	@Override
	public void delete(String id) {
		this.addressRepository.delete(id);
	}
	
	
	@Override
	public Address get(String id) {
		return this.addressRepository.findOne(id);
	}

	@Override
	public Page<Address> findAll(Pageable pageable) {
		return this.addressRepository.findAll(pageable);
	}
	
	@Override
	public Page<Address> findByAllLike(Address obj, Pageable pageable) {
		String line1 = PERCENT, line2 = PERCENT, line3 = PERCENT, line4 = PERCENT,
				city = PERCENT, postalCode = PERCENT, countryId = PERCENT, stateId = PERCENT;
		if (obj.getLine1() != null)
			line1 = PERCENT + obj.getLine1() + PERCENT;
		if (obj.getLine2() != null)
			line2 = PERCENT + obj.getLine2() + PERCENT;
		if (obj.getLine3() != null)
			line3 = PERCENT + obj.getLine3() + PERCENT;
		if (obj.getLine4() != null)
			line4 = PERCENT + obj.getLine4() + PERCENT;		
		if (obj.getCity() != null)
			city = PERCENT + obj.getCity() + PERCENT;
		if (obj.getPostalCode() != null)
			postalCode = PERCENT + obj.getPostalCode() + PERCENT;
		if (obj.getCountry() != null)
			countryId = PERCENT + obj.getCountry().getCountryId() + PERCENT;
		if (obj.getState() != null)
			stateId = PERCENT + obj.getState().getStateId() + PERCENT;
		
		Page<Address> objs = this.addressRepository.findByAllLikeAllIgnoreCase(
				line1, line2, line3, line4, city, postalCode, countryId, stateId, pageable);
		return objs;
	}


	
}
