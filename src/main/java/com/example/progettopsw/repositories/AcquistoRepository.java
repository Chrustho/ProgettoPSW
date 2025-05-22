package com.example.progettopsw.repositories;

import com.example.progettopsw.entities.Acquisto;
import com.example.progettopsw.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcquistoRepository extends JpaRepository<Acquisto, Long> {

    List<Acquisto> findByBuyer(Users buyer);
}
