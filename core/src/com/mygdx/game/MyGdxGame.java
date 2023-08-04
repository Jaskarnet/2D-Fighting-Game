package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Command;
import com.mygdx.commands.InputHandler;
import com.mygdx.commands.Player;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class MyGdxGame extends ApplicationAdapter {
	private Collection<Entity> entities;
	private Fighter player1, player2;
	private Texture p1tex, p2tex;
	private SpriteBatch batch;
	InputHandler p1InputHandler;
	InputHandler p2InputHandler;

	@Override
	public void create () {
		p1tex = new Texture("FighterP1.png");
		p2tex = new Texture("FighterP2.png");
		player1 = new Fighter(200, 20, p1tex);
		player2 = new Fighter(600, 20, p2tex);
		entities = new ArrayList<>();
		entities.add(player1);
		entities.add(player2);
		batch = new SpriteBatch();
		p1InputHandler = new InputHandler(player1, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.R, 600);
		p2InputHandler = new InputHandler(player2, Player.PLAYER2, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.SHIFT_RIGHT, 600);

	}

	@Override
	public void render () {
		//clearing
		ScreenUtils.clear(1, (float) 0.54, 0, 1);

		//logic
		Command command = p1InputHandler.handleInput();
		command.execute();
		command = p2InputHandler.handleInput();
		command.execute();

		for (Entity entity : entities) {
			entity.update();
		}

		//rendering
		batch.begin();
		for(Entity entity : entities) {
			batch.draw(entity.getTexture(), entity.getX(), entity.getY());
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		p1tex.dispose();
		p2tex.dispose();
		batch.dispose();
	}
}
