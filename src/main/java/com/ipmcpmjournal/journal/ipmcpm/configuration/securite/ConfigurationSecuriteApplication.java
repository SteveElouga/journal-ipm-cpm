package com.ipmcpmjournal.journal.ipmcpm.configuration.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class ConfigurationSecuriteApplication {

    private  final  BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtFilter jwtFilter;

    private CustomLogoutHandler logoutHandler;

    public ConfigurationSecuriteApplication(
            BCryptPasswordEncoder bCryptPasswordEncoder,
            JwtFilter jwtFilter,
            CustomLogoutHandler logoutHandler
    ){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter = jwtFilter;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .rememberMe(
                        r -> r
                                .rememberMeCookieName("maSession")
                                .key("A9z#pQ5yE@r4tL$%3!s8G&hJ7k^W*xU")
                                .tokenValiditySeconds(86400)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                        {
                            authorize
                                    .requestMatchers(POST, "/api/users/inscription").permitAll()
                                    .requestMatchers(POST, "/api/users/inscriptionAdmin").permitAll()
                                    .requestMatchers(POST, "/api/users/activation").permitAll()
                                    .requestMatchers(POST, "/api/users/connexion").permitAll()
                                    .requestMatchers(POST, "/api/users/refresh-token").permitAll()
                                    .requestMatchers(POST, "/api/users/modifier-mot-de-passe").permitAll()
                                    .requestMatchers(POST, "/api/users/nouveau-mot-de-passe").permitAll()
                                    .requestMatchers(POST, "/savePhoto").hasAnyRole("USER", "ADMIN")
                                    .requestMatchers(
                                            "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui.html",
                                            "/v2/api-docs/**"
                                    ).permitAll()
                                    .anyRequest().authenticated();
                        }
                )
                .oauth2Login(
                        oauth2 -> oauth2
                                .defaultSuccessUrl("/api/users/callBackUri")
                )
//                .formLogin(
//                        login -> login
//                                .loginProcessingUrl("/api/users/connexion")
//                                .permitAll()
//                                .defaultSuccessUrl("/api/users/callBackUri")
//                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        l -> l
                                .deleteCookies("JSESSIONID", "maSession")
                                .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(
                                        (request, response, authentication) ->
                                        {
                                            SecurityContextHolder.clearContext();
                                            response.sendRedirect("/cpmipmjournal/login");
                                        }
                                )
                                .logoutSuccessUrl("/login")
                )
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return daoAuthenticationProvider;
    }
}
