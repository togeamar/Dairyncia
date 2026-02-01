package com.dairyncia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.Farmer;
import com.dairyncia.entities.User;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByUserId(Long userId);
    boolean existsByUser(User user);
    void deleteByUserId(Long userId);
    
    @Query("""
            SELECT f FROM Farmer f
            JOIN FETCH f.user u
        """)
        List<Farmer> findAllFarmers();
    
//    @Query("""
//    		SELECT f FROM FARMER f
//    		JOIN FETCH f.user u
//    		JOIN u.roles r
//    		WHERE r.name = 'ROLE_FARMER'
//    		""")
//    List<Farmer> findAllAFarmers();

}
