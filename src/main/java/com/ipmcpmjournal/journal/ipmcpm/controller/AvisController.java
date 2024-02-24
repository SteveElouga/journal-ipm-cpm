package com.ipmcpmjournal.journal.ipmcpm.controller;

import com.ipmcpmjournal.journal.ipmcpm.model.Avis;
import com.ipmcpmjournal.journal.ipmcpm.service.impl.AvisServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RequestMapping("avis")
@RestController
public class AvisController {
    private final AvisServiceImpl avisServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis) {
        this.avisServiceImpl.creer(avis);
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Avis> liste() {
        return this.avisServiceImpl.lister();
    }
}
