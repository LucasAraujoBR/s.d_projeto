package com.hotel.costa.mar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Reserva {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int hotel;
    private int idQuarto;
    private int idUsuario;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;

    public Reserva(int id, int hotel, int idQuarto, int idUsuario, LocalDate dataEntrada, LocalDate dataSaida) {
        this.id = id;
        this.hotel = hotel;
        this.idQuarto = idQuarto;
        this.idUsuario = idUsuario;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }

    public Reserva(int hotel, int idQuarto, int idUsuario, LocalDate dataEntrada, LocalDate dataSaida) {
        this.idQuarto = idQuarto;
        this.idUsuario = idUsuario;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.hotel = hotel;
    }
    public Reserva(){

    }



}
