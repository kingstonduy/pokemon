package com.gdx.pokemon.battle;

public class BattleOnline {
    private static volatile BattleOnline instance = null;
    private String opponentAddress;
    private int damage;
    private int input = -1;

    private BattleOnline() {
    }

    public static BattleOnline getInstance() {
        if (instance == null) {
            synchronized (BattleOnline.class) {
                if (instance == null) {
                    instance = new BattleOnline();
                }
            }
        }
        return instance;
    }

    public void setOpponentAddress(String address) {
        this.opponentAddress = address;
    }

    public String getOpponentAddress() {
        return this.opponentAddress;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getInput() {
        return this.input;
    }


}
