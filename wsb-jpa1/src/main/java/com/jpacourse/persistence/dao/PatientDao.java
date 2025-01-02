package com.jpacourse.persistence.dao;

import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PatientDao extends Dao<PatientEntity, Long> {
    VisitEntity addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);

}