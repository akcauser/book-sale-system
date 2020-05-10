/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name = "manageUsersController")
public class ManageUsersController {
    
    DataSource datasource;
  
    
    public ManageUsersController(){
        try {
            Context context = new InitialContext();
            datasource = (DataSource) context.lookup("jdbc/addressbook");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getUsers() throws SQLException{
        
        if(datasource == null){
            throw new SQLException("datasource bulunamadı...");
        }
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Bağlantı kurulamadı...");
        }
        
        try {
            
            /*
            SORGU : 
            select ID,ADI,SOYADI,EPOSTA,KAYIT_TARIHI,AKTIF,ADRES FROM KULLANICILAR
            */
            PreparedStatement preparedStatement1 = connection.prepareStatement("select ID,ADI,SOYADI,EPOSTA,KAYIT_TARIHI,AKTIF,ADRES FROM KULLANICILAR");
            CachedRowSet resultset1 = new com.sun.rowset.CachedRowSetImpl();
            resultset1.populate(preparedStatement1.executeQuery());
            return resultset1;
        } finally {
            
            connection.close();
        }
        
        
        
    }
    
    public String changeUserStatus(int id,int currentStatus) throws SQLException{
         if(datasource == null)
            throw new SQLException("Datasource bağlantı hatası");
        
        Connection connection = datasource.getConnection();
        
        if(connection == null){
            throw new SQLException("Veritabanı bağlantı hatası");
        }
        
        
        try{
            //sorgu cümleciği
            /* 
            UPDATE KULLANICILAR
            SET AKTIF=0
            WHERE ID=9
            */
            
            int newStatus;
            if(currentStatus == 1){
                newStatus=0;
            }else{
                newStatus=1;
            }
            
            PreparedStatement query = connection.prepareStatement("UPDATE KULLANICILAR " +
                                                                  "SET AKTIF="+newStatus+" "+
                                                                  "WHERE ID="+id);
            
            query.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("İşlem tamamlandı.."));
                   }
        finally{
            //Veri tabanı bağlantısını kapat
            connection.close();
        }
                
        return "/admin-secret/manage-user.xhtml?faces-redirect=true";
    }
    
   
}
