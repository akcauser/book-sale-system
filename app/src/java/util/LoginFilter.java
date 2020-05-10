/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.Admin;
import util.LoginController;
import entity.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ProgApp
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        
        User u = (User) req.getSession().getAttribute("valid_user");
        Admin a = (Admin) req.getSession().getAttribute("valid_admin");
        
        
        /*
        Eğer kullanıcı girişi yapıldıysa admin-secret ve guest-secret sayfalarına erişilemez.
        Eğer admin girişi yapıldıysa user-secret ve guest-secret sayfalarına eişilemez
        Eğer giriş yapılmadıysa user-secret ve admin-secret sayfalarına erişelemez.
        */
        //FİLTRELEMELER
        
        if (u != null) {
            //EĞER kullanıcı giriş yaptıysa;
            if (url.contains("guest-secret") || url.contains("admin-secret")) {
                res.sendRedirect(req.getContextPath() + "/user-secret/user-main.xhtml");
            } else if (url.contains("user-logout.xhtml")) {
                req.getSession().invalidate();
                res.sendRedirect(req.getContextPath() + "/guest-secret/kullanici-giris.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else if( a != null){
            //Eğer admin giriş yaptıysa;
            if (url.contains("user-secret") || url.contains("guest-secret")) {
                res.sendRedirect(req.getContextPath() + "/admin-secret/manage-orders.xhtml");
            } else if (url.contains("admin-logout.xhtml")) {
                req.getSession().invalidate();
                res.sendRedirect(req.getContextPath() + "/guest-secret/admin-giris.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        
        }else {
            //Eğer giriş yapılmadıysa;
            if (url.contains("user-secret") || url.contains("admin-secret")) {
                res.sendRedirect(req.getContextPath() + "/guest-secret/guest-main.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        }
       

    }

}
