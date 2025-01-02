package com.jpacourse.rest;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.rest.exception.EntityNotFoundException;
import com.jpacourse.service.PatientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{id}")
    public PatientTO findById(@PathVariable Long id) {
        PatientTO patient = patientService.findById(id);
        if (patient == null) {
            throw new EntityNotFoundException(id);
        }
        return patient;
    }

    @PostMapping
    public PatientTO createOrUpdate(@RequestBody PatientTO patientTO) {
        return patientService.savePatient(patientTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        PatientTO existing = patientService.findById(id);
        if (existing == null) {
            throw new EntityNotFoundException(id);
        }
        patientService.deletePatient(id);
    }
}