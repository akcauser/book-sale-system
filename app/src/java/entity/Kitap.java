/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name="kitapModel")
public class Kitap {

    private String isbn;
    private String adi;
    private String yazar;
    private String basimYili;
    private String yayinevi;
    private String fiyat;
    private String stok;
    private String satisMiktari;
    private String kategori;
    private String sayfaSayisi;
    private String yazarHakkinda;
    
    DataSource datasource;
    
    public String fetchGuestBookInfo(String bookIsbn) throws SQLException {
        
       if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        try{
            /*
            Bir tane kitap bilgileri çekilcek
            SELECT KITAPLAR.ISBN,
                KITAPLAR.ADI AS KITAP_ADI,
                YAZARLAR.AD AS YAZAR_ADI,
                KITAPLAR.BASIM_YILI,
                YAYINEVLERI.ID AS YAYINEVI_ADI,
                KITAPLAR.FIYAT,
                KITAPLAR.STOK,
                KITAPLAR.KATEGORI,
                KITAPLAR.SAYFA_SAYISI
            FROM KITAPLAR 
            INNER JOIN YAZARLAR ON KITAPLAR.YAZAR_ID = YAZARLAR.ID
            INNER JOIN YAYINEVLERI ON KITAPLAR.YAYINEVI_ID = YAYINEVLERI.ID
            WHERE KITAPLAR.ISBN='3333333333333'
            */
            PreparedStatement psFetchOneBook = connection.prepareStatement("SELECT KITAPLAR.ISBN, " +
                                                                                "KITAPLAR.ADI AS KITAP_ADI, " +
                                                                                "YAZARLAR.AD AS YAZAR_ADI, " +
                                                                                "YAZARLAR.SOYAD AS YAZAR_SOYADI, " +
                                                                                "YAZARLAR.BIYOGRAFI AS YAZAR_HAKKINDA, " +
                                                                                "KITAPLAR.BASIM_YILI, " +
                                                                                "YAYINEVLERI.ADI AS YAYINEVI_ADI, " +
                                                                                "KITAPLAR.FIYAT, " +
                                                                                "KITAPLAR.STOK, " +
                                                                                "KITAPLAR.KATEGORI, " +
                                                                                "KITAPLAR.SAYFA_SAYISI " +
                                                                                "FROM KITAPLAR " +
                                                                            "INNER JOIN YAZARLAR ON KITAPLAR.YAZAR_ID = YAZARLAR.ID " +
                                                                            "INNER JOIN YAYINEVLERI ON KITAPLAR.YAYINEVI_ID = YAYINEVLERI.ID " +
                                                                            "WHERE KITAPLAR.ISBN='"+bookIsbn+"'");
            
            ResultSet rsOneBook = psFetchOneBook.executeQuery();
            
            while(rsOneBook.next()){
                this.adi = rsOneBook.getString("KITAP_ADI");
                this.basimYili = rsOneBook.getString("BASIM_YILI");
                this.fiyat = rsOneBook.getString("FIYAT");
                this.isbn = rsOneBook.getString("ISBN");
                this.kategori = rsOneBook.getString("KATEGORI");
                this.sayfaSayisi = rsOneBook.getString("SAYFA_SAYISI");
                this.stok = rsOneBook.getString("STOK");
                this.yayinevi = rsOneBook.getString("YAYINEVI_ADI");
                this.yazar = rsOneBook.getString("YAZAR_ADI")+" "+ rsOneBook.getString("YAZAR_SOYADI");
                this.yazarHakkinda = rsOneBook.getString("YAZAR_HAKKINDA");
            }
            
            
            
        }finally{
            connection.close();
        }
        
        return "kitap-incele.xhtml";
        
        
    }
    
    public Kitap(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public ArrayList<Kitap> fetchKitaplar() throws SQLException{
        ArrayList<Kitap> kitaplar = new ArrayList<>();
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            yazarları çekecek sorgu.
                
            select * from APP.YAYINEVLERI;
                
            */
            String query = "select * from APP.KITAPLAR";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            int _yazarID;
            int _yayineviID;
            
            while(rs.next()){
                Kitap kitap = new Kitap();
                kitap.isbn = rs.getString("ISBN");
                kitap.adi = rs.getString("ADI");
                _yazarID = rs.getInt("YAZAR_ID");
                kitap.basimYili = Integer.toString(rs.getInt("BASIM_YILI"));
                _yayineviID = rs.getInt("YAYINEVI_ID");
                kitap.fiyat = Integer.toString(rs.getInt("FIYAT"));
                kitap.stok = Integer.toString(rs.getInt("STOK"));
                kitap.satisMiktari = Integer.toString(rs.getInt("SATIS_MIKTARI"));
                kitap.kategori = rs.getString("KATEGORI");
                kitap.sayfaSayisi = Integer.toString(rs.getInt("SAYFA_SAYISI"));
                
                 String yazarSorgu = "select * from APP.YAZARLAR WHERE ID="+_yazarID;
                 PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                 ResultSet rsYazar = psYazar.executeQuery();
                 while(rsYazar.next()){
                     kitap.yazar = rsYazar.getString("AD") + " " +rsYazar.getString("SOYAD");
                 }
                 
                 String yayineviSorgu = "select * from APP.YAYINEVLERI WHERE ID="+_yayineviID;
                 PreparedStatement psYayinevi = connection.prepareStatement(yayineviSorgu);
                 ResultSet rsYayinevi = psYayinevi.executeQuery();
                 while(rsYayinevi.next()){
                     kitap.yayinevi= rsYayinevi.getString("ADI");
                 }
                
                kitaplar.add(kitap);
            }   
            
           
            
            
        } finally{
            connection.close();
        }
        
        return kitaplar;
    }
   

    //kategoriye göre kitapları çekme işlemi 
     public ArrayList<Kitap> fetchKitaplar(String kategori) throws SQLException{
        ArrayList<Kitap> kitaplar = new ArrayList<>();
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            yazarları çekecek sorgu.
                
            select * from APP.YAYINEVLERI;
                
            */
            String query = "select * from APP.KITAPLAR WHERE KATEGORI='"+kategori+"'";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            int _yazarID;
            int _yayineviID;
            
            while(rs.next()){
                Kitap kitap = new Kitap();
                kitap.isbn = rs.getString("ISBN");
                kitap.adi = rs.getString("ADI");
                _yazarID = rs.getInt("YAZAR_ID");
                kitap.basimYili = Integer.toString(rs.getInt("BASIM_YILI"));
                _yayineviID = rs.getInt("YAYINEVI_ID");
                kitap.fiyat = Integer.toString(rs.getInt("FIYAT"));
                kitap.stok = Integer.toString(rs.getInt("STOK"));
                kitap.satisMiktari = Integer.toString(rs.getInt("SATIS_MIKTARI"));
                kitap.kategori = rs.getString("KATEGORI");
                kitap.sayfaSayisi = Integer.toString(rs.getInt("SAYFA_SAYISI"));
                
                 String yazarSorgu = "select * from APP.YAZARLAR WHERE ID="+_yazarID;
                 PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                 ResultSet rsYazar = psYazar.executeQuery();
                 while(rsYazar.next()){
                     kitap.yazar = rsYazar.getString("AD") + " " +rsYazar.getString("SOYAD");
                 }
                 
                 String yayineviSorgu = "select * from APP.YAYINEVLERI WHERE ID="+_yayineviID;
                 PreparedStatement psYayinevi = connection.prepareStatement(yayineviSorgu);
                 ResultSet rsYayinevi = psYayinevi.executeQuery();
                 while(rsYayinevi.next()){
                     kitap.yayinevi= rsYayinevi.getString("ADI");
                 }
                
                kitaplar.add(kitap);
            }   
            
           
            
            
        } finally{
            connection.close();
        }
        
        return kitaplar;
    }
    
    
    
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public String getBasimYili() {
        return basimYili;
    }

    public void setBasimYili(String basimYili) {
        this.basimYili = basimYili;
    }

    public String getYayinevi() {
        return yayinevi;
    }

    public void setYayinevi(String yayinevi) {
        this.yayinevi = yayinevi;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getSatisMiktari() {
        return satisMiktari;
    }

    public void setSatisMiktari(String satisMiktari) {
        this.satisMiktari = satisMiktari;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getSayfaSayisi() {
        return sayfaSayisi;
    }

    public void setSayfaSayisi(String sayfaSayisi) {
        this.sayfaSayisi = sayfaSayisi;
    }

    public String getYazarHakkinda() {
        return yazarHakkinda;
    }

    public void setYazarHakkinda(String yazarHakkinda) {
        this.yazarHakkinda = yazarHakkinda;
    }

    
    
    
    


}
