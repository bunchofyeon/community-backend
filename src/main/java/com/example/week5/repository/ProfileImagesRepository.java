package com.example.week5.repository;

import com.example.week5.entity.ProfileImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImagesRepository extends JpaRepository<ProfileImages, Long> {
    Optional<ProfileImages> findByUsersId(Long usersId);
}
