package com.Hospital.repository;

import com.Hospital.models.adminModel;


import org.springframework.data.jpa.repository.JpaRepository;

public interface adminRepository extends JpaRepository<adminModel, Integer> {

	adminModel findByName(String name);    
    
}
