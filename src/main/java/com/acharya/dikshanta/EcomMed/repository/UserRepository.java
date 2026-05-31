package com.acharya.dikshanta.EcomMed.repository;

import com.acharya.dikshanta.EcomMed.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
                SELECT u FROM User u
                WHERE (:name IS NULL OR :name = '' 
                    OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
                AND (:email IS NULL OR :email = '' 
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
            """)
    Page<User> findPagedUsers(
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable
    );
}
