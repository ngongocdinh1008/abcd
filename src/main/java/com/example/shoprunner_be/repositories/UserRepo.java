package com.example.shoprunner_be.repositories;

import com.example.shoprunner_be.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findUserByName(String name);
    boolean existsByPhoneNumber(String PhoneNumber);
    boolean existsByEmail(String Email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);

    @Query("SELECT o FROM User o WHERE " +
            "(:keyword IS NULL OR " +
            "o.name LIKE CONCAT('%', :keyword, '%') OR " +
            "o.address LIKE CONCAT('%', :keyword, '%') OR " +
            "o.phoneNumber LIKE CONCAT('%', :keyword, '%'))")
    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);


}
