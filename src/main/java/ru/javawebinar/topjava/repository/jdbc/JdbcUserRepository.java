package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    public static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    ResultSetExtractor<List<User>> resultExtractor = resultSet -> {
        Map<Integer, User> userMap = new LinkedHashMap<>();
        int i = 0;
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            User user = userMap.get(id);
            if (user == null) {
                user = ROW_MAPPER.mapRow(resultSet, i);
                userMap.put(id, user);
            }
            i++;
            String role = resultSet.getString("role");
            if (role != null) {
                EnumSet<Role> roleEnumSet = EnumSet.of(Enum.valueOf(Role.class, role));
                if (user.getRoles() != null) {
                    roleEnumSet.addAll(user.getRoles());
                }
                user.setRoles(roleEnumSet);
            } else {
                user.setRoles(EnumSet.noneOf(Role.class));
            }
        }
        return new ArrayList<>(userMap.values());
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                    UPDATE users SET name=:name, email=:email, password=:password, registered=:registered, enabled=:enabled,
                     calories_per_day=:caloriesPerDay WHERE id=:id""", parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }

        if (!user.getRoles().isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_role VALUES (?,?)", new BatchPreparedStatementSetter() {
                Iterator<Role> roleIterator = user.getRoles().iterator();

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.getId());
                    ps.setObject(2, roleIterator.next().toString());
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_role ur on users.id = ur.user_id WHERE id=?", resultExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_role ur on users.id = ur.user_id WHERE email=?", resultExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_role " +
                "ur on users.id = ur.user_id ORDER BY name, email", resultExtractor);
    }
}