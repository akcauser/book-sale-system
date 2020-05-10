/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import entity.Admin;
import entity.Artist;
import entity.Cd;
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
@ManagedBean(name="manageCdController")
public class manageCdController {
    
    int seciliArtistID;
    
    Artist eklenecekArtist;
    
    Cd eklenecekCD;
    
    DataSource datasource;
    
    private Part eklenecekResim;
        
    public manageCdController(){
        //veri tabanı bağlantısı için yapıcı metod
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public String cdEkle() throws SQLException{
        
        //Kitap eklemek için metod
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
         try {   
            
            InputStream in = eklenecekResim.getInputStream();
            File f = new File("C:\\Users\\ProgApp\\Documents\\NetBeansProjects\\BookSaleManagementSystem\\web\\images\\cdler\\"+eklenecekCD.getIsbn()+".jpg");
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
            cd eklemek için gerekli sorgular 
            artistId biliniyor
            
            cd adı alındı
            isbn alındı 
            
            fiyat alındı
            stok alındı
            satış miktarı  = 0 
            kategori alındı
            eklendiği tarih = değiştirilenTarih = now
            ekleyen üye = değiştirenuye = current session username-name??
            
            
            cd ekleme sorgu = : 
            INSERT INTO CDLER 
            (ISBN, ADI, ARTIST_ID, FIYAT, STOK, SATIS_MIKTARI, KATEGORI, KAYDETTIGI_TARIH, KAYDEDEN_UYE, DEGISTIREN_UYE, DEGISTIRDIGI_TARIH)
            VALUES ('1', 'ASD', 2, 1233, 2, 2, 'yerli', '1998-01-01', 'ALİ', 'veli','1999-01-01')
            
            */
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CDLER " +
            "(ISBN, ADI, ARTIST_ID, FIYAT, STOK, SATIS_MIKTARI, KATEGORI, KAYDETTIGI_TARIH, KAYDEDEN_UYE, DEGISTIREN_UYE, DEGISTIRDIGI_TARIH) " +
            "VALUES (?, ?, ?, ?, ?, 0, ?, ?, ?, ?, ?)");
            
            LocalDate localDate = LocalDate.now();
            
            Admin currentAdmin = (Admin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_admin");
            
            ps.setString(1, eklenecekCD.getIsbn());
            ps.setString(2, eklenecekCD.getAdi());
            ps.setInt(3, seciliArtistID);
            ps.setInt(4, Integer.parseInt(eklenecekCD.getFiyat()));
            ps.setInt(5, Integer.parseInt(eklenecekCD.getStok()));
            ps.setString(6, eklenecekCD.getKategori());
            ps.setString(7, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
            ps.setString(8, currentAdmin.getUsername());
            ps.setString(9, currentAdmin.getUsername());
            ps.setString(10, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
            
            ps.executeUpdate();
            FacesMessage message = new FacesMessage("Cd Eklendi");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("cdEkleForm", message);
            return "/admin-secret/manage-cd.xhtml?faces-redirect=true";
        }
        finally{
            connection.close();
        }
       
        
        
    }
    //artist eklemek için kullanılan fonksiyon
    public String artistEkle() throws SQLException{
        
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        
        try {
           /*
            Artis eklemek için gerekli sorgu:
            INSERT INTO ARTISTLER (AD, SOYAD, DOGUM_YILI, BIYOGRAFI)
            VALUES('Reşat Nuri', 'Güntekin', 1889, 'Çağdaş Türk Edebiyatının ünlü roman, öykü ve tiyatro yazarı Reşat Nuri Güntekin, 25 kasım 1889 tarihinde İstanbul`da doğdu. Babası Askerî Doktor Nuri Bey, annesi Erzurum Valisi Yaver Paşa`nın kızı Lütfiye Hanım`dır.')

            */ 
            
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ARTISTLER (AD, SOYAD, DOGUM_YILI, BIYOGRAFI)" +
                                                                "VALUES(?, ?, ?, ?)");
            ps.setString(1, eklenecekArtist.getAdi());
            ps.setString(2, eklenecekArtist.getSoyadi());
            ps.setString(3, eklenecekArtist.getDogum_yili());
            ps.setString(4, eklenecekArtist.getBiyografisi());
            
            ps.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Yazar Eklendi"));
            return "/admin-secret/cd-ekle.xhtml?faces-redirect=true";
           
           
            
        } finally {
            
            connection.close();
        }
    }

    public int getSeciliArtistID() {
        return seciliArtistID;
    }

    public void setSeciliArtistID(int seciliArtistID) {
        this.seciliArtistID = seciliArtistID;
    }

    public Artist getEklenecekArtist() {
        if(eklenecekArtist == null)
            eklenecekArtist = new Artist();
        return eklenecekArtist;
    }

    public void setEklenecekArtist(Artist eklenecekArtist) {
        this.eklenecekArtist = eklenecekArtist;
    }

    public Cd getEklenecekCD() {
        if(eklenecekCD == null)
            eklenecekCD = new Cd();
        return eklenecekCD;
    }

    public void setEklenecekCD(Cd eklenecekCD) {
        this.eklenecekCD = eklenecekCD;
    }

    public Part getEklenecekResim() {
        return eklenecekResim;
    }

    public void setEklenecekResim(Part eklenecekResim) {
        this.eklenecekResim = eklenecekResim;
    }
    
    
    
    
}
