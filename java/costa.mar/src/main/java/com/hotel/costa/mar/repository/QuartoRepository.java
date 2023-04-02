package com.hotel.costa.mar.repository;

import com.hotel.costa.mar.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Integer> {

    @Query(value = "select * from quarto where id_hotel = ?1 ", nativeQuery = true)
    List<Quarto> findQuartosFromHotel(int idHotel);
}
