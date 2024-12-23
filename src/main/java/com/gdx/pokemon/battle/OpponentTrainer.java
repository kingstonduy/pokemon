package com.gdx.pokemon.battle;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.Json.JsonPokemon;
import com.gdx.Json.JsonPokemons;
import com.gdx.pokemon.PokemonGame;
import com.gdx.pokemon.model.Pokemon;

import java.util.HashMap;
import java.util.Random;

public class OpponentTrainer {
    private static volatile OpponentTrainer instance = null;

    private Trainer opponentTrainer;

    private PokemonGame app;

    private OpponentTrainer(PokemonGame app) {
        this.app = app;
        opponentTrainer = new Trainer();
        randomPokemon();
    }

    public static OpponentTrainer createInstance(PokemonGame app) {
        if (instance == null) {
            synchronized (OpponentTrainer.class) {
                if (instance == null) {
                    instance = new OpponentTrainer(app);
                }
            }
        }
        return instance;
    }

    public static OpponentTrainer getInstance() {
        return instance;
    }

    public void randomPokemon(){
        if (opponentTrainer != null) {
            opponentTrainer.clearTeam();
        }
        JsonPokemons pokemons = JsonPokemons.getInstance();
        opponentTrainer = new Trainer();
        HashMap<String, JsonPokemon> pokemonHashMap = pokemons.getPokemons();
        //random 3 pokemons from the list
        for (int i = 0; i < 1; i++) {
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
            Random random = new Random();
            int randomEV = random.nextInt(2);
            float EV;
            if(randomEV == 0){
                EV = 0.5f;
            } else {
                EV = 1f;
            }
            Defence = (int) (Defence * (1+ EV));
            Special_Defence = (int) (Special_Defence * (1+ EV));
            Special_Attack = (int) (Special_Attack * (1+ EV));
            HP = (int) (HP * (1+ EV));
            opponentTrainer.addPokemon(Pokemon.generatePokemon(name,level,exp,HP,Attack,Defence,Special_Attack,Special_Defence,Speed, texture, app.getMoveDatabase()));
        }
//        Texture bulbasaur = app.getAssetManager().get("res/graphics/pokemon/Bulbasaur.png", Texture.class);
//        Texture slowpoke = app.getAssetManager().get("res/graphics/pokemon/Slowpoke.png", Texture.class);
//        Random random = new Random();
//        int randomInt = random.nextInt(2);
//        if (randomInt == 0) {
//            Pokemon pokemon = Pokemon.generatePokemon("Bulbasaur", bulbasaur, app.getMoveDatabase());
//            opponentTrainer.addPokemon(pokemon);
//        } else if(randomInt == 1) {
//            Pokemon pokemon = Pokemon.generatePokemon("Slowpoke", slowpoke, app.getMoveDatabase());
//            opponentTrainer.addPokemon(pokemon);
//        }
//        } else if(randomInt == 2){
//            opponentTrainer = new Trainer(Pokemon.generatePokemon("Charmander", charmander, app.getMoveDatabase()));
//        }
    }

    public void addPokemon(Pokemon pokemon){
        opponentTrainer.addPokemon(pokemon);
    }

    public void addPokemon(String name){
        String path = "res/graphics/pokemon/" + name + ".png";
        Texture texture = app.getAssetManager().get(path, Texture.class);
        Pokemon pokemon = Pokemon.generatePokemon(name, texture, app.getMoveDatabase());
        opponentTrainer.addPokemon(pokemon);
    }

    public void addPokemon(String name, int level, int exp, int HP, int Attack, int Defence, int Special_Attack, int Special_Defence, int Speed){
        String path = "res/graphics/pokemon/" + name + ".png";
        Texture texture = app.getAssetManager().get(path, Texture.class);
        Pokemon pokemon = Pokemon.generatePokemon(name, level, exp, HP, Attack, Defence, Special_Attack, Special_Defence, Speed, texture, app.getMoveDatabase());
        opponentTrainer.addPokemon(pokemon);
    }

    public Trainer getPlayerTrainer() {
        return opponentTrainer;
    }

    public int sumAllExp(){
        int sum = 0;
        for (int i = 0; i < opponentTrainer.getTeamSize(); i++) {
            sum += opponentTrainer.getPokemon(i).getExp();
        }
        return sum;
    }

    public void clearTeam(){
        if (opponentTrainer != null)
            opponentTrainer.clearTeam();
    }
}
