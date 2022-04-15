package com.maviay.pazaryeri.activities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UrunSorgulari {
    // Müşterek fonksiyon ve sabitler için singleton olarak çalışır.
    private static UrunSorgulari instance = null;
    private static String webSayfa = "https://dummyjson.com";
    private static String token = "";
    private static String expiresInMins = "60";
    private UrunSorgulari() {

    }
    public static UrunSorgulari getInstance()
    {
        if (instance == null) instance = new UrunSorgulari();
        return instance;
    }
    public void SetWebSayfa(String _webSayfa) {
        webSayfa = _webSayfa;
    }
    public void SetToken (String _token) {
        token = _token;
    }
    public String GetWebSayfa() {
        return webSayfa;
    }
    public String GetToken() {
        return token;
    }
    public String GetExpires() {
        return expiresInMins;
    }
    public static Urun[] urunBul() {
        return null;
    }

    public static ArrayList<Urun> products(String aranan, String select, int limit, int skip, Long id) {
        // Parametrelerde belirtilen sorgulama ve seçim kriterlerine göre ürünler getirilir.
        // Hiç parametre belirtilmezse bütün ürünler getirilir.
        // id seçildi ise diğer kriterler dikkate alınmaz.
        ArrayList<Urun> urunler = new ArrayList<Urun>();
        ArrayList<Resim> resimler = new ArrayList<Resim>();
        Urun urun = null;
        Resim resim = null;
        if (token.isEmpty()) {
            System.out.println("Giriş yapılmadı.");
            return null;
        }
        boolean query = false; // İlk sorgu ya da filtre eklendi mi?
        String bolum = "products"; // https://dummyjson.com/products
        String jsonAdres = webSayfa + "/" + bolum;
        if (id > 0) { // Ürün ID > 0, diğer kriterler dikkate alınmaz.
            jsonAdres += "/" + id.toString();
        } else {
            if (!aranan.trim().isEmpty()) {
                if (!query) { // Bu ilk eklentidir.
                    if (!jsonAdres.substring(jsonAdres.length() - 1, jsonAdres.length()).equals("/"))
                        jsonAdres += "/";
                    jsonAdres += "/search?q=" + aranan;
                    query = true;
                }
            }
            if (!select.trim().isEmpty()) {
                if (!query) { // İlk ek
                    jsonAdres += "?select=" + select;
                    query = true;
                } else {
                    jsonAdres += "&select=" + select;
                }
            }
            if (limit > 0) {
                if (!query) { // İlk ek
                    jsonAdres += "?limit=" + Integer.toString(limit);
                    query = true;
                } else {
                    jsonAdres += "&limit=" + Integer.toString(limit);
                }
            }
            if (skip > 0) {
                if (!query) { // İlk ek
                    jsonAdres += "?skip=" + Integer.toString(skip);
                    query = true;
                } else {
                    jsonAdres += "&skip=" + Integer.toString(skip);
                }
            }
        }
        URL url;
        String jsonSatir = "";
        jsonAdres = jsonAdres.replaceAll(" ", "%20");
        System.out.println(jsonAdres);
        try {
            url = new URL(jsonAdres);
            HttpURLConnection conn;
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200) { // Bağlantı başarılı ise kod 200 olmalıdır.
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            }
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                jsonSatir += sc.nextLine();
            }
            sc.close();  // Buraya kadar json nesnesini string değişkene aldık
            conn.disconnect(); // Bağlantı ile ilgili nesneleri kapatabiliriz.
            try {
                JSONParser parse = new JSONParser();
                JSONObject jobj = (JSONObject)parse.parse(jsonSatir);
                if (id > 0) { // Ürün sorgulamada belirtildi ise "products" kökü bulunmaz.
                    resimler = new ArrayList<Resim>();
                    JSONArray jsonarr_2 = (JSONArray) jobj.get("images");
                    if (jsonarr_2 != null) {
                        if (jsonarr_2.size() > 0) {
                            for (int j = 0; j < jsonarr_2.size(); j++) {
                                //System.out.println(j + " : " + jsonarr_2.get(j));
                                resim = new Resim((Long) jobj.get("id"), j, (String) jsonarr_2.get(j));
                                resimler.add(resim);
                            }
                        }
                    }
                    urun = new Urun((Long) jobj.get("id"),
                            (jobj.get("title") != null ? (String) jobj.get("title") : ""),
                            (jobj.get("description") != null ? (String) jobj.get("description") : ""),
                            (jobj.get("price") != null ? (Long) jobj.get("price") : 0),
                            (jobj.get("discountPercentage") != null ? (Double) jobj.get("discountPercentage") : 0),
                            (jobj.get("rating") != null ? (Double) jobj.get("rating") : 0),
                            (jobj.get("stock") != null ? (Long) jobj.get("stock") : 0),
                            (jobj.get("brand") != null ? (String) jobj.get("brand") : ""),
                            (jobj.get("category") != null ? (String) jobj.get("category") : ""),
                            (jobj.get("thumbnail") != null ? (String) jobj.get("thumbnail") : ""),
                            resimler);
                    urunler.add(urun);
                } else { // products için
                    JSONArray jsonarr_1 = (JSONArray) jobj.get("products"); // products dizesine karşılık gelen değerler array'a alınır.
                    if (jsonarr_1.size() > 0) {
                        // products array içindeki değerler sırasıyla okunur.
                        for (int i = 0; i < jsonarr_1.size(); i++) {
                            JSONObject jsonobj_1 = (JSONObject) jsonarr_1.get(i);
                            resimler = new ArrayList<Resim>();
                            JSONArray jsonarr_2 = (JSONArray) jsonobj_1.get("images");
                            if (jsonarr_2 != null) {
                                if (jsonarr_2.size() > 0) {
                                    for (int j = 0; j < jsonarr_2.size(); j++) {
                                        //System.out.println(j + " : " + jsonarr_2.get(j));
                                        resim = new Resim((Long) jsonobj_1.get("id"), j, (String) jsonarr_2.get(j));
                                        resimler.add(resim);
                                    }
                                }
                            }
                            urun = new Urun((Long) jsonobj_1.get("id"),
                                    (jsonobj_1.get("title") != null ? (String) jsonobj_1.get("title") : ""),
                                    (jsonobj_1.get("description") != null ? (String) jsonobj_1.get("description") : ""),
                                    (jsonobj_1.get("price") != null ? (Long) jsonobj_1.get("price") : 0),
                                    (jsonobj_1.get("discountPercentage") != null ? (Double) jsonobj_1.get("discountPercentage") : 0),
                                    (jsonobj_1.get("rating") != null ? (Double) jsonobj_1.get("rating") : 0),
                                    (jsonobj_1.get("stock") != null ? (Long) jsonobj_1.get("stock") : 0),
                                    (jsonobj_1.get("brand") != null ? (String) jsonobj_1.get("brand") : ""),
                                    (jsonobj_1.get("category") != null ? (String) jsonobj_1.get("category") : ""),
                                    (jsonobj_1.get("thumbnail") != null ? (String) jsonobj_1.get("thumbnail") : ""),
                                    resimler);
                            urunler.add(urun);
                        }
                    } else {
                        System.out.println("veri yok.");
                    }
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // Step 9) Convert the string objects into JSON objects:


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urunler;
    }

}
