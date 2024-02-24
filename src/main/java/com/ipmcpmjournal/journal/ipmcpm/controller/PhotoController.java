package com.ipmcpmjournal.journal.ipmcpm.controller;

import com.flickr4java.flickr.FlickrException;
import com.ipmcpmjournal.journal.ipmcpm.model.User;
import com.ipmcpmjournal.journal.ipmcpm.service.strategy.StrategyPhotoContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class PhotoController {

    private StrategyPhotoContext strategyPhotoContext;

    @Operation(
            summary = "Ajout/Modification de la photo d'un compte utilisateur",
            description = "Endpoint ajouter/Modifier de la photo d'un compte utilisateur",
            responses = {
                    @ApiResponse(
                            description = "Requête traitée avec succès",
                            responseCode = "200"
                    )
                    ,
                    @ApiResponse(
                            description = "Non autorise",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Vous n'avez pas les droits necessaires.",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("savePhoto/{email}/{title}/{context}")
    public Object savePhoto(@PathVariable("context") String context, @PathVariable("email") String email, @RequestPart("file") MultipartFile photo,  @PathVariable("title") String title) throws IOException, FlickrException {
//        User utilisateur = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return strategyPhotoContext.savePhoto(context, email, photo.getInputStream(), title);
    }
}
