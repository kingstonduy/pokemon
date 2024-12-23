package com.gdx.pokemon.controller;

import com.badlogic.gdx.Input.Keys;
import com.gdx.pokemon.PokemonGame;
import com.gdx.pokemon.model.DIRECTION;
import com.gdx.pokemon.model.actor.Actor;
import com.gdx.pokemon.model.actor.Actor.MOVEMENT_MODE;
import com.gdx.pokemon.screen.BattleScreen;

import java.util.Random;

public class TempController   {

    private Actor player;
    private PokemonGame game;

    private boolean[] directionPress;		// which arrow-keys are pressed
    private float[] directionPressTimer;	// how long have they been pressed for

    private boolean isRunning = false;

    private float WALK_REFACE_THRESHOLD = 0.07f;

    private int currentKeyUpCode= -1;
    private int currentKeyDownCode= -1;

    public TempController(Actor p , PokemonGame game) {
        this.player = p;
        this.game = game;
        directionPress = new boolean[DIRECTION.values().length];
        directionPress[DIRECTION.NORTH.ordinal()] = false;
        directionPress[DIRECTION.SOUTH.ordinal()] = false;
        directionPress[DIRECTION.EAST.ordinal()] = false;
        directionPress[DIRECTION.WEST.ordinal()] = false;
        directionPressTimer = new float[DIRECTION.values().length];
        directionPressTimer[DIRECTION.NORTH.ordinal()] = 0f;
        directionPressTimer[DIRECTION.SOUTH.ordinal()] = 0f;
        directionPressTimer[DIRECTION.EAST.ordinal()] = 0f;
        directionPressTimer[DIRECTION.WEST.ordinal()] = 0f;
    }

    public boolean keyDown(int keycode) {
        //System.out.println("Key down: "+keycode);
        // enable running if appropiate
        currentKeyDownCode = keycode;
        currentKeyUpCode = -1;
        if (keycode == Keys.SHIFT_LEFT && player.getMovementMode() == MOVEMENT_MODE.WALKING) {
            player.setNextMode(MOVEMENT_MODE.RUNNING);
        }

        // update arrow key pressing
        if (keycode == Keys.UP) {
            directionPress[DIRECTION.NORTH.ordinal()] = true;
        }
        if (keycode == Keys.DOWN) {
            directionPress[DIRECTION.SOUTH.ordinal()] = true;
        }
        if (keycode == Keys.LEFT) {
            directionPress[DIRECTION.WEST.ordinal()] = true;
        }
        if (keycode == Keys.RIGHT) {
            directionPress[DIRECTION.EAST.ordinal()] = true;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        // disable running if appropiate
        //System.out.println("Key up: " + keycode);
        currentKeyUpCode = keycode;
        currentKeyDownCode = -1;
        if (keycode == Keys.SHIFT_LEFT && player.getMovementMode() == MOVEMENT_MODE.RUNNING) {
            player.setNextMode(MOVEMENT_MODE.WALKING);
        }

        // update biking/not biking
        if (keycode == Keys.F1) {
            if (player.getMovementMode() == MOVEMENT_MODE.WALKING) {
                player.setNextMode(MOVEMENT_MODE.BIKING);
            } else if (player.getMovementMode() == MOVEMENT_MODE.BIKING) {
                player.setNextMode(MOVEMENT_MODE.WALKING);
            }
        }

        // update arrow key pressing// update
        if (keycode == Keys.UP) {
            releaseDirection(DIRECTION.NORTH);
        }
        if (keycode == Keys.DOWN) {
            releaseDirection(DIRECTION.SOUTH);
        }
        if (keycode == Keys.LEFT) {
            releaseDirection(DIRECTION.WEST);
        }
        if (keycode == Keys.RIGHT) {
            releaseDirection(DIRECTION.EAST);
        }
        return false;
    }

    public void update(float delta) {
        if (directionPress[DIRECTION.NORTH.ordinal()]) {
            updateDirection(DIRECTION.NORTH, delta);
            return;
        }
        if (directionPress[DIRECTION.SOUTH.ordinal()]) {
            updateDirection(DIRECTION.SOUTH, delta);
            return;
        }
        if (directionPress[DIRECTION.WEST.ordinal()]) {
            updateDirection(DIRECTION.WEST, delta);
            return;
        }
        if (directionPress[DIRECTION.EAST.ordinal()]) {
            updateDirection(DIRECTION.EAST, delta);
            return;
        }
    }

    /**
     * Runs every frame, for each direction whose key is pressed.
     */
    private int count = 0;
    private void updateDirection(DIRECTION dir, float delta) {
        directionPressTimer[dir.ordinal()] += delta;
        considerMove(dir);
        int xx = player.getX();
        int yy = player.getY();
        int min = 1;
        int max = 20000;

        if (player.getWorld().getVis(xx , yy) == 1) {
            Random random = new Random();
            int randomNumber = random.nextInt(max - min + 1) + min;
            if (randomNumber <= 10) {
                System.out.println("You have encountered a wild Pokemon!");
                //player.getWorld().getGame().setScreen(player.getWorld().getGame().getBattleScreen());
                //PokemonGame.create().setScreen();
                BattleScreen newBattleScreen = game.createOfflineBattleScreen();
                game.setScreen(newBattleScreen);
                directionPress[DIRECTION.NORTH.ordinal()] = false;
                directionPress[DIRECTION.SOUTH.ordinal()] = false;
                directionPress[DIRECTION.EAST.ordinal()] = false;
                directionPress[DIRECTION.WEST.ordinal()] = false;

            }
            else {
                game.setScreen(game.getGameScreen());
            }
        }
    }

    /**
     * Runs when a key is released, argument is its corresponding direction.
     */
    private void releaseDirection(DIRECTION dir) {
        directionPress[dir.ordinal()] = false;
        considerReface(dir);
        directionPressTimer[dir.ordinal()] = 0f;
    }

    private void considerMove(DIRECTION dir) {
        if (directionPressTimer[dir.ordinal()] > WALK_REFACE_THRESHOLD) {
            player.move(dir);
        }
    }

    private void considerReface(DIRECTION dir) {
        if (directionPressTimer[dir.ordinal()] < WALK_REFACE_THRESHOLD) {
            player.reface(dir);
        }
    }

    public int getCurrentKeyUpCode() {
        return currentKeyUpCode;
    }

    public int getCurrentKeyDownCode() {
        return currentKeyDownCode;
    }
}
