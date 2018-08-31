package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import Tweens.ObjectAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

public class ObjectPulse extends Object {
	
	
	
	private Image image;
	private SpriteSheet spriteSheet;
	private int spriteSheetNumber;
	private float scale;
	
	private TweenManager tweenManager;
	private TweenCallback fadeOut = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END)
				readyToBeDeleted = true;
		}
	};
	
	

	public ObjectPulse(float x, float y, Image image) {
		super(x, y);
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		alpha = 1;
		scale = 1;
		tweenManager = new TweenManager();
		Tween.to(this, ObjectAccessor.ALPHA, 500.0f).target(0).setCallback(fadeOut).setCallbackTriggers(TweenCallback.END).ease(Linear.INOUT).start(tweenManager);
	}
	public ObjectPulse(float x, float y, SpriteSheet spriteSheet, int spriteSheetNumber) {
		super(x, y);
		this.spriteSheet = spriteSheet;
		this.spriteSheetNumber = spriteSheetNumber;
		width = (spriteSheet.getWidth()/spriteSheet.getHorizontalCount());
		height = spriteSheet.getHeight();
		alpha = 1;
		scale = 1;
		tweenManager = new TweenManager();
		Tween.to(this, ObjectAccessor.ALPHA, 400.0f).target(0).setCallback(fadeOut).setCallbackTriggers(TweenCallback.END).ease(Linear.INOUT).start(tweenManager);
	}
	
	
	
	public void update(int delta) {
		tweenManager.update(delta);
		scale += 0.08f;
		if (image != null) {
			width = image.getWidth();
			height = image.getHeight();
		}
		if (spriteSheet != null) {
			width = (int) (spriteSheet.getSubImage(spriteSheetNumber, 0).getWidth()*scale);
			height = (int) (spriteSheet.getSubImage(spriteSheetNumber, 0).getHeight()*scale);
		}
	}
	
	public void render(Graphics g) {
		
		if (image != null) {
			image.draw((x - width/2), (y - height/2), scale, new Color(255, 255, 255, alpha));
			//g.drawImage(image, (x - image.getWidth()/2), (y - image.getHeight()/2), new Color(255, 255, 255, alpha));
		}
		
		if (spriteSheet != null) {
			spriteSheet.getSubImage(spriteSheetNumber, 0).draw((x - width/2), (y - height/2), scale, new Color(255, 255, 255, alpha));
			//spriteSheet.startUse();
			//g.setColor(new Color(255, 255, 255, alpha));
			//spriteSheet.getSubImage(spriteSheetNumber, 0).drawEmbedded((x - width/2), (y - height/2), width, height);
			//spriteSheet.endUse();
		}
			
	}
	
}