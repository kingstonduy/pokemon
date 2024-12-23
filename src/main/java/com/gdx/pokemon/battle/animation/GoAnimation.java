package com.gdx.pokemon.battle.animation;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.assets.AssetManager;


public class GoAnimation extends BattleAnimation {

	public GoAnimation() {
		super(0.8f);
	}
	
	@Override
	public void initialize(AssetManager assetManager, TweenManager tweenManager) {
		super.initialize(assetManager, tweenManager);
		
		this.setPrimaryX(-0.5f);
		this.setPrimaryAlpha(0f);
		Tween.to(this, BattleAnimationAccessor.PRIMARY_OFFSET_X, 0.8f)
			.target(0f)
			.start(tweenManager);
		Tween.to(this, BattleAnimationAccessor.PRIMARY_ALPHA, 0.8f)
			.target(1f)
			.start(tweenManager);
	}

}
