package com.ipmcpmjournal.journal.ipmcpm.service.impl;

import com.ipmcpmjournal.journal.ipmcpm.model.Avis;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.repository.AvisRepository;
import com.ipmcpmjournal.journal.ipmcpm.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AvisServiceImpl implements AvisService {

    private final AvisRepository avisRepository;

    public void creer(Avis avis){
        User utilisateur = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUser(utilisateur);
        this.avisRepository.save(avis);
    }

    public List<Avis> lister() {
        return (List<Avis>) this.avisRepository.findAll();

    }
}
