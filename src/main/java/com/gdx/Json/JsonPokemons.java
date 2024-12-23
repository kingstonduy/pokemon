package com.gdx.Json;

import java.util.HashMap;

public class JsonPokemons {
    private static volatile JsonPokemons instance = null;

    private HashMap<String, JsonPokemon> pokemons = new HashMap<>();

    private JsonPokemons() {
    }

    public static JsonPokemons getInstance() {
        if (instance == null) {
            synchronized (JsonPokemons.class) {
                if (instance == null) {
                    instance = new JsonPokemons();
                }
            }
        }
        return instance;
    }

    public void addPokemon(JsonPokemon pokemon) {
        pokemons.put(pokemon.getName(), pokemon);
    }

    public JsonPokemon getPokemon(String name) {
        return pokemons.get(name);
    }

    public HashMap<String, JsonPokemon> getPokemons() {
        return pokemons;
    }
}
