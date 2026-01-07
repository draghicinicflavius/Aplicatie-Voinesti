package com.voinesti.versuriapp.controller;

import com.voinesti.versuriapp.repository.SongRepository;
import com.voinesti.versuriapp.model.Song; // ATENTIE: Asigura-te ca path-ul 'com.voinesti.versuriapp.model' este corect!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // NECESAR pentru a prelua ID-ul din URL

@Controller
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/") // 1. Afiseaza lista de melodii
    public String showSongList(Model model) {
        // Preluam toate melodiile si le trimitem catre song_list.html
        model.addAttribute("songs", songRepository.findAll());
        return "song_list"; 
    }
    
    // 2. Afiseaza detaliile unei singure melodii
    @GetMapping("/song/{id}") 
    public String showSongDetails(@PathVariable Long id, Model model) {
        
        // Gaseste melodia dupa ID. Daca nu exista, arunca o eroare 404 (in mod implicit, aici 500).
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Melodia cu ID-ul: " + id + " nu a fost gasita."));

        // Adauga melodia gasita la Model
        model.addAttribute("song", song);
        
        // Returneaza pagina HTML de detaliu
        return "song_detail"; 
    }
}