package com.ipmcpmjournal.journal.ipmcpm.configuration.securite;

import com.ipmcpmjournal.journal.ipmcpm.exception.JwtNotFoundException;
import com.ipmcpmjournal.journal.ipmcpm.model.Jwt;
import com.ipmcpmjournal.journal.ipmcpm.service.UserService;
import com.ipmcpmjournal.journal.ipmcpm.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private HandlerExceptionResolver handlerExceptionResolver;
    private UserServiceImpl utilisateurService;
    private JwtService jwtService;

    public JwtFilter(UserServiceImpl utilisateurService, JwtService jwtService, HandlerExceptionResolver handlerExceptionResolver) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Jwt tokenDansBDD = null;
        String username = null;
        boolean isTokenExpired = true;
        OAuth2AuthenticationToken tokenOAuth = null;


        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJub20iOiJBY2hpbGxlIE1CT1VHVUVORyIsImVtYWlsIjoiYWNoaWxsZS5tYm91Z3VlbmdAY2hpbGxvLnRlY2gifQ.zDuRKmkonHdUez-CLWKIk5Jdq9vFSUgxtgdU1H2216U
        try{
//            tokenOAuth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//
//            // Vérifier si l'utilisateur est authentifié via OAuth
//            if (tokenOAuth != null && tokenOAuth.isAuthenticated()) {
//                // L'utilisateur est authentifié via OAuth, traiter en conséquence
//                // Exemple : récupérer les détails de l'utilisateur OAuth
//                String userName = tokenOAuth.getPrincipal().getAttribute("name");
//                System.out.println("Utilisateur OAuth authentifié : " + userName);
//            }

            final String authorization = request.getHeader("Authorization");
            if(authorization != null && authorization.startsWith("Bearer ")){
                token = authorization.substring(7);
                tokenDansBDD = this.jwtService.tokenByValue(token);
                isTokenExpired = jwtService.isTokenExpired(token);
                username = jwtService.extractUsername(token);
            }


            if(!isTokenExpired && tokenDansBDD.getUser().getEmail().equals(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        }catch (final  Exception exception){
            this.handlerExceptionResolver.resolveException(request, response, null, exception);
            System.out.println(exception);
            System.out.println(exception.getMessage());
            System.out.println(exception.getCause());

        }
    }
}
