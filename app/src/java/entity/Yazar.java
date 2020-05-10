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

@ManagedBean(name="yazarModel")
public class Yazar {
    private int id;
    private String adi;
    private String soyadi;
    private String dogum_yili;
    private String biyografisi;
    
    DataSource datasource;
    
    public Yazar(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public ArrayList<Yazar> fetchYazarlar() throws SQLException{
        ArrayList<Yazar> yazarlar = new ArrayList<>();
        
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
                
            select * from APP.YAZARLAR;
                
            */
            String query = "select * from APP.YAZARLAR";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Yazar yazar = new Yazar();
                yazar.adi = rs.getString("AD");
                yazar.soyadi = rs.getString("SOYAD");
                yazar.dogum_yili = rs.getString("DOGUM_YILI");
                yazar.biyografisi = rs.getString("BIYOGRAFI");
                yazar.id = rs.getInt("ID");
                yazarlar.add(yazar);
            }   
            
            
        } finally{
            connection.close();
        }
        
        
        
        return yazarlar;
    }
    
  

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
