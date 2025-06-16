package com.example.progettopsw.services;

import com.example.progettopsw.entities.Users;
import com.example.progettopsw.support.exceptions.EmailGiaRegistrataException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KCService {
    @Autowired
    private UserService userService;
    private Keycloak keycloak;

    public KCService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public Users addUser(Users userToAdd, String password) {
        try {
            //definizione dello user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(userToAdd.getEmail());
            user.setEmail(userToAdd.getEmail());
            user.setEmailVerified(true);
            // definizione delle credenziali
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);
            List<CredentialRepresentation> list = new ArrayList<>();
            list.add(passwordCred);
            user.setCredentials(list);
            //per avere il token per creare nuove risorse
            RealmResource realmResource = keycloak.realm("album-app");
            UsersResource usersResource = realmResource.users();
            //end point per creare user
            Response response = usersResource.create(user);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                Users s=userService.registraUtente(userToAdd);
                return s;
            }
        } catch (EmailGiaRegistrataException e) { //sono sicuro che non ci sia questa eventualità ma comunque l'integro
            throw new EmailGiaRegistrataException();
        }
        return null;
    }

}