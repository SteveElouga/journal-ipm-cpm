package com.ipmcpmjournal.journal.ipmcpm.service.impl;

import com.ipmcpmjournal.journal.ipmcpm.exception.CodeException;
import com.ipmcpmjournal.journal.ipmcpm.exception.ResourceNotFoundException;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.model.Validation;
import com.ipmcpmjournal.journal.ipmcpm.repository.ValidationRepository;
import com.ipmcpmjournal.journal.ipmcpm.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Notification;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
@AllArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private ValidationRepository validationRepository;

    private NotificationServiceImpl notificationService;


    @Override
    public void enregistrer(User user) {
        Validation validation = new Validation();
        validation.setUser(user);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
        validation.setExpiration(expiration);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }

    @Override
    public void delete(Long userId) {
        final Optional<Validation> findValidation = validationRepository.findByUserId(userId);

        if (findValidation.isPresent()) {
            // Si la validation existe, on la supprime
            Validation validation = findValidation.get();
            validationRepository.delete(validation);
        } else {
            // Si la validation n'existe pas, on peut passer à l'étape suivante du programme
            // Faites ici ce que vous souhaitez réaliser dans ce cas
        }
    }

    @Override
    public Validation lireEnFonctionDuCode(String code){
       if(code == ""){
           return this.validationRepository
                   .findByCode(code)
                   .orElseThrow(
                           ()->new CodeException("Aucun code renseigne")
                   );
       }else{
           return this.validationRepository
                   .findByCode(code)
                   .orElseThrow(
                           ()->new CodeException("Votre code est invalide")
                   );
       }

    }

    @Override
    public void deleteValidation(Long id) {
        Validation validation = validationRepository.findByUserId(id).orElseThrow(
                ()-> new ResourceNotFoundException("Validation Delete Error: " + id)
        );

        validationRepository.deleteById(validation.getId());
        System.out.println(validation.getId());
    }

//    @Scheduled(cron = "*/30 * * * * *")
//    public void nettoyerTable() {
//        final Instant now = Instant.now();
//        log.info("Suppression des token à {}", now);
//        this.validationRepository.deleteAllByExpirationBefore(now);
//    }
}
