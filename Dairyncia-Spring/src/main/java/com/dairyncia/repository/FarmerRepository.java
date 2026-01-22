
package com.dairyncia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.Farmer;

import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer> {
    Optional<Farmer> findByUserId(String userId);
    Boolean existsByUserId(String userId);
    
    
    @Query("SELECT f from Farmer f Left Join Fetch f.user")
    List<Farmer> findAllWithUser();
}