package com.example.pakanotomatis;

public class Jadwal {
    private int id;
    private String judul;
    private String jam;
    private boolean aktif;

    public Jadwal(int id, String judul, String jam, boolean aktif) {
        this.id = id;
        this.judul = judul;
        this.jam = jam;
        this.aktif = aktif;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getJam() { return jam; }
    public boolean isAktif() { return aktif; }

    public void setAktif(boolean aktif) { this.aktif = aktif; }
}

