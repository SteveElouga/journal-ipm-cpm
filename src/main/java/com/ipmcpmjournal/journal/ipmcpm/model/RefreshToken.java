package com.ipmcpmjournal.journal.ipmcpm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh-token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean expire;
    private String valeur;
    private Instant creation;
    private Instant expiration;
}
