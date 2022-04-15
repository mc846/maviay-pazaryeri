package com.maviay.pazaryeri.activities;

import java.util.ArrayList;

public class Urun {
  private Long id;
  private String title;
  private String description;
  private Long price;
  private Double discountPercentage;
  private Double rating;
  private Long stock;
  private String brand;
  private String category;
  private String thumbnail;
  private ArrayList<Resim> resimler;
  public Urun(Long id, String title, String description, Long price, Double discountPercentage, Double rating, Long stock, String brand, String category, String thumbnail, ArrayList<Resim> resimler) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.price = price;
    this.discountPercentage = discountPercentage;
    this.rating = rating;
    this.stock = stock;
    this.brand = brand;
    this.category = category;
    this.thumbnail = thumbnail;
    this.resimler = resimler;
  }

  public Long getId() {
    return this.id;
  }
  public String getTitle() { return this.title; }
  public String getDescription() { return this.description; };
  public Long getPrice() { return this.price; }
  public Double getDiscountPercentage() { return this.discountPercentage; }
  public Double getRating() { return this.rating; }
  public Long getStock() { return this.stock; }
  public String getBrand() { return this.brand; }
  public String getCategory() { return this.category; }
  public String getThumbnail() { return this.thumbnail; }
  public ArrayList<Resim> getResimler() { return this.resimler; }
  public String getText() {
    return this.title;
  }

}
