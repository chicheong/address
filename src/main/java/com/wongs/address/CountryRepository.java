package com.wongs.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface CountryRepository extends CrudRepository<Country, String> {

	Page<Country> findAll(Pageable pageable);

//	Page<Address> findByNameContainingAndCountryContainingAllIgnoringCase(String name,
//			String country, Pageable pageable);
//
//	Address findByNameAndCountryAllIgnoringCase(String name, String country);

	
	Page<Country> findByCountryIdContainingAndCodeContainingAndNumContainingAndNameContainingAllIgnoreCase(@Param("countryId") String countryId, @Param("code") String code, @Param("num") String num, @Param("name") String name, Pageable pageable);
	
	Page<Country> findByCountryIdContainingOrCodeContainingOrNumContainingOrNameContainingAllIgnoreCase(@Param("countryId") String countryId, @Param("code") String code, @Param("num") String num, @Param("name") String name, Pageable pageable);
}