package com.mommyce.appservice.handler;

import com.mommyce.appcore.dao.common.UserDAO;
import com.mommyce.appcore.domain.common.User;
import com.mommyce.appcore.utils.AppUtils;
import com.mommyce.appservice.config.UserDetailsConfig;
import com.mommyce.appservice.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by israjhaliri on 8/30/17.
 */
@Component
public class LogOutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;


    @Override
    public void onLogoutSuccess(HttpServletRequest httpRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String authToken = httpRequest.getHeader(this.tokenHeader);

        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer "))
            authToken = authToken.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        User user = userDAO.getDataById(username);
        if(username != null && user.getToken().equals(authToken)){
            try (PrintWriter writer = response.getWriter()) {
                userDAO.deleteToken(username);
                writer.write("{\"code\":\"" + response.getStatus()
                        + "\", \"status\":\"SUCESS\"}");
                writer.flush();
                writer.close();
            }
        }else{
            response.setStatus(400);
            try (PrintWriter writer = response.getWriter()) {
                writer.write("{\"code\":\"" + response.getStatus()
                        + "\", \"status\":\"FAILED\", "
                        + "\"message\": Make sure token is correct and you have logged in}");
                writer.flush();
                writer.close();
            }
        }
    }
}