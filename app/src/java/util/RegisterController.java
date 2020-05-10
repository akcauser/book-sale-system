/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ProgApp
 */

@ManagedBean(name="registerControler")
public class RegisterController {
    
    private User newUser; 
    

    DataSource datasource;
    
    public RegisterController(){
        //veri tabanı bağlantısı yapılıyor.
        try {
            Context ctx = new InitialContext();
            datasource = (DataSource) ctx.lookup("jdbc/addressbook");
        }catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    public String saveUser() throws SQLException, NoSuchAlgorithmException{
        
        if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        if(!(newUser.getPasswordAgain().equals(newUser.getPassword()))){
            
            FacesMessage message = new FacesMessage("Şifreler eşleşmiyor");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("kayitForm:sifre2", message);
            return "/guest-secret/kayit-ol.xhtml";
        }
        
        try{
            //sorgu cümleciği
            /* 
            INSERT INTO KULLANICILAR
            (ADI,SOYADI,SIFRE,EPOSTA,KAYIT_TARIHI,AKTIF,ADRES)
            VALUES('cemil','emir','123' , 'cemil@gmail.com','2019-03-12',1,'İstanbul Zeytinburnu');
            */
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newUser.getPassword().getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            
            PreparedStatement query = connection.prepareStatement("INSERT INTO KULLANICILAR " +
                                        "(ADI,SOYADI,SIFRE,EPOSTA,KAYIT_TARIHI,AKTIF,ADRES) " +
                                        "VALUES(?, ?, ? , ?, ?, 1, ?)");
            
            LocalDate localDate = LocalDate.now();
             
            
            query.setString(1, newUser.getAd());
            query.setString(2, newUser.getSoyad());
            query.setString(3, myHash);
            query.setString(4, newUser.getUsername());
            query.setString(5, DateTimeFormatter.ofPattern("yyy-MM-dd").format(localDate));
            query.setString(6, newUser.getAdres());
            
            query.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Kaydınız Oluşturuldu.Giriş Yapabilirsiniz."));
            return "/guest-secret/kullanici-giris.xhtml";
        }
        finally{
            //Veri tabanı bağlantısını kapat
            connection.close();
        }
                
        
        
        
        
        
    }
    
    
    public User getNewUser() {
        if(newUser == null)
            this.newUser = new User();
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
    
}
