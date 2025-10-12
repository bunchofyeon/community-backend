package com.example.week5.service;

import com.example.week5.repository.CommentsRepository;
import com.example.week5.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;

}
