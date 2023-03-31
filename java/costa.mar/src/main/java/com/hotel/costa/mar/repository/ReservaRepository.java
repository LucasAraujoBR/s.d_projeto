package com.hotel.costa.mar.repository;

import com.hotel.costa.mar.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    List<Reserva> findByIdQuarto(int idQuarto);


    @Query(value = "select * from reserva where id_quarto = ?1 " +
            "and hotel = ?2 and (( ?3 between data_entrada and data_saida) or ( ?4 between data_entrada and data_saida))", nativeQuery = true)
    Reserva testIfDatesAreBetweenReservas(int idQuarto, int idHotel, String dataEntrada, String dataSaida);
}
