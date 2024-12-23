package com.gdx.pokemon.screen;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.pokemon.PokemonGame;
import com.gdx.pokemon.Settings;
import com.gdx.pokemon.battle.*;
import com.gdx.pokemon.battle.Battle.STATE;
import com.gdx.pokemon.battle.animation.BattleAnimation;
import com.gdx.pokemon.battle.event.BattleEvent;
import com.gdx.pokemon.battle.event.BattleEventPlayer;
import com.gdx.pokemon.controller.BattleScreenController;
import com.gdx.pokemon.model.Pokemon;
import com.gdx.pokemon.screen.renderer.BattleDebugRenderer;
import com.gdx.pokemon.screen.renderer.BattleRenderer;
import com.gdx.pokemon.screen.renderer.EventQueueRenderer;
import com.gdx.pokemon.ui.*;

import java.util.ArrayDeque;
import java.util.Queue;

public class BattleScreen extends AbstractScreen implements BattleEventPlayer {
	
	/* Controller */
	private BattleScreenController controller;
	
	/* Event system */
	private BattleEvent currentEvent;
	private Queue<BattleEvent> queue = new ArrayDeque<BattleEvent>();
	
	/* Model */
	private Battle battle;
	
	private BATTLE_PARTY animationPrimary;
	private BattleAnimation battleAnimation = null;
	
	/* View */
	private BitmapFont text = new BitmapFont();
	
	private Viewport gameViewport;
	
	private SpriteBatch batch;
	private BattleRenderer battleRenderer;
	private BattleDebugRenderer battleDebugRenderer;
	private EventQueueRenderer eventRenderer;
	
	/* UI */
	private Stage uiStage;
	private Table dialogueRoot;
	private DialogueBox dialogueBox;
	private OptionBox optionBox;
	
	private Table moveSelectRoot;
	private MoveSelectBox moveSelectBox; 
	
	private Table statusBoxRoot;
	private DetailedStatusBox playerStatus;
	private StatusBox opponentStatus;
	
	/* DEBUG */
	private boolean uiDebug = false;
	private boolean battleDebug = true;

	private GameState gameState;

	public BattleScreen(PokemonGame app, Trainer playerTrainer, Trainer opponentTrainer, GameState gameState) {
		super(app);
		this.gameState = gameState;
		gameViewport = new ScreenViewport();
		batch = new SpriteBatch();

		battle = new Battle(
				playerTrainer,
				opponentTrainer, gameState);
		battle.setEventPlayer(this);
		animationPrimary = BATTLE_PARTY.PLAYER;
		
		battleRenderer = new BattleRenderer(app.getAssetManager(), app.getOverlayShader());
		battleDebugRenderer = new BattleDebugRenderer(battleRenderer);
		eventRenderer = new EventQueueRenderer(app.getSkin(), queue);
		
		initUI();
		
		controller = new BattleScreenController(battle, queue, dialogueBox, moveSelectBox, optionBox, gameState);

		battle.beginBattle();

	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}
	
	@Override
	public void update(float delta) {
		/* DEBUG */
		if (Gdx.input.isKeyJustPressed(Keys.F9)) {
			uiDebug = !uiDebug;
			uiStage.setDebugAll(uiDebug);
		}
		if (Gdx.input.isKeyJustPressed(Keys.F10)) {
			battleDebug = !battleDebug;
		}
		
		while (currentEvent == null || currentEvent.finished()) { // no active event
			if (queue.peek() == null) { // no event queued up
				currentEvent = null;
				
				if (battle.getState() == STATE.SELECT_NEW_POKEMON) {
					if (controller.getState() != BattleScreenController.STATE.USE_NEXT_POKEMON) {
						controller.displayNextDialogue();
					}
				} if (battle.getState() == STATE.SELECT_NEW_POKEMON_OPPONENT) {
					for (int i = 0; i < battle.getOpponentTrainer().getTeamSize(); i++) {
						if (!battle.getOpponentTrainer().getPokemon(i).isFainted()) {
							battle.chooseNewPokemonForOpponent(battle.getOpponentTrainer().getPokemon(i));
							break;
						}
					}

				} else if (battle.getState() == STATE.READY_TO_PROGRESS) {
					controller.restartTurn();
				} else if (battle.getState() == STATE.WIN) {
					getApp().setScreen(getApp().getGameScreen());
					if (gameState == GameState.OFFLINE){
						Pokemon pokemon = battle.getOpponentPokemon();
						PlayerTrainer.getInstance().addPokemon(pokemon);
					} else {
						int sumExpLose = OpponentTrainer.getInstance().sumAllExp();
						sumExpLose/= 3;
						PlayerTrainer.getInstance().increasePokemonExp(sumExpLose);
					}
				} else if (battle.getState() == STATE.LOSE) {
					getApp().setScreen(getApp().getGameScreen());
				} else if (battle.getState() == STATE.RAN) {
					getApp().setScreen(getApp().getGameScreen());
				}
				break;
			} else {					// event queued up
				currentEvent = queue.poll();
				currentEvent.begin(this);
			}
		}
		
		if (currentEvent != null) {
			currentEvent.update(delta);
		}
		
		controller.update(delta);
		uiStage.act(); // update ui
	}

