package com.ipmcpmjournal.journal.ipmcpm.service;

import com.ipmcpmjournal.journal.ipmcpm.model.Avis;

import java.util.List;

public interface AvisService {
    void creer(Avis avis);
    List<Avis> lister();
}
