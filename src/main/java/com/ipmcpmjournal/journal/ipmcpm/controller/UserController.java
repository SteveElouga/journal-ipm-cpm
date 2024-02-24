package com.ipmcpmjournal.journal.ipmcpm.controller;

import com.ipmcpmjournal.journal.ipmcpm.model.SetRole;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.outil.TypeDeRole;
import com.ipmcpmjournal.journal.ipmcpm.configuration.securite.JwtService;
import com.ipmcpmjournal.journal.ipmcpm.dto.AuthenticationDto;
import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;
import com.ipmcpmjournal.journal.ipmcpm.exception.InvalidOperationException;
import com.ipmcpmjournal.journal.ipmcpm.exception.ResourceNotFoundException;
import com.ipmcpmjournal.journal.ipmcpm.model.Role;
import com.ipmcpmjournal.journal.ipmcpm.service.UserService;
import com.ipmcpmjournal.journal.ipmcpm.service.ValidationService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users/")
@AllArgsConstructor
@Tag(
        name = "Utilisateur",
        description = "Controlleur des utilisateurs"
)
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private ValidationService validationService;

    @GetMapping(path = "callBackUri")
    public ResponseEntity<Map<String,String>> callBackUri(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("callBackUri");
        log.info(String.valueOf(authentication.isAuthenticated()));
        log.info(authentication.getPrincipal().getClass().getName());


        Map<String, String> response = new HashMap<>();
        response.put("details", authentication.getDetails().toString());

        ResponseEntity<Map<String,String>> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);


        return responseEntity;
    }


    @Operation(
            summary = "Enregistrer un nouvel administrateur (Pas d'authentification requise)",
            description = "Endpoint pour creer un nouvel utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès et création d’un utilisateur",
                            responseCode = "201",
                            content={
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "La syntaxe de la requête est erronée.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Ce compte existe deja.",
                            responseCode = "409"
                    )
            }
    )
    @SecurityRequirement(name = "NoAuth")
    @PostMapping(path ="inscription")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto ){

        log.info("Inscription");

        userDto.setRole(
                Role.builder()
                        .libelle(TypeDeRole.USER)
                        .build()
        );

        System.out.println(userDto);

        UserDto savedUser = userService.createUser(userDto);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Enregistrer un nouvel administrateur (Pas d'authentification requise)",
            description = "Endpoint pour creer un nouvel administrateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès et création d’un administrateur",
                            responseCode = "201",
                            content={
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            description = "La syntaxe de la requête est erronée.",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Ce compte existe deja.",
                            responseCode = "409"
                    )
            }
    )
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @PostMapping(path ="inscriptionAdmin")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto userDto){

        log.info("InscriptionAdmin");

        userDto.setRole(
                Role.builder()
                        .libelle(TypeDeRole.ADMIN)
                        .build()
        );

        UserDto savedUser = userService.createUser(userDto);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Activation du compte utilisateur",
            description = "Endpoint pour activer le compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Le code renseigne est invalide",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "NoAuth")
    @PostMapping(path ="activation")
    public void activation(@RequestBody Map<String, String> activation){

        this.userService.activation(activation);
    }


    @Operation(
            summary = "Demande de modification du mot de passe du compte utilisateur",
            description = "Endpoint pour demander la modification du mot de passe compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Idendifiant inexistant",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "NoAuth")
    @PostMapping(path ="modifier-mot-de-passe")
    public void modifierMotDePasse(@RequestBody Map<String, String> activation){

        this.userService.modifierMotDePasse(activation);
    }


    @Operation(
            summary = "Modification du mot de passe du compte utilisateur",
            description = "Endpoint pour modifier le mot de passe compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Le code renseigne est invalide",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Idendifiant inexistant",
                            responseCode = "404"
                    )
            }
    )
    @SecurityRequirement(name = "NoAuth")
    @PostMapping(path ="nouveau-mot-de-passe")
    public void nouveauMotDePasse(@RequestBody Map<String, String> activation){

        this.userService.nouveauMotDePasse(activation);
    }


    @Hidden
    @PostMapping(path = "refresh-token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenRequest) {
        return this.jwtService.refreshToken(refreshTokenRequest);
    }


    @Operation(
            summary = "Connexion du mot de passe du compte utilisateur",
            description = "Endpoint pour connecter le compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Erreur survenue lors de l'authentification",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "NoAuth")
    @PostMapping(path ="connexion")
    public Map<String, String> connexion(@RequestBody AuthenticationDto authenticationDto) {

        if(authenticationDto.username() == "" || authenticationDto.password() == ""){
            throw new InvalidOperationException("username or password empty");
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.username(),
                        authenticationDto.password()
                )

        );

        if (authentication.isAuthenticated()){
            return  this.jwtService.generate(authenticationDto.username());
        }

        return null;
    }

    @Operation(
            summary = "Requete des informations du compte utilisateur",
            description = "Endpoint pour demander les informations du compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Acces non autorise",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Vous ne disposez pas des droits necessaires",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }


    @Operation(
            summary = "Requete des informations des comptes utilisateur",
            description = "Endpoint pour demander les informations des comptes utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Acces non autorise",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Vous ne disposez pas des droits necessaires",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @GetMapping(path = "allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @Operation(
            summary = "Modification des informations du compte utilisateur",
            description = "Endpoint pour modifier les informations du compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Acces non autorise",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Vous ne disposez pas des droits necessaires",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto updatedUser){
        UserDto userDto = userService.updateUser(userId, updatedUser);

        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Suppression des informations du compte utilisateur",
            description = "Endpoint pour supprimer les informations du compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Acces non autorise",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Vous ne disposez pas des droits necessaires",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        try {
            validationService.delete(userId);
            userService.deleteUser(userId);
            return ResponseEntity.ok("Utilisateur supprimé avec succès!");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Échec de la suppression de l'utilisateur. " + e.getMessage());
        }
    }

    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("setRole")
    public Map<String, String> setRole(@RequestBody SetRole role) {

        UserDto userDto = userService.getUserByEmail(role.getUsername());

        if(userDto != null){
            userDto.setRole(
                    Role
                            .builder()
                            .libelle(TypeDeRole.valueOf(role.getRole()))
                    .build()
            );

            this.userService.updateUser(
                    userDto.getId(),
                    userDto
            );
        }else{
            throw new ResourceNotFoundException("l'utilisateur " + role.getUsername() + " n'existe pas.");
        }


        return null;
    }
}
