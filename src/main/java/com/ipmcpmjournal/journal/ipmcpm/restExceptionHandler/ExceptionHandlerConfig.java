package com.ipmcpmjournal.journal.ipmcpm.restExceptionHandler;

import com.ipmcpmjournal.journal.ipmcpm.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DeserializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiError> ressourceNotFound(BadCredentialsException ex){
        return new ResponseEntity<>(new ApiError(UNAUTHORIZED.value(), ex.getMessage(), "nous n'avons pas pu vous identifier"), UNAUTHORIZED);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(
            value = {
                    SignatureException.class,
                    MalformedJwtException.class,
                    DeserializationException.class,
                    NullPointerException.class,
                    NoSuchElementException.class
            }
            )
    public ResponseEntity<ApiError> signatureException(Exception ex){
        return new ResponseEntity<>(new ApiError(401, ex.getMessage(), "Token invalide"), UNAUTHORIZED);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = {ResourceNotFoundException.class, JwtNotFoundException.class})
    public ResponseEntity<ApiError> resourceException(Exception ex){
        return new ResponseEntity<>(new ApiError(UNAUTHORIZED.value(), ex.getMessage(), "Token inconnu"), UNAUTHORIZED);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiError> accessDeniedException(AccessDeniedException ex){
        return new ResponseEntity<>(new ApiError(FORBIDDEN.value(), ex.getMessage(), "Vous n'etes pas autorises a effectuer cette action"), FORBIDDEN);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = InvalidOperationException.class)
    public ResponseEntity<ApiError> invalidOperationException(InvalidOperationException ex){

        return new ResponseEntity<>(new ApiError(BAD_REQUEST.value(), ex.getMessage(), "Vous n'etes pas autorises a effectuer cette action"), BAD_REQUEST);

    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> exceptionHandler(Exception ex){

        return new ResponseEntity<>(new ApiError(BAD_REQUEST.value(), ex.getMessage(), "Tous les champs n'ont pas ete correctement remplis"), BAD_REQUEST);

    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = EmailInvalid.class)
    public ResponseEntity<ApiError> emailInvalid(EmailInvalid ex){

        return new ResponseEntity<>(new ApiError(BAD_REQUEST.value(), ex.getMessage(), "L'adresse email renseignee ne respecte pas le bon format"), BAD_REQUEST);

    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = CodeException.class)
    public ResponseEntity<ApiError> codeException(CodeException ex){

        return new ResponseEntity<>(new ApiError(BAD_REQUEST.value(), ex.getMessage(), "Le code renseigne n'est pas valide"), BAD_REQUEST);

    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ApiError> usernameNotFoundException(UsernameNotFoundException ex){

        return new ResponseEntity<>(new ApiError(NOT_FOUND.value(), ex.getMessage(), "L'identifiant renseigne n'existe pas"), NOT_FOUND);

    }

//    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String, String> exception(Exception ex){

        return Map.of(ex.getMessage(), ex.getLocalizedMessage());
    }
    @ResponseStatus(CONFLICT)
    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public ResponseEntity<ApiError> resourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        return new ResponseEntity<>(new ApiError(CONTINUE.value(), ex.getMessage(), "CONFLICT"), HttpStatus.CONFLICT);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> JwtExpiredException(ExpiredJwtException ex){
        return new ResponseEntity<>(new ApiError(UNAUTHORIZED.value(), ex.getMessage(),"Votre session a expiree"), UNAUTHORIZED);
    }
}
