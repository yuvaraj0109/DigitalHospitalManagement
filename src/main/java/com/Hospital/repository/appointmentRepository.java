package com.Hospital.repository;

import com.Hospital.models.appointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface appointmentRepository
        extends JpaRepository<appointmentModel, Integer> {

    List<appointmentModel> findByStatus(String status);

    List<appointmentModel> findByDoctorId(int doctorId);

    List<appointmentModel> findByPatientId(int patientId);
    
    List<appointmentModel> findByDoctorIdAndStatus(int doctorId, String status);
 
    
    List<appointmentModel> findByStatusIn(List<String> statuses);
    
    List<appointmentModel> findByStatusNot(String status);



}
