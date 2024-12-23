package com.gdx.pokemon.model;

import com.badlogic.gdx.graphics.Texture;
import com.gdx.pokemon.battle.STAT;
import com.gdx.pokemon.battle.moves.Move;
import com.gdx.pokemon.battle.moves.MoveDatabase;
import com.gdx.pokemon.battle.moves.MoveSpecification;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Pokemon. This representation is used globally.
 * 
 *
 */
public class Pokemon {
	
	private String name;
	
	private int level;
	private int exp;
	
	private Map<STAT, Integer> stats;
	private int currentHitpoints;
	
	/*
	 * This array holds prototypes. Use #clone to get an instance.
	 */
	private Move[] moves = new Move[4];
	
	private Texture image;
	
	public Pokemon(String name, Texture image) {
		this.name = name;
		this.image = image;
		this.level = 5;
		this.exp = 50;
		
		/* init all stats to 1 */
		stats = new HashMap<STAT, Integer>();
		for (STAT stat : STAT.values()) {
			stats.put(stat, 15);
		}
		stats.put(STAT.HITPOINTS, 10);
		currentHitpoints = stats.get(STAT.HITPOINTS);
	}

	public Pokemon(String name, int level, int exp, int HP, int Attack, int Defence, int Special_Attack, int Special_Defence, int Speed, Texture image){
		this.name = name;
		this.level = level;
		this.exp = exp;
		stats = new HashMap<>();
		stats.put(STAT.HITPOINTS, HP);
		stats.put(STAT.ATTACK, Attack);
		stats.put(STAT.DEFENCE, Defence);
		stats.put(STAT.SPECIAL_ATTACK, Special_Attack);
		stats.put(STAT.SPECIAL_DEFENCE, Special_Defence);
		stats.put(STAT.SPEED, Speed);
		currentHitpoints = stats.get(STAT.HITPOINTS);
		this.image = image;
	}
	
	public Texture getSprite() {
		return image;
	}

	public int getCurrentHitpoints() {
		return currentHitpoints;
	}

	public void setCurrentHitpoints(int currentHitpoints) {
		this.currentHitpoints = currentHitpoints;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @param index		Position of the move in the move-array. 0 - 3 allowed.
	 * @param move		Move to put in
	 */
	public void setMove(int index, Move move) {
		moves[index] = move;
	}
	
	/**
	 * NOTE: This should deplete PP.
	 * @param index		Move to use, 0 - 3 allowed
	 * @return			Clone (safe) of move at index
	 */
	public Move getMove(int index) {
		return moves[index].clone();
	}
	
	public MoveSpecification getMoveSpecification(int index) {
		return moves[index].getMoveSpecification();
	}
	
	public int getStat(STAT stat) {
		return stats.get(stat);
	}
	
	public void setStat(STAT stat, int value) {
		stats.put(stat, value);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void applyDamage(int amount) {
		currentHitpoints -= amount;
		if (currentHitpoints < 0) {
			currentHitpoints = 0;
		}
	}
	
	
	public boolean isFainted() {
		return currentHitpoints == 0;
	}
	
	public static Pokemon generatePokemon(String name, Texture sprite, MoveDatabase moveDatabase) {
		Pokemon generated = new Pokemon(name, sprite);
		generated.setMove(0, moveDatabase.getMove(0));
		generated.setMove(1, moveDatabase.getMove(1));
		generated.setMove(2, moveDatabase.getMove(2));
		generated.setMove(3, moveDatabase.getMove(3));

		return generated;
	}

	public static Pokemon generatePokemon(String name, int level, int exp, int HP, int Attack, int Defence, int Special_Attack, int Special_Defence, int Speed,
										  Texture sprite, MoveDatabase moveDatabase){
		Pokemon generated = new Pokemon(name, level, exp, HP, Attack, Defence, Special_Attack, Special_Defence, Speed, sprite);
		generated.setMove(0, moveDatabase.getMove(0));
		generated.setMove(1, moveDatabase.getMove(1));
		generated.setMove(2, moveDatabase.getMove(2));
		generated.setMove(3, moveDatabase.getMove(3));

		return generated;
	}

	public void increaseExp(int iExp){
		exp += iExp;
		if (exp >= 100){
			level++;
			exp = 0;
		}
	}

	public int getExp(){
		return exp;
	}
}
