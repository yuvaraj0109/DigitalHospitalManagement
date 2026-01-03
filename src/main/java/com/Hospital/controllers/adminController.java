package com.Hospital.controllers;

import com.Hospital.models.adminModel;
import com.Hospital.models.appointmentModel;
import com.Hospital.services.adminService;
import com.Hospital.repository.appointmentRepository;
import com.Hospital.services.appointmentService;
import com.Hospital.services.doctorService;
import com.Hospital.services.patientService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class adminController {

    private final adminService adminService;
    private final doctorService doctorService;
    private final patientService patientService;
    private final appointmentService appointmentService;
    private final appointmentRepository appointmentRepository;

    public adminController(adminService adminService,
                           doctorService doctorService,
                           patientService patientService,
                           appointmentService appointmentService,
                           appointmentRepository appointmentRepository) {
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
    }

    // ================= ADMIN LOGIN =================

    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin-login";
    }
    
    @PostMapping("/login")
    public String adminLogin(@RequestParam String name,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        adminModel admin = adminService.login(name, password);

        if (admin == null) {
            model.addAttribute("error", true);
            return "admin-login";
        }

        session.setAttribute("username", admin.getName());
        session.setAttribute("role", "ADMIN");

        return "redirect:/admin/dashboard?loginSuccess";
    }


    // ================= ADMIN DASHBOARD =================

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "dashboard";
    }

    // ================= DOCTOR MANAGEMENT =================

    @GetMapping("/doctors")
    public String viewDoctors(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "admin-doctors";
    }

    @GetMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable int id,
                               RedirectAttributes redirectAttributes) {

        doctorService.deleteDoctor(id);

        // ✅ delete success flag
        redirectAttributes.addFlashAttribute("deleteSuccess", true);

        return "redirect:/admin/doctors";
    }
    
    @PostMapping("/doctors/update")
    public String updateDoctor(@RequestParam int id,
                               @RequestParam String name,
                               RedirectAttributes redirectAttributes) {

        doctorService.updateDoctor(id, name);

        // ✅ success flag
        redirectAttributes.addFlashAttribute("updateSuccess", true);

        return "redirect:/admin/doctors";
    }


    // ================= PATIENT MANAGEMENT =================

    @GetMapping("/patients")
    public String viewPatients(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "admin-patients";
    }

    @GetMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable int id,
                                RedirectAttributes redirectAttributes) {

        patientService.deletePatient(id);

        // ✅ delete popup flag
        redirectAttributes.addFlashAttribute("patientDeleteSuccess", true);

        return "redirect:/admin/patients";
    }


    @PostMapping("/patients/update")
    public String updatePatient(@RequestParam int id,
                                @RequestParam String name,
                                RedirectAttributes redirectAttributes) {

        patientService.updatePatient(id, name);

        // ✅ update popup flag
        redirectAttributes.addFlashAttribute("patientUpdateSuccess", true);

        return "redirect:/admin/patients";
    }


 // ================= APPOINTMENT MANAGEMENT =================

    @GetMapping("/appointments")
    public String viewPendingAppointments(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("appointments",
                appointmentService.getByStatus("PENDING"));

        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "admin-appointment";
    }

    @PostMapping("/appointments/approve")
    public String approveAppointment(@RequestParam int appointmentId,
                                     RedirectAttributes redirectAttributes) {

        appointmentService.approveAppointment(appointmentId);

        // ✅ success popup flag
        redirectAttributes.addFlashAttribute("appointmentApproved", true);

        return "redirect:/admin/appointments";
    }


    @PostMapping("/appointments/reject")
    public String rejectAppointment(@RequestParam int appointmentId,
                                    RedirectAttributes redirectAttributes) {

        appointmentService.rejectAppointment(appointmentId);

        redirectAttributes.addFlashAttribute("appointmentRejected", true);

        return "redirect:/admin/appointments";
    }


    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/appointments/history")
    public String adminAppointmentHistory(HttpSession session, Model model) {

        // 🔒 ADMIN only
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("appointments",
                appointmentRepository.findByStatusNot("PENDING"));

        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("username", session.getAttribute("username"));

        return "admin-appointment-history";
    }


    
}
