/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.Admin;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author ProgApp
 */

@ManagedBean(name="adminLoginController")
@RequestScoped
public class AdminLoginController {
 
    private Admin admin;

    public Admin getAdmin() {
        if(this.admin == null)
            this.admin = new Admin();
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    DataSource datasource;
    
    public AdminLoginController(){
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
        md.update(admin.getPassword().getBytes());
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
            SELECT sifre FROM ADMINLER WHERE EPOSTA='admin@gmail.com'
            
            */
            String query = "SELECT sifre FROM ADMINLER WHERE EPOSTA='"+admin.getUsername()+"'";
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
                context.addMessage("adminForm:email", message);
                
                return "/guest-secret/admin-giris.xhtml";
            }
            
            if(!(myHash.equals(tempPassword))){
                FacesMessage message = new FacesMessage("Şifre hatalı");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("adminForm:sifre", message);
                return "/guest-secret/admin-giris.xhtml";
            }
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("valid_admin", this.admin);    
            return "/admin-secret/manage-orders.xhtml?faces-redirect=true";
            
        }finally{
            connection.close();
        }    
    }
}
