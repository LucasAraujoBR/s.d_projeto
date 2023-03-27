package com.hotel.costa.mar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idQuarto;
    @ManyToOne
    @JoinColumn(name = "idHotel")
    private Hotel hotel;

    private boolean ocupado;
    private int pessoasMax;
    private String descricaoQuarto;


}
