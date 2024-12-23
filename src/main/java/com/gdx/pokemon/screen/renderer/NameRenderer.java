package com.gdx.pokemon.screen.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.pokemon.model.Camera;
import com.gdx.pokemon.model.world.World;

public class NameRenderer {
    private BitmapFont name = new BitmapFont();

    private World world;
    private Camera cam;

    public NameRenderer(World world, Camera cam) {
        this.world = world;
        this.cam = cam;
    }

    public void render(SpriteBatch batch, float playerX, float playerY) {
       name.draw(batch, "Brendan", 375f, 380f);
    }


}
