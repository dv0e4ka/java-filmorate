package ru.yandex.practicum.filmorate.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapterUtil extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter writer, Duration duration) throws IOException {
        try {
            writer.value(duration.toMinutes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Duration read(JsonReader reader) throws IOException {
        try {
            return Duration.ofMinutes(reader.nextLong());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}