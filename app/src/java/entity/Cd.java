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
@ManagedBean(name = "cdModel")
public class Cd {

    private String isbn;
    private String adi;
    private String artist;
    private String fiyat;
    private String stok;
    private String satisMiktari;
    private String kategori;
    private String artistHakkinda;

    DataSource datasource;

    public String fetchOneCd(String cdIsbn) throws SQLException {

        if (datasource == null) {
            throw new SQLException("datasource bulunamadı...");
        }

        Connection connection = datasource.getConnection();

        if (connection == null) {
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        try{
            /*
                1 tane cd yi özellikleri ile çeken sorgu
                SELECT CDLER.ISBN,
                        CDLER.ADI AS CD_ADI,
                        CDLER.KATEGORI,
                        CDLER.FIYAT,
                        CDLER.STOK,
                        ARTISTLER.AD AS ARTIST_ADI,
                        ARTISTLER.SOYAD AS ARTIST_SOYADI,
                        ARTISTLER.BIYOGRAFI AS ARTIST_HAKKINDA
                FROM CDLER 
                INNER JOIN ARTISTLER ON CDLER.ARTIST_ID = ARTISTLER.ID
                WHERE ISBN='1111111111111'
            */
            PreparedStatement ps = connection.prepareStatement("SELECT CDLER.ISBN, " +
                                                                        "CDLER.ADI AS CD_ADI, " +
                                                                        "CDLER.KATEGORI, " +
                                                                        "CDLER.FIYAT, " +
                                                                        "CDLER.STOK, " +
                                                                        "ARTISTLER.AD AS ARTIST_ADI, " +
                                                                        "ARTISTLER.SOYAD AS ARTIST_SOYADI, " +
                                                                        "ARTISTLER.BIYOGRAFI AS ARTIST_HAKKINDA " +
                                                                "FROM CDLER " +
                                                                "INNER JOIN ARTISTLER ON CDLER.ARTIST_ID = ARTISTLER.ID " +
                                                                "WHERE ISBN='"+cdIsbn+"'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                this.adi = rs.getString("CD_ADI");
                this.artist = rs.getString("ARTIST_ADI") + " " + rs.getString("ARTIST_SOYADI");
                this.fiyat = rs.getString("FIYAT");
                this.stok = rs.getString("STOK");
                this.isbn = rs.getString("ISBN");
                this.kategori = rs.getString("KATEGORI");
                this.artistHakkinda = rs.getString("ARTIST_HAKKINDA");
                
                
            }
            
            
            
                
        }finally{
            connection.close();
        }
        
        
        
        

        return "cd-incele.xhtml";
    }

    public Cd() {
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //cd lerin çekildiği yer
    public ArrayList<Cd> fetchCDler() throws SQLException {
        ArrayList<Cd> cdler = new ArrayList<>();

        if (datasource == null) {
            throw new SQLException("datasource bulunamadı...");
        }

        Connection connection = datasource.getConnection();

        if (connection == null) {
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }

        try {
            /* sorgu:
             cd çekecek sorgu.
                
             select * from APP.CDLER;
                
             */
            String query = "select * from APP.CDLER";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int _artistID;

            while (rs.next()) {
                Cd cd = new Cd();
                cd.isbn = rs.getString("ISBN");
                cd.adi = rs.getString("ADI");
                _artistID = rs.getInt("ARTIST_ID");
                cd.fiyat = Integer.toString(rs.getInt("FIYAT"));
                cd.stok = Integer.toString(rs.getInt("STOK"));
                cd.satisMiktari = Integer.toString(rs.getInt("SATIS_MIKTARI"));
                cd.kategori = rs.getString("KATEGORI");

                //Artist ID ye göre Artist bilgisi alınıyor
                String yazarSorgu = "select * from APP.ARTISTLER WHERE ID=" + _artistID;
                PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                ResultSet rsYazar = psYazar.executeQuery();
                while (rsYazar.next()) {
                    cd.artist = rsYazar.getString("AD") + " " + rsYazar.getString("SOYAD");
                }

                cdler.add(cd);
            }

        } finally {
            connection.close();
        }

        return cdler;
    }

    //cd lerin çekildiği yer kategoriye göre 
    public ArrayList<Cd> fetchCDler(String kategori) throws SQLException {
        ArrayList<Cd> cdler = new ArrayList<>();

        if (datasource == null) {
            throw new SQLException("datasource bulunamadı...");
        }

        Connection connection = datasource.getConnection();

        if (connection == null) {
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }

        try {
            /* sorgu:
             cd çekecek sorgu.
                
             select * from APP.CDLER WHERE KATEGORI='';
                
             */
            String query = "select * from APP.CDLER WHERE KATEGORI='" + kategori + "'";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int _artistID;

            while (rs.next()) {
                Cd cd = new Cd();
                cd.isbn = rs.getString("ISBN");
                cd.adi = rs.getString("ADI");
                _artistID = rs.getInt("ARTIST_ID");
                cd.fiyat = Integer.toString(rs.getInt("FIYAT"));
                cd.stok = Integer.toString(rs.getInt("STOK"));
                cd.satisMiktari = Integer.toString(rs.getInt("SATIS_MIKTARI"));
                cd.kategori = rs.getString("KATEGORI");

                //Artist ID ye göre Artist bilgisi alınıyor
                String yazarSorgu = "select * from APP.ARTISTLER WHERE ID=" + _artistID;
                PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                ResultSet rsYazar = psYazar.executeQuery();
                while (rsYazar.next()) {
                    cd.artist = rsYazar.getString("AD") + " " + rsYazar.getString("SOYAD");
                }

                cdler.add(cd);
            }

        } finally {
            connection.close();
        }

        return cdler;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getArtistHakkinda() {
        return artistHakkinda;
    }

    public void setArtistHakkinda(String artistHakkinda) {
        this.artistHakkinda = artistHakkinda;
    }
    
    
}
