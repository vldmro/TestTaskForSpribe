package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {

    public static <T> T deserializeJsonFromFile(String filePath, TypeReference<T> typeReference) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.readValue(new FileReader(filePath), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
