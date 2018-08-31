package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import Main.Resources;
import Tweens.ObjectAccessor;
import Utilities.Text;
import Utilities.Text.*;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;

public class HelpShibe extends Object {
	
	
	
	private Image image;
	private TrueTypeFont textFont;
	private Text text;
	private String[] phrases = {
			"its ok",
			"you can do it",
			"chub dislikes smoke",
			"woops",
			"where is the golden milkbone",
			"feed them",
			"hit start",
			"hit help again",
			"snif",
			"hello"
	};
	
	private TweenManager tweenManager;
	private TweenCallback onShibeTweenIn = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END) {
				stayTimer = System.nanoTime();
			}
		}
	};
	private TweenCallback onShibeTweenOut = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END) {
				readyToBeDeleted = true;
			}
		}
	};
	
	private long stayTimer;
	private int stayTimerLength = 3000;
	private long stayTimerElapsed;
	
	

	public HelpShibe(float x, float y) {
		
		super(x, y);
		
		try {
			image = new Image("res/IMAGES/Misc/HelpShibe.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		do {
			this.x = (float) (Math.random()*Main.Main.WIDTH);
		} while (this.x > (Main.Main.WIDTH - width));
		this.y = Main.Main.HEIGHT + height;
		
		textFont = Resources.getFont("Comic Sans MS.ttf");
		
		int phraseNumber = (int) Math.floor(Math.random()*phrases.length);
		text = new Text((this.x + width/2), (Main.Main.HEIGHT - (height*1.1f)), phrases[phraseNumber], textFont, Color.blue, BorderType.NO_BORDER, PlacementType.MIDDLE);
		
		tweenManager = new TweenManager();
		Tween.to(this, ObjectAccessor.POSITION_XY, 1000).target(this.x, (Main.Main.HEIGHT)).setCallback(onShibeTweenIn).setCallbackTriggers(TweenCallback.END).ease(Expo.OUT).start(tweenManager);
		
	}
	
	public void update(int delta) {
		
		tweenManager.update(delta);
		
		if (stayTimer != 0) {
			stayTimerElapsed = (System.nanoTime() - stayTimer) / 1000000;
			if (stayTimerElapsed > stayTimerLength) {
				stayTimer = 0;
				Tween.to(this, ObjectAccessor.POSITION_XY, 1000).target(x, (Main.Main.HEIGHT + height)).setCallback(onShibeTweenOut).setCallbackTriggers(TweenCallback.END).ease(Expo.IN).start(tweenManager);
			}
		}
		
	}
	
	public void render(Graphics g) {
		
		image.draw(x, (y - height));
		
		//quick and dirty way to render text during the stay period
		if (stayTimer != 0)
			text.render(g);
		
	}
	
}