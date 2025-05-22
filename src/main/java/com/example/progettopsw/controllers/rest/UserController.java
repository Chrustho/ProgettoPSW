package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.entities.Users;
import com.example.progettopsw.services.UserService;
import com.example.progettopsw.support.ResponseMessage;
import com.example.progettopsw.support.exceptions.EmailGiaRegistrataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity aggiungiUtente(@RequestBody @Validated Users user){
        try {
            userService.registraUtente(user);
        }catch (EmailGiaRegistrataException e){
            return new ResponseEntity(new ResponseMessage("Utente già presente!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Utente aggiunto correttamente!"), HttpStatus.OK);
    }

    @GetMapping("/users/reviewers_leaderboard")
    public ResponseEntity recensoriPiuAttivi(){
        List<Users> users=userService.recensoriPiuAttivi(10);
        if (users.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(users,HttpStatus.OK);
    }

    @GetMapping("/users/users_leaderboard")
    public ResponseEntity utentiPiuAttivi(){
        List<Users> users=userService.utentiPiuAttivi(10,10);
        if (users.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(users,HttpStatus.OK);
    }

    @GetMapping("/search/by_name_or_surname")
    public ResponseEntity trovaPerNomeOCognome(@RequestParam String nome, @RequestParam String cognome){
        if (nome.isBlank() && cognome.isBlank()){
            return new ResponseEntity<>(new ResponseMessage("Inserisci nome o cognome da ricercare!"), HttpStatus.BAD_REQUEST);
        }
        List<Users> users=userService.trovaPerNomeOCognome(nome, cognome);
        if (users.isEmpty()){
            return new ResponseEntity(new ResponseMessage("Nessun risultato!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(users,HttpStatus.OK);
    }



}
