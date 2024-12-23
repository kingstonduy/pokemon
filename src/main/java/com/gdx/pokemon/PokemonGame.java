package com.gdx.pokemon;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gdx.pokemon.battle.GameState;
import com.gdx.pokemon.battle.OpponentTrainer;
import com.gdx.pokemon.battle.PlayerTrainer;
import com.gdx.pokemon.battle.Trainer;
import com.gdx.pokemon.battle.animation.*;
import com.gdx.pokemon.battle.moves.MoveDatabase;
import com.gdx.pokemon.model.world.World;
import com.gdx.pokemon.screen.AbstractScreen;
import com.gdx.pokemon.screen.BattleScreen;
import com.gdx.pokemon.screen.GameScreen;
import com.gdx.pokemon.screen.TransitionScreen;
import com.gdx.pokemon.screen.transition.BattleBlinkTransition;
import com.gdx.pokemon.screen.transition.BattleBlinkTransitionAccessor;
import com.gdx.pokemon.screen.transition.Transition;
import com.gdx.pokemon.util.Action;
import com.gdx.pokemon.util.SkinGenerator;
import com.gdx.pokemon.worldloader.*;

import java.io.File;

/**
 * Topmost class of the game. Logic is delegated to Screens from here.
 * 
 *
 */
public class PokemonGame extends Game {
	
	private GameScreen gameScreen;
	private BattleScreen battleScreen;
	private TransitionScreen transitionScreen;
	
	private MoveDatabase moveDatabase;
	
	private AssetManager assetManager;
	
	private Skin skin;
	
	private TweenManager tweenManager;
	
	private ShaderProgram overlayShader;
	private ShaderProgram transitionShader;
	
	private String version;


