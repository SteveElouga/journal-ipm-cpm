package com.ipmcpmjournal.journal.ipmcpm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class CodeException extends RuntimeException{
    public CodeException(String message){
        super(message);
    }
}
