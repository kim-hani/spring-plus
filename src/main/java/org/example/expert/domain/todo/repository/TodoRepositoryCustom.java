package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TodoRepositoryCustom {

    Optional<Todo> findByIdWithUserCustom(Long todoId);

}
