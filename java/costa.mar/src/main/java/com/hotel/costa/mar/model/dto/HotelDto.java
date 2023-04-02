package com.hotel.costa.mar.model.dto;

import com.hotel.costa.mar.model.Quarto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelDto {

    private Integer idHotel;
    private List<Quarto> quartos;
    private String siglaEstado;
    private String keyWord;
    private String endereco;
    private String descricao;
    private String nome;

    public HotelDto(Integer idHotel, String siglaEstado, String keyWord, String endereco, String descricao, List<Quarto> quartos, String nome) {
        this.idHotel = idHotel;
        this.siglaEstado = siglaEstado;
        this.keyWord = keyWord;
        this.endereco = endereco;
        this.descricao = descricao;
        this.quartos = quartos;
        this.nome = nome;
    }
    public HotelDto(){

    }
}
