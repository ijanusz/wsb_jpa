package com.jpacourse.mapper;

import com.jpacourse.dto.MedicalTreatmentTO;
import com.jpacourse.persistence.entity.MedicalTreatmentEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class MedicalTreatmentMapper {

    private MedicalTreatmentMapper() {
    }

    public static MedicalTreatmentTO mapToTO(MedicalTreatmentEntity entity) {
        if (entity == null) {
            return null;
        }
        MedicalTreatmentTO to = new MedicalTreatmentTO();
        to.setId(entity.getId());
        to.setDescription(entity.getDescription());
        to.setType(entity.getType());
        return to;
    }

    public static MedicalTreatmentEntity mapToEntity(MedicalTreatmentTO to) {
        if (to == null) {
            return null;
        }
        MedicalTreatmentEntity entity = new MedicalTreatmentEntity();
        entity.setId(to.getId());
        entity.setDescription(to.getDescription());
        entity.setType(to.getType());
        return entity;
    }

    public static List<MedicalTreatmentTO> mapToTOList(List<MedicalTreatmentEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(MedicalTreatmentMapper::mapToTO)
                .collect(Collectors.toList());
    }

    public static List<MedicalTreatmentEntity> mapToEntityList(List<MedicalTreatmentTO> tos) {
        if (tos == null) {
            return null;
        }
        return tos.stream()
                .map(MedicalTreatmentMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}