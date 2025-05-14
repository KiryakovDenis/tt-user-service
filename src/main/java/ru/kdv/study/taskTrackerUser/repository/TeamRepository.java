package ru.kdv.study.taskTrackerUser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.taskTrackerUser.exception.DataBaseException;
import ru.kdv.study.taskTrackerUser.model.Team;
import ru.kdv.study.taskTrackerUser.repository.mapper.TeamMapper;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private static final String INSERT = """
            INSERT INTO tt_users.team ("name", owner_team)
            VALUES(:name, :owner_team)
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT id, "name", owner_team, created_at, updated_at
              FROM tt_users.team
             WHERE id = :id""";


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TeamMapper teamMapper;

    public Team insert(Team team) {
        try {
            return jdbcTemplate.queryForObject(INSERT, teamToSql(team), teamMapper);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("team_uk1")) {
                throw DataBaseException.create(String.format("Команда с именем {name=%s} уже существует", team.getName()));
            } else {
                throw DataBaseException.create(e.getMessage());
            }
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    public Team getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), teamMapper);
        } catch (EmptyResultDataAccessException e) {
            throw DataBaseException.create(String.format("Команда не найдена {id = %s}", id));
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    private MapSqlParameterSource teamToSql(Team team) {
        return new MapSqlParameterSource()
                .addValue("id", team.getId())
                .addValue("name", team.getName())
                .addValue("owner_team", team.getOwnerId());
    }
}
