package com.ipmcpmjournal.journal.ipmcpm.configuration.securite;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private JwtService jwtService;

    public CustomLogoutHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Boolean authenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();

        if(authenticated){

//            log.info(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
//            log.info(SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
//            log.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
            SecurityContextHolder.getContext().setAuthentication(null);

        }else{
            this.jwtService.deconnexion();
        }

        }
}
