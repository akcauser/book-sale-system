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
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import util.NavigationClass;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name="siparisModel")
public class Siparis {
    private String siparisNo;
    private int toplamTutar;
    private int musteriId;
    private int sonDurum;

    public int getSonDurum() {
        return sonDurum;
    }

    public void setSonDurum(int sonDurum) {
        this.sonDurum = sonDurum;
    }
    
    public String getSiparisNo() {
        return siparisNo;
    }

    public void setSiparisNo(String siparisNo) {
        this.siparisNo = siparisNo;
    }

    public int getToplamTutar() {
        return toplamTutar;
    }

    public void setToplamTutar(int toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public int getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(int musteriId) {
        this.musteriId = musteriId;
    }

    
    
    DataSource datasource;
    
    public Siparis(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public ArrayList<Siparis> fetchSiparisler() throws SQLException{
        ArrayList<Siparis> siparisler = new ArrayList<>();
        
        if(datasource == null)
            throw new SQLException("Datasoruce bağlantı hatası");
        
          Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            /*
            Siparişler getirilecek 
            sorgu :
            SELECT * FROM ORDERS;
            
            */
            
            PreparedStatement psSiparisler = connection.prepareStatement("SELECT*FROM ORDERS");
            ResultSet rsSiparisler = psSiparisler.executeQuery();
            
            while(rsSiparisler.next()){
                Siparis siparis = new Siparis();
                siparis.siparisNo = rsSiparisler.getString("SIPARIS_NO");
                siparis.toplamTutar = rsSiparisler.getInt("TOPLAM_TUTAR");
                siparis.musteriId = rsSiparisler.getInt("MUSTERI_ID");
                siparis.sonDurum = rsSiparisler.getInt("DURUM");
                
                siparisler.add(siparis);
            }
            
            
        }finally{
            connection.close();
        }
        
        
        return siparisler;
    }
    
     public ArrayList<Siparis> fetchMySiparisler() throws SQLException{
        ArrayList<Siparis> siparisler = new ArrayList<>();
        
        if(datasource == null)
            throw new SQLException("Datasoruce bağlantı hatası");
        
          Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
        
        
        try{
            /*
            Siparişler getirilecek 
            sorgu :
            SELECT * FROM ORDERS;
            
            */
            
            PreparedStatement psSiparisler = connection.prepareStatement("SELECT*FROM ORDERS WHERE MUSTERI_ID="+currentUser.getId());
            ResultSet rsSiparisler = psSiparisler.executeQuery();
            
            while(rsSiparisler.next()){
                Siparis siparis = new Siparis();
                siparis.siparisNo = rsSiparisler.getString("SIPARIS_NO");
                siparis.toplamTutar = rsSiparisler.getInt("TOPLAM_TUTAR");
                siparis.musteriId = rsSiparisler.getInt("MUSTERI_ID");
                siparis.sonDurum = rsSiparisler.getInt("DURUM");
                siparisler.add(siparis);
            }
            
            
        }finally{
            connection.close();
        }
        
        
        return siparisler;
    }
    
     public ArrayList<Kitap> fetchSipariskitaplar() throws SQLException{
        ArrayList<Kitap> sepetKitaplar = new ArrayList<>();
      
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            KULLANICININ SEPETİNİ çekecek sorgu.
                
            select * FROM KITAPLAR WHERE ISBN IN (SELECT SEPETLER.ISBN FROM SEPETLER WHERE USER_ID=32)
                
            */
            
            
            
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
           
            String query = "select * FROM KITAPLAR WHERE ISBN IN (SELECT ORDERS_KITAP.ISBN FROM ORDERS_KITAP WHERE USER_ID="+currentUser.getId()+")";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            int _yazarID;
            int _yayineviID;
            
            while(rs.next()){
                Kitap kitap = new Kitap();
                kitap.setIsbn(rs.getString("ISBN"));
                kitap.setAdi(rs.getString("ADI"));
                _yazarID = rs.getInt("YAZAR_ID");
                kitap.setBasimYili(Integer.toString(rs.getInt("BASIM_YILI")));
                _yayineviID = rs.getInt("YAYINEVI_ID");
                kitap.setFiyat(Integer.toString(rs.getInt("FIYAT")));
                kitap.setStok(Integer.toString(rs.getInt("STOK")));
                kitap.setSatisMiktari(Integer.toString(rs.getInt("SATIS_MIKTARI")));
                kitap.setKategori(rs.getString("KATEGORI"));
                kitap.setSayfaSayisi(Integer.toString(rs.getInt("SAYFA_SAYISI")));
                
                 String yazarSorgu = "select * from APP.YAZARLAR WHERE ID="+_yazarID;
                 PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                 ResultSet rsYazar = psYazar.executeQuery();
                 while(rsYazar.next()){
                     kitap.setYazar(rsYazar.getString("AD") + " " +rsYazar.getString("SOYAD"));
                 }
                 
                 String yayineviSorgu = "select * from APP.YAYINEVLERI WHERE ID="+_yayineviID;
                 PreparedStatement psYayinevi = connection.prepareStatement(yayineviSorgu);
                 ResultSet rsYayinevi = psYayinevi.executeQuery();
                 while(rsYayinevi.next()){
                     kitap.setYayinevi(rsYayinevi.getString("ADI"));
                 }
                
                sepetKitaplar.add(kitap);
            }   
            
            
        } finally{
            connection.close();
        }
        
        
        
        return sepetKitaplar;
    
    }
    
