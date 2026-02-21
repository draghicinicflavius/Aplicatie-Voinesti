package com.voinesti.versuriapp.repository;

import com.voinesti.versuriapp.model.AppState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStateRepository extends JpaRepository<AppState, Long> {
}