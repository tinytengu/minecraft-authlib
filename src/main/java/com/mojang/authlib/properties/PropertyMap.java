package com.mojang.authlib.properties;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

public class PropertyMap extends ForwardingMultimap<String, Property> {

    private final Multimap<String, Property> properties = LinkedHashMultimap.create();

    protected Multimap<String, Property> delegate() {
        return this.properties;
    }

    public static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

        public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            PropertyMap result = new PropertyMap();
            if (json instanceof JsonObject) {
                JsonObject object = (JsonObject) json;
                for (Iterator<Map.Entry<String, JsonElement>> localIterator1 = object.entrySet().iterator(); localIterator1.hasNext();) {
                    Map.Entry<String, JsonElement> entry = localIterator1.next();
                    if (entry.getValue() instanceof JsonArray) {
                        for (JsonElement element : (JsonArray) entry.getValue()) {
                            result.put(entry.getKey(), new Property(entry.getKey(), element.getAsString()));
                        }
                    }
                }
            } else if (json instanceof JsonArray) {
                for (JsonElement element : (JsonArray) json) {
                    if (element instanceof JsonObject) {
                        JsonObject object = (JsonObject) element;
                        String name = object.getAsJsonPrimitive("name").getAsString();
                        String value = object.getAsJsonPrimitive("value").getAsString();
                        if (object.has("signature")) {
                            result.put(name, new Property(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                            continue;
                        }
                        result.put(name, new Property(name, value));
                    }
                }
            }
            return result;
        }

        public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray result = new JsonArray();
            for (Property property : src.values()) {
                JsonObject object = new JsonObject();
                object.addProperty("name", property.getName());
                object.addProperty("value", property.getValue());
                if (property.hasSignature()) {
                    object.addProperty("signature", property.getSignature());
                }
                result.add((JsonElement) object);
            }
            return (JsonElement) result;
        }
    }
}
