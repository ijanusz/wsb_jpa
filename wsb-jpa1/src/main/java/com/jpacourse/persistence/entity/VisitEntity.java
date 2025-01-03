package com.jpacourse.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "VISIT")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dwukierunkowa relacja One-to-Many między Patient (rodzic) a Visit (dziecko)
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    // Jednokierunkowa relacja Many-to-One od strony dziecka (Visit) do rodzica (Doctor)
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @ManyToMany
    @JoinTable(
            name = "visit_treatment",
            joinColumns = @JoinColumn(name = "visit_id"),
            inverseJoinColumns = @JoinColumn(name = "treatment_id")
    )
    private List<MedicalTreatmentEntity> treatments;
    private String description;
    @Column(nullable = false)
    private LocalDateTime time;

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public List<MedicalTreatmentEntity> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<MedicalTreatmentEntity> treatments) {
        this.treatments = treatments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}
