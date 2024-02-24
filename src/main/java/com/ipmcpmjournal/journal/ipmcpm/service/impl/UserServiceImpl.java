package com.ipmcpmjournal.journal.ipmcpm.service.impl;

import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;
import com.ipmcpmjournal.journal.ipmcpm.exception.CodeException;
import com.ipmcpmjournal.journal.ipmcpm.exception.EmailInvalid;
import com.ipmcpmjournal.journal.ipmcpm.exception.ResourceAlreadyExistsException;
import com.ipmcpmjournal.journal.ipmcpm.exception.ResourceNotFoundException;
import com.ipmcpmjournal.journal.ipmcpm.mapper.UserMapper;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.model.Validation;
import com.ipmcpmjournal.journal.ipmcpm.repository.UserRepository;
import com.ipmcpmjournal.journal.ipmcpm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private ValidationServiceImpl validationService;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);

        System.out.println(user+ " " + userDto.getEmail());

        if(!user.getEmail().contains("@")){
            throw new EmailInvalid("Votre mail est invalide");
        }
        if(!user.getEmail().contains(".")){
            throw new EmailInvalid("Votre mail est invalide");
        }

        Optional<User> userEmail = userRepository.findByEmail(user.getEmail());

        if(userEmail.isPresent()) {
            throw new ResourceAlreadyExistsException("User is already exist with given email: " + user.getEmail());
        }

        String mdpCrypte = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(mdpCrypte);



        User savedUser = userRepository.save(user);
        this.validationService.enregistrer(user);

        return UserMapper.mapToUserDto(savedUser);

    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User is not exist with given id: " + userId)
        );
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User is not exist with given email: " + email)
        );
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map((user -> UserMapper.mapToUserDto(user))).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updateUser) {


        validationService.delete(userId);

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User is not exist with given id: " + userId)
        );

        user.setFirstname(updateUser.getFirstname());
        user.setLastname(updateUser.getLastname());
        user.setEmail(updateUser.getEmail());
        user.setAdresse(updateUser.getAdresse());
        user.setNbPublication(user.getNbPublication());
        user.setPhoto(updateUser.getPhoto());

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User is not exist with given id: " + userId)
        );
//        this.validationService.deleteValidation(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));

        if(Instant.now().isAfter(validation.getExpiration())){
            throw new CodeException("Votre code a expire");
        }
        User userActiver = this.userRepository
                .findById(validation
                        .getUser().getId())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("User is not exist with given id: " + validation
                                .getUser().getId())
                );

        userActiver.setActif(true);
        this.userRepository.save(userActiver);

    }

    @Override
    public void modifierMotDePasse(Map<String, String> parametres) {
        User utilisateur = (User) this.loadUserByUsername(parametres.get("email"));
        this.validationService.enregistrer(utilisateur);
    }

    @Override
    public void nouveauMotDePasse(Map<String, String> parametres) {
        User user = (User) this.loadUserByUsername(parametres.get("email"));
        final  Validation validation = validationService.lireEnFonctionDuCode(parametres.get("code"));

        if(validation.getUser().getEmail().equals(user.getEmail())){
            String mdpCrypte = this.passwordEncoder.encode(parametres.get("password"));
            user.setPassword(mdpCrypte);
            this.userRepository.save(user);
        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Aucun utilisateur ne correspond a cet identifant"));
    }
}
