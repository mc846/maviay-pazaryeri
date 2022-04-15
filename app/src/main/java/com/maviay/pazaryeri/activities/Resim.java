package com.maviay.pazaryeri.activities;

public class Resim {
    Long urunId;
    int id;
    String adres;
    public Resim (Long urunId, int id, String adres) {
        this.urunId = urunId;
        this.id = id;
        this.adres = adres;
    }
    public String getAdres() { return this.adres; }
}
