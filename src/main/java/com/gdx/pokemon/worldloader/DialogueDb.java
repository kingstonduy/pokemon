package com.gdx.pokemon.worldloader;

import com.gdx.pokemon.dialogue.Dialogue;

import java.util.HashMap;

public class DialogueDb {
	
	private HashMap<String, Dialogue> knownDialogue = new HashMap<String, Dialogue>();
	
	protected void addTerrain(String name, Dialogue dialogue) {
		knownDialogue.put(name, dialogue);
	}
	
	public Dialogue getDialogue(String name) {
		if (!knownDialogue.containsKey(name)) {
			throw new NullPointerException("Could not find Dialogue of name "+name);
		}
		return knownDialogue.get(name);
	}

}