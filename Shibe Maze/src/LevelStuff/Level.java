package LevelStuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import Entities.Milkbone;
import Entities.Shibe;
import Main.*;
import Tweens.LevelAccessor;
import Tweens.TextAccessor;
import Utilities.PixelScanner;
import Utilities.Text;

public class Level {
	
	
	
	private Image levelMap;
	private String levelName;
	
	private Milkbone milkbone;
	private Shibe shibe;
	private Text text;
	
	private Color[] pixelsToCheck = new Color[9];
	private boolean milkboneIsDead;
	private boolean levelComplete;
	private boolean currentlyFading;
	private boolean readyToBeDeleted;
	
	private TweenManager tweenManager;
	private TweenCallback onFadeIn = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END) {
				shibe.setChomp(false);
				shibe.setCurrentShibeSprite(0);
				milkbone.setCurrentlyActive(true);
				currentlyFading = false;
				Tween.to(text, TextAccessor.POSITION_X, 500).target(50).ease(Expo.OUT).start(tweenManager);
			}
		}
	};
	private TweenCallback fadeOut = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.END)
				readyToBeDeleted = true;
		}
	};
	
	private float scale;
	private float alpha;
	private int nextShibeSpawnX;
	private int nextShibeSpawnY;
	
	
	
	public Level(int worldNumber, int levelNumber, Shibe shibe, Milkbone milkbone) {
		
		this.levelMap = Resources.getImage(worldNumber + "-" + levelNumber + ".png");
		currentlyFading = true;
		
		this.shibe = shibe;
		this.milkbone = milkbone;
		this.milkbone.setCurrentlyActive(false);
		this.milkbone.setCurrentMilkboneSprite(0);
		
		try {
			
			File levelFile = new File("res/IMAGES/Levels/TextFiles/" + worldNumber + "-" + levelNumber + ".properties");
			FileInputStream fileInput = new FileInputStream(levelFile);
			
			Properties levelProperties = new Properties();
			levelProperties.load(fileInput);
			
			fileInput.close();

			@SuppressWarnings("rawtypes")
			Enumeration enuKeys = levelProperties.keys();
			while (enuKeys.hasMoreElements()) {
				
				String key = (String) enuKeys.nextElement();
				String value = levelProperties.getProperty(key);
				
				if (key.equals("name"))
					levelName = value;
				
				//TODO: create a method that scans the level for a pixel of a very specific color, and then make the pixel at that location
				//		the spawn area for the shibe. Do the same for a different very specific color and make that the milkbone's spawn area.
				//		Use an efficient pixel scanning method, and have it default to some behaviour if it can't find the pixel
				//		Milkbone spawn point uses color 100, 0, 255
				//		Shibe spawn point uses color 0, 100, 255
				
				if (key.equals("milkboneSpawnPoint")) {
					//If the level has a preset milkboneSpawnPoint, set the spawn point as it
					if (value.length() > 1) {
						//Remove any excess white spaces
						String newValue = value.replaceAll("\\s+","");
						//Split the values at any comma found
						String[] splitValues = newValue.split(",");
						//Set the milkbone's position and spawn point
						int tempX = Integer.parseInt(splitValues[0]);
						int tempY = Integer.parseInt(splitValues[1]);
						this.milkbone.setPos(tempX, tempY);
						this.milkbone.setSpawn(tempX, tempY);
					}
					//If the level does not have a preset milkboneSpawnPoint, find the purple pixel and set that as the spawn point
					else {
						
						//Find the purple pixel
						PixelScanner pixelScanner = new PixelScanner();
						Color temp = new Color(100, 0, 255, 255);
						pixelScanner.scanImageForPixel(this.levelMap, temp);
						this.milkbone.setPos(pixelScanner.getX(), pixelScanner.getY());
						this.milkbone.setSpawn(pixelScanner.getX(), pixelScanner.getY());
						
						//Set the milkbone spawn point as the purple pixel's location
						String newPropertyValue = pixelScanner.getX() + ", " + pixelScanner.getY();
						levelProperties.setProperty("milkboneSpawnPoint", newPropertyValue);
						
						//Overwrite the levelProperties file to show this new information
						FileOutputStream fileOut = new FileOutputStream(levelFile);
						levelProperties.store(fileOut, "Level " + worldNumber + "-" + levelNumber + " Properties");
						fileOut.close();
						
					}
				}
				
				if (key.equals("nextShibeSpawnPoint")) {
					if (value.length() > 1) {
						//Remove any excess white spaces
						String newValue = value.replaceAll("\\s+","");
						//Split the values at any comma found
						String[] splitValues = newValue.split(",");
						nextShibeSpawnX = Integer.parseInt(splitValues[0]);
						nextShibeSpawnY = Integer.parseInt(splitValues[1]);
					}
					else {
						
						//Find the blue pixel
						PixelScanner pixelScanner = new PixelScanner();
						Image nextLevelMap = Resources.getImage(worldNumber + "-" + (levelNumber+1) + ".png");
						if (nextLevelMap != null) {
							Color temp = new Color(0, 100, 255, 255);
							pixelScanner.scanImageForPixel(nextLevelMap, temp);
							nextShibeSpawnX = pixelScanner.getX();
							nextShibeSpawnY = pixelScanner.getY();
							
							//Set the next level's shibe spawn point as the blue pixel's location
							String newPropertyValue = pixelScanner.getX() + ", " + pixelScanner.getY();
							levelProperties.setProperty("nextShibeSpawnPoint", newPropertyValue);
							
							//Overwrite the levelProperties file to show this new information
							FileOutputStream fileOut = new FileOutputStream(levelFile);
							levelProperties.store(fileOut, "Level " + worldNumber + "-" + levelNumber + " Properties");
							fileOut.close();
						}
						else {
							System.out.println("must be the last level in the current world");
							nextShibeSpawnX = Main.WIDTH*2;
							nextShibeSpawnY = -Main.HEIGHT;
						}
						
					}
				}
				
				//System.out.println(key + ": " + value);
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		text = new Text(Main.WIDTH, Main.HEIGHT - 75, levelName, Resources.getFont("Gamegirl.ttf"), Color.white, Text.BorderType.THIN_BORDER, Text.PlacementType.CORNER, 255, 1);
		text.enterSway(Text.SWAY_SLOW, Text.TRANSITION_SLOW, Text.SWAY_INTENSITY_INTENSE);
		
		scale = 1.2f;
		alpha = 0;
		
		tweenManager = new TweenManager();
		Tween.to(this, LevelAccessor.SCALE, 1000).target(1).ease(Expo.OUT).start(tweenManager);
		Tween.to(this, LevelAccessor.ALPHA, 1000).target(1).setCallback(onFadeIn).setCallbackTriggers(TweenCallback.END).ease(Quad.IN).start(tweenManager);
		
	}
	
	public void checkForDeath(GameContainer container, StateBasedGame sbg, Graphics g) {
		
		//Get the 9 main points on the milkbone's hitbox and check the current graphics
		//context, which is the level, for any red at those points
		
		for (int i = 0; i < pixelsToCheck.length; i++) {
			pixelsToCheck[i] = g.getPixel((int) milkbone.getLocalXCoord(i+1), (int) milkbone.getLocalYCoord(i+1));
			if (containsRed(pixelsToCheck[i])) {
				milkboneIsDead = true;
				break;
			}
			else {
				milkboneIsDead = false;
			}
		}
		
		if ( milkboneIsDead == true ) {
			milkbone.kill(container);
		}
		else {
			//System.out.println("not dead");
		}
		
		milkboneIsDead = false;
		
	}
	
	private boolean containsRed(Color colorToCheck) {
		boolean temp = false;
		if (colorToCheck.getRed() > 200) {
			//System.out.println(colorToCheck.getRed());
			temp = true;
		}
		return temp;
	}
	
	public void fadeOut() {
		currentlyFading = true;
		Tween.to(this, LevelAccessor.SCALE, 1000).target(0.8f).ease(Expo.OUT).start(tweenManager);
		Tween.to(this, LevelAccessor.ALPHA, 1000).target(0).setCallback(fadeOut).setCallbackTriggers(TweenCallback.END).ease(Linear.INOUT).start(tweenManager);
		Tween.to(text, TextAccessor.POSITION_X, 500).target(Main.WIDTH*(-1)).ease(Expo.OUT).start(tweenManager);
		shibe.repositionShibe(nextShibeSpawnX, nextShibeSpawnY);
	}
	
	
	
	public boolean getLevelComplete() { return levelComplete; }
	public void setLevelComplete(boolean value) { levelComplete = value; }
	
	public boolean getCurrentlyFading() { return currentlyFading; }
	public void setCurrentlyFading(boolean value) { currentlyFading = value; }
	
	public float getScale() { return scale; }
	public void setScale(float value) { scale = value; }
	
	public float getAlpha() { return alpha; }
	public void setAlpha(float value) { alpha = value; }
	
	public boolean getReadyToBeDeleted() { return readyToBeDeleted; }
	public void setReadyToBeDeleted(boolean value) { readyToBeDeleted = value; }
	
	
	
	public void update(GameContainer container, int delta) {
		
		tweenManager.update(delta);
		
		if (milkbone != null)
			milkbone.update(container, delta);
		
		if (shibe != null) {
			shibe.update(container, delta);
			if (shibe.getChomp() == true && levelComplete == false && currentlyFading == false) {
				levelComplete = true;
			}
			if (shibe.getReadyToBeDeleted() == true) {
				milkbone = null;
				shibe = null;
			}
		}
		
		if (text != null)
			text.update(container, delta);
		
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) {
		
		levelMap.draw( (0 - ( (levelMap.getWidth()*scale)-levelMap.getWidth() )/2 ), (0 - ( (levelMap.getHeight()*scale)-levelMap.getHeight() )/2 ), scale, new Color(255, 255, 255, alpha));
		
		if (text != null)
			text.render(g);
		
		if (milkbone != null) {
			checkForDeath(container, sbg, g);
			milkbone.render(g);
		}
		
		if (shibe != null)
			shibe.render(g);
		
	}
	
}