package GameStates;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Tweens.RectangleAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import Main.Main;
import Main.Resources;

public class StartUpLoadState extends BasicGameState {
	
	private DeferredResource nextResource;
	
	private TweenManager tweenManager;
	
	private boolean started;
	private float progressBarWidth = (float) (Main.WIDTH * 0.6);
	private int progressBarHeight = 60;
	
	private Rectangle progressBar = new Rectangle((float) (Main.WIDTH * 0.2), (Main.HEIGHT/2 - progressBarHeight/2), 0, progressBarHeight);
	
	

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
		LoadingList.setDeferredLoading(true);
		
		// load font from a .ttf file
		/*try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("resources/FONTS/8bitWonder.ttf");
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(24f); //set font size
			font = new TrueTypeFont(awtFont, false);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		
		//Adding large resources to the loading list for deferred loading
		//asgoreMusic = new Music("resources/MUSIC/Asgore.ogg");
				
		//Streaming large resources, like music and fonts
		/*try{
			asgoreMusic = new Music("resources/MUSIC/Asgore.ogg",true);
		}catch (Exception e){
			e.printStackTrace();
		}*/
		
		
		
		//Music and sounds
		Resources.addSound("res/SOUNDS/Chomp.ogg");
		Resources.addSound("res/SOUNDS/Select.ogg");
		Resources.addSound("res/SOUNDS/MilkboneBuild.ogg");
		Resources.addSound("res/SOUNDS/MilkboneBreak1.ogg");
		Resources.addSound("res/SOUNDS/MilkboneBreak2.ogg");
		
		//Fonts
		Resources.addFont("res/FONTS/8bitWonder.ttf", 50f);
		Resources.addFont("res/FONTS/Gamegirl.ttf", 25f);
		Resources.addFont("Comic Sans MS", 22, true);
		
		//Main Menu
		Resources.addImage("res/IMAGES/Misc/MenuShibe.png");
		Resources.addImage("res/IMAGES/Backgrounds/MainMenuBackground.png");
		
		//Shibe
		Resources.addSpriteSheet("res/IMAGES/Shibe/Shibe.png", 120, 132);
		
		//Milkbone
		Resources.addSpriteSheet("res/IMAGES/Milkbone/Milkbone.png", 60, 32);
		Resources.addImage("res/IMAGES/Milkbone/MilkboneBrokenLeft.png");
		Resources.addImage("res/IMAGES/Milkbone/MilkboneBrokenRight.png");
		
		//Levels
		Resources.addImage("res/IMAGES/Levels/World1/1-1.png");
		Resources.addImage("res/IMAGES/Levels/World1/1-2.png");
		Resources.addImage("res/IMAGES/Levels/World1/1-3.png");
		Resources.addImage("res/IMAGES/Levels/World1/1-4.png");
		Resources.addImage("res/IMAGES/Levels/World1/1-5.png");
		Resources.addImage("res/IMAGES/Levels/World1/1-6.png");
		
		//Miscellaneous
		Resources.addImage("res/IMAGES/Misc/TransitionFace20x.png");
		Resources.addImage("res/IMAGES/Misc/TransitionFace40x.png");
		Resources.addImage("res/IMAGES/Misc/TransitionFace80x.png");
		
		//Test stuff
		Resources.addSpriteSheet("res/IMAGES/Shibbens/ShibbenAscend.png", 92, 184);
		
		tweenManager = new TweenManager();
		Tween.to(progressBar, RectangleAccessor.POSITION, 1000.0f).target( (float) (Main.WIDTH*0.2), -60 ).ease(Expo.IN).start(tweenManager);
		
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		
		if (nextResource != null) {
			
			try {
				nextResource.load();
				// slow down loading for example purposes
				//try { Thread.sleep(100); } catch (Exception e) {}
			} catch (IOException e) {
				throw new SlickException("Failed to load: "+nextResource.getDescription(), e);
			}
			
			nextResource = null;
			
		}
		
		if (LoadingList.get().getRemainingResources() > 0) {
	        nextResource = LoadingList.get().getNext();
	    } else {
	    	
	        // loading is complete, do normal update here
	    	started = true;
	    	tweenManager.update(delta);
	    	
	    	if (progressBar.getY() <= -60) {
	    		sbg.enterState(1);
	    		LoadingList.setDeferredLoading(false);
	    		//sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
	    	}
	    	
	    }
		
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		if (!started) {
			
			//if (nextResource != null)
				//g.drawString("Loading: " + nextResource.getDescription(), 100, 100);
			
			int total = LoadingList.get().getTotalResources();
			int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
			
			float bar = (loaded / (float) total);
			
			progressBar.setWidth(progressBarWidth*bar);
			
			g.setColor(Color.white);
			g.fill(progressBar);
			
			g.setColor(Color.white);
			g.drawRect((float) (Main.WIDTH*0.2), (Main.HEIGHT/2 - progressBarHeight/2), progressBarWidth, progressBarHeight);
			
		}
		else {
			
			//Resources.getFont("8bitWonder").drawString((Main.WIDTH/2 - Resources.getFont("8bitWonder").getWidth("Done")/2), (Main.HEIGHT/2 + 100), "Done", Color.green);
			
			g.setColor(Color.green);
			g.fill(progressBar);
			
		}
	}
	
	@Override
	public int getID() {
		return 0;
	}
	
	
	
}
