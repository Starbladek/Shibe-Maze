package CustomTransitions;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

import Main.Resources;

public class DissolveTransitionIn implements Transition {
	
	
	
	public static enum boxTypes {
		SMALL_BOXES, NORMAL_BOXES, LARGE_BOXES
	}
	
	private int numOfBoxesPerRow;
	private int numOfBoxesPerCol;
	private int boxSize;
	
	private Rectangle[][] squaresCovered;
	private Image image;
	private long squareTimer;
	private int squareTimerDelay;
	private long squareTimerElapsed;
	private int amountOfSquaresToRemove;
	
	private boolean complete;
	
	
	
	public DissolveTransitionIn(boxTypes boxType, boolean usesImages) {
		
		switch (boxType) {
		
		case SMALL_BOXES:
			numOfBoxesPerRow = 64;
			numOfBoxesPerCol = 36;
			boxSize = 20;
			amountOfSquaresToRemove = 50;
			break;
			
		case NORMAL_BOXES:
			numOfBoxesPerRow = 32;
			numOfBoxesPerCol = 18;
			boxSize = 40;
			amountOfSquaresToRemove = 10;
			break;
			
		case LARGE_BOXES:
			numOfBoxesPerRow = 16;
			numOfBoxesPerCol = 9;
			boxSize = 80;
			amountOfSquaresToRemove = 3;
			break;
			
		default:
			numOfBoxesPerRow = 16;
			numOfBoxesPerCol = 9;
			boxSize = 80;
			amountOfSquaresToRemove = 5;
			break;
			
		}
		
		if (usesImages == true) {
			switch (boxType) {
			case SMALL_BOXES:
				image = Resources.getImage("TransitionFace20x.png");
				break;
			case NORMAL_BOXES:
				image = Resources.getImage("TransitionFace40x.png");
				break;
			case LARGE_BOXES:
				image = Resources.getImage("TransitionFace80x.png");
				break;
			}
		}
		
		squaresCovered = new Rectangle[numOfBoxesPerRow][numOfBoxesPerCol];
		
		for (int i = 0; i < numOfBoxesPerRow; i++)
			for (int j = 0; j < numOfBoxesPerCol; j++)
				squaresCovered[i][j] = new Rectangle(i*boxSize, j*boxSize, boxSize, boxSize);
		
		squareTimerDelay = 0;
		squareTimer = System.nanoTime();
		
	}
	
	@Override
	public void init(GameState firstState, GameState secondState) {
		
	}
	
	private boolean checkIfEmpty() {
		
		boolean temp = true;
		
		mainLoop:
		for (int i = 0; i < numOfBoxesPerRow; i++) {
			for (int j = 0; j < numOfBoxesPerCol; j++) {
				if (squaresCovered[i][j] != null) {
					temp = false;
					break mainLoop;
				}
			}
		}
		
		return temp;
		
	}
	
	@Override
	public void update(StateBasedGame sbg, GameContainer container, int delta) throws SlickException {
		
		squareTimerElapsed = (System.nanoTime() - squareTimer) / 1000000;
		if (squareTimerElapsed > squareTimerDelay) {
			
			int randX;
			int randY;
			boolean removedBox;
			
			for (int i = 0; i < amountOfSquaresToRemove; i++) {
				
				removedBox = false;
				
				do {
					
					if (checkIfEmpty() == false) {
					
						randX = (int) Math.floor(Math.random()*numOfBoxesPerRow);
						randY = (int) Math.floor(Math.random()*numOfBoxesPerCol);
						
						if (squaresCovered[randX][randY] != null) {
							squaresCovered[randX][randY] = null;
							removedBox = true;
						}
					
					}
					else {
						complete = true;
						return;
					}
					
				} while (removedBox == false);
				
			}
			
			squareTimer = System.nanoTime();
			
		}
		
	}
	
	@Override
	public boolean isComplete() {
		return complete;
	}
	
	@Override
	public void postRender(StateBasedGame sbg, GameContainer container, Graphics g) throws SlickException {
		
		g.setColor(Color.black);
		
		for (int i = 0; i < numOfBoxesPerRow; i++) {
			for (int j = 0; j < numOfBoxesPerCol; j++) {
				if (squaresCovered[i][j] != null) {
					if (image != null) {
						g.drawImage(image, i*boxSize, j*boxSize);
					}
					else {
						g.fill(squaresCovered[i][j]);
					}
				}
			}
		}
		
	}
	
	@Override
	public void preRender(StateBasedGame sbg, GameContainer container, Graphics g) throws SlickException {
		
	}
	
}