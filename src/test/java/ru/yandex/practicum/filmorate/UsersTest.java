package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.FRUtils;
import ru.yandex.practicum.filmorate.utils.LocalDateAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


public class UsersTest {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    private URI uri = URI.create("http://localhost:8080/users");

    @BeforeAll
    public static void startServer() {
        FilmorateApplication.main(new String[0]);
    }

    @AfterAll
    public static void stopServer() {
        FilmorateApplication.stopServer();
    }

    @Test
    public void postNewUserTest() throws IOException, InterruptedException {
        User u1 = new User(null,
                "user1@mail.ru",
                "user1login",
                "user1name",
                LocalDate.of(2005,05,05));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(u1)))
                .build();

        HttpClient htpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = htpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "POST: Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response.body(), "POST: Пустое тело ответа - ошибка добавления пользователя");

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        assertEquals(u1.getName(), jsonObject.get("name").getAsString(), "Отличаются имена пользователей");
        assertEquals(u1.getEmail(), jsonObject.get("email").getAsString(), "Отличаются почтовые адреса пользователей");
        assertEquals(u1.getLogin(), jsonObject.get("login").getAsString(), "Отличаются логины пользователей");
        assertEquals(u1.getBirthday(),
                LocalDate.parse(
                        jsonObject.get("birthday").getAsString(),
                        DateTimeFormatter.ofPattern(FRUtils.getDateFormat())),
                "Отличаются даты рождения пользователей");
    }

    @Test
    public void putNotExistUserTest() throws IOException, InterruptedException {
        User u1 = new User(100500,
                "user2@mail.ru",
                "user2login",
                "user2name",
                LocalDate.of(2005,05,05));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(u1)))
                .build();

        HttpClient htpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = htpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(200, response.statusCode(),
                "Проверка обновления несуществующего пользователя не должна возвращать код успешной операции");
    }

    @Test
    public void putExistUserTest() throws IOException, InterruptedException {
        User u1 = new User(null,
                "user3@mail.ru",
                "user3login",
                "user3name",
                LocalDate.of(2005,05,05));

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(u1)))
                .build();

        HttpClient htpClient1 = HttpClient.newHttpClient();
        HttpResponse<String> response1 = htpClient1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode(), "PUT: Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response1.body(), "PUT: Пустое тело ответа - ошибка добавления пользователя");

        JsonObject jsonObject1 = JsonParser.parseString(response1.body()).getAsJsonObject();
        assertNotNull(jsonObject1.get("id").getAsInt(), "Пользователю присвоен неправильный id");

        u1.setId(jsonObject1.get("id").getAsInt());
        u1.setName(u1.getName() + "updated");
        u1.setLogin(u1.getLogin() + "updated");
        u1.setEmail(u1.getEmail() + ".upd");

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(u1)))
                .build();

        HttpClient htpClient2 = HttpClient.newHttpClient();
        HttpResponse<String> response2 = htpClient1.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode(), "Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response2.body(), "Пустое тело ответа - ошибка обновления пользователя");

        JsonObject jsonObject2 = JsonParser.parseString(response2.body()).getAsJsonObject();
        assertEquals(u1.getName(), jsonObject2.get("name").getAsString(), "Отличаются имена пользователей");
        assertEquals(u1.getLogin(), jsonObject2.get("login").getAsString(), "Отличаются логины");
        assertEquals(u1.getEmail(), jsonObject2.get("email").getAsString(), "Отличаются почтовые адреса");
    }

    @Test
    public void getUserTest() throws IOException, InterruptedException {
        User u1 = new User(null,
                "user4@mail.ru",
                "user4login",
                "user4name",
                LocalDate.of(2005,05,05));

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(u1)))
                .build();

        HttpClient htpClient1 = HttpClient.newHttpClient();
        HttpResponse<String> response1 = htpClient1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpClient htpClient2 = HttpClient.newHttpClient();
        HttpResponse<String> response2 = htpClient1.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode(), "GET: ошибка запроса списка пользователей");
        assertNotEquals(null, response2.body(), "GET: Пустое тело ответа - ошибка запроса списка пользователей");
    }

}

