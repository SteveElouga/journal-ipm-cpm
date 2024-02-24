package com.ipmcpmjournal.journal.ipmcpm.repository;

import com.ipmcpmjournal.journal.ipmcpm.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByCode(String  code);
    Optional<Validation> findByUserId(Long id);


    void deleteByUserId(Long id);
}
