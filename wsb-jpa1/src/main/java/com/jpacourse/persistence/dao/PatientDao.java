package com.jpacourse.persistence.dao;

import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientDao extends Dao<PatientEntity, Long> {

    VisitEntity addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);

    List<PatientEntity> findByLastName(String lastName);

    List<PatientEntity> findByVisitsCountGreaterThan(long x);

    List<PatientEntity> findByAgeGreaterThan(int age);

}