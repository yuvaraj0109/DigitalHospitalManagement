package com.Hospital.services;

import com.Hospital.models.appointmentModel;
import com.Hospital.repository.appointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class appointmentService {

    private final appointmentRepository appointmentRepository;

    public appointmentService(appointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // ================= SAVE =================
    public void saveAppointment(appointmentModel appointment) {
        appointmentRepository.save(appointment);
    }

    // ================= FETCH =================
    public List<appointmentModel> getByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    public appointmentModel getById(int id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<appointmentModel> getByDoctorId(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<appointmentModel> getByPatientId(int patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    
    public List<appointmentModel> getByDoctorIdAndStatus(int doctorId, String status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }


    // ================= ADMIN ACTIONS =================
    public void approveAppointment(int id) {
        appointmentModel appt = appointmentRepository.findById(id).orElse(null);
        if (appt != null) {
            appt.setStatus("ASSIGNED");   // ✅ IMPORTANT
            appointmentRepository.save(appt);
        }
    }

    public void rejectAppointment(int id) {
        appointmentModel appt = appointmentRepository.findById(id).orElse(null);
        if (appt != null) {
            appt.setStatus("REJECTED");
            appointmentRepository.save(appt);
        }
    }

}
