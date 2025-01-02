package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistence.entity.PatientEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class PatientMapper {

    private PatientMapper() {
    }

    public static PatientTO mapToTO(PatientEntity entity) {
        if (entity == null) {
            return null;
        }
        PatientTO to = new PatientTO();
        to.setId(entity.getId());
        to.setFirstName(entity.getFirstName());
        to.setLastName(entity.getLastName());
        to.setTelephoneNumber(entity.getTelephoneNumber());
        to.setEmail(entity.getEmail());
        to.setPatientNumber(entity.getPatientNumber());
        to.setDateOfBirth(entity.getDateOfBirth());
        to.setAge(entity.getAge());

        to.setAddress(AddressMapper.mapToTO(entity.getAddress()));

        to.setVisits(VisitMapper.mapToTOList(entity.getVisits()));

        return to;
    }

    public static PatientEntity mapToEntity(PatientTO to) {
        if (to == null) {
            return null;
        }
        PatientEntity entity = new PatientEntity();
        entity.setId(to.getId());
        entity.setFirstName(to.getFirstName());
        entity.setLastName(to.getLastName());
        entity.setTelephoneNumber(to.getTelephoneNumber());
        entity.setEmail(to.getEmail());
        entity.setPatientNumber(to.getPatientNumber());
        entity.setDateOfBirth(to.getDateOfBirth());
        entity.setAge(to.getAge());

        entity.setAddress(AddressMapper.mapToEntity(to.getAddress()));

        entity.setVisits(VisitMapper.mapToEntityList(to.getVisits()));
        if (entity.getVisits() != null) {
            entity.getVisits().forEach(v -> v.setPatient(entity));
        }

        return entity;
    }

    public static List<PatientTO> mapToTOList(List<PatientEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(PatientMapper::mapToTO)
                .collect(Collectors.toList());
    }

    public static List<PatientEntity> mapToEntityList(List<PatientTO> tos) {
        if (tos == null) {
            return null;
        }
        return tos.stream()
                .map(PatientMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}