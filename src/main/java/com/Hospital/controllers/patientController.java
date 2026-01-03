package com.Hospital.controllers;

import com.Hospital.models.PrescriptionModel;
import com.Hospital.models.appointmentModel;
import com.Hospital.models.userModel;
import com.Hospital.repository.PrescriptionRepository;
import com.Hospital.services.appointmentService;
import com.Hospital.services.userService;
import com.Hospital.services.doctorService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patient")
public class patientController {

    private final userService userService;
    private final appointmentService appointmentService;
    private final doctorService doctorService;
    private final PrescriptionRepository PrescriptionRepository;


    public patientController(userService userService,
                             appointmentService appointmentService,
                             doctorService doctorService,
                             PrescriptionRepository PrescriptionRepository) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.doctorService  = doctorService;
        this.PrescriptionRepository = PrescriptionRepository;
    }

    // ================= VIEW DOCTORS =================
    @GetMapping("/doctors")
    public String viewDoctors(Model model, HttpSession session) {

        if (!"PATIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        model.addAttribute("doctors",
                userService.getUsersByRole("DOCTOR"));

        return "patient-doctors";
    }

    // ================= APPOINTMENT PAGE =================
    @GetMapping("/appointment/{id}")
    public String appointmentPage(@PathVariable int id,
                                  Model model,
                                  HttpSession session) {

        if (!"PATIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        userModel doctor = doctorService.getDoctorById(id);

        if (doctor == null) {
            return "redirect:/patient/doctors"; // safe redirect
        }

        appointmentModel appointment = new appointmentModel();
        appointment.setDoctorId(doctor.getId());
        appointment.setDoctorName(doctor.getName());

        model.addAttribute("appointment", appointment);

        return "patient-appointments";
    }

    // ================= SUBMIT APPOINTMENT =================
    
    @PostMapping("/appointment")
    public String submitAppointment(@ModelAttribute appointmentModel appointment,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        Integer patientId = (Integer) session.getAttribute("userId");
        String patientName = (String) session.getAttribute("username");

        if (patientId == null) {
            return "redirect:/login";
        }

        appointment.setPatientId(patientId);
        appointment.setPatientName(patientName);
        appointment.setStatus("PENDING");

        appointmentService.saveAppointment(appointment);

        // ✅ success popup flag
        redirectAttributes.addFlashAttribute("appointmentBooked", true);

        return "redirect:/dashboard";
    }
    
    
 // ================= MY APPOINTMENTS =================
    @GetMapping("/myappointments")
    public String myAppointments(HttpSession session, Model model) {

        if (!"PATIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        Integer patientId = (Integer) session.getAttribute("userId");

        if (patientId == null) {
            return "redirect:/login";
        }

        // ✅ fetch all appointments of this patient
        model.addAttribute(
            "appointments",
            appointmentService.getByPatientId(patientId)
        );

        return "patient-showappointment";
    }


    
    @GetMapping("/prescriptions")
    public String viewPatientPrescriptions(HttpSession session, Model model) {

        String patientName = (String) session.getAttribute("username");

        if (patientName == null) {
            return "redirect:/login";
        }

        List<PrescriptionModel> prescriptions =
        		PrescriptionRepository.findByPatientName(patientName);

        System.out.println("Patient = " + patientName);
        System.out.println("Prescriptions = " + prescriptions.size());

        model.addAttribute("prescriptions", prescriptions);

        return "patient-view-prescriptions";
    }



}
