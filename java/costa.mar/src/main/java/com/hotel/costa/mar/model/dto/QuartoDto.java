package com.hotel.costa.mar.model.dto;

import com.hotel.costa.mar.model.Hotel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartoDto {

    private int idQuarto;
    private int hotel;
    private boolean ocupado;
    private int pessoasMax;
    private String descricaoQuarto;

    public QuartoDto(int idQuarto, int hotel, boolean ocupado, int pessoasMax, String descricaoQuarto) {
        this.idQuarto = idQuarto;
        this.hotel = hotel;
        this.ocupado = ocupado;
        this.pessoasMax = pessoasMax;
        this.descricaoQuarto = descricaoQuarto;
    }
}
