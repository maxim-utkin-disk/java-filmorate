package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "select user_id, email, login, user_name, birthday from users";
    private static final String FIND_BY_ID_QUERY = "select user_id, email, login, user_name, birthday from genres where user_id = ?";
    private static final String INSERT_QUERY = "insert into users(email, login, user_name, birthday) values (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "update users set email = ?, login = ?, user_name = ?, birthday = ? where user_id = ?";
    private static final String FRIENDSHIP_ADD_QUERY = "insert into friendships(user1_id, user2_id, state_id) values(?, ?, ?)";
    private static final Integer FRIENDSHIP_CONSIDERING = 1;
    private static final Integer FRIENDSHIP_ACCEPTED = 2;
    private static final Integer FRIENDSHIP_REFUSED = 3;
    private static final String FRIENDS_PER_USER_QUERY =
            "select user_id, email, login, user_name, birthday \n" +
                    "from users u \n" +
                    "where u.user_id in \n" +
                    "  (select f1.user2_id from friendships f1 where f1.user1_id = ? and f1.state_id = 2 \n" +
                    "  union \n" +
                    "  select f2.user1_id from friendships f2 where f2.USER2_ID = ? and f2.state_id = 2 \n" +
                    "  )";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(Integer userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User create(User user) {
        Integer id = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                java.sql.Date.valueOf(user.getBirthday())
                );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                java.sql.Date.valueOf(user.getBirthday()),
                user.getId()
        );
        return user;
    }

    public void addFriend(int userId, int friendUserId) {
        update(FRIENDSHIP_ADD_QUERY, userId, friendUserId, FRIENDSHIP_ACCEPTED);
        update(FRIENDSHIP_ADD_QUERY, friendUserId, userId, FRIENDSHIP_CONSIDERING);
    }

    public List<User> getUserFriendsList(int userId) {
        return findMany(FRIENDS_PER_USER_QUERY, userId, userId);
    }


}

