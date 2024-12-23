package com.gdx.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.gdx.pokemon.dialogue.Dialogue;
import com.gdx.pokemon.model.DIRECTION;
import com.gdx.pokemon.model.Tile;
import com.gdx.pokemon.model.actor.Actor;
import com.gdx.pokemon.udp.UDP_client;
import org.lwjgl.Sys;

/**
 * Controller that interacts with what is in front of the player Actor.
 * 
 *
 */
public class InteractionController extends InputAdapter {
	
	private Actor a;
	private DialogueController dialogueController;
	
	public InteractionController(Actor a, DialogueController dialogueController) {
		this.a = a;
		this.dialogueController = dialogueController;
	}
	
	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.Y) {
			Tile target = a.getWorld().getMap().getTile(a.getX()+a.getFacing().getDX(), a.getY()+a.getFacing().getDY());
			if (target.getActor() != null) {
				Actor targetActor = target.getActor();
				if (targetActor.getDialogue() != null) {
					if (targetActor.refaceWithoutAnimation(DIRECTION.getOpposite(a.getFacing()))){
						UDP_client udp = UDP_client.getInstance();
						String message= "battle " + targetActor.getAddress() + " ";

						udp.sendMessage(message);
						Dialogue dialogue = targetActor.getDialogue();
						dialogueController.startDialogue(dialogue);
					}
				}
			}
			return false;
		}
		return false;
	}

}
