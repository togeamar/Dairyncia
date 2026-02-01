package com.dairyncia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dairyncia.entities.Role.RoleType;
import com.dairyncia.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    
    // Pending users (users without roles)
    @Query("SELECT u FROM User u WHERE u.roles IS EMPTY")
    List<User> findPendingUsers();
    
    // fetch all users with role_manager
    @Query("""
    		SELECT u FROM User u
    		JOIN u.roles r
    		WHERE r.name = 'ROLE_MANAGER'
    		""")
    List<User> findAllManagers();
    
    @Query("""
            SELECT u FROM User u
            JOIN u.roles r
            WHERE r.name = :role
        """)
        List<User> findUsersByRole(@Param("role") RoleType role);
}