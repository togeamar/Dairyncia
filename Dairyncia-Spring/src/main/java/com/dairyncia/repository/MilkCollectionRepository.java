package com.dairyncia.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.MilkCollection;
import com.dairyncia.entities.User;
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

    @Query("""
            SELECT 
                f.id AS farmerId,
                u.id AS userId,
                u.fullName AS farmerName,

                SUM(CASE WHEN m.milkType = 'Buffalo' AND m.milkShift = 'Morning' THEN m.quantity ELSE 0 END) AS buffaloMorningMilkCount,
                SUM(CASE WHEN m.milkType = 'Buffalo' AND m.milkShift = 'Evening' THEN m.quantity ELSE 0 END) AS buffaloEveningMilkCount,

                SUM(CASE WHEN m.milkType = 'Cow' AND m.milkShift = 'Morning' THEN m.quantity ELSE 0 END) AS cowMorningMilkCount,
                SUM(CASE WHEN m.milkType = 'Cow' AND m.milkShift = 'Evening' THEN m.quantity ELSE 0 END) AS cowEveningMilkCount

            FROM MilkCollection m
            JOIN m.farmer f
            JOIN f.user u
            WHERE f.id = :managerId
            GROUP BY f.id, u.id, u.fullName
            ORDER BY f.id
        """)
	List<?> getFarmerMilkCollection(String managerId);
    
    // ---------- By manager ----------
    @Query("""
        SELECT mc
        FROM MilkCollection mc
        JOIN mc.farmer f
        JOIN f.manager m
        WHERE m.id = :managerId
        ORDER BY mc.createdAt DESC
    """)
    List<MilkCollection> findByManagerId(@Param("managerId") Long managerId);

    // ---------- Today by manager ----------
    @Query("""
        SELECT mc
        FROM MilkCollection mc
        JOIN mc.farmer f
        JOIN f.manager m
        WHERE m.id = :managerId
          AND mc.createdAt >= :startOfDay
          AND mc.createdAt < :endOfDay
        ORDER BY mc.createdAt DESC
    """)
    List<MilkCollection> findTodaysByManagerId(
            @Param("managerId") Long managerId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
