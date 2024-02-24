package com.ipmcpmjournal.journal.ipmcpm.service.strategy;

import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;

import java.io.InputStream;

public interface Strategy<T> {
    T savePhoto(String email,InputStream photo, String titre);
}
