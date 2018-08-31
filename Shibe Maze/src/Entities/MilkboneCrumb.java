package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import Tweens.ObjectAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;

public class MilkboneCrumb extends Object {
	
	
	
	private Rectangle crumb;
	
	public static enum ForceType {
		LARGE,
		MEDIUM,
		SMALL
	};
	
	private float randomRed;
	private float randomGreen;
	private float randomBlue;
	
	private TweenManager tweenManager;
	private TweenCallback fadeOut = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END)
				readyToBeDeleted = true;
		}
	};
	
	

	public MilkboneCrumb(float x, float y, ForceType forceType) {
		super(x, y);
		
		alpha = 1;
		
		tweenManager = new TweenManager();
		Tween.to(this, ObjectAccessor.ALPHA, 1000.0f).target(0).setCallback(fadeOut).setCallbackTriggers(TweenCallback.END).ease(Linear.INOUT).start(tweenManager);
		
		width = (int) (Math.ceil(Math.random()*3) + 7);
		height = (int) (Math.ceil(Math.random()*3) + 7);
		
		switch (forceType) {
		case SMALL:
			dx = (float) (Math.ceil(Math.random()*4) - 2);
			dy = (float) (Math.ceil(Math.random()*3) - 4);
			break;
		case MEDIUM:
			dx = (float) (Math.ceil(Math.random()*6) - 3);
			dy = (float) (Math.ceil(Math.random()*3) - 6);
			break;
		case LARGE:
			dx = (float) (Math.ceil(Math.random()*8) - 4);
			dy = (float) (Math.ceil(Math.random()*3) - 9);
			break;
		}
		
		crumb = new Rectangle(x, y, width, height);
		
		randomRed = (float) ((Math.random()*100 + 155)/255);
		randomGreen = (float) ((Math.random()*135 + 100)/255);
		randomBlue = (float) ((Math.random()*50)/255);
	}
	
	public float getAlpha() { return alpha; }
	public void setAlpha(float value) { alpha = value; }
	
	public void update(int delta) {
		x += dx;
		y += dy;
		crumb.setCenterX(x);
		crumb.setCenterY(y);
		dy += 0.3;
		tweenManager.update(delta);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(randomRed, randomGreen, randomBlue, alpha));
		g.fill(crumb);
	}
	
}