/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name="userModel")
public class User {
    
    
    private int id;
    private String ad;
    private String soyad;
    private String kayitTarihi;
    private int aktif;
    private String adres;
    //username = email olacak şekilde alınmıştır.
    private String username;
    private String password;
    private String passwordAgain;
    
    public String fetchUserInfo(){
        User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valid_user");
        
        this.id = currentUser.id;
        this.ad = currentUser.ad;
        this.soyad = currentUser.soyad;
        this.adres = currentUser.adres;
        this.username = currentUser.username;
        
        return "/BookSaleManagementSystem/user-secret/user-account.xhtml?faces-redirect=true";
    }
    

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    
    public String getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(String kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
