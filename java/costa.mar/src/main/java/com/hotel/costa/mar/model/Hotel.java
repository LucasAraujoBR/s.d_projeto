package com.hotel.costa.mar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @OneToMany(mappedBy = "hotel")
    private List<Quarto> quartos;
    private String nome;
    private String siglaEstado;
    private String keyWord;
    private String endereco;
    private String descricao;




}
