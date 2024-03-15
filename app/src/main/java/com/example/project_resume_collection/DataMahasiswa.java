package com.example.project_resume_collection;

public class DataMahasiswa {
    private String nama;
    private String kelas;
    private String nim;
    private String prodi;
    private String jurusan;
    private String email;

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    private String whatsapp;

    public DataMahasiswa() {
        // Default constructor required for Firestore
    }

    public DataMahasiswa(String nama, String kelas, String nim, String prodi, String jurusan, String email, String whatsapp) {
        this.nama = nama;
        this.kelas = kelas;
        this.nim = nim;
        this.prodi = prodi;
        this.jurusan = jurusan;
        this.email = email;
        this.whatsapp = whatsapp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
