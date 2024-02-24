package com.ipmcpmjournal.journal.ipmcpm.model;

import com.ipmcpmjournal.journal.ipmcpm.outil.TypeDeRole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeDeRole libelle;
}
