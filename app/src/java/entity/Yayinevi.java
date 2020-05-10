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
@ManagedBean(name="yayineviModel")
public class Yayinevi {
    private int id;
    private String adi;
    private String telefonNumarasi;
    private String adres;
    
    
    DataSource datasource;
    
    public Yayinevi(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Yayinevi> fetchYayinevleri() throws SQLException{
        ArrayList<Yayinevi> yayinevleri = new ArrayList<>();
        
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
            String query = "select * from APP.YAYINEVLERI";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Yayinevi yayinevi = new Yayinevi();
                yayinevi.id = rs.getInt("ID");
                yayinevi.adi = rs.getString("ADI");
                yayinevi.telefonNumarasi = rs.getString("TELEFON");
                yayinevi.adres = rs.getString("ADRES");
                
                yayinevleri.add(yayinevi);
            }   
            
            
        } finally{
            connection.close();
        }
        
        return yayinevleri;
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

    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }

    public void setTelefonNumarasi(String telefonNumarasi) {
        this.telefonNumarasi = telefonNumarasi;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
    
    
    
    
    
}
