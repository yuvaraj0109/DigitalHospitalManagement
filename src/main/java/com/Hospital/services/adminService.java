package com.Hospital.services;

import com.Hospital.models.adminModel;
import com.Hospital.repository.adminRepository;
import org.springframework.stereotype.Service;

@Service
public class adminService {

    private adminRepository adminRepository;
    
    public adminService (adminRepository adminRepository){
    	this.adminRepository = adminRepository;
    }

    public adminModel login(String name, String password) {

        adminModel admin = adminRepository.findByName(name);

        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }

        return null;
    }
}
