package com.hotel.costa.mar.controller;

import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HotelController {

    @Autowired
    private ApplicationService applicationService;
    @GetMapping("/")
    public ResponseEntity<?> getHotels(){
        List<HotelDto> hotels = applicationService.getAllHotels();
        return
                ResponseEntity.ok(hotels);
    }
    @GetMapping("/{hotel}")
    public ResponseEntity<?> getHotel(@PathVariable("hotel") int hotel){
        HotelDto hotelDto = applicationService.findHotelById(hotel);
        return ResponseEntity.ok(hotelDto);
    }
    @GetMapping("/{hotel}/{quarto}")
    public ResponseEntity<?> getQuarto(@PathVariable("hotel") int hotel, @PathVariable("quarto") int quarto){
        QuartoDto qtDto = applicationService.findHotelQuarto(hotel,quarto);
        return ResponseEntity.ok(qtDto);
    }

    @GetMapping("/reservar/{hotel}/{quarto}/{usuario}/{dataEntrada}/{dataSaida}")
    public ResponseEntity<?> reservarQuarto(@PathVariable int hotel, @PathVariable int quarto,
                                            @PathVariable String dataEntrada, @PathVariable String dataSaida,
                                            @PathVariable String usuario){
        String resposta = applicationService.reservarQuarto(hotel,quarto, dataEntrada, dataSaida, usuario);

        return ResponseEntity.ok(resposta);
    }







}
