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
@ManagedBean(name="artistModel")
public class Artist {
    
    private int id;
    private String adi;
    private String soyadi;
    private String dogum_yili;
    private String biyografisi;
    
    DataSource datasource;
    
    public Artist(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Artistleri getiren fonksiyon
    public ArrayList<Artist> fetchArtistler() throws SQLException{
        ArrayList<Artist> artistler = new ArrayList<>();
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            artistleri çekecek sorgu.
                
            select * from APP.ARTISTLER;
                
            */
            String query = "select * from APP.ARTISTLER";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Artist artist = new Artist();
                artist.adi = rs.getString("AD");
                artist.soyadi = rs.getString("SOYAD");
                artist.dogum_yili = rs.getString("DOGUM_YILI");
                artist.biyografisi = rs.getString("BIYOGRAFI");
                artist.id = rs.getInt("ID");
                artistler.add(artist);
            }   
            
            
        } finally{
            connection.close();
        }
        
        return artistler;
    }
    
    

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return soyadi;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public String getDogum_yili() {
        return dogum_yili;
    }

    public void setDogum_yili(String dogum_yili) {
        this.dogum_yili = dogum_yili;
    }

    public String getBiyografisi() {
        return biyografisi;
    }

    public void setBiyografisi(String biyografisi) {
        this.biyografisi = biyografisi;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
  
}