	@Override
	public void render(float delta) {
		gameViewport.apply();
		batch.begin();
		battleRenderer.render(batch, battleAnimation, animationPrimary);
		if (currentEvent != null && battleDebug) {
			eventRenderer.render(batch, currentEvent);
		}
		batch.end();
		
		if (battleDebug) {
			battleDebugRenderer.render();
		}
		
		uiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		uiStage.getViewport().update(
				(int)(Gdx.graphics.getWidth()/Settings.SCALE_UI), 
				(int)(Gdx.graphics.getHeight()/Settings.SCALE_UI),
				true);
		gameViewport.update(width, height);
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controller);
	}
	
	private void initUI() {
		/* ROOT UI STAGE */
		uiStage = new Stage(new ScreenViewport());
		uiStage.getViewport().update(
				(int)(Gdx.graphics.getWidth()/Settings.SCALE_UI), 
				(int)(Gdx.graphics.getHeight()/Settings.SCALE_UI),
				true);
		uiStage.setDebugAll(false);
		
		/* STATUS BOXES */
		statusBoxRoot = new Table();
		statusBoxRoot.setFillParent(true);
		uiStage.addActor(statusBoxRoot);
		
		playerStatus = new DetailedStatusBox(getApp().getSkin());
		playerStatus.setText(battle.getPlayerPokemon().getName());
		
		opponentStatus = new StatusBox(getApp().getSkin());
		opponentStatus.setText(battle.getOpponentPokemon().getName());
		
		statusBoxRoot.add(playerStatus).expand().align(Align.left);
		statusBoxRoot.add(opponentStatus).expand().align(Align.right);
		
		/* MOVE SELECTION BOX */
		moveSelectRoot = new Table();
		moveSelectRoot.setFillParent(true);
		uiStage.addActor(moveSelectRoot);
		
		moveSelectBox = new MoveSelectBox(getApp().getSkin());
		moveSelectBox.setVisible(false);
		
		moveSelectRoot.add(moveSelectBox).expand().align(Align.bottom);
		
		/* OPTION BOX */
		dialogueRoot = new Table();
		dialogueRoot.setFillParent(true);
		uiStage.addActor(dialogueRoot);
		
		optionBox = new OptionBox(getApp().getSkin());
		optionBox.setVisible(false);
		
		/* DIALOGUE BOX */
		dialogueBox = new DialogueBox(getApp().getSkin());
		dialogueBox.setVisible(false);
		
		Table dialogTable = new Table();
		dialogTable.add(optionBox).expand().align(Align.right).space(8f).row();
		dialogTable.add(dialogueBox).expand().align(Align.bottom).space(8f);
		
		dialogueRoot.add(dialogTable).expand().align(Align.bottom);
	}
	
	public StatusBox getStatus(BATTLE_PARTY hpbar) {
		if (hpbar == BATTLE_PARTY.PLAYER) {
			return playerStatus;
		} else if (hpbar == BATTLE_PARTY.OPPONENT) {
			return opponentStatus;
		} else {
			return null;
		}
	}

	@Override
	public void queueEvent(BattleEvent event) {
		queue.add(event);
	}

	@Override
	public DialogueBox getDialogueBox() {
		return dialogueBox;
	}
	
	@Override
	public BattleAnimation getBattleAnimation() {
		return battleAnimation;
	}
	
	@Override
	public TweenManager getTweenManager() {
		return getApp().getTweenManager();
	}

	@Override
	public void playBattleAnimation(BattleAnimation animation, BATTLE_PARTY party) {
		this.animationPrimary = party;
		this.battleAnimation = animation;
		animation.initialize(getApp().getAssetManager(), getApp().getTweenManager());
	}

	@Override
	public StatusBox getStatusBox(BATTLE_PARTY party) {
		if (party == BATTLE_PARTY.PLAYER) {
			return playerStatus;
		} else if (party == BATTLE_PARTY.OPPONENT) {
			return opponentStatus;
		} else {
			return null;
		}
	}

	@Override
	public void setPokemonSprite(Texture region, BATTLE_PARTY party) {
		battleRenderer.setPokemonSprite(region, party);
	}
}
