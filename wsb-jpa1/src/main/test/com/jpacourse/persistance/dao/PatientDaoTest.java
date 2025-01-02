package com.jpacourse.persistance.dao;

import com.jpacourse.persistence.dao.AddressDao;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.entity.AddressEntity;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private AddressDao addressDao;


    @Test
    public void testAddVisitToPatient() {
        // given
        AddressEntity patientAddress = new AddressEntity();
        patientAddress.setCity("Warszawa");
        patientAddress.setAddressLine1("ul. Kwiatowa 1");
        patientAddress.setPostalCode("00-001");
        patientAddress = addressDao.save(patientAddress);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Jan");
        patient.setLastName("Kowalski");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("jan.kowalski@example.com");
        patient.setPatientNumber("P1001");
        patient.setDateOfBirth(LocalDate.of(1985, 5, 20));
        patient.setAge(39);
        patient.setAddress(patientAddress);
        patient.setVisits(new ArrayList<>());
        patient = patientDao.save(patient);

        AddressEntity doctorAddress = new AddressEntity();
        doctorAddress.setCity("Warszawa");
        doctorAddress.setAddressLine1("ul. Lekarska 10");
        doctorAddress.setPostalCode("00-002");
        doctorAddress = addressDao.save(doctorAddress);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Anna");
        doctor.setLastName("Nowak");
        doctor.setTelephoneNumber("987654321");
        doctor.setEmail("anna.nowak@example.com");
        doctor.setDoctorNumber("D2002");
        doctor.setSpecialization(Specialization.GP);
        doctor.setAddress(doctorAddress);
        doctor = doctorDao.save(doctor);

        // when
        Long patientId = patient.getId();
        Long doctorId = doctor.getId();
        LocalDateTime visitTime = LocalDateTime.of(2025, 1, 1, 10, 30);
        String description = "Kontrola po operacji";

        VisitEntity visit = patientDao.addVisitToPatient(patientId, doctorId, visitTime, description);

        // then
        assertThat(visit).isNotNull();
        assertThat(visit.getId()).isNotNull();

        assertThat(visit.getPatient().getId()).isEqualTo(patientId);
        assertThat(visit.getDoctor().getId()).isEqualTo(doctorId);
        assertThat(visit.getTime()).isEqualTo(visitTime);
        assertThat(visit.getDescription()).isEqualTo(description);

        PatientEntity updatedPatient = patientDao.findOne(patientId);
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getVisits()).isNotNull();
        assertThat(updatedPatient.getVisits()).hasSize(1);
    }

}
