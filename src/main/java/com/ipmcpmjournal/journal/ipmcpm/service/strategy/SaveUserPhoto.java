package com.ipmcpmjournal.journal.ipmcpm.service.strategy;

import ch.qos.logback.core.spi.ErrorCodes;
import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;
import com.ipmcpmjournal.journal.ipmcpm.exception.InvalidOperationException;
import com.ipmcpmjournal.journal.ipmcpm.repository.UserRepository;
import com.ipmcpmjournal.journal.ipmcpm.service.FlickrService;
import com.ipmcpmjournal.journal.ipmcpm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("userStrategy")
@Slf4j
@AllArgsConstructor
public class SaveUserPhoto implements Strategy<UserDto>{
    private FlickrService flickrService;
    private UserService userService;

    @Override
    public UserDto savePhoto(String email, InputStream photo, String titre) {
        UserDto userDto = userService.getUserByEmail(email);
        Long id = userDto.getId();

        String urlPhoto = flickrService.savePhoto(photo, titre);

        if (!StringUtils.hasLength(urlPhoto)) {
            throw new InvalidOperationException("Erreur lors de l'enregistrement de photo de l'article");
        }
        userDto.setPhoto(urlPhoto);
        return userService.updateUser(id, userDto);
    }
}
