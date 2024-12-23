package com.gdx.pokemon.model.world.cutscene;

import com.badlogic.gdx.graphics.Color;
import com.gdx.pokemon.model.DIRECTION;
import com.gdx.pokemon.model.world.World;

public interface CutscenePlayer {
	
	/**
	 * Smooth transition to another world.
	 * @param newWorld
	 * @param x
	 * @param y
	 * @param facing
	 * @param color
	 */
	public void changeLocation(World newWorld, int x, int y, DIRECTION facing, Color color);
	
	/**
	 * Get a loaded World from name
	 * @param worldName
	 * @return
	 */
	public World getWorld(String worldName);
	
	public void queueEvent(CutsceneEvent event);
}
