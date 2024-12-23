package com.gdx.pokemon.battle;

import com.gdx.pokemon.battle.event.BattleEvent;

/**
 * Objects can implement this interface and subscribe to a {@link Battle}.
 * 
 *
 */
public interface BattleObserver {
	
	/**
	 * {@link BattleEvent} spat out from a {@link Battle}.
	 * @param event	Catch it fast, and get free visuals for a live fight.
	 */
	public void queueEvent(BattleEvent event);
}
