package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import Main.Resources;
import Tweens.ObjectAccessor;

public class Shibe extends Object {
	
	
	
	private SpriteSheet shibeSpriteSheet;
	private boolean openingMouth;
	private boolean chomp;
	private boolean currentlyFading;
	private Milkbone milkbone;
	
	private TweenManager tweenManager;
	private TweenCallback shibeFadeOut = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END) {
				milkbone.clearMilkboneCrumbs();
				readyToBeDeleted = true;
			}
		}
	};
	
	private int currentShibeSprite;
	private long spriteUpdateTimer;
	private int spriteUpdateTimerLength = 50;
	
	protected Rectangle hitBox;	//Milkbone accesses this frequently, so I made it protected, rather than having a function access it
	private float hitBoxWidth;
	private float hitBoxHeight;
	
	
	
	public Shibe(float x, float y) {
		
		super(x, y);
		
		shibeSpriteSheet = Resources.getSpriteSheet("Shibe.png");
		width = (shibeSpriteSheet.getWidth()/shibeSpriteSheet.getHorizontalCount());
		height = (shibeSpriteSheet.getHeight());
		alpha = 0;
		
		tweenManager = new TweenManager();
		Tween.to(this, ObjectAccessor.ALPHA, 1000.0f).target(1).ease(Linear.INOUT).start(tweenManager);
		
		hitBoxWidth = (float) (width*0.8);
		hitBoxHeight = (float) (height*0.8);
		hitBox = new Rectangle(x, y, hitBoxWidth, hitBoxHeight);
		hitBox.setCenterX(x);
		hitBox.setCenterY(y);
		
		spriteUpdateTimer = System.nanoTime();
		
	}
	
	public void attachMilkbone(Milkbone milkbone) { this.milkbone = milkbone; }
	
	public void fadeOut() {
		Tween.to(this, ObjectAccessor.ALPHA, 1500.0f).target(0).setCallback(shibeFadeOut).setCallbackTriggers(TweenCallback.END).ease(Quad.IN).start(tweenManager);
		currentlyFading = true;
	}
	
	public void repositionShibe(float newX, float newY) {
		Tween.to(this, ObjectAccessor.POSITION_XY, 2000).target(newX, newY).ease(Cubic.INOUT).start(tweenManager);
	}
	
	
	
	public boolean getChomp() { return chomp; }
	public void setChomp(boolean value) { chomp = value; }
	
	public boolean getCurrentlyFading() { return currentlyFading; }
	public void setCurrentlyFading(boolean value) { currentlyFading = value; }
	
	public int getCurrentShibeSprite() { return currentShibeSprite; }
	public void setCurrentShibeSprite(int value) { currentShibeSprite = value; }
	
	
	
	public void update(GameContainer container, int delta) {
		
		x1 = x;
		y1 = y;
		
		tweenManager.update(delta);
		
		long elapsed = (System.nanoTime() - spriteUpdateTimer) / 1000000;
		
		if (elapsed > spriteUpdateTimerLength) {
			
			//If the shibe has not eaten a milkbone
			if (chomp == false) {
				
				//Open mouth if the milkbone is close enough
				if (getDistanceTo(milkbone) < 200)
					openingMouth = true;
				//CLose mouth if the milkbone is not close enough
				else
					openingMouth = false;
				
				//If the mouth is currently opening
				if (openingMouth == true) {
					//If the mouth isn't fully open, continue opening it
					if (currentShibeSprite < 5)
						currentShibeSprite++;
					//If the mouth is fully open, don't do anything
					else {
						
					}
				}
				
				//If the mouth is closing
				else {
					//If the mouth isn't completely closed, continue closing it
					if (currentShibeSprite > 0)
						currentShibeSprite--;
				}
				
			}
			
			//If the shibe has eaten a milkbone
			else {
				//If the shibe hasn't completely chomped down on the milkbone, continue chomping
				if (currentShibeSprite < 7)
					currentShibeSprite++;
			}
			
			spriteUpdateTimer = System.nanoTime();
			
		}
		
		hitBox.setCenterX(x);
		hitBox.setCenterY(y);
		
		velocity = getXVelocity();
		rotation = velocity*2;
		
	}
	
	public void render(Graphics g) {
		shibeSpriteSheet.startUse();
		
		Image image2 = shibeSpriteSheet.getSubImage(currentShibeSprite, 0);
		//shibeSpriteSheet.getSubImage(currentShibeSprite, 0).drawEmbedded((x - width/2), (y - height/2), width, height);
		
		shibeSpriteSheet.endUse();
		
		image2.setRotation(rotation);
		image2.draw((x - width/2), (y - height/2), width, height, new Color(255, 255, 255, alpha));
		
		g.setColor(new Color (255, 0, 0, 50));
		//g.fill(hitBox);
	}
	
}