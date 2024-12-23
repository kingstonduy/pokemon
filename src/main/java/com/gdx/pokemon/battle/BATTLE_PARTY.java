package com.gdx.pokemon.battle;


public enum BATTLE_PARTY {
	PLAYER,
	OPPONENT,
	;
	
	public static BATTLE_PARTY getOpposite(BATTLE_PARTY party) {
        return switch (party) {
            case PLAYER -> OPPONENT;
            case OPPONENT -> PLAYER;
        };
	}
}
