package com.gdx.pokemon.battle;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.Json.JsonPokemon;
import com.gdx.Json.JsonPokemons;
import com.gdx.pokemon.PokemonGame;
import com.gdx.pokemon.model.Pokemon;

import java.util.HashMap;

public class PlayerTrainer {
    private static volatile PlayerTrainer instance = null;

    private Trainer playerTrainer;

    private PlayerTrainer(PokemonGame app) {
//        Texture bulbasaur = app.getAssetManager().get("res/graphics/pokemon/Arbok.png", Texture.class);
//        Texture slowpoke = app.getAssetManager().get("res/graphics/pokemon/Slowpoke.png", Texture.class);
//        playerTrainer = new Trainer(Pokemon.generatePokemon("Bulbasaur", bulbasaur, app.getMoveDatabase()));
//        playerTrainer.addPokemon(Pokemon.generatePokemon("Slowpoke", slowpoke, app.getMoveDatabase()));
        JsonPokemons pokemons = JsonPokemons.getInstance();
        playerTrainer = new Trainer();
        HashMap<String, JsonPokemon> pokemonHashMap = pokemons.getPokemons();
        //random 3 pokemons from the list
        for (int i = 0; i < 3; i++) {
            int randomInt = (int) (Math.random() * pokemonHashMap.size());
            String randomPokemonName = (String) pokemonHashMap.keySet().toArray()[randomInt];
            JsonPokemon randomPokemon = pokemonHashMap.get(randomPokemonName);
            Texture texture = app.getAssetManager().get("res/graphics/pokemon/" + randomPokemonName + ".png", Texture.class);
            String name = randomPokemon.getName();
            int level = 5;
            int exp = 50;
            int HP = randomPokemon.getHp();
            int Attack = randomPokemon.getAtk();
            int Defence = randomPokemon.getDef();
            int Special_Attack = randomPokemon.getSpAtk();
            int Special_Defence = randomPokemon.getSpDef();
            int Speed = randomPokemon.getSpeed();

            playerTrainer.addPokemon(Pokemon.generatePokemon(name,level,exp,HP,Attack,Defence,Special_Attack,Special_Defence,Speed, texture, app.getMoveDatabase()));
        }
    }

    public static PlayerTrainer createInstance(PokemonGame app) {
        if (instance == null) {
            synchronized (PlayerTrainer.class) {
                if (instance == null) {
                    instance = new PlayerTrainer(app);
                }
            }
        }
        return instance;
    }

    public static PlayerTrainer getInstance() {
        return instance;
    }


    public Trainer getPlayerTrainer() {
        return playerTrainer;
    }

    public void increasePokemonExp(int exp) {
        for (int i = 0; i < playerTrainer.getTeamSize(); i++) {
            playerTrainer.getPokemon(i).increaseExp(exp);
        }
    }

    public void addPokemon(Pokemon pokemon) {
        playerTrainer.addPokemon(pokemon);
    }

}
