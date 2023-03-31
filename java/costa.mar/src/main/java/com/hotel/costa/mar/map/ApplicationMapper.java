package com.hotel.costa.mar.map;

import com.hotel.costa.mar.model.Hotel;
import com.hotel.costa.mar.model.Quarto;
import com.hotel.costa.mar.model.Reserva;
import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.model.dto.ReservaDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationMapper {

    public List<HotelDto> hotelToDto(List<Hotel> hotels){
        List<HotelDto> dtos = new ArrayList<>();
        HotelDto dto;
        for(Hotel hotel: hotels){
            dto = new HotelDto(hotel.getIdHotel(),
                    hotel.getCodEstado(),
                    hotel.getKeyWord(),
                    hotel.getEndereco(),
                    hotel.getDescricao(),
                    hotel.getQuartos());
            dtos.add(dto);
        }
        return dtos;
    }

    public HotelDto hoteToDto(Hotel hotel) {
        HotelDto dto;
        dto = new HotelDto(hotel.getIdHotel(),
                hotel.getCodEstado(),
                hotel.getKeyWord(),
                hotel.getEndereco(),
                hotel.getDescricao(),
                hotel.getQuartos());
        return dto;
    }

    public QuartoDto quartoToDto(Quarto qt) {

        QuartoDto dto = new QuartoDto(qt.getIdQuarto(),
                    qt.getHotel().getIdHotel(),
                    qt.isOcupado(),
                    qt.getPessoasMax(),
                    qt.getDescricaoQuarto());
        return dto;
    }

    public QuartoDto quartoToDto(Quarto qt, List<Reserva> reservas) {
        List<ReservaDto> reservasDto = reservatoDto(reservas);
        QuartoDto dto = new QuartoDto(qt.getIdQuarto(),
                qt.getHotel().getIdHotel(),
                qt.isOcupado(),
                qt.getPessoasMax(),
                qt.getDescricaoQuarto(),
                reservasDto);
        return dto;

    }

    public List<ReservaDto> reservatoDto(List<Reserva> reservas) {
        List<ReservaDto> dtos = new ArrayList<>();

        for(Reserva re:reservas){
            dtos.add(new ReservaDto(re.getId(),re.getIdQuarto(), re.getIdUsuario(), re.getDataEntrada(), re.getDataSaida()));
        }
        return dtos;
    }

}
