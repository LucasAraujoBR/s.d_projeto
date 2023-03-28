package com.hotel.costa.mar.model.dto;

import com.hotel.costa.mar.model.Quarto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelDto {

    private int idHotel;
    private List<Quarto> quartos;
    private int codEstado;
    private String keyWord;
    private String endereco;
    private String descricao;

    public HotelDto(int idHotel, int codEstado, String keyWord, String endereco, String descricao, List<Quarto> quartos) {
        this.idHotel = idHotel;
        this.codEstado = codEstado;
        this.keyWord = keyWord;
        this.endereco = endereco;
        this.descricao = descricao;
        this.quartos = quartos;
    }
}
