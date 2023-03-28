package com.hotel.costa.mar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idHotel")
    private Hotel hotel;

    private boolean ocupado;
    private int pessoasMax;
    private String descricaoQuarto;


}
