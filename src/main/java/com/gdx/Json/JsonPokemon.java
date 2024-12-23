package com.gdx.Json;

public class JsonPokemon {
    private String name;
    private int Hp;
    private int Atk;
    private int Def;
    private int SpAtk;
    private int SpDef;
    private int Speed;

    public JsonPokemon(String name, int Hp, int Atk, int Def, int SpAtk, int SpDef, int Speed) {
        this.name = name;
        this.Hp = Hp;
        this.Atk = Atk;
        this.Def = Def;
        this.SpAtk = SpAtk;
        this.SpDef = SpDef;
        this.Speed = Speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return Hp;
    }

    public void setHp(int hp) {
        Hp = hp;
    }

    public int getAtk() {
        return Atk;
    }

    public void setAtk(int atk) {
        Atk = atk;
    }

    public int getDef() {
        return Def;
    }

    public void setDef(int def) {
        Def = def;
    }

    public int getSpAtk() {
        return SpAtk;
    }

    public void setSpAtk(int spAtk) {
        SpAtk = spAtk;
    }

    public int getSpDef() {
        return SpDef;
    }

    public void setSpDef(int spDef) {
        SpDef = spDef;
    }

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }
}
