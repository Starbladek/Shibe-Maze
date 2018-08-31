package Main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import GameStates.*;

public class Main extends StateBasedGame {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int HALF_WIDTH = WIDTH/2;	//Just so that I don't have to do this miniscule operation
	public static final int HALF_HEIGHT = HEIGHT/2;	//everytime I want half of the width or half of the height
	
	public Main(String title) {
		
		super(title);
		
	}
	
	public static void main(String[] args) throws SlickException {
		
		AppGameContainer app = new AppGameContainer(new Main("Shibe Maze"));
		
		app.setDisplayMode(WIDTH, HEIGHT, false);
		app.setAlwaysRender(true);	//Always render, even when unfocused
		app.setTargetFrameRate(60);
		
		app.setIcons(new String[] { "res/IMAGES/Misc/TempIcon32x.tga", "res/IMAGES/Misc/TempIcon24x.tga", "res/IMAGES/Misc/TempIcon16x.tga" });
		//.tga's are flipped upside down lol
		
		app.start();
		
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		
		new Resources();	//Initialize the resources
		
		this.addState(new StartUpLoadState());
		this.addState(new TutorialState());
		this.addState(new MainMenuState());
		this.addState(new World1State());
		
	}

}
