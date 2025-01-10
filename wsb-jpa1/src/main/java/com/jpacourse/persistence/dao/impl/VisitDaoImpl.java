package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VisitDaoImpl extends AbstractDao<VisitEntity, Long> implements VisitDao {
    @Override
    public List<VisitEntity> findByPatientId(Long patientId) {
        return entityManager.createQuery(
                        "SELECT v FROM VisitEntity v WHERE v.patient.id = :pid",
                        VisitEntity.class
                )
                .setParameter("pid", patientId)
                .getResultList();
    }
}