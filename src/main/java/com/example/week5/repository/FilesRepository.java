package com.example.week5.repository;

import com.example.week5.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<Files, Long> {
}
