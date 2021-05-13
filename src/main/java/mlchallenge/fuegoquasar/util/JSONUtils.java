package mlchallenge.fuegoquasar.util;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONUtils {
    private JSONUtils() {
    }

    public static String getJSONString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }


    public static class JSONParser<T> {
        final Class<? extends T> clazz;
        final Gson gson;

        public JSONParser(Class<? extends T> clazz) {
            this.clazz = clazz;
            gson = new Gson();
        }

        public T parse(String path) throws FileNotFoundException {
            return gson.fromJson(new FileReader(path), clazz);
        }

    }
}
