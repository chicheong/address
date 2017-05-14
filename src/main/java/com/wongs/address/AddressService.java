package com.wongs.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AddressService {
	
	Address add(Address instance);
	Address update(Address instance);
	void delete(String id);
	
	Address get(String id);
	Page<Address> findAll(Pageable pageable);
	Page<Address> findByAllLike(Address obj, Pageable pageable);

}
