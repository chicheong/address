package com.wongs.address;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface StateRepository extends CrudRepository<State, String> {

	Page<State> findAll(Pageable pageable);

//	Page<Address> findByNameContainingAndCountryContainingAllIgnoringCase(String name,
//			String country, Pageable pageable);
//
//	Address findByNameAndCountryAllIgnoringCase(String name, String country);
	Page<State> findByStateIdContainingAndCountryIdContainingAndCodeContainingAndNameContainingAllIgnoreCase(@Param("stateId") String stateId, @Param("countryId") String countryId, @Param("code") String code, @Param("name") String name, Pageable pageable);
	
	Page<State> findByStateIdContainingOrCountryIdContainingOrCodeContainingOrNameContainingAllIgnoreCase(@Param("stateId") String stateId, @Param("countryId") String countryId, @Param("code") String code, @Param("name") String name, Pageable pageable);
	
	//Not in use
	List<State> findByCountryId(@Param("countryId") String countryId);

}