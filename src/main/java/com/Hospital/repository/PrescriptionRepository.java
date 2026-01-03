package com.Hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Hospital.models.PrescriptionModel;

@Repository
public interface PrescriptionRepository extends JpaRepository<PrescriptionModel, Integer> {
	 List<PrescriptionModel> findByDoctorUsername(String doctorUsername);
	 List<PrescriptionModel> findByPatientName(String patientName);
	 PrescriptionModel findByAppointmentId(int appointmentId);



}

