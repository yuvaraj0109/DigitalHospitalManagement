package com.Hospital.services;

import com.Hospital.models.userModel;
import com.Hospital.repository.userRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class patientService {

    private final userRepository userRepository;

    public patientService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all patients
    public List<userModel> getAllPatients() {
        return userRepository.findByRole("PATIENT");
    }

    // Delete patient
    public void deletePatient(int id) {
        userRepository.deleteById(id);
    }

    // Update patient
    public void updatePatient(int id, String name) {
        userModel patient = userRepository.findById(id).orElse(null);
        if (patient != null) {
            patient.setName(name);
            userRepository.save(patient);
        }
    }
}
