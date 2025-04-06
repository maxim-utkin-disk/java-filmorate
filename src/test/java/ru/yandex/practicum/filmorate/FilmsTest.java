package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
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


public class FilmsTest {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    private URI uri = URI.create("http://localhost:8080/films");

    @BeforeAll
    public static void startServer() {
        FilmorateApplication.main(new String[0]);
    }

    @AfterAll
    public static void stopServer() {
        FilmorateApplication.stopServer();
    }

    @Test
    public void postNewFilmTest() throws IOException, InterruptedException {
        Film f1 = new Film(null,
                "Film 1 name",
                "Film 1 Description",
                LocalDate.of(2025,04,04),
                60);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(f1)))
                .build();

        HttpClient htpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = htpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "POST: Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response.body(), "POST: Пустое тело ответа - ошибка добавления фильма");

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        assertEquals(f1.getName(), jsonObject.get("name").getAsString(), "Отличаются наименования фильма");
        assertEquals(f1.getDescription(), jsonObject.get("description").getAsString(), "Отличаются наименования фильма");
        assertEquals(f1.getReleaseDate(),
                LocalDate.parse(
                        jsonObject.get("releaseDate").getAsString(),
                        DateTimeFormatter.ofPattern(FRUtils.getDateFormat())),
                "Отличаются даты выпуска фильмов");
        assertEquals(f1.getDuration(), jsonObject.get("duration").getAsInt(), "Отличаются продолжительности фильма");
        }

    @Test
    public void putNotExistFilmTest() throws IOException, InterruptedException {
        Film f1 = new Film(100500,
                "Film 2 name",
                "Film 2 Description",
                LocalDate.of(2025,04,04),
                60);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(f1)))
                .build();

        HttpClient htpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = htpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotEquals(200, response.statusCode(),
                "Проверка обновления несуществующего фильма не должна возвращать код успешной операции");
    }

    @Test
    public void putExistFilmTest() throws IOException, InterruptedException {
        Film f1 = new Film(null,
                "Film 3 name",
                "Film 3 Description",
                LocalDate.of(2025,04,04),
                60);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(f1)))
                .build();

        HttpClient htpClient1 = HttpClient.newHttpClient();
        HttpResponse<String> response1 = htpClient1.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode(), "PUT: Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response1.body(), "PUT: Пустое тело ответа - ошибка добавления фильма");

        JsonObject jsonObject1 = JsonParser.parseString(response1.body()).getAsJsonObject();
        assertNotNull(jsonObject1.get("id").getAsInt(), "Фильму присвоен неправильный id");

        f1.setId(jsonObject1.get("id").getAsInt());
        f1.setName(f1.getName() + " updated");
        f1.setDescription(f1.getDescription() + " updated");
        f1.setDuration(f1.getDuration() + 100);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(f1)))
                .build();

        HttpClient htpClient2 = HttpClient.newHttpClient();
        HttpResponse<String> response2 = htpClient1.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode(), "Неправильный код ответа либо неуспешное выполнение");
        assertNotEquals(null, response2.body(), "Пустое тело ответа - ошибка обновления фильма");

        JsonObject jsonObject2 = JsonParser.parseString(response2.body()).getAsJsonObject();
        assertEquals(f1.getName(), jsonObject2.get("name").getAsString(), "Отличаются наименования фильма");
        assertEquals(f1.getDescription(), jsonObject2.get("description").getAsString(), "Отличаются описания фильма");
        assertEquals(f1.getDuration(), jsonObject2.get("duration").getAsInt(), "Отличаются продолжительности фильма");
    }

    @Test
    public void getFilmTest() throws IOException, InterruptedException {
        Film f1 = new Film(null,
                "Film 4 name",
                "Film 4 Description",
                LocalDate.of(2025,04,04),
                60);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(f1)))
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

        assertEquals(200, response2.statusCode(), "GET: ошибка запроса списка фильмов");
        assertNotEquals(null, response2.body(), "GET: Пустое тело ответа - ошибка запроса списка фильма");
    }

}
