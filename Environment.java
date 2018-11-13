package com.p22.beast;

import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class Environment {

    private final Map<String, Object> values = new HashMap<>();

    Object get(Token name) {

        if(values.containsKey(name.getValue().toString())) {
            return values.get(name.getValue().toString());
        }

        //throw new RuntimeErrorException(0);
        return null;

    }

    void assign(Token name, Object value) {

        if(values.containsKey(name.getValue().toString())) {
            values.put(name.getValue().toString(), value);
            return;
        }

    }

    void define(Token name, Object value) {

        values.put(name.getValue().toString(), value);

    }


}