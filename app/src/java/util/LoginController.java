/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.User;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;




/**
 *
 * @author ProgApp
 */

@ManagedBean(name="loginController")
@SessionScoped
public class LoginController implements Serializable {
    private User user;
    
    DataSource datasource;
    
    public LoginController(){
         try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login() throws SQLException, NoSuchAlgorithmException{
        
        //MD5 HASHING ALGORİTMASI 
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(user.getPassword().getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        
       if (datasource == null){
           throw new SQLException("datasource bulunamadı...");
       }
       
        Connection connection  = datasource.getConnection();
        
        if (connection == null){
            throw new SQLException("veritabanı bağlantısı kurulamadı...");
        }
        
        try {
            /* 
            girilen kullanıcı bilgisi kontrol edilecek
            kullanıcı adı kontrol edilecek 
            kullanıcı varsa ; şifresi eşleşiyor mu bakılacak 
                şifre de eşleşiyorsa ; giriş yapılacak
                şifre eşleşmiyorsa ; kullanıcı şifre uyuşmuyor mesajı 
            kullanıcı yoksa ; direk böyle bir kullanıcı-şifre uyumlu değil mesajı ;
            
            ! sorgu kullanıcı ve şifre çeken sorgu :
            SELECT sifre FROM KULLANICILAR WHERE EPOSTA='admin@gmail.com' and AKTIF=1
            
            */
            String query = "SELECT sifre FROM KULLANICILAR WHERE EPOSTA='"+user.getUsername()+"' and AKTIF=1";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            boolean kullaniciVarmi = false;
            String tempPassword = "";
            while(rs.next()){
                kullaniciVarmi = true;
                tempPassword = rs.getString("SIFRE");
            }
            
            if(!kullaniciVarmi){
                
                FacesMessage message = new FacesMessage("Eposta hatalı");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("myform:email", message);
                return "/guest-secret/kullanici-giris.xhtml";
            }
            
            if(!(myHash.equals(tempPassword))){
                FacesMessage message = new FacesMessage("Şifre hatalı");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("girisForm:password", message);
                return "/guest-secret/kullanici-giris.xhtml";
            }
            /*
                "SELECT * FROM KULLANICILAR WHERE EPOSTA='"+user.getUsername()+"'"
                
            */
            
            PreparedStatement psGetCurrentUser = connection.prepareStatement("SELECT * FROM KULLANICILAR"+
                                                                    " WHERE EPOSTA='"+user.getUsername()+"'");
            
            ResultSet rsCurrentUser = psGetCurrentUser.executeQuery();
            
            while(rsCurrentUser.next()){
                this.user.setAd(rsCurrentUser.getString("ADI"));
                this.user.setSoyad(rsCurrentUser.getString("SOYADI"));
                this.user.setId(rsCurrentUser.getInt("ID"));
                this.user.setAdres(rsCurrentUser.getString("ADRES"));
                this.user.setAktif(rsCurrentUser.getInt("AKTIF"));
                this.user.setKayitTarihi(rsCurrentUser.getString("KAYIT_TARIHI"));
                
            }
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("valid_user", this.user);
            return "/user-secret/user-main.xhtml?faces-redirect=true";
            
        }finally{
            connection.close();
        }
        
    }
    
    public User getUser() {
        if (this.user == null){
            this.user = new User();
        }    
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
