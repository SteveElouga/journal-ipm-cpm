package com.ipmcpmjournal.journal.ipmcpm.model;

import com.ipmcpmjournal.journal.ipmcpm.outil.TypeDeRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetRole {

    private String username;
    private String role;

}
