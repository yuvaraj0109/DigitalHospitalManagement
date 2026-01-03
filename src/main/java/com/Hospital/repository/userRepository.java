package com.Hospital.repository;

import com.Hospital.models.userModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<userModel, Integer> {

    userModel findByNameAndPassword(String name, String password);

    boolean existsByName(String name);
    
    userModel findByNameAndRole(String name, String role);
    
    List<userModel> findByRole(String role);
    
}
