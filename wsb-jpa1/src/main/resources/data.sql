
INSERT INTO ADDRESS (id, city, address_line1, address_line2, postal_code)
VALUES (1, 'Warszawa', 'ul. Kwiatowa 1', '', '00-001'),
       (2, 'Kraków', 'ul. Zielona 2', '', '30-002');


INSERT INTO PATIENT (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, address_id)
VALUES (1, 'Jan', 'Kowalski', '123456789', 'jan.kowalski@example.com', 'P001', '1980-01-01', 1);


INSERT INTO DOCTOR (id, first_name, last_name, telephone_number, email, doctor_number, specialization, address_id)
VALUES (1, 'Anna', 'Nowak', '987654321', 'anna.nowak@example.com', 'D001', 'GP', 2);


INSERT INTO MEDICAL_TREATMENT (id, description, type)
VALUES (1, 'Badanie USG', 'USG'),
       (2, 'Badanie EKG', 'EKG');


INSERT INTO VISIT (id, description, time, patient_id, doctor_id)
VALUES (1, 'Pierwsza wizyta', '2023-10-15 10:00:00', 1, 1);


INSERT INTO VISIT_TREATMENT (visit_id, treatment_id)
VALUES (1, 1),
       (1, 2);