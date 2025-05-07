package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Acquisto;
import com.example.progettopsw.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcquistoRepository extends JpaRepository<Acquisto, Long> {

    List<Acquisto> findByBuyer(User buyer);
}
