package com.ipmcpmjournal.journal.ipmcpm.dto;

import com.ipmcpmjournal.journal.ipmcpm.model.Adresse;
import com.ipmcpmjournal.journal.ipmcpm.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String photo;
    private Adresse adresse;
    private String password;
    private Role role;


}
