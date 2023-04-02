package com.hotel.costa.mar.controller;

import com.hotel.costa.mar.model.Hotel;
import com.hotel.costa.mar.model.Reserva;
import com.hotel.costa.mar.model.dto.HotelDto;
import com.hotel.costa.mar.model.dto.QuartoDto;
import com.hotel.costa.mar.model.dto.ReservaDto;
import com.hotel.costa.mar.service.ApplicationService;
import com.hotel.costa.mar.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdmController {

    @Autowired
    private PublisherService publisherService;
    @Autowired
    private ApplicationService applicationService;

    @GetMapping(value = "/adm")
    public String index(Model model){
        return "index";
    }

    @GetMapping(value = "/cadastrarhotel")
    public String cadastroHotel(Model model){
        model.addAttribute("hotelParaCadastro", new HotelDto());
        return "cadastrarHotel";
    }

    @RequestMapping(value = "/saveHotel",
                    method = RequestMethod.POST)
    public String salvarHotel(Model model, @ModelAttribute("hotelParaCadastro") HotelDto hotelParaCadastro){
        publisherService.salvarHotel(hotelParaCadastro);
        return "index";
    }
    @GetMapping(value = "/cadastrarQuarto")
    public String cadastrarQuarto(Model model){
        List<HotelDto> hoteis = publisherService.findAllHoteis();
        model.addAttribute("hoteis", hoteis);

        model.addAttribute("quartoParaCadastro", new QuartoDto());
        return "cadastrarQuarto";
    }
    @RequestMapping(value = "/saveQuarto",
            method = RequestMethod.POST)
    public String salvarHotel(Model model, @ModelAttribute("quartoParaCadastro") QuartoDto quartoParaCadastro){
        publisherService.salvarQuarto(quartoParaCadastro);
        return "index";
    }
    @GetMapping(value = "/cadastrarReserva")
    public String criarReserva(Model model){
        model.addAttribute("reservaParaCadastro", new ReservaDto());
        model.addAttribute("hoteis", applicationService.getAllHotels());
        model.addAttribute("quartos", null);
        model.addAttribute("hotelEscolhido", "");
        model.addAttribute("hotelId", 0);

        return "cadastrarReserva";
    }

    @GetMapping(value = "/cadastrarReserva/{hotel}")
    public String criarReservaEscolhidoHotel(Model model, @PathVariable("hotel") int idHotel){
        model.addAttribute("hotelEscolhido", applicationService.findHotelById(idHotel).getNome());
        model.addAttribute("hoteis", applicationService.getAllHotels());
        model.addAttribute("quartos", publisherService.getQuartosFromHotel(idHotel));
        model.addAttribute("hotelId", idHotel);
        ReservaDto reserva = new ReservaDto();
        reserva.setIdHotel(idHotel);
        model.addAttribute("reservaParaCadastro", reserva);
        return "cadastrarReserva";
    }
    @RequestMapping(value = "/saveReservaLocal",
    method = RequestMethod.POST)
    public String reservarQuarto(@ModelAttribute("reservaParaCadastro") ReservaDto reservaDto, Model model){
        String texto = applicationService.reservarQuarto(reservaDto.getIdHotel(), reservaDto.getIdQuarto(), LocalDate.now().toString(), reservaDto.getDataSaida(), reservaDto.getIdUsuario());
        model.addAttribute("situcao", texto);
        return "fimReserva";
    }
    @GetMapping(value = "/cancelar-reservar/{hotel}")
    public String cancelarReserva(Model model, @PathVariable int hotel){
        List<ReservaDto> reservaDtos = publisherService.findAllReservasNoHotel(hotel);
        model.addAttribute("reservas", reservaDtos);

        return "cancelarReserva";
    }


}
