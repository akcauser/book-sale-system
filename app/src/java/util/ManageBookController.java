/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import entity.Admin;
import entity.Kitap;
import entity.Yayinevi;
import entity.Yazar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.Part;
import javax.sql.DataSource;

/**
 *
 * @author ProgApp
 */

@ManagedBean(name="manageBookController")
public class ManageBookController {
    
    int seciliYazarID;
    int seciliYayineviID;
    
    Yazar eklenecekYazar;
    Yayinevi eklenecekYayinevi;
    Kitap eklenecekKitap;
    
    DataSource datasource;
    
    private Part eklenecekResim;
    
  
        
    public ManageBookController(){
            
       
        //veri tabanı bağlantısı için yapıcı metod
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public String kitapEkle() throws SQLException{
        //Kitap eklemek için metod
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
         try {   
            
            InputStream in = eklenecekResim.getInputStream();
            File f = new File("C:\\Users\\ProgApp\\Documents\\NetBeansProjects\\BookSaleManagementSystem\\web\\images\\kitaplar\\"+eklenecekKitap.getIsbn()+".jpg");
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            
            byte[] buffer = new byte[1024];
            int length;
             
            while((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            
            out.close();
            in.close();
            
            
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        
        try{
            /* 
            kitap eklemek için gerekli sorgular 
            yazarId biliniyor
            yayıneviId biliniyor 
            kitap adı alındı
            isbn alındı 
            basım yılı alındı
            fiyat alındı
            stok alındı
            satış miktarı  = 0 
            kategori alındı
            eklendiği tarih = değiştirilenTarih = now
            ekleyen üye = değiştirenuye = current session username-name??
            sayfası sayısı alındı 
            
            kitap ekleme sorgu = : 
            INSERT INTO KITAPLAR 
            (ISBN, ADI, YAZAR_ID, BASIM_YILI, YAYINEVI_ID, FIYAT, STOK, SATIS_MIKTARI, KATEGORI, EKLENDIGI_TARIH, EKLEYEN_UYE, SAYFA_SAYISI, DEGISTIREN_UYE, DEGISTIRDIGI_TARIH)
            VALUES ('1', 'ASD', 2, 1233, 2, 2, 2, 2,'ASD','1998-01-01','ALİ',123,'ASD','1999-01-01')
            
            */
            PreparedStatement ps = connection.prepareStatement("INSERT INTO KITAPLAR "+
"(ISBN, ADI, YAZAR_ID, BASIM_YILI, YAYINEVI_ID, FIYAT, STOK, SATIS_MIKTARI, KATEGORI, EKLENDIGI_TARIH, EKLEYEN_UYE, SAYFA_SAYISI, DEGISTIREN_UYE, DEGISTIRDIGI_TARIH) " +
"VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?, ?, ?)");
            
            LocalDate localDate = LocalDate.now();
                
            
            
            Admin currentAdmin = (Admin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_admin");
            
                   
                
            
            ps.setString(1, eklenecekKitap.getIsbn());
            ps.setString(2, eklenecekKitap.getAdi());
            ps.setInt(3, seciliYazarID);
            ps.setInt(4, Integer.parseInt(eklenecekKitap.getBasimYili()));
            ps.setInt(5, seciliYayineviID);
            ps.setInt(6, Integer.parseInt(eklenecekKitap.getFiyat()));
            ps.setInt(7, Integer.parseInt(eklenecekKitap.getStok()));
            ps.setString(8, eklenecekKitap.getKategori());
            ps.setString(9, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
            ps.setString(10, currentAdmin.getUsername());
            ps.setInt(11, Integer.parseInt(eklenecekKitap.getSayfaSayisi()));
            ps.setString(12, currentAdmin.getUsername());
            ps.setString(13, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
            
            ps.executeUpdate();
            FacesMessage message = new FacesMessage("Kitap Eklendi");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("kitapEkleForm", message);
            return "/admin-secret/manage-book.xhtml?faces-redirect=true";
        }
        finally{
            connection.close();
        }
       
    }
    
    public String yazarEkle() throws SQLException{
        //Kitap eklemeden önce yazar eklemek için metod
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        
        try {
           /*
            Yazar eklemek için gerekli sorgu:
            INSERT INTO YAZARLAR (AD, SOYAD, DOGUM_YILI, BIYOGRAFI)
            VALUES('Reşat Nuri', 'Güntekin', 1889, 'Çağdaş Türk Edebiyatının ünlü roman, öykü ve tiyatro yazarı Reşat Nuri Güntekin, 25 kasım 1889 tarihinde İstanbul`da doğdu. Babası Askerî Doktor Nuri Bey, annesi Erzurum Valisi Yaver Paşa`nın kızı Lütfiye Hanım`dır.')

            */ 
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO YAZARLAR (AD, SOYAD, DOGUM_YILI, BIYOGRAFI)" +
                                                                "VALUES(?, ?, ?, ?)");
            ps.setString(1, eklenecekYazar.getAdi());
            ps.setString(2, eklenecekYazar.getSoyadi());
            ps.setString(3, eklenecekYazar.getDogum_yili());
            ps.setString(4, eklenecekYazar.getBiyografisi());
            
            ps.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Yazar Eklendi"));
            return "/admin-secret/yazar-ekle.xhtml?faces-redirect=true";
           
           
            
        } finally {
            
            connection.close();
        }
        
    
    
    }
    
    public String yayineviEkle() throws SQLException{
        //Kitap eklemeden önce yayınevi eklemek için metod
         if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        
        try {
           /*
            Yazar eklemek için gerekli sorgu:
            "INSERT INTO YAZARLAR (ADI, TELEFON, ADRES) VALUES(?, ?, ?, ?)"
            */ 
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO YAYINEVLERI (ADI, TELEFON, ADRES)" +
                                                                "VALUES(?, ?, ?)");
            ps.setString(1, eklenecekYayinevi.getAdi());
            ps.setString(2, eklenecekYayinevi.getTelefonNumarasi());
            ps.setString(3, eklenecekYayinevi.getAdres());
            
            
            ps.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Yazar Eklendi"));
            return "/admin-secret/yayinevi-ekle.xhtml?faces-redirect=true";
           
           
            
        } finally {
            
            connection.close();
        }
        
    }

    public Yazar getEklenecekYazar() {
        if(eklenecekYazar == null)
            eklenecekYazar = new Yazar();
        return eklenecekYazar;
    }

    public void setEklenecekYazar(Yazar eklenecekYazar) {
        this.eklenecekYazar = eklenecekYazar;
    }

    public Yayinevi getEklenecekYayinevi() {
        if(eklenecekYayinevi == null)
            eklenecekYayinevi = new Yayinevi();
        return eklenecekYayinevi;
    }

    public void setEklenecekYayinevi(Yayinevi eklenecekYayinevi) {
        this.eklenecekYayinevi = eklenecekYayinevi;
    }

    public int getSeciliYazarID() {
        return seciliYazarID;
    }

    public void setSeciliYazarID(int seciliYazarID) {
        this.seciliYazarID = seciliYazarID;
    }

    public int getSeciliYayineviID() {
        return seciliYayineviID;
    }

    public void setSeciliYayineviID(int seciliYayineviID) {
        this.seciliYayineviID = seciliYayineviID;
    }

    public Kitap getEklenecekKitap() {
        if(eklenecekKitap == null)
            eklenecekKitap = new Kitap();
        return eklenecekKitap;
    }

    public void setEklenecekKitap(Kitap eklenecekKitap) {
        this.eklenecekKitap = eklenecekKitap;
    }

    public Part getEklenecekResim() {
        return eklenecekResim;
    }

    public void setEklenecekResim(Part eklenecekResim) {
        this.eklenecekResim = eklenecekResim;
    }

    
    
    
    
    
    
    
    
    
    
}
