package com.dairyncia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyncia.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
