package ru.kdv.study.tTUser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.tTUser.exception.DataBaseException;
import ru.kdv.study.tTUser.model.User;
import ru.kdv.study.tTUser.repository.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO tt_users."user" (username, password_hash)
            VALUES(:username, :password_hash)
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
              FROM tt_users."user"
             WHERE id = :id
               AND is_deleted = false
            """;

    private static final String GET_ALL_ACTIVE = """
            SELECT *
              FROM tt_users."user"
             WHERE is_deleted = false
            """;

    private static final String DELETE_USER = """
            UPDATE tt_users."user"
               SET is_deleted = true
             WHERE id = :id
               AND is_deleted = false
            RETURNING *
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public User insert(User user) {
        //TODO: Добавить в базу unique constraint к полю username и обработать его здесь.
        try {
            return jdbcTemplate.queryForObject(INSERT, UserToSql(user), userMapper);
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (EmptyResultDataAccessException e) {
            throw DataBaseException.create(String.format("Пользователь не найден {id = %s}", id));
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    public List<User> getAllActive() {
        try {
            return jdbcTemplate.query(GET_ALL_ACTIVE, new MapSqlParameterSource(), userMapper);
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    public void delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        //TODO: Возможно определение deleted_at нужно перенести в базу.
        params.addValue("deleted_at", LocalDateTime.now());
        try {
            User user = jdbcTemplate.queryForObject(DELETE_USER, params, userMapper);
        }  catch (EmptyResultDataAccessException e) {
            throw DataBaseException.create(String.format("Пользователь не найден {id = %s}", id));
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    private MapSqlParameterSource UserToSql(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("username", user.getUsername());
        params.addValue("password_hash", user.getPasswordHash());
        params.addValue("is_deleted", user.isDeleted());

        return params;
    }
}