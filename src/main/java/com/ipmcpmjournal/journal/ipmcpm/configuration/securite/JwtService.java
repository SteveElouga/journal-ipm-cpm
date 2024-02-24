package com.ipmcpmjournal.journal.ipmcpm.configuration.securite;

import com.ipmcpmjournal.journal.ipmcpm.exception.ResourceNotFoundException;
import com.ipmcpmjournal.journal.ipmcpm.model.Jwt;
import com.ipmcpmjournal.journal.ipmcpm.model.RefreshToken;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.repository.JwtRepository;
import com.ipmcpmjournal.journal.ipmcpm.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    public static final String TOKEN_INVALIDE = "Token invalide";

    Map<String, String> fresh = null;

    private UserServiceImpl userService;

    private JwtRepository jwtRepository;

    public Jwt tokenByValue(String value){

        System.out.println("la valeur de value est :" + value);


        try{

            return this.jwtRepository.findByValeurAndDesactiveAndExpire(
                    value,
                    false,
                    false
            ).get();
        }catch (Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
            System.out.println(e.getCause());

            return null;
        }
    }

    public Map<String, String> generate(String username){

        User user = (User) this.userService.loadUserByUsername(username);
        this.disableTokens(user);
        final Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwt(user));

        RefreshToken refreshToken = RefreshToken.builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .creation(Instant.now())
                .expiration(Instant.now().plusMillis(30 *60 *1000))
                .build();

        System.out.println("refresh : " + refreshToken.getValeur());

        Instant creation = Instant.now();
        Instant expiration = Instant.now().plusMillis(10 *1000);

         final Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get(BEARER))
                .desactive(false)
                .expire(false)
                .user(user)
                .creation(creation)
                .expiration(expiration)
                .refreshToken(refreshToken)
                .build();



        this.jwtRepository.save(jwt);
        jwtMap.put(REFRESH,  refreshToken.getValeur());

        System.out.println(jwtMap.get("refresh"));

        return jwtMap;
    }

    private void disableTokens(User utilisateur) {
        final List<Jwt> jwtList = this.jwtRepository.findUser(utilisateur.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());

        System.out.println(jwtList);

        this.jwtRepository.saveAll(jwtList);
    }
    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(User user) {

        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", user.getFirstname(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getEmail()
        );



        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getEmail())
                .addClaims(claims) // Utilisation de addClaims() pour ajouter des claims
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();

// Construction de la Map contenant le jeton JWT
        return Map.of(BEARER, bearer);
    }

    private Key getKey() {
        String ENCRYPTION_KEY = "fd6d55c3e6f612edc523d7b362f1b8d394d9917ddd90d418907bbc6d78e0858b2a6c76b63826c8d3a687b17a3416f43173698f04745ba1c8d63b5749dcf72826";
        byte[] decode = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decode);
    }


    public void deconnexion() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(user != null){
            Jwt jwt = this.jwtRepository.findByUserValidToken(
                    user.getEmail(),
                    false,
                    false
            ).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
            jwt.setExpire(true);
            jwt.setDesactive(true);
            this.jwtRepository.save(jwt);
        }else{

        }
    }

    @Scheduled(cron = "@daily")
//    @Scheduled(cron = "0 */1 * * * *")
    public void removeUselessJwt() {

        try{
            this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
            log.info("Suppression des token à {}", Instant.now());
        }catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }


    // jwt.getExpiration().isAfter(Instant.now());
    @Scheduled(cron = "@daily")
    public void expiredJwt() {

        try {
            List<Jwt> jwtList = this.jwtRepository.findAllByExpireAndDesactive(false, false);
            jwtList.forEach(jwt -> {
                jwt.setExpire(true);
                jwt.setDesactive(true);
                this.jwtRepository.save(jwt);
            });
//            jwt.setExpire(true);
//            jwt.setDesactive(true);
//            this.jwtRepository.save(jwt);
            log.info("Expiration des token à {}", Instant.now());

        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

//    @Scheduled(cron = "* */30 * * * *")
//    public Map<String, String> refreshJwt() {
//        Jwt jwt = this.jwtRepository.findAllByExpireAndDesactive(false, false);
//        RefreshToken refreshToken = jwt.getRefreshToken();
//
//
//
//        fresh.put(REFRESH, String.valueOf(refreshToken.getValeur()));
//
//        System.out.println(refreshToken.getValeur());
//
//        log.info("refresh des token à {}", Instant.now());
//
//        return refreshToken(fresh);
//    }


    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        final Jwt jwt = this.jwtRepository.findByRefreshToken(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
        if(jwt.getRefreshToken().isExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException(TOKEN_INVALIDE);
        }
        this.disableTokens(jwt.getUser());
        return this.generate(jwt.getUser().getEmail());
    }



}
