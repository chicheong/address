package com.wongs.address;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CountryService {

	Country add(Country instance);
	Country update(Country instance);
	void delete(String id);
	
	Country get(String id);
	Page<Country> findAll(Pageable pageable);
	Page<Country> findByAllANDLike(Country obj, Pageable pageable);
	Page<Country> findByAllORLike(Country obj, Pageable pageable);

}
