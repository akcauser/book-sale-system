/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author ProgApp
 */
@ManagedBean(name = "navigation")
public class NavigationClass {

    //UNKNOWN USER NAVIGATION
    public String toIndex() {

        return "/BookSaleManagementSystem/guest-secret/guest-main.xhtml?faces-redirect=true";
    }

    public String toLogin() {

        return "/BookSaleManagementSystem/guest-secret/kullanici-giris.xhtml?faces-redirect=true";
    }

    public String toAdminlogin() {

        return "/BookSaleManagementSystem/guest-secret/admin-giris.xhtml?faces-redirect=true";
    }

    public String toRegister() {

        return "/BookSaleManagementSystem/guest-secret/kayit-ol.xhtml?faces-redirect=true";
    }

    public String toKitap() {

        return "/BookSaleManagementSystem/guest-secret/guest-kitap.xhtml?faces-redirect=true";
    }

    public String toCD() {

        return "/BookSaleManagementSystem/guest-secret/guest-cd.xhtml?faces-redirect=true";
    }
    
     public String toShowOneBook() {

        return "/BookSaleManagementSystem/guest-secret/kitap-incele.xhtml?faces-redirect=true";
    }
    //CD kategorileri
     public String toYerli() {

        return "/BookSaleManagementSystem/guest-secret/cd-kategorileri/yerli-cd.xhtml?faces-redirect=true";
    } 
     public String toYabanci() {

        return "/BookSaleManagementSystem/guest-secret/cd-kategorileri/yabanci-cd.xhtml?faces-redirect=true";
    }

    //LOGGED USER NAVIGATION
    public String toSecretMainPage() {

        return "/BookSaleManagementSystem/user-secret/user-main.xhtml?faces-redirect=true";
    }

    public String toSepet() {

        return "/BookSaleManagementSystem/user-secret/user-sepet.xhtml?faces-redirect=true";
    }

    public String toAccount() {

        return "/BookSaleManagementSystem/user-secret/user-account.xhtml?faces-redirect=true";
    }

    public String toSecretKitap() {

        return "/BookSaleManagementSystem/user-secret/user-kitap.xhtml?faces-redirect=true";
    }

    public String toSecretCd() {

        return "/BookSaleManagementSystem/user-secret/user-cd.xhtml?faces-redirect=true";
    }

     public String toUserYerli() {

        return "/BookSaleManagementSystem/user-secret/cd-kategorileri/user-yerli-cd.xhtml?faces-redirect=true";
    } 
     public String toUserYabanci() {

        return "/BookSaleManagementSystem/user-secret/cd-kategorileri/user-yabanci-cd.xhtml?faces-redirect=true";
    }
    
    public String toOrders() {

        return "/BookSaleManagementSystem/user-secret/user-orders.xhtml?faces-redirect=true";
    }
        
     public String toSiparisDetay() {

        return "/BookSaleManagementSystem/user-secret/siparis-detay.xhtml?faces-redirect=true";
    }

    public String toUserLogout() {
        return "/BookSaleManagementSystem/user-secret/user-logout.xhtml?faces-redirect=true";
    }

    //ADMIN NAVIGATION
    public String toAdminMain() {
        return "/BookSaleManagementSystem/admin-secret/admin-main.xhtml?faces-redirect=true";
    }

    public String toManageBook() {
        return "/BookSaleManagementSystem/admin-secret/manage-book.xhtml?faces-redirect=true";
    }
    public String toAddBook() {
        return "/BookSaleManagementSystem/admin-secret/kitap-ekle.xhtml?faces-redirect=true";
    }
    public String toAddAuthor() {
        return "/BookSaleManagementSystem/admin-secret/yazar-ekle.xhtml?faces-redirect=true";
    }
    public String toAddYayinevi() {
        return "/BookSaleManagementSystem/admin-secret/yayinevi-ekle.xhtml?faces-redirect=true";
    }
    public String toYayineviListele() {
        return "/BookSaleManagementSystem/admin-secret/yayinevi-listele.xhtml?faces-redirect=true";
    }
    public String toManageCd() {
        return "/BookSaleManagementSystem/admin-secret/manage-cd.xhtml?faces-redirect=true";
    }
    public String toAddCD() {
        return "/BookSaleManagementSystem/admin-secret/cd-ekle.xhtml?faces-redirect=true";
    }
    public String toAddArtist() {
        return "/BookSaleManagementSystem/admin-secret/artist-ekle.xhtml?faces-redirect=true";
    }

