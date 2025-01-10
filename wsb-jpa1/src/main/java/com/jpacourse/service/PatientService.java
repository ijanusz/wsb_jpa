package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistence.entity.VisitEntity;

import java.util.List;

public interface PatientService {

    PatientTO findById(Long id);

    PatientTO savePatient(PatientTO patientTO);

    void deletePatient(Long id);

    List<VisitEntity> findAllVisitsForPatient(Long patientId);


}