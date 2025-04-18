package com.example.progettopsw.services;

import com.example.progettopsw.entities.User;
import com.example.progettopsw.repositories.UserRepository;
import com.example.progettopsw.support.exceptions.EmailGiaRegistrataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> recensoriPiuAttivi(int n){
        return userRepository.findActiveSongReviewers(n);
    }

    @Transactional(readOnly = true)
    public List<User> utentiPiuAttivi(int m, int n){
        return userRepository.findPowerUsers(m,n);
    }

    @Transactional(readOnly = true)
    public List<User> utentiConGustiSimili(){
        return userRepository.findUsersReviewingSameArtistContent();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registraUtente(User user){
        if (userRepository.findByEmailIgnoreCase(user.getEmail())!=null){
            throw new EmailGiaRegistrataException();
        }
        return userRepository.save(user);
    }
}
