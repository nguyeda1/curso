/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.ejb.util;

import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author adakl
 */
public class JsonObjectManipulator {

    public static JsonObject removeProperty(JsonObject origin, String key) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : origin.entrySet()) {
            if (entry.getKey().equals(key)) {
                continue;
            } else {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public static JsonObject addProperty(JsonObject source, String key, JsonValue value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(key, value);
        for (Map.Entry<String, JsonValue> e : source.entrySet()) {
            builder.add(e.getKey(), e.getValue());
        }
        return builder.build();
    }

    public static JsonObject changeValue(JsonObject source, String key, String value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> e : source.entrySet()) {
            if (e.getKey().equals(key)) {
                builder.add(e.getKey(), value);
            }else{
                builder.add(e.getKey(), e.getValue());
            }
        }
        return builder.build();
    }

    public static JsonObject wrapIn(JsonObject source, String key) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(key, source);
        return builder.build();
    }
}
