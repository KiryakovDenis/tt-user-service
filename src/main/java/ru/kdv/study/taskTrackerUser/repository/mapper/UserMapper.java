package ru.kdv.study.taskTrackerUser.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .passwordHash(rs.getString("password_hash"))
                .deleted(rs.getBoolean("is_deleted"))
                .deletedAt(rs.getObject("deleted_at", LocalDateTime.class))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .role(Role.valueOf(rs.getString("role")))
                .build();
    }
}
