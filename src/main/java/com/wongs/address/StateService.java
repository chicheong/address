package com.wongs.address;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StateService {
	
	State add(State instance);
	State update(State instance);
	void delete(String id);
	
	State get(String id);
	Page<State> findAll(Pageable pageable);
	Page<State> findByAllANDLike(State obj, Pageable pageable);
	Page<State> findByAllORLike(State obj, Pageable pageable);
	
	List<State> findByCountryId(String countryId);

}
