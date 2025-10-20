package com.example.week5.repository;

import com.example.week5.entity.Comments;
import com.example.week5.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    // 1. 댓글 조회
    // 1-1. 댓글 전체 조회
    @Query("""
        SELECT c
        FROM Comments c
        JOIN FETCH c.users
        WHERE c.posts.id = :postId
        """)
    Page<Comments> findAllByPostIdWithUsers(@Param("postId") Long postId, Pageable pageable);

    // 1-2. 댓글 상세 조회
    // 게시글 상세 조회 했을때 게시글과 댓글 같이 보여주려고 -> Service에서 합쳐야함
    @Query(value = "SELECT c FROM Comments c JOIN FETCH c.users WHERE c.id = :id")
    Optional<Comments> findByIdWithUsers(@Param("id") Long id);

    // 1-3. 사용자별 댓글 조회
    Page<Comments> findAllByUsers(Pageable pageable, Users users);

    // 2. 댓글 검색
    // 2-1. 댓글 내용 검색
    @Query(value = "SELECT c FROM Comments c JOIN FETCH c.users WHERE c.content LIKE %:content%")
    Page<Comments> findAllContentContaining(String content, Pageable pageable);

    // 2-2. 댓글 작성자 검색
    @Query(value = "SELECT c FROM Comments c JOIN FETCH c.users WHERE c.users.nickname LIKE %:nickname%")
    Page<Comments> findAllNicknameContaining(String nickname, Pageable pageable);

}
