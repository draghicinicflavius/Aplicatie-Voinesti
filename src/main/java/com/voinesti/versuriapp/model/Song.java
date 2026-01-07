package com.voinesti.versuriapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // Indica ca aceasta clasa este o entitate DB
@Table(name = "song") // Mapeaza la tabela 'song'
public class Song {

    @Id // Cheia primara
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    // ATENTIE: Mapeaza la coloana 'versuri' din baza de date
    @Column(name = "versuri", columnDefinition = "TEXT")
    private String versuri;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    // --- Constructor implicit (necesar de JPA)
    public Song() {}

    // --- GETTERS & SETTERS (Acestea sunt necesare pentru a accesa campurile)
    // Puteti genera automat in Eclipse: Click dreapta in editor -> Source -> Generate Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersuri() {
        return versuri;
    }

    public void setVersuri(String versuri) {
        this.versuri = versuri;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
}