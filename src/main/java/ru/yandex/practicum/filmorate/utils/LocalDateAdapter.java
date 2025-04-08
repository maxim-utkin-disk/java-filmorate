package ru.yandex.practicum.filmorate.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if (localDate != null) {
            jsonWriter.value(localDate.format(DateTimeFormatter.ofPattern(FRUtils.getDateFormat())));
        } else {
            jsonWriter.nullValue();
        }

    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString(), DateTimeFormatter.ofPattern(FRUtils.getDateFormat()));
    }
}
