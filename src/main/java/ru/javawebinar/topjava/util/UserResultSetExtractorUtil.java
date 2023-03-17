package ru.javawebinar.topjava.util;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.repository.jdbc.JdbcUserRepository.ROW_MAPPER;

public class UserResultSetExtractorUtil implements ResultSetExtractor<List<User>> {
    public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        Map<Integer, User> userMap = new LinkedHashMap<>();
        int i = 0;
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            User user = userMap.get(id);

            if (user == null) {
                user = ROW_MAPPER.mapRow(resultSet, i++);
                userMap.put(id, user);
            } else {
                i++;
            }
            String roles = resultSet.getString("role");
            if (!(roles == null) && user != null) {
                EnumSet<Role> roleEnumSet = EnumSet.of(Enum.valueOf(Role.class, roles));
                if (user.getRoles() != null) {
                    roleEnumSet.addAll(user.getRoles());
                    user.setRoles(roleEnumSet);
                } else {
                    user.setRoles(roleEnumSet);
                }
            } else {
                user.setRoles(EnumSet.noneOf(Role.class));
            }
        }
        return new ArrayList<>(userMap.values());
    }
}