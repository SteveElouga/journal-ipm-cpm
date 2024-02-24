package com.ipmcpmjournal.journal.ipmcpm.repository;

import com.ipmcpmjournal.journal.ipmcpm.model.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {

    Optional<Jwt> findByValeurAndDesactiveAndExpire(String email, boolean desactive, boolean expire);

    @Query("SELECT j FROM Jwt j WHERE j.desactive = :desactive AND j.expire = :expire AND j.user.email = :email")
    Optional<Jwt> findByUserValidToken(String email, boolean desactive, boolean expire);


    @Query("SELECT j FROM Jwt j WHERE j.user.email = :email")
    Stream<Jwt> findUser(String email);


    @Query("SELECT j FROM Jwt j WHERE j.refreshToken.valeur = :valeur")
    Optional<Jwt> findByRefreshToken(String valeur);

    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);

//    Jwt findAllByExpireAndDesactive(boolean expire, boolean desactive);

    List<Jwt> findAllByExpireAndDesactive(boolean expire, boolean desactive);

}
