package com.dairyncia.repository;

import com.dairyncia.entities.MilkCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MilkCollectionRepository extends JpaRepository<MilkCollection, Long> {

    
    @Query("""
    	    SELECT mc FROM MilkCollection mc
    	    JOIN FETCH mc.manager
    	    WHERE mc.farmer.id = :farmerId
    	""")
    	List<MilkCollection> findByFarmerIdWithManager(Long farmerId);

}
