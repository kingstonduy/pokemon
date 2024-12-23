package com.gdx.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class readJson {
    public readJson () {
        try {
            String content = new String(Files.readAllBytes(Paths.get("./pokemon.json")));
            JSONArray jsonArray = new JSONArray(content);
            JsonPokemons pokemons = JsonPokemons.getInstance();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int nationalNumber = jsonObject.getInt("national_number");
                String englishName = jsonObject.getString("english_name");
                int hp = jsonObject.getInt("hp");
                int attack = jsonObject.getInt("attack");
                int defense = jsonObject.getInt("defense");
                int spAttack = jsonObject.getInt("sp_attack");
                int spDefense = jsonObject.getInt("sp_defense");
                int speed = jsonObject.getInt("speed");

                JsonPokemon pokemon = new JsonPokemon(englishName, hp, attack, defense, spAttack, spDefense, speed);
                pokemons.addPokemon(pokemon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}