package com.jpacourse.service;

import com.jpacourse.dto.AddressTO;
import com.jpacourse.dto.PatientTO;
import com.jpacourse.mapper.AddressMapper;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.persistence.dao.AddressDao;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.AddressEntity;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private AddressDao addressDao;

    @Before
    public void setUp() {
        clearDatabase();
    }

    private void clearDatabase() {
        visitDao.deleteAll();
        patientDao.deleteAll();
        doctorDao.deleteAll();
        addressDao.deleteAll();
    }

    @Test
    public void testShouldDeletePatientAndHisVisitsButNotDoctors() {
        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisit(doctor, savedPatientTO);

        long doctorCountBefore = doctorDao.count();
        long visitCountBefore = visitDao.count();
        long patientCountBefore = patientDao.count();

        System.out.println(patientDao.findAll());
        patientService.deletePatient(savedPatientTO.getId());


        assertThat(patientDao.findOne(savedPatientTO.getId())).isNull();
        assertThat(visitDao.count()).isEqualTo(visitCountBefore - 1);
        assertThat(patientDao.count()).isEqualTo(patientCountBefore - 1);

        assertThat(doctorDao.count()).isEqualTo(doctorCountBefore);

    }

    @Test
    public void testShouldFindPatientById() {

        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisit(doctor, savedPatientTO);


        PatientTO found = patientService.findById(savedPatientTO.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(savedPatientTO.getId());
        assertThat(found.getFirstName()).isEqualTo(savedPatientTO.getFirstName());
        assertThat(found.getLastName()).isEqualTo(savedPatientTO.getLastName());
        assertThat(found.getPatientNumber()).isEqualTo(savedPatientTO.getPatientNumber());
        assertThat(found.getAge()).isEqualTo(savedPatientTO.getAge());

        assertThat(found.getVisits()).isNotNull();
        assertThat(found.getVisits()).hasSize(1);
        assertThat(found.getVisits().get(0).getDoctorFirstName()).isEqualTo("Anna");
        assertThat(found.getVisits().get(0).getDoctorLastName()).isEqualTo("Nowak");
        assertThat(found.getVisits().get(0).getTime()).isEqualTo(LocalDateTime.of(2023, 10, 15, 10, 0));
    }


    private AddressEntity createAndSaveAddress() {
        AddressTO addressTO = new AddressTO();
        addressTO.setCity("Testowo");
        addressTO.setAddressLine1("ul. Testowa 1");
        addressTO.setPostalCode("12-345");
        AddressEntity addressEntity = AddressMapper.mapToEntity(addressTO);
        return addressDao.save(addressEntity);
    }

    private DoctorEntity createAndSaveDoctor(AddressEntity address) {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Anna");
        doctor.setLastName("Nowak");
        doctor.setTelephoneNumber("987654321");
        doctor.setEmail("anna.nowak@example.com");
        doctor.setDoctorNumber("D001");
        doctor.setSpecialization(Specialization.GP);
        doctor.setAddress(address);
        return doctorDao.save(doctor);
    }

    private PatientTO createAndSavePatient(AddressEntity address) {

        AddressTO addressTO = AddressMapper.mapToTO(address);
        PatientTO patientTO = new PatientTO();
        patientTO.setFirstName("Jan");
        patientTO.setLastName("Kowalski");
        patientTO.setTelephoneNumber("123456789");
        patientTO.setEmail("jan.kowalski@example.com");
        patientTO.setPatientNumber("P001");
        patientTO.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patientTO.setAge(43);
        patientTO.setAddress(addressTO);

        return patientService.savePatient(patientTO);
    }


    private void createAndSaveVisit(DoctorEntity doctor, PatientTO patient) {
        VisitEntity visit = new VisitEntity();
        visit.setDoctor(doctor);
        visit.setPatient(PatientMapper.mapToEntity(patient));
        visit.setDescription("Pierwsza wizyta");
        visit.setTime(LocalDateTime.of(2023, 10, 15, 10, 0));
        visitDao.save(visit);
    }

}
