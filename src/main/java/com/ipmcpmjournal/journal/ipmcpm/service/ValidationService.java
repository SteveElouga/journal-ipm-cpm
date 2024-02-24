package com.ipmcpmjournal.journal.ipmcpm.service;

import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.model.Validation;

public interface ValidationService {
    void enregistrer(User user) ;

    void delete(Long userId);

    Validation lireEnFonctionDuCode(String code);

    void deleteValidation(Long id);
}
