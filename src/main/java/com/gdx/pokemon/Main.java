package com.gdx.pokemon;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.Json.readJson;

public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.height = 800;
		config.width = 800;
		config.vSyncEnabled = false;
		config.foregroundFPS = 200;
		config.addIcon("res/graphics/pokeball_icon.png", Files.FileType.Local);
		readJson json = new readJson();
		new LwjglApplication(new PokemonGame(), config);
	}

}
