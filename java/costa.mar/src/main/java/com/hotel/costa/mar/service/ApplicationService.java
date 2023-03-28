package com.hotel.costa.mar.service;


import com.hotel.costa.mar.map.ApplicationMapper;
import com.hotel.costa.mar.model.Hotel;
import com.hotel.costa.mar.model.Quarto;
import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ApplicationMapper mapper;

    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return mapper.hotelToDto(hotels);
    }

    public HotelDto findHotelById(int hotelId) {
        Hotel hot = hotelRepository.findById(hotelId).get();
        return mapper.hoteToDto(hot);
    }

    public QuartoDto findHotelQuarto(int hotel, int quarto) {
        Quarto qt = hotelRepository.findById(hotel).get().getQuartos().stream().filter(q -> q.getIdQuarto()==quarto).findFirst().get();
        return mapper.quartoToDto(qt);
    }
}
