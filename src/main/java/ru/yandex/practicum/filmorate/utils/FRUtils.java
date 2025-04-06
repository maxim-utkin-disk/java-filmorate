package ru.yandex.practicum.filmorate.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FRUtils {

    public static Integer getNextIdForObjectsList(Set<Integer> objectsList) {
        int lastId = objectsList
                .stream()
                .mapToInt(item -> item)
                .max()
                .orElse(0);
        return ++lastId;
    }

    public static String getDateFormat() {
        return "yyyy-MM-dd";
    }

    public static Charset getDefaultCharset() {
        return StandardCharsets.UTF_8;
    }
}
