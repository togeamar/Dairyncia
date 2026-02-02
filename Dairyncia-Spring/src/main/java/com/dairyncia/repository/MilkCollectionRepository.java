package com.dairyncia.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.MilkCollection;
import com.dairyncia.enums.MilkShift;
import com.dairyncia.enums.MilkType;

@Repository
public interface MilkCollectionRepository extends JpaRepository<MilkCollection, Long> {

   
    @Query("""
        SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
        FROM MilkCollection m
        WHERE m.farmer.id = :farmerId
        AND m.milkShift = :milkShift
        AND m.milkType = :milkType
        AND CAST(m.createdAt AS LocalDate) = :today
    """)
    boolean existsTodaySubmission(
            @Param("farmerId") Long farmerId,
            @Param("milkShift") MilkShift milkShift,
            @Param("milkType") MilkType milkType,
            @Param("today") LocalDate today
    );

 
    @Query("""
        SELECT m FROM MilkCollection m
        JOIN FETCH m.farmer f
        JOIN FETCH f.user fu
        JOIN FETCH m.manager mu
        ORDER BY m.createdAt DESC
    """)
    List<MilkCollection> findAllWithDetails();

  
    @Query("""
        SELECT m FROM MilkCollection m
        JOIN FETCH m.farmer f
        JOIN FETCH f.user fu
        JOIN FETCH m.manager mu
        WHERE CAST(m.createdAt AS LocalDate) = :today
        ORDER BY m.createdAt DESC
    """)
    List<MilkCollection> findTodaysCollections(@Param("today") LocalDate today);

   
    @Query("""
        SELECT m FROM MilkCollection m
        JOIN FETCH m.farmer f
        JOIN FETCH f.user fu
        JOIN FETCH m.manager mu
        WHERE m.id = :id
    """)
    Optional<MilkCollection> findByIdWithDetails(@Param("id") Long id);

    
    boolean existsByFarmerId(Long farmerId);

    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MilkCollection m WHERE m.manager.id = :managerId")
    boolean existsByManagerId(@Param("managerId") Long managerId);


    @Query("""
        SELECT m FROM MilkCollection m
        JOIN FETCH m.farmer f
        JOIN FETCH f.user fu
        JOIN FETCH m.manager mu
        WHERE m.farmer.id = :farmerId
        ORDER BY m.createdAt DESC
    """)
    List<MilkCollection> findByFarmerId(@Param("farmerId") Long farmerId);
}