	@Override
	public void create() {
		/*
		 * LOAD VERSION
		 */
		version = Gdx.files.internal("version.txt").readString();
		System.out.println("Pokémon, version "+version);
		Gdx.app.getGraphics().setTitle("Pokémon, version "+version);
		
		/*
		 * LOADING SHADERS
		 */
		ShaderProgram.pedantic = false;
		overlayShader = new ShaderProgram(
				Gdx.files.internal("res/shaders/overlay/vertexshader.txt"), 
				Gdx.files.internal("res/shaders/overlay/fragmentshader.txt"));
		if (!overlayShader.isCompiled()) {
			System.out.println(overlayShader.getLog());
		}
		transitionShader = new ShaderProgram(
				Gdx.files.internal("res/shaders/transition/vertexshader.txt"), 
				Gdx.files.internal("res/shaders/transition/fragmentshader.txt"));
		if (!transitionShader.isCompiled()) {
			System.out.println(transitionShader.getLog());
		}
		
		/*
		 * SETTING UP TWEENING
		 */
		tweenManager = new TweenManager();
		Tween.registerAccessor(BattleAnimation.class, new BattleAnimationAccessor());
		Tween.registerAccessor(BattleSprite.class, new BattleSpriteAccessor());
		Tween.registerAccessor(AnimatedBattleSprite.class, new BattleSpriteAccessor());
		Tween.registerAccessor(BattleBlinkTransition.class, new BattleBlinkTransitionAccessor());
		
		/*
		 * LOADING ASSETS
		 */
		assetManager = new AssetManager();
		assetManager.setLoader(LWorldObjectDb.class, new LWorldObjectLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(LTerrainDb.class, new LTerrainLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(DialogueDb.class, new DialogueLoader(new InternalFileHandleResolver()));
		assetManager.setLoader(World.class, new WorldLoader(new InternalFileHandleResolver()));
		
		assetManager.load("res/LTerrain.xml", LTerrainDb.class);
		assetManager.load("res/LWorldObjects.xml", LWorldObjectDb.class);
		assetManager.load("res/Dialogues.xml", DialogueDb.class);
		
		assetManager.load("res/graphics_packed/tiles/tilepack.atlas", TextureAtlas.class);
		assetManager.load("res/graphics_packed/ui/uipack.atlas", TextureAtlas.class);
		assetManager.load("res/graphics_packed/battle/battlepack.atlas", TextureAtlas.class);

		assetManager.load("res/graphics/pokemon/Arbok.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Beedrill.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Blastoise.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Bulbasaur.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Butterfree.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Caterpie.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Charizard.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Charmander.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Charmeleon.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Clefable.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Clefairy.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Diglett.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Ekans.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Fearow.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Gloom.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Golbat.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Ivysaur.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Jigglypuff.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Kakuna.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Metapod.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Nidoking.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Nidoqueen.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Nidoran.png", Texture.class);
		assetManager.load("res/graphics/pokemon/NidoranF.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Nidorina.png", Texture.class);
//		assetManager.load("res/graphics/pokemon/Nidorino.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Ninetales.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Oddish.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Paras.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Parasect.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Pidgeot.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Pidgeotto.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Pidgey.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Pikachu.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Raichu.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Raticate.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Rattata.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Sandshrew.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Sandslash.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Slowpoke.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Spearow.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Squirtle.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Venomoth.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Venonat.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Venusaur.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Vileplume.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Vulpix.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Wartortle.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Weedle.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Wigglytuff.png", Texture.class);
		assetManager.load("res/graphics/pokemon/Zubat.png", Texture.class);

		for (int i = 0; i < 32; i++) {
			assetManager.load("res/graphics/statuseffect/attack_"+i+".png", Texture.class);
		}
		assetManager.load("res/graphics/statuseffect/white.png", Texture.class);
		
		for (int i = 0; i < 13; i++) {
			assetManager.load("res/graphics/transitions/transition_"+i+".png", Texture.class);
		}
		assetManager.load("res/font/small_letters_font.fnt", BitmapFont.class);
		
		File dir = new File("res/worlds/");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			int cnt = 0;
			cnt++;
			//System.out.println("Loaded world: " + cnt + ": " +w.getName());
			for (File child : directoryListing) {
				System.out.println("Loading world " + child.getPath());
				assetManager.load(child.getPath(), World.class);
			}
		}

		assetManager.finishLoading();
		
		skin = SkinGenerator.generateSkin(assetManager);
		
		moveDatabase = new MoveDatabase();
		
		gameScreen = GameScreen.getInstance(this);
		Trainer playerTrainer = PlayerTrainer.createInstance(this).getPlayerTrainer();
		Trainer opponentTrainer;
		OpponentTrainer.createInstance(this);
		OpponentTrainer.getInstance().addPokemon("Bulbasaur");
		OpponentTrainer.getInstance().addPokemon("Slowpoke");
		opponentTrainer  = OpponentTrainer.getInstance().getPlayerTrainer();
		battleScreen = new BattleScreen(this, playerTrainer, opponentTrainer, GameState.OFFLINE);
		transitionScreen = new TransitionScreen(this);
		this.setScreen(gameScreen);
	}

	public BattleScreen createOfflineBattleScreen() {
		Trainer playerTrainer = PlayerTrainer.getInstance().getPlayerTrainer();
		Trainer opponentTrainer;
		OpponentTrainer.createInstance(this);
		OpponentTrainer.getInstance().randomPokemon();
		opponentTrainer = OpponentTrainer.getInstance().getPlayerTrainer();
		return new BattleScreen(this, playerTrainer, opponentTrainer, GameState.OFFLINE);
	}

	public BattleScreen createOnlineBattleScreen() {
		Trainer playerTrainer = PlayerTrainer.getInstance().getPlayerTrainer();
		Trainer opponentTrainer = OpponentTrainer.getInstance().getPlayerTrainer();
		if (opponentTrainer == null) {
			System.out.println("Opponent trainer is null");
		}
		return new BattleScreen(this, playerTrainer, opponentTrainer, GameState.ONLINE);
	}

	@Override
	public void render() {
		//System.out.println(Gdx.graphics.getFramesPerSecond());
		
		/* UPDATE */
		tweenManager.update(Gdx.graphics.getDeltaTime());
		if (getScreen() instanceof AbstractScreen) {
			((AbstractScreen)getScreen()).update(Gdx.graphics.getDeltaTime());
		}
		
		/* RENDER */
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getScreen().render(Gdx.graphics.getDeltaTime());
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public TweenManager getTweenManager() {
		return tweenManager;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
	
	public BattleScreen getBattleScreen() {
		return battleScreen;
	}
	
	public void startTransition(AbstractScreen from, AbstractScreen to, Transition out, Transition in, Action action) {
		transitionScreen.startTransition(from, to, out, in, action);
	}
	
	public ShaderProgram getOverlayShader() {
		return overlayShader;
	}
	
	public ShaderProgram getTransitionShader() {
		return transitionShader;
	}
	
	public MoveDatabase getMoveDatabase() {
		return moveDatabase;
	}
	
	public String getVersion() {
		return version;
	}

}
