package com.wongs.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface AddressRepository extends CrudRepository<Address, String> {

	Page<Address> findAll(Pageable pageable);

//	Page<Address> findByNameContainingAndCountryContainingAllIgnoringCase(String name,
//			String country, Pageable pageable);
//
//	Address findByNameAndCountryAllIgnoringCase(String name, String country);

	@Query("FROM Address a WHERE a.line1 like ?1 AND a.line2 like ?2 AND a.line3 like ?3 AND a.line4 like ?4 AND a.city like ?5 AND a.postalCode like ?6 AND ((?7 = '' AND a.country is null) OR a.country.countryId like ?7) AND ((?8 = '%' AND a.state is null) OR a.state.stateId like ?8)")
	Page<Address> findByAllLikeAllIgnoreCase(
			@Param("line1") String line1, @Param("line2") String line2, @Param("line3") String line3, @Param("line4") String line4,
			@Param("city") String city, @Param("postalCode") String postalCode, @Param("countryId") String countryId,
			@Param("stateId") String stateId, Pageable pageable);
}