package com.example.progettopsw.controllers.rest;

import com.example.progettopsw.services.SolistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solo_artists")
public class SolistaController {
    @Autowired
    private SolistaService solistaService;
}
