package com.voinesti.versuriapp.controller;

import com.voinesti.versuriapp.repository.AppStateRepository;
import com.voinesti.versuriapp.repository.SongRepository;
import com.voinesti.versuriapp.model.AppState;
import com.voinesti.versuriapp.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;



@Controller
public class SongController {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private AppStateRepository appStateRepository;
    @PostMapping("/select-song/{id}")
public String selectSong(@PathVariable Long id) {
    AppState state = appStateRepository.findById(1L).orElse(new AppState());
    state.setCurrentSongId(id);
    appStateRepository.save(state);
    return "redirect:/"; // Ne întoarcem la listă după ce am ales
}
@GetMapping("/live")
public String liveView(Model model) {
    try {
        AppState state = appStateRepository.findById(1L).orElse(null);
        if (state != null && state.getCurrentSongId() != null) {
            Song currentSong = songRepository.findById(state.getCurrentSongId()).orElse(null);
            model.addAttribute("song", currentSong);
        } else {
            model.addAttribute("song", null);
        }
    } catch (Exception e) {
        // Dacă e vreo eroare de bază de date, măcar să nu vedem Error 500
        System.out.println("Eroare la citirea stării: " + e.getMessage());
        model.addAttribute("song", null);
    }
    return "live";
}



    // 1. Afișează lista de melodii cu numărători și sortare
    @GetMapping("/") 
    public String showSongList(@RequestParam(required = false) String cat, Model model) {
        List<Song> songs;
        
        if (cat != null && !cat.isEmpty()) {
            songs = songRepository.findByCategoryOrderByTitleAsc(cat);
        } else {
            songs = songRepository.findAllByOrderByTitleAsc();
        }
        
        // Trimitem numărul de piese pentru fiecare buton
        model.addAttribute("countToate", songRepository.count());
        model.addAttribute("countPopulara", songRepository.findByCategory("Populara").size());
        model.addAttribute("countColinde", songRepository.findByCategory("Colinde").size());
        model.addAttribute("countPatriotice", songRepository.findByCategory("Patriotice").size());
        
        model.addAttribute("songs", songs);
        return "song_list"; 
    }
    
    // 2. Afișează detaliile unei singure melodii
    @GetMapping("/song/{id}") 
    public String showSongDetails(@PathVariable Long id, Model model) {
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Melodia cu ID-ul: " + id + " nu a fost găsită."));

        model.addAttribute("song", song);
        return "song_detail"; 
    }

    // 3. Afișează formularul pentru adăugare piesă nouă
    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("song", new Song());
        return "adauga_piesa";
    }

    // 4. Salvează piesa în baza de date
    @PostMapping("/salveaza")
    public String saveSong(@ModelAttribute("song") Song song) {
        song.setDateAdded(java.time.LocalDate.now());
        songRepository.save(song);
        return "redirect:/";
    }
 // Afișează formularul de editare
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Song song = songRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID invalid: " + id));
        model.addAttribute("song", song);
        return "adauga_piesa"; // Refolosim același formular
    }

    // Șterge o piesă
    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        songRepository.deleteById(id);
        return "redirect:/";
    }
    
}