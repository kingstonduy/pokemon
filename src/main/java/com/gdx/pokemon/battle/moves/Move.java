package com.gdx.pokemon.battle.moves;

import com.gdx.pokemon.battle.BATTLE_PARTY;
import com.gdx.pokemon.battle.BattleMechanics;
import com.gdx.pokemon.battle.BattleOnline;
import com.gdx.pokemon.battle.GameState;
import com.gdx.pokemon.battle.animation.BattleAnimation;
import com.gdx.pokemon.battle.event.BattleEventQueuer;
import com.gdx.pokemon.model.Pokemon;
import com.gdx.pokemon.udp.UDP_client;

/**
 * Represents a move a pokemon can do in battle. 
 * 
 * Do not make new instances of these! 
 * Instead, use {@link #clone()}.
 *
 */
public abstract class Move {
	
	protected MoveSpecification spec;
	protected Class<? extends BattleAnimation> animationClass;
	
	public Move(MoveSpecification spec, Class<? extends BattleAnimation> animationClass) {
		this.spec = spec;
		this.animationClass = animationClass;
	}
	
	public int useMove(BattleMechanics mechanics, Pokemon user, Pokemon target, BATTLE_PARTY party, BattleEventQueuer broadcaster, BATTLE_PARTY userParty, GameState gameState) {
		int damage = mechanics.calculateDamage(this, user, target);
		if(gameState == GameState.OFFLINE){
			target.applyDamage(damage);
		} else {
			if (userParty == BATTLE_PARTY.PLAYER) {
				target.applyDamage(damage);
				BattleOnline battleOnline = BattleOnline.getInstance();
				String address = battleOnline.getOpponentAddress();
				UDP_client client = UDP_client.getInstance();
				String message = "damage " + damage + " " + address + " ";
				client.sendMessage(message);
			} else {
				BattleOnline battleOnline = BattleOnline.getInstance();
				target.applyDamage(battleOnline.getDamage());
			}
		}
		return damage;
	}
	
	public abstract BattleAnimation animation();
	
	public abstract String message();
	
	/**
	 * @return If this move deals damage
	 */
	public abstract boolean isDamaging();
	
	public String getName() {
		return spec.getName();
	}
	
	public MOVE_TYPE getType(){
		return spec.getType();
	}
	
	public MOVE_CATEGORY getCategory() {
		return spec.getCategory();
	}
	
	public int getPower() {
		return spec.getPower();
	}
	
	public float getAccuracy() {
		return spec.getAccuracy();
	}
	
	public MoveSpecification getMoveSpecification() {
		return spec;
	}
	
	/**
	 * @return A copy of this instance.
	 */
	public abstract Move clone();
}
