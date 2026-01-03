package com.Hospital.services;


import com.Hospital.models.appointmentModel;
import com.Hospital.repository.appointmentRepository;
import com.Hospital.models.userModel;
import com.Hospital.repository.userRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class doctorService {

    private final userRepository userRepository;
    private final appointmentRepository appointmentRepository;

    public doctorService(userRepository userRepository, appointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // Get all doctors
    public List<userModel> getAllDoctors() {
        return userRepository.findByRole("DOCTOR");
    }
    
    // GET DOCTOR BY ID 
    public userModel getDoctorById(int id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public List<appointmentModel> getByDoctorIdAndStatus(int doctorId, String status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }


    // Delete doctor
    public void deleteDoctor(int id) {
        userRepository.deleteById(id);
    }

    // Update doctor
    public void updateDoctor(int id, String name) {
        userModel doctor = userRepository.findById(id).orElse(null);
        if (doctor != null) {
            doctor.setName(name);
            userRepository.save(doctor);
        }
    }
}
