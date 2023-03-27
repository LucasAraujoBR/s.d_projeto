package com.hotel.costa.mar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController {

    @GetMapping("/")
    public ResponseEntity<String> getHotels(){
        return ResponseEntity.ok("OK!");
    }
    @GetMapping("/{hotel}")
    public ResponseEntity<?> getHotel(@PathVariable("hotel") int hotel){
        return ResponseEntity.ok("OK!");
    }
    @GetMapping("/{hotel}/{quarto}")
    public ResponseEntity<?> getQuarto(@PathVariable("hotel") int hotel, @PathVariable("quarto") int quarto){
        return ResponseEntity.ok("OK!");
    }



}
