package org.example.bankcards.repository;

import org.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @Query(value = "SELECT id FROM _user WHERE user_details_id = :userDetailsId", nativeQuery = true)
    UUID findIdByUserDetailsId(UUID userDetailsId);

}
