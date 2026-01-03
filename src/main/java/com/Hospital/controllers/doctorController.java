package com.Hospital.controllers;

import com.Hospital.models.PrescriptionModel;
import com.Hospital.models.userModel;
import com.Hospital.repository.PrescriptionRepository;
import com.Hospital.services.appointmentService;
import com.Hospital.services.userService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctor")
public class doctorController {

    private final appointmentService appointmentService;
    private final PrescriptionRepository prescriptionRepository;
    private final userService userService;

    public doctorController(appointmentService appointmentService,
                            PrescriptionRepository prescriptionRepository,
                            userService userService) {
        this.appointmentService = appointmentService;
        this.prescriptionRepository = prescriptionRepository;
        this.userService = userService;
    }

    // ================= DOCTOR APPOINTMENTS =================

    @GetMapping("/appointments")
    public String myAppointments(HttpSession session, Model model) {

        Integer doctorId = (Integer) session.getAttribute("userId");

        if (doctorId == null) {
            return "redirect:/login";
        }

        // ONLY admin-assigned appointments
        model.addAttribute(
                "appointments",
                appointmentService.getByDoctorIdAndStatus(doctorId, "ASSIGNED")
        );

        return "doctor-appointments";
    }

    // ================= PRESCRIPTION PAGE =================
    @GetMapping("/prescription")
    public String prescriptionPage(Model model) {

        List<userModel> patients = userService.getUsersByRole("PATIENT");
        model.addAttribute("patients", patients);

        return "doctor-prescription";
    }


    // ================= SAVE PRESCRIPTION =================

    @PostMapping("/prescription/save")
    public String savePrescription(
            @RequestParam String patientName,
            @RequestParam String medicine,
            @RequestParam String instructions,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String doctorUsername = (String) session.getAttribute("username");

        if (doctorUsername == null) {
            return "redirect:/login";
        }

        PrescriptionModel prescription = new PrescriptionModel();
        prescription.setPatientName(patientName);
        prescription.setMedicine(medicine);
        prescription.setInstructions(instructions);
        prescription.setDoctorUsername(doctorUsername);

        prescriptionRepository.save(prescription);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/doctor/prescription";
    }

    // ================= VIEW PRESCRIPTIONS =================

    @GetMapping("/prescriptions")
    public String viewPrescriptions(HttpSession session, Model model) {

        String doctorUsername = (String) session.getAttribute("username");

        if (doctorUsername == null) {
            return "redirect:/login";
        }

        List<PrescriptionModel> prescriptions =
                prescriptionRepository.findByDoctorUsername(doctorUsername);

        model.addAttribute("prescriptions", prescriptions);

        return "doctor-view-prescriptions";
    }
}
