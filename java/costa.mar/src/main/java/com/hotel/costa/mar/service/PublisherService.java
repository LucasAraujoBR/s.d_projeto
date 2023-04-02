package com.hotel.costa.mar.service;


import com.hotel.costa.mar.map.ApplicationMapper;
import com.hotel.costa.mar.model.Hotel;
import com.hotel.costa.mar.model.Quarto;
import com.hotel.costa.mar.model.Reserva;
import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.model.dto.ReservaDto;
import com.hotel.costa.mar.repository.HotelRepository;
import com.hotel.costa.mar.repository.QuartoRepository;
import com.hotel.costa.mar.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PublisherService {


    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ReservaRepository reservaRepository;
    @Transactional
    public void salvarHotel(HotelDto hotelDto){
        Hotel hot = mapper.hotelToEntity(hotelDto);
        hotelRepository.save(hot);
    }


    public List<HotelDto> findAllHoteis() {
       List<Hotel> hoteis =  hotelRepository.findAll();
       List<HotelDto> dtos = mapper.hotelToDto(hoteis);
       return dtos;
    }

    public void salvarQuarto(QuartoDto quartoParaCadastro) {
        Hotel hotel = hotelRepository.findById(quartoParaCadastro.getHotel()).get();
        Quarto qt = mapper.quartoToEntity(quartoParaCadastro, hotel);
        quartoRepository.save(qt);
    }

    public List<ReservaDto> findAllReservasNoHotel(int hotel) {
        List<Reserva> reservas = reservaRepository.findByHotel(hotel);
        List<ReservaDto> dtosReserva = mapper.reservatoDto(reservas);
        return dtosReserva;

    }

    @Transactional
    public String reservarQuarto(int hotel, int quarto, String dataEntrada,
                                 String dataSaida, String usuario) {

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

    public List<QuartoDto> getQuartosFromHotel(int idHotel) {
        List<Quarto> quartos = quartoRepository.findQuartosFromHotel(idHotel);
        List<QuartoDto> dtos = mapper.quartoToDto(quartos);
        return dtos;
    }
}
