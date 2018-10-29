package com.android.anime2;

public class Ula {
    public Ula(){

    }
    public Ula(int id, String isi, String img_user, String nama) {
        this.id = id;
        this.isi = isi;
        this.img_user = img_user;
        this.nama = nama;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    private String isi,img_user,nama;
}
