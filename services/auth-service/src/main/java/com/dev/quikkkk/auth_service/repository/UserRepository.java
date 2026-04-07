package com.dev.quikkkk.auth_service.repository;

import com.dev.quikkkk.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.roles
            WHERE u.email = :email
            """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);
}
