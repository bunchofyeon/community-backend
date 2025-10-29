package com.example.week5.repository;

import com.example.week5.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Long> {

    List<Files> findByPostsId(Long postsId);

    boolean existsByStorageKey(String storageKey);
}
