package com.jpacourse.service.impl;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;
    private final VisitDao visitDao;


    @Autowired
    public PatientServiceImpl(PatientDao patientDao, VisitDao visitDao) {
        this.patientDao = patientDao;
        this.visitDao = visitDao;
    }

    public List<PatientTO> findAll() {
        return patientDao.findAll().stream().map(PatientMapper::mapToTO).toList();
    }

    @Override
    @Transactional
    public PatientTO findById(Long id) {
        PatientEntity entity = patientDao.findOne(id);
        return PatientMapper.mapToTO(entity);
    }

    @Override
    public PatientTO savePatient(PatientTO patientTO) {
        PatientEntity entity = PatientMapper.mapToEntity(patientTO);
        PatientEntity savedEntity = patientDao.save(entity);
        return PatientMapper.mapToTO(savedEntity);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        patientDao.delete(id);
    }

    @Override
    public List<VisitEntity> findAllVisitsForPatient(Long patientId) {

        return visitDao.findByPatientId(patientId);
    }
}