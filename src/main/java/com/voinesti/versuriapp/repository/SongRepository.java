package com.voinesti.versuriapp.repository;

import com.voinesti.versuriapp.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marca ca fiind un component de Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // Spring va implementa automat metodele de baza:
    // .findAll() -> Citeste toate cantecele
    // .findById(id) -> Citeste un singur cantec
    // .save(song) -> Salveaza un cantec nou sau il actualizeaza
    // .delete(song) -> Sterge un cantec

}