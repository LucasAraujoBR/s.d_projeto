package com.hotel.costa.mar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idHotel;
    @OneToMany(mappedBy = "hotel")
    private List<Quarto> quartos;


}
