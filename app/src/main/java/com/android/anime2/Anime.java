package com.android.anime2;

public class Anime {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    private int id;

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    private int member;
    private String rating;



    private String title;
    private String sinopsis;
    private String genre;
    private String tipe;


    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    private String gambar;
    public Anime(){

    }
    public Anime(int id, String rating, String title, String sinopsis, String genre, String tipe,String gambar,int member) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.sinopsis = sinopsis;
        this.genre = genre;
        this.tipe = tipe;
        this.gambar=gambar;
        this.member = member;
    }
}
