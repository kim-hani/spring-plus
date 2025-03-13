package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoRepositoryCustom {

    Optional<Todo> findByIdWithUserCustom(Long todoId);


    /**
     * QueryDSL을 통한 검색:
     * - 제목(title) 부분검색
     * - 날짜(createdAt) 범위 검색
     * - 닉네임(username) 부분검색
     */
    Page<TodoSearchResponse> searchTodos(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String username,
            Pageable pageable
    );
}
