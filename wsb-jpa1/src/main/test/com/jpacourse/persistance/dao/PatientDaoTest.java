package com.jpacourse.persistance.dao;

import com.jpacourse.persistence.dao.AddressDao;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
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
import java.util.List;

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

    @Autowired
    private VisitDao visitDao;

    @Test
    public void testAddVisitToPatient() {
        // given
        AddressEntity patientAddress = createAndSaveAddress("Warszawa", "ul. Kwiatowa 1", "00-001");
        PatientEntity patient = createAndSavePatient(patientAddress,
                "Jan", "Kowalski", "P1001",
                "jan.kowalski@example.com", "123456789", LocalDate.of(1985, 5, 20), 39);

        AddressEntity doctorAddress = createAndSaveAddress("Warszawa", "ul. Lekarska 10", "00-002");
        DoctorEntity doctor = createAndSaveDoctor(doctorAddress,
                "Anna", "Nowak", "D2002",
                "anna.nowak@example.com", "987654321", Specialization.GP);

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
        assertThat(updatedPatient.getVisits()).isNotNull().hasSize(1);
    }

    @Test
    public void testShouldFindByLastName() {
        // given
        AddressEntity patientAddress = createAndSaveAddress("Warszawa", "ul. Kwiatowa 1", "00-001");
        createAndSavePatient(patientAddress,
                "Jan", "Kowalski", "P1001",
                "jan.kowalski@example.com", "123456789", LocalDate.of(1985, 5, 20), 39);

        AddressEntity doctorAddress = createAndSaveAddress("Warszawa", "ul. Lekarska 10", "00-002");
        createAndSaveDoctor(doctorAddress,
                "Anna", "Nowak", "D2002",
                "anna.nowak@example.com", "987654321", Specialization.GP);

        // when
        List<PatientEntity> found = patientDao.findByLastName("Kowalski");

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getFirstName()).isEqualTo("Jan");
    }

    @Test
    @Transactional
    public void testShouldFindPatientsWithMoreThanXVisits() {
        AddressEntity patientAddress = createAndSaveAddress("Warszawa", "ul. Kwiatowa 1", "00-001");
        PatientEntity patient = createAndSavePatient(patientAddress,
                "Jan", "Kowalski", "P1001",
                "jan.kowalski@example.com", "123456789", LocalDate.of(1985, 5, 20), 39);

        // Also create and save a Doctor
        AddressEntity doctorAddress = createAndSaveAddress("Warszawa", "ul. Lekarska 2", "00-002");
        DoctorEntity doctor = createAndSaveDoctor(doctorAddress,
                "Anna", "Nowak", "D2002",
                "anna.nowak@example.com", "987654321", Specialization.GP);

        // Add visits with the doctor assigned
        for (int i = 0; i < 5; i++) {
            VisitEntity visit = new VisitEntity();
            visit.setPatient(patient);
            visit.setTime(LocalDateTime.of(2023, 10, 15, 10, 0));
            visit.setDoctor(doctor); // Set doctor here
            patient.getVisits().add(visit);
            visitDao.save(visit);
        }

        // Ensure data is saved correctly
        assertThat(patient.getVisits().size()).isGreaterThan(1);

        // Perform the test
        List<PatientEntity> found = patientDao.findByVisitsCountGreaterThan(1);
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getLastName()).isEqualTo("Kowalski");
    }


    @Test
    public void testShouldFindByAgeGreaterThan() {
        // given
        AddressEntity patientAddress = createAndSaveAddress("Warszawa", "ul. Kwiatowa 1", "00-001");
        createAndSavePatient(patientAddress,
                "Jan", "Kowalski", "P1001",
                "jan.kowalski@example.com", "123456789", LocalDate.of(1985, 5, 20), 39);

        AddressEntity doctorAddress = createAndSaveAddress("Warszawa", "ul. Lekarska 10", "00-002");
        createAndSaveDoctor(doctorAddress,
                "Anna", "Nowak", "D2002",
                "anna.nowak@example.com", "987654321", Specialization.GP);

        // when
        List<PatientEntity> result = patientDao.findByAgeGreaterThan(1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    @Transactional
    public void testOptimisticLocking() {
        // given
        AddressEntity patientAddress = createAndSaveAddress("Warszawa", "ul. Kwiatowa 1", "00-001");
        PatientEntity patient = createAndSavePatient(patientAddress,
                "Jan", "Kowalski", "P1001",
                "jan.kowalski@example.com", "123456789", LocalDate.of(1985, 5, 20), 39);

        PatientEntity patientA = patientDao.findOne(patient.getId());
        PatientEntity patientB = patientDao.findOne(patient.getId());

        patientA.setLastName("Zmiana A");
        patientDao.update(patientA);

        patientB.setLastName("Zmiana B");
        try {
            patientDao.update(patientB);
        } catch (Exception e) {
            System.out.println("Catch exception: " + e);
            assertThat(e).hasRootCauseInstanceOf(jakarta.persistence.OptimisticLockException.class);
        }
    }

    private AddressEntity createAndSaveAddress(String city, String addressLine1, String postalCode) {
        AddressEntity address = new AddressEntity();
        address.setCity(city);
        address.setAddressLine1(addressLine1);
        address.setPostalCode(postalCode);
        return addressDao.save(address);
    }

    private PatientEntity createAndSavePatient(AddressEntity address,
                                               String firstName,
                                               String lastName,
                                               String patientNumber,
                                               String email,
                                               String phone,
                                               LocalDate birthDate,
                                               int age) {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setTelephoneNumber(phone);
        patient.setEmail(email);
        patient.setPatientNumber(patientNumber);
        patient.setDateOfBirth(birthDate);
        patient.setAge(age);
        patient.setAddress(address);
        patient.setVisits(new ArrayList<>());
        return patientDao.save(patient);
    }

    private DoctorEntity createAndSaveDoctor(AddressEntity address,
                                             String firstName,
                                             String lastName,
                                             String doctorNumber,
                                             String email,
                                             String phone,
                                             Specialization specialization) {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setDoctorNumber(doctorNumber);
        doctor.setEmail(email);
        doctor.setTelephoneNumber(phone);
        doctor.setSpecialization(specialization);
        doctor.setAddress(address);
        return doctorDao.save(doctor);
    }
}
