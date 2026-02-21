package com.voinesti.versuriapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_state")
public class AppState {
    @Id
    private Long id = 1L; // ID-ul trebuie să fie fixat la 1

    @Column(name = "current_song_id")
    private Long currentSongId;
    
    // Asigură-te că ai un constructor gol (obligatoriu pentru Hibernate)
    public AppState() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCurrentSongId() { return currentSongId; }
    public void setCurrentSongId(Long currentSongId) { this.currentSongId = currentSongId; }
}