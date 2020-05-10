/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.Cd;
import entity.Kitap;
import entity.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name="sepetController")
public class SepetController {
    DataSource datasource;
    
    private String isbn;
    private int kullaniciID;
    
    public SepetController(){
        //veri tabanı bağlantısı için yapıcı metod
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String siparisiTamamla() throws SQLException{
        
        //Siparişi tamamla butonuna tıklandığında, 
        /*
            1- kullanıcıya ait olan siparişler siparisler tablosuna atılacak 
            2- kullanıcıya ait olan sepet bilgileri silinecek 
            3- Siparişiniz verildi. iyi günlerde kullanın ekranına  gidilecek
        
            
        */
        if(datasource == null)
            throw new SQLException("Datasoruce bağlantı hatası");
        
          Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            
            /* 
                Sepetteki verileri siparişler tablosuna atma sorgusu:
                
                SELECT * FROM SEPET_KITAP WHERE USER_ID=32
            
            
                Siparişlere ekleme yapma sorgusu: 
            
                INSERT INTO ORDERS (ISBN, USER_ID, SIPARIS_TARIHI, SIPARIS_DURUMU) 
                VALUES('1233333', 12, '2019-01-01', 0)
                
            
            */
            
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            
            
            //kitap için
            PreparedStatement psSepetByUserID = connection.prepareStatement("SELECT * FROM SEPET_KITAP WHERE USER_ID="+currentUser.getId());
            ResultSet rsSepetByUserID = psSepetByUserID.executeQuery();
            LocalDate localDate = LocalDate.now();
            
             SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
             Date date = new Date();  
            
            String siparisNo = currentUser.getId()+formatter.format(date);
            int toplamTutar = 0;
            
            while(rsSepetByUserID.next()){
                
                PreparedStatement psSipariseTasi = connection.prepareStatement("INSERT INTO ORDERS_KITAP (ISBN, USER_ID, SIPARIS_TARIHI,SIPARIS_NO)" +
                                                                                " VALUES(?, ?, ?, ?)");
                psSipariseTasi.setString(1, rsSepetByUserID.getString("ISBN"));
                psSipariseTasi.setInt(2, currentUser.getId());
                psSipariseTasi.setString(3, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
                
                
                psSipariseTasi.setString(4, siparisNo);
                
                psSipariseTasi.executeUpdate();
                
                //kitap fiyatı toplam tutara ekle
                PreparedStatement psKitapFiyat = connection.prepareStatement("SELECT FIYAT FROM KITAPLAR WHERE ISBN ='"+rsSepetByUserID.getString("ISBN")+"'");
                ResultSet rsfiyat = psKitapFiyat.executeQuery();
                while(rsfiyat.next()){
                    toplamTutar = toplamTutar + rsfiyat.getInt("FIYAT");
                }
                
                
                //Siparişe taşınan sepetten siliniyor
                PreparedStatement psSepettenSil = connection.prepareStatement("DELETE FROM SEPET_KITAP WHERE ISBN= ? AND USER_ID= ?");
                psSepettenSil.setString(1, rsSepetByUserID.getString("ISBN"));
                psSepettenSil.setInt(2, rsSepetByUserID.getInt("USER_ID"));
                
               
                psSepettenSil.executeUpdate();
                
            }
            
            if(toplamTutar == 0){
                connection.close();
                return "";
            }
            
            PreparedStatement psCdSepetByUserID = connection.prepareStatement("SELECT * FROM SEPET_CD WHERE USER_ID="+currentUser.getId());
            ResultSet rsCdSepetByUserID = psCdSepetByUserID.executeQuery();
            
            while(rsCdSepetByUserID.next()){
                
                PreparedStatement psSipariseTasi = connection.prepareStatement("INSERT INTO ORDERS_CD (ISBN, USER_ID, SIPARIS_TARIHI, SIPARIS_NO)" +
                                                                                " VALUES(?, ?, ?, ?)");
                psSipariseTasi.setString(1, rsCdSepetByUserID.getString("ISBN"));
                psSipariseTasi.setInt(2, currentUser.getId());
                psSipariseTasi.setString(3, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
                
                psSipariseTasi.setString(4, siparisNo);
                
                
                psSipariseTasi.executeUpdate();
                
                //cd fiyatları toplam tutara ekle
                PreparedStatement psCdFiyat = connection.prepareStatement("SELECT FIYAT FROM CDLER WHERE ISBN ='"+rsCdSepetByUserID.getString("ISBN")+"'");
                ResultSet rsfiyat = psCdFiyat.executeQuery();
                while(rsfiyat.next()){
                    toplamTutar = toplamTutar + rsfiyat.getInt("FIYAT");
                }
                
                
                //Siparişe taşınan sepetten siliniyor
                PreparedStatement psSepettenSil = connection.prepareStatement("DELETE FROM SEPET_CD WHERE ISBN= ? AND USER_ID= ?");
                psSepettenSil.setString(1, rsCdSepetByUserID.getString("ISBN"));
                psSepettenSil.setInt(2, rsCdSepetByUserID.getInt("USER_ID"));
                
               
                psSepettenSil.executeUpdate();
                
            }
            
            PreparedStatement psSipariEkle = connection.prepareStatement("INSERT INTO ORDERS (SIPARIS_NO, MUSTERI_ID, TOPLAM_TUTAR, DURUM)" +
                                                                                " VALUES(?, ?, ?, 0)");
            
            psSipariEkle.setString(1, siparisNo);
            psSipariEkle.setInt(2, currentUser.getId());
            psSipariEkle.setInt(3, toplamTutar);
            
            psSipariEkle.executeUpdate();
            
            
        }finally{
            connection.close();
        }
        
        
        return "";
    }
   
    
    
    public String sepettenSilCD(String isbn) throws SQLException{
        
        
       
        if(datasource == null)
            throw new SQLException("Datasoruce bağlantı hatası");
        
          Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            //Kullanıcı bilgileri alınacak 
            //isbn bilgisi alınacak
            //Verilen kullanıcı ID ve İSBN e göre sepetten silme yapılıyor.
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            setKullaniciID(currentUser.getId());
            
            PreparedStatement ps = connection.prepareStatement("DELETE FROM SEPET_CD WHERE ISBN ='"+isbn+"' AND USER_ID="+currentUser.getId());
            
            ps.executeUpdate();
         
            
            
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            
            return url;
        }
        finally{
            connection.close();
        }
    
    }
   
    
     public String sepettenSilKitap(String isbn) throws SQLException{
        
        
       
        if(datasource == null)
            throw new SQLException("Datasoruce bağlantı hatası");
        
          Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            //Kullanıcı bilgileri alınacak 
            //isbn bilgisi alınacak
            //Verilen kullanıcı ID ve İSBN e göre sepetten silme yapılıyor.
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            setKullaniciID(currentUser.getId());
            
            PreparedStatement ps = connection.prepareStatement("DELETE FROM SEPET_KITAP WHERE ISBN ='"+isbn+"' AND USER_ID="+currentUser.getId());
            
            ps.executeUpdate();
         
            
            
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            
            return url;
        }
        finally{
            connection.close();
        }
    
    }
     
      
    public String sepeteInceleKitapEkle(String isbn) throws SQLException, IOException{
        
        this.isbn = isbn;
        
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            //Kullanıcı bilgileri alınacak 
            //isbn bilgisi alınacak
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            setKullaniciID(currentUser.getId());
            
            /*
                kitap sepette varsa 
            sorgu:
            SELECT * FROM SEPET_KITAP WHERE ISBN = '?' AND USER_ID= '?';
            */
            
            PreparedStatement psControl = connection.prepareStatement("SELECT * FROM SEPET_KITAP WHERE ISBN = '"+getIsbn()+"' AND USER_ID="+currentUser.getId());
            ResultSet rsControl = psControl.executeQuery();
            while(rsControl.next()){
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String url = request.getRequestURL().toString();
                
                return url;
            }
            
            
            //sepete ekleme yapılacağı yer.
            /*
            sorgu: 
            INSERT INTO SEPET (ISBN, USER_ID) 
            VALUES(?, ?)
                
            
            */
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SEPET_KITAP (ISBN, USER_ID)" +
                                                                            " VALUES(?, ?)");
            ps.setString(1, getIsbn());
            ps.setInt(2, kullaniciID);
            
            ps.executeUpdate();
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            
            
            return "asdasdas";
        }
        finally{
            connection.close();
        }
       
    }
    
    
    public String sepeteKitapEkle(String isbn) throws SQLException, IOException{
        
        this.isbn = isbn;
        
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            //Kullanıcı bilgileri alınacak 
            //isbn bilgisi alınacak
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            setKullaniciID(currentUser.getId());
            
            /*
                kitap sepette varsa 
            sorgu:
            SELECT * FROM SEPET_KITAP WHERE ISBN = '?' AND USER_ID= '?';
            */
            
            PreparedStatement psControl = connection.prepareStatement("SELECT * FROM SEPET_KITAP WHERE ISBN = '"+getIsbn()+"' AND USER_ID="+currentUser.getId());
            ResultSet rsControl = psControl.executeQuery();
            while(rsControl.next()){
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String url = request.getRequestURL().toString();
                
                return url;
            }
            
            
            //sepete ekleme yapılacağı yer.
            /*
            sorgu: 
            INSERT INTO SEPET (ISBN, USER_ID) 
            VALUES(?, ?)
                
            
            */
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SEPET_KITAP (ISBN, USER_ID)" +
                                                                            " VALUES(?, ?)");
            ps.setString(1, getIsbn());
            ps.setInt(2, kullaniciID);
            
            ps.executeUpdate();
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            
            
            return url;
        }
        finally{
            connection.close();
        }
        
    }
    
      public String sepeteCdEkle(String isbn) throws SQLException, IOException{
        
        this.isbn = isbn;
        
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        try{
            //Kullanıcı bilgileri alınacak 
            //isbn bilgisi alınacak
            User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
            setKullaniciID(currentUser.getId());
            
            /*
                cd sepette varsa 
            sorgu:
            SELECT * FROM SEPET_CD WHERE ISBN = '?' AND USER_ID= '?';
            */
            
            PreparedStatement psControl = connection.prepareStatement("SELECT * FROM SEPET_CD WHERE ISBN = '"+getIsbn()+"' AND USER_ID="+currentUser.getId());
            ResultSet rsControl = psControl.executeQuery();
            while(rsControl.next()){
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String url = request.getRequestURL().toString();
                
                return url;
            }
            
            
            //sepete ekleme yapılacağı yer.
            /*
            sorgu: 
            INSERT INTO SEPET (ISBN, USER_ID) 
            VALUES(?, ?)
                
            
            */
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SEPET_CD (ISBN, USER_ID)" +
                                                                            " VALUES(?, ?)");
            ps.setString(1, getIsbn());
            ps.setInt(2, kullaniciID);
            
            ps.executeUpdate();
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            
            
            return url;
        }
        finally{
            connection.close();
        }
        
    }
    
    
    public ArrayList<Kitap> fetchSepetkitaplar() throws SQLException{
        ArrayList<Kitap> sepetKitaplar = new ArrayList<Kitap>();
      
        
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
           
            String query = "select * FROM KITAPLAR WHERE ISBN IN (SELECT SEPET_KITAP.ISBN FROM SEPET_KITAP WHERE USER_ID="+currentUser.getId()+")";
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
    
    public ArrayList<Cd> fetchSepetCdler() throws SQLException{
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
           
            String query = "select * FROM CDLER WHERE ISBN IN (SELECT SEPET_CD.ISBN FROM SEPET_CD WHERE USER_ID="+currentUser.getId()+")";
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
    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getKullaniciID() {
        return kullaniciID;
    }

    public void setKullaniciID(int kullaniciID) {
        this.kullaniciID = kullaniciID;
    }
    
    
    
    
    
    
}