    public String toManageUser() {
        return "/BookSaleManagementSystem/admin-secret/manage-user.xhtml?faces-redirect=true";
    }

    public String toManageOrders() {
        return "/BookSaleManagementSystem/admin-secret/manage-orders.xhtml?faces-redirect=true";
    }

    public String toAdminLogout() {
        return "/BookSaleManagementSystem/admin-secret/admin-logout.xhtml?faces-redirect=true";
    }
    

    //IMAGES PATH
    
    public String bookImage(String isbn) {
        return "/BookSaleManagementSystem/images/kitaplar/"+isbn+".jpg";
    }
    
    public String imageKargo() {
        return "/BookSaleManagementSystem/images/kargo-free.png";
    }
    
    public String bgAdminGiris() {
        return "/BookSaleManagementSystem/images/bg-admin-giris.jpg";
    }

    public String bgKayitOl() {
        return "/BookSaleManagementSystem/images/bg-kayit-ol.jpg";
    }

    public String bgKullaniciGiris() {
        return "/BookSaleManagementSystem/images/bg-kullanici-giris.jpg";
    }

    public String iconKCD() {
        return "/BookSaleManagementSystem/images/icon-kcd.png";
    }

    public String kitapBagisiBannes() {
        return "/BookSaleManagementSystem/images/kitap-bagisi-banner.jpg";
    }
    public String bgKitapEkle() {
        return "/BookSaleManagementSystem/images/black-background.jpg";
    }
    public String bgCD() {
        return "/BookSaleManagementSystem/images/bg-cd.jpg";
    }

    //KATEGORİLER PATH
    //
    public String toBilimKurgu() {
        return "/BookSaleManagementSystem/guest-secret/kitap-kategorileri/bilim-kurgu.xhtml?faces-redirect=true";
    }

    public String toBilgisayar() {
        return "/BookSaleManagementSystem/guest-secret/kitap-kategorileri/bilgisayar.xhtml?faces-redirect=true";
    }

    public String toKisiselGelisim() {
        return "/BookSaleManagementSystem/guest-secret/kitap-kategorileri/kisisel-gelisim.xhtml?faces-redirect=true";
    }

    public String toFelsefe() {
        return "/BookSaleManagementSystem/guest-secret/kitap-kategorileri/felsefe.xhtml?faces-redirect=true";
    }

    public String toPolisiye() {
        return "/BookSaleManagementSystem/guest-secret/kitap-kategorileri/polisiye.xhtml?faces-redirect=true";
    }
    
    //USER KİTAP KATEGORİLERİ
    
    public String toUserBilimKurgu() {
        return "/BookSaleManagementSystem/user-secret/kitap-kategorileri/bilim-kurgu.xhtml?faces-redirect=true";
    }

    public String toUserBilgisayar() {
        return "/BookSaleManagementSystem/user-secret/kitap-kategorileri/bilgisayar.xhtml?faces-redirect=true";
    }

    public String toUserKisiselGelisim() {
        return "/BookSaleManagementSystem/user-secret/kitap-kategorileri/kisisel-gelisim.xhtml?faces-redirect=true";
    }

    public String toUserFelsefe() {
        return "/BookSaleManagementSystem/user-secret/kitap-kategorileri/felsefe.xhtml?faces-redirect=true";
    }

    public String toUserPolisiye() {
        return "/BookSaleManagementSystem/user-secret/kitap-kategorileri/polisiye.xhtml?faces-redirect=true";
    }

}
