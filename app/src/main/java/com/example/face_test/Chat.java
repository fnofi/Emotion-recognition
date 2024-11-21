package com.example.face_test;


public class Chat {
    private String id;
    private String patientId;
    private String doctorId;

    public Chat() {
        // Пустой конструктор требуется для использования Firebase
    }

    public Chat(String id, String patientId, String doctorId) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
