package com.hotel.costa.mar.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservaDto {



    private int id;
    private int idQuarto;
    private int idUsuario;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;


    public ReservaDto(int id, int idQuarto, int idUsuario, LocalDate dataEntrada, LocalDate dataSaida) {
        this.id = id;
        this.idQuarto = idQuarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }
}