    public ArrayList<Cd> fetchSiparisCdler() throws SQLException{
        ArrayList<Cd> sepetCdler = new ArrayList<Cd>();
      
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            KULLANICININ SEPETİNİ çekecek sorgu.
                
            select * FROM KITAPLAR WHERE ISBN IN (SELECT SEPETLER.ISBN FROM SEPETLER WHERE USER_ID=32)
                
            */
            
            
            
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
           
            String query = "select * FROM CDLER WHERE ISBN IN (SELECT ORDERS_CD.ISBN FROM ORDERS_CD WHERE USER_ID="+currentUser.getId()+"AND SIPARIS_NO="+siparisNo+")";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            int _artistID;
            
            
            while(rs.next()){
                Cd cd = new Cd();
                cd.setIsbn(rs.getString("ISBN"));
                cd.setAdi(rs.getString("ADI"));
                _artistID = rs.getInt("ARTIST_ID");
                cd.setFiyat(Integer.toString(rs.getInt("FIYAT")));
                cd.setStok(Integer.toString(rs.getInt("STOK")));
                cd.setSatisMiktari(Integer.toString(rs.getInt("SATIS_MIKTARI")));
                cd.setKategori(rs.getString("KATEGORI"));
                
                
                 String yazarSorgu = "select * from APP.ARTISTLER WHERE ID="+_artistID;
                 PreparedStatement psYazar = connection.prepareStatement(yazarSorgu);
                 ResultSet rsArtist = psYazar.executeQuery();
                 while(rsArtist.next()){
                     cd.setArtist(rsArtist.getString("AD") + " " +rsArtist.getString("SOYAD"));
                 }
               
                sepetCdler.add(cd);
            }   
            
            
        } finally{
            connection.close();
        }
        
        
        
        return sepetCdler;
    
    }
     
    public String siparisDetayaGit(String siparisNo){
        this.siparisNo = siparisNo;
        
        
        return "user-secret/siparis-detay.xhtml?faces-redirect=true";
    }
    
    public String siparisDurumuDegistir(String sipNo, int durum) throws SQLException{
        
          if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        
        try {
            /* sorgu:
            Sipariş durumunu değiştirme sorgu
            
            UPDATE ORDERS 
            SET DURUM=1
            WHERE SIPARIS_NO=
            */
            
            int newStatus;
            if(durum == 1){
                newStatus=0;
            }else{
                newStatus=1;
            }
            
          
                 String query = "UPDATE ORDERS " +
                            "SET DURUM="+newStatus+
                            "WHERE SIPARIS_NO='"+sipNo+"'";
                 PreparedStatement ps = connection.prepareStatement(query);
                 ps.executeUpdate();
            
            
            
            
        } finally{
            connection.close();
        }
            
        
        return "";
    }
    
}
