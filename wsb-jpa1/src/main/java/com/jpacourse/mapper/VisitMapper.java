package com.jpacourse.mapper;

import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistence.entity.VisitEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class VisitMapper {

    private VisitMapper() {
    }

    public static VisitTO mapToTO(VisitEntity entity) {
        if (entity == null) {
            return null;
        }
        VisitTO to = new VisitTO();
        to.setId(entity.getId());
        to.setTime(entity.getTime());
        if (entity.getDoctor() != null) {
            to.setDoctorFirstName(entity.getDoctor().getFirstName());
            to.setDoctorLastName(entity.getDoctor().getLastName());
        }
        to.setTreatments(MedicalTreatmentMapper.mapToTOList(entity.getTreatments()));
        return to;
    }

    public static VisitEntity mapToEntity(VisitTO to) {
        if (to == null) {
            return null;
        }
        VisitEntity entity = new VisitEntity();
        entity.setId(to.getId());
        entity.setTime(to.getTime());
        entity.setTreatments(MedicalTreatmentMapper.mapToEntityList(to.getTreatments()));
        return entity;
    }

    public static List<VisitTO> mapToTOList(List<VisitEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(VisitMapper::mapToTO)
                .collect(Collectors.toList());
    }

    public static List<VisitEntity> mapToEntityList(List<VisitTO> tos) {
        if (tos == null) {
            return null;
        }
        return tos.stream()
                .map(VisitMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}