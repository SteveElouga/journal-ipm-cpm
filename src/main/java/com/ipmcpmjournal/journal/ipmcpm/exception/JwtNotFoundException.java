package com.ipmcpmjournal.journal.ipmcpm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class JwtNotFoundException extends org.springframework.security.core.userdetails.UsernameNotFoundException {

    public JwtNotFoundException(String msg) {
        super(msg);
    }
}
