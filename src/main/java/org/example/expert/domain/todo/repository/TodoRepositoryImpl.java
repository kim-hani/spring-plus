package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUserCustom(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = queryFactory.selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }


    @Override
    public Page<TodoSearchResponse> searchTodos(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            String username,
            Pageable pageable
    ) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QManager manager = QManager.manager;

        // 메인 쿼리
        List<TodoSearchResponse> content = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.id,
                        todo.title,
                        manager.id.countDistinct().intValue(),
                        comment.id.countDistinct().intValue()
                ))
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(comment).on(comment.todo.eq(todo))
                .leftJoin(todo.user, user)
                .where(
                        titleContains(title),
                        usernameContains(username),
                        dateBetween(startDate, endDate)
                )
                .groupBy(todo.id)
                .orderBy(todo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트
        Long total = queryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(comment).on(comment.todo.eq(todo))
                .leftJoin(todo.user, user)
                .where(
                        titleContains(title),
                        usernameContains(username),
                        dateBetween(startDate, endDate)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }


    private BooleanExpression titleContains(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        return QTodo.todo.title.contains(title);
    }

    private BooleanExpression usernameContains(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return QUser.user.username.contains(username);
    }

    private BooleanExpression dateBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return null;
        }

        return QTodo.todo.createdAt.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }
}
