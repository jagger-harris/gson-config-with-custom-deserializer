package jagger_harris.config.test;

import com.google.gson.*;
import jagger_harris.config.Config;
import jagger_harris.config.ConfigDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class ConfigDeserializerTest {
    private JsonObject createValidJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("myConfigOption", true);

        return json;
    }

    private JsonObject createInvalidJsonObjectNonExistingField() {
        JsonObject json = new JsonObject();
        json.addProperty("nonExistingField", "true");

        return json;
    }

    @Test
    public void deserializeValidJsonReturnsConfigObject() {
        JsonElement json = createValidJsonObject();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Config.class, new ConfigDeserializer())
                .create();

        Config config = gson.fromJson(json, Config.class);

        Assert.assertNotNull(config);
    }

    @Test
    public void deserializeInvalidJsonThrowsJsonParseException() {
        JsonElement json = createInvalidJsonObjectNonExistingField();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Config.class, new ConfigDeserializer())
                .create();

        try {
            gson.fromJson(json, Config.class);
            Assert.fail("Expected JsonParseException, but no exception was thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof com.google.gson.JsonParseException);
        }
    }
}
