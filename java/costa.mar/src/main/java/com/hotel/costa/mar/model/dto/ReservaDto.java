package com.hotel.costa.mar.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDto {



    private int id;
    private int idQuarto;
    private String idUsuario;
    private int idHotel;
    private String dataEntrada;
    private String dataSaida;


    public ReservaDto(int id, int idQuarto, String idUsuario, String dataEntrada, String dataSaida, int idHotel ) {
        this.id = id;
        this.idQuarto = idQuarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.idHotel = idHotel;
        this.idUsuario = idUsuario;
    }
    public ReservaDto(){

    }

    public String toString(){
        String texto = "";
        texto = "Quarto: " + this.getIdQuarto() + " Data Entrada: " + this.getDataEntrada().toString() + " " + this.getIdUsuario();
        return texto;
    }
}
