package GameStates;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import Entities.Milkbone;
import Entities.Shibe;
import Main.Main;
import Main.Resources;
import Tweens.TextAccessor;
import Utilities.Text;

public class TutorialState extends BasicGameState {
	
	
	
	private int currentMessage;
	private String[] messages = {
			"HELLO",
			"DID YOU KNOW",
			"THAT SHIBES",
			"LOVE MILKBONES",
			"GIVE THE SHIBE A MILKBONE",
			"PERFECT",
			"GO MAKE",
			"THEM HAPPY"
	};
	
	private Rectangle background = new Rectangle(0, 0, Main.WIDTH, Main.HEIGHT);
	
	private Text text;
	private TrueTypeFont font;
	
	private Milkbone milkbone;
	private Shibe shibe;
	
	private boolean interactPhase;
	private boolean readyToLeave;
	
	private TweenManager tweenManager;
	private TweenCallback messageIncreaseCallback = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source)
		{
			if(type == TweenCallback.END) {
				
				currentMessage++;
				text.setText(messages[currentMessage]);
				
				//I tried my best to make this look neat
				
				//If the message is "GIVE THE SHIBE A MILKBONE" enter the next tutorial phase
				if (currentMessage == 4) {
					Timeline.createSequence()
					.setCallback(phaseIncreaseCallback)
					.setCallbackTriggers(TweenCallback.END)
					.push(Tween.set(text, TextAccessor.POSITION_Y).target((float) (Main.HEIGHT*0.4)))
					.delay(1000f)
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 2500.0f).target((float) (Main.HEIGHT*0.2)).ease(Expo.OUT))
					.push(Tween.to(text, TextAccessor.ALPHA, 2500.0f).target(255).ease(Expo.OUT))
					.end()
					.start(tweenManager);
				}
				
				//If the message is "PERFECT" pause for a few moments before moving on to the next messages
				else if (currentMessage == 5) {
					Timeline.createSequence()
					.setCallback(messageIncreaseCallback)
					.setCallbackTriggers(TweenCallback.END)
					.push(Tween.set(text, TextAccessor.POSITION_Y).target((float) (Main.HEIGHT*0.6)).ease(Expo.OUT))
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target(Main.HEIGHT/2).ease(Expo.OUT))
					.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(255).ease(Expo.OUT))
					.end()
					.pushPause(800.0f)
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target((float) (Main.HEIGHT*0.4)).ease(Expo.IN))
					.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(0).ease(Expo.IN))
					.end()
					.pushPause(800.0f)
					.start(tweenManager);
				}
				
				//If the message is "THEM HAPPY" wait a few moments and then transition out of the tutorial
				else if (currentMessage == 7) {
					Timeline.createSequence()
					.setCallback(exitTutorialCallback)
					.setCallbackTriggers(TweenCallback.END)
					.push(Tween.set(text, TextAccessor.POSITION_Y).target((float) (Main.HEIGHT*0.6)).ease(Expo.OUT))
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target(Main.HEIGHT/2).ease(Expo.OUT))
					.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(255).ease(Expo.OUT))
					.end()
					.pushPause(800.0f)
					.start(tweenManager);
				}
				
				//If the message isn't special, display and cycle the text as usual
				else {
					Timeline.createSequence()
					.setCallback(messageIncreaseCallback)
					.setCallbackTriggers(TweenCallback.END)
					.push(Tween.set(text, TextAccessor.POSITION_Y).target((float) (Main.HEIGHT*0.6)).ease(Expo.OUT))
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target(Main.HEIGHT/2).ease(Expo.OUT))
					.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(255).ease(Expo.OUT))
					.end()
					.pushPause(400.0f)
					.beginParallel()
					.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target((float) (Main.HEIGHT*0.4)).ease(Expo.IN))
					.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(0).ease(Expo.IN))
					.end()
					.start(tweenManager);
				}
				
			}
			else
				System.out.println("hit callback type " + type);
		}
		
	};
	private TweenCallback phaseIncreaseCallback = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			
			if (type == TweenCallback.END) {
				
				text.enterSway(Text.SWAY_SLOW, Text.TRANSITION_SLOW, 20);
				
				milkbone = new Milkbone((float) (Main.WIDTH*0.3), (float) (Main.HEIGHT*0.7));
				shibe = new Shibe((float) (Main.WIDTH*0.7), (float) (Main.HEIGHT*0.7));
				milkbone.attachShibe(shibe);
				shibe.attachMilkbone(milkbone);
				
				milkbone.setCurrentlyActive(true);
				
				interactPhase = true;
				
			}
			
		}
		
	};
	private TweenCallback exitTutorialCallback = new TweenCallback() {

		@Override
		public void onEvent(int type, BaseTween<?> source) {
			
			if (type == TweenCallback.END) {
				readyToLeave = true;
			}
			
		}
		
	};
	
	
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
		font = Resources.getFont("8bitWonder.ttf");
		
		text = new Text(Main.WIDTH/2, (float) (Main.HEIGHT*0.6), messages[0], font, Color.white, Text.BorderType.NO_BORDER, Text.PlacementType.MIDDLE, 0, 1);
		
		tweenManager = new TweenManager();
		
		//Hello
		Timeline.createSequence()
		.setCallback(messageIncreaseCallback)
		.setCallbackTriggers(TweenCallback.END)
		.beginParallel()
		.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target(Main.HEIGHT/2).ease(Expo.OUT))
		.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(255).ease(Expo.OUT))
		.end()
		.pushPause(500.0f)
		.beginParallel()
		.push(Tween.to(text, TextAccessor.POSITION_Y, 500.0f).target((float) (Main.HEIGHT*0.4)).ease(Expo.IN))
		.push(Tween.to(text, TextAccessor.ALPHA, 500.0f).target(0).ease(Expo.IN))
		.end()
		.start(tweenManager);
		
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		
		Input input = container.getInput();
		if (input.isKeyPressed(Input.KEY_X)) {
			readyToLeave = true;
			container.setMouseGrabbed(false);
			if (milkbone != null)
				Mouse.setCursorPosition((int) milkbone.getX(), (int) (Main.HEIGHT - milkbone.getY()));
		}
		
		if (text != null)
			text.update(container, delta);
		
		if (milkbone != null)
			milkbone.update(container, delta);
		
		if (shibe != null)
			shibe.update(container, delta);
		
		if (shibe != null) {
			if (shibe.getChomp() == true && shibe.getCurrentlyFading() == false) {
				shibe.fadeOut();
			}
			if (shibe.getReadyToBeDeleted() == true) {
				milkbone = null;
				shibe = null;
			}
		}
		
		if (interactPhase == true) {
			if (shibe == null && milkbone == null) {
				
				interactPhase = false;
				text.exitSway(Text.TRANSITION_FAST);
				
				Timeline.createSequence()
				.setCallback(messageIncreaseCallback)
				.setCallbackTriggers(TweenCallback.END)
				.beginParallel()
				.push(Tween.to(text, TextAccessor.POSITION_Y, 1000.0f).target(0).ease(Expo.IN))
				.push(Tween.to(text, TextAccessor.ALPHA, 1000.0f).target(0).ease(Expo.IN))
				.end()
				.pushPause(400.0f)
				.start(tweenManager);
				
			}
		}
		
		if (readyToLeave == true)
			sbg.enterState(2, new EmptyTransition(), new HorizontalSplitTransition());
		
		tweenManager.update(delta);
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		
		//g.drawString("huhuhu", 100, 100);
		
		g.setColor(Color.black);
		g.fill(background);
		
		if (text != null)
			text.render(g);
		
		if (milkbone != null)
			milkbone.render(g);
		
		if (shibe != null)
			shibe.render(g);
		
	}
	
	@Override
	public int getID() {
		return 1;
	}
	
}