package ru.kdv.study.taskTrackerUser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.taskTrackerUser.exception.DataBaseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private static final String FIND_USER_BY_TEAM = """
            SELECT user_id
              FROM tt_users.member
             WHERE team_id = :team_id
               AND deleted_at is null
            """;

    private static final String ADD_USER_TO_TEAM = """
            INSERT INTO tt_users.member (team_id, user_id)
            VALUES(:team_id, :user_id);
            """;

    private static final String REMOVE_USER_FROM_TEAM = """
            WITH updated AS (
                UPDATE tt_users.member
                   set deleted_at = current_timestamp
                 WHERE team_id = :team_id
                   AND user_id = :user_id
                RETURNING 1
            )
            SELECT COUNT(1) FROM UPDATED
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Long> findMembers(Long teamId) {
        try {
            return jdbcTemplate.queryForList(FIND_USER_BY_TEAM, new MapSqlParameterSource("team_id", teamId), Long.class);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void addUserToTeam(Long userId, Long teamId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user_id", userId);
            params.addValue("team_id", teamId);

            jdbcTemplate.update(ADD_USER_TO_TEAM,  params);

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("member_uk1")) {
                throw DataBaseException.create(String.format("Пользователь {user_id=%d} уже добавлен в команду {team_id=%d}", userId, teamId));
            } else if (e.getMessage().contains("member_team_fk")) {
                throw DataBaseException.create(String.format("Команда {team_id=%d} не найдена", teamId));
            } else if (e.getMessage().contains("member_user_fk")) {
                throw DataBaseException.create(String.format("Пользователь {user_id=%d} не найден", userId));
            } else {
                throw DataBaseException.create(e.getMessage());
            }
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    public void removeUserFromTeam(Long userId, Long teamId) {
        try {

            Optional.ofNullable(jdbcTemplate.queryForObject(REMOVE_USER_FROM_TEAM, new MapSqlParameterSource("user_id", userId).addValue("team_id", teamId), Integer.class))
                    .ifPresent(
                            count -> {
                                if (count == 0) {
                                    throw DataBaseException.create(String.format("Пользователь не найден в команде {userId=%d}", userId));
                                }
                            }
                    );
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }
}