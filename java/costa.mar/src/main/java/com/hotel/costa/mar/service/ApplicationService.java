package com.hotel.costa.mar.service;


import com.hotel.costa.mar.map.ApplicationMapper;
import com.hotel.costa.mar.model.Hotel;
import com.hotel.costa.mar.model.Quarto;
import com.hotel.costa.mar.model.Reserva;
import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.repository.HotelRepository;
import com.hotel.costa.mar.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReservaRepository reservaRepository;
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
        List<Reserva> reservas = new ArrayList<>();
        Quarto qt = hotelRepository.findById(hotel).get().getQuartos().stream().filter(q -> q.getIdQuarto()==quarto).findFirst().get();

        if(qt != null){
            reservas = reservaRepository.findByIdQuarto(qt.getIdQuarto());
        }
        return mapper.quartoToDto(qt, reservas);
    }

    @Transactional
    public String reservarQuarto(int hotel, int quarto, String dataEntrada,
                                 String dataSaida, int usuario) {

        LocalDate dtIn = LocalDate.parse(dataEntrada);
        LocalDate dtOut = LocalDate.parse(dataSaida);

        Reserva reserva = reservaRepository.testIfDatesAreBetweenReservas(quarto, hotel, dataEntrada, dataSaida);
        if(reserva == null){
            reservaRepository.save(new Reserva(hotel, quarto, usuario, dtIn, dtOut));
            return "Quarto Reservado com sucesso";
        }else{
            return "Quarto já reservado nessa data";
        }
    }
}
