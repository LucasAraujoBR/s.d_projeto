package com.hotel.costa.mar.repository;


import com.hotel.costa.mar.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {



}
