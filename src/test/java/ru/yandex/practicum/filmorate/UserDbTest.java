package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@Import({DbUserStorage.class})
public class UserDbTest {

    private final UserStorage userStorage;
    private User user4Test;

    @Autowired
    public UserDbTest(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @BeforeEach
    public void prepareData4Test() {
        user4Test = new User(null, "e@mail.ru", "test_login", "test_name", LocalDate.of(1970,1,1));
    }

    @Test
    public void testCreateUserAndGetById() {
        User createdUser = userStorage.createNewUser(user4Test);
        User foundUser = userStorage.getUserById(createdUser.getId());
        assertThat(foundUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(createdUser);
    }

    @Test
    public void testUpdateUser() {
        User createdUser = userStorage.createNewUser(user4Test);
        User updatedUser = new User(createdUser.getId(),
                "upd.e@mail.ru",
                "upd_test_login",
                "upd_test_name",
                LocalDate.of(1970,2,2)
        );
        User updResult = userStorage.updateExistingUser(updatedUser);

        assertThat(updResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    void testGetAllUsers() {
        User user1 = userStorage.createNewUser(user4Test);
        User user2 =  userStorage.createNewUser(
                new User(null,
                        "upd1_" + user4Test.getEmail(),
                        "upd1_" + user4Test.getLogin(),
                        "upd1_" + user4Test.getName(),
                        LocalDate.of(1970,2,2)
                )
        );
        User user3 =  userStorage.createNewUser(
                new User(null,
                        "upd2_" + user4Test.getEmail(),
                        "upd2_" + user4Test.getLogin(),
                        "upd2_" + user4Test.getName(),
                        LocalDate.of(1970,3,3)
                )
        );
        List<User> users = userStorage.getUsersList();

        assertThat(users).hasSize(3);
    }

    @Test
    public void testFriendship() {
        User user1 = userStorage.createNewUser(user4Test);
        User user2 =  userStorage.createNewUser(
                new User(null,
                        "upd1_" + user4Test.getEmail(),
                        "upd1_" + user4Test.getLogin(),
                        "upd1_" + user4Test.getName(),
                        LocalDate.of(1970,2,2)
                )
        );
        User user3 =  userStorage.createNewUser(
                new User(null,
                        "upd2_" + user4Test.getEmail(),
                        "upd2_" + user4Test.getLogin(),
                        "upd2_" + user4Test.getName(),
                        LocalDate.of(1970,3,3)
                )
        );

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user3.getId(), user2.getId());

        List<User> commonFriends13 = userStorage.getCommonFriendsList(user1.getId(), user3.getId());
        assertThat(commonFriends13).hasSize(1);

        List<User> friendsOf1 = userStorage.getUserFriendsList(user1.getId());
        assertThat(friendsOf1).hasSize(1);

        userStorage.removeFriend(user3.getId(), user2.getId());

        List<User> friendsOf3 = userStorage.getUserFriendsList(user3.getId());
        assertThat(friendsOf3).hasSize(0);
    }

}
