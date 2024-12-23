package com.gdx.pokemon.model.actor;

import com.gdx.pokemon.model.world.World;
import com.gdx.pokemon.model.world.cutscene.CutscenePlayer;
import com.gdx.pokemon.util.AnimationSet;

public class PlayerActor extends Actor {
	
	private CutscenePlayer cutscenePlayer;

	public PlayerActor(World world, int x, int y, AnimationSet animations, CutscenePlayer cutscenePlayer, String Address) {
		super(world, x, y, animations, Address);
		this.cutscenePlayer = cutscenePlayer;
	}
	
	public CutscenePlayer getCutscenePlayer() {
		return cutscenePlayer;
	}
}
