package ru.kdv.study.taskTrackerUser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.taskTrackerUser.exception.DataBaseException;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.repository.mapper.UserMapper;

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

    private static final String DELETE_USER = """
            UPDATE tt_users."user"
               SET is_deleted = true
             WHERE id = :id
               AND is_deleted = false
            RETURNING *
            """;

    private static final String GET_ACTIVE_BY_IDS = """
            SELECT *
              FROM tt_users."user"
             WHERE is_deleted = false
               and id in (:ids)
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public User insert(User user) {
        try {
            return jdbcTemplate.queryForObject(INSERT, UserToSql(user), userMapper);
        } catch (DuplicateKeyException e) {
            throw DataBaseException.create(String.format("Пользователь с именем '%s' уже существует", user.getUsername()));
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

    public List<User> getActiveByIds(List<Long> ids) {
        return jdbcTemplate.query(GET_ACTIVE_BY_IDS, new MapSqlParameterSource("ids", ids), userMapper);
    }

    public void delete(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(DELETE_USER, new MapSqlParameterSource("id", id), userMapper);
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