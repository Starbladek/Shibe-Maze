package GameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Entities.Milkbone;
import Entities.Shibe;
import LevelStuff.Level;
import Main.Main;

public class World1State extends BasicGameState {
	
	
	
	private Level currentLevel;
	private int currentWorldNumber = 1;
	private int currentLevelNumber = 1;
	private int totalNumberOfLevels = 6;
	
	private Milkbone milkbone;
	private Shibe shibe;
	
	
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		
		milkbone = new Milkbone(0, 0);
		shibe = new Shibe((float) (Main.WIDTH*0.7), (float) (Main.HEIGHT*0.5));
		
		milkbone.attachShibe(shibe);
		shibe.attachMilkbone(milkbone);
		
		currentLevel = new Level(currentWorldNumber, currentLevelNumber, shibe, milkbone);
		
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		
		if (currentLevel != null) {
			
			currentLevel.update(container, delta);
			
			//If the current level has been beaten, initiate the transition to the next level by having the current level fade out, and the next level fade in
			if (currentLevel.getLevelComplete() == true && currentLevel.getCurrentlyFading() == false) {
				currentLevel.fadeOut();
				currentLevelNumber++;
			}
			
			//If the current level is ready to be deleted, delete it and make it the next level
			if (currentLevel.getReadyToBeDeleted() == true) {
				if (currentLevelNumber <= totalNumberOfLevels)
					currentLevel = new Level(currentWorldNumber, currentLevelNumber, shibe, milkbone);
				else {
					currentLevel = null;
				}
			}
			
		}
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		if (currentLevel != null)
			currentLevel.render(container, sbg, g);
	}
	
	@Override
	public int getID() {
		return 3;
	}
	
}