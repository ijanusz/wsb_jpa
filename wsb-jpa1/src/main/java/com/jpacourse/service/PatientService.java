package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;

public interface PatientService {

    PatientTO findById(Long id);

    PatientTO savePatient(PatientTO patientTO);

    void deletePatient(Long id);

}