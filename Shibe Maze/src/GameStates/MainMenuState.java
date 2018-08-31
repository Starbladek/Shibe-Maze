package GameStates;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import CustomTransitions.*;
import Entities.HelpShibe;
import Main.Resources;
import Main.Main;
import Utilities.*;

public class MainMenuState extends BasicGameState {
	
	
	
	private Image menuShibe;
	private Image background;
	
	private String[] options = {
			"Start",
			"Options",
			"Help",
			"Exit"
	};
	private TrueTypeFont optionFont;
	private int currentOption;
	private Text[] optionTexts;
	
	private long shibeBobTimer;
	private int shibeBobTimerLength = 500;
	private int shibeOffset;
	
	private ArrayList<HelpShibe> helpShibes;
	
	private Panel optionsPanel;
	
	private Sound selectSound;
	
	
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
		
		menuShibe = Resources.getImage("MenuShibe.png");
		background = Resources.getImage("MainMenuBackground.png");
		
		Font awtFont = new Font("Comic Sans MS", Font.PLAIN, 36);
		optionFont = new TrueTypeFont(awtFont, false);
		
		optionTexts = new Text[options.length];
		for (int i = 0; i < optionTexts.length; i++) {
			optionTexts[i] = new Text((float) (Main.HALF_WIDTH), (float) (Main.HEIGHT*0.35 + i*60), options[i], optionFont, Color.white, Text.BorderType.NO_BORDER, Text.PlacementType.MIDDLE, 255, 2);
		}
		
		helpShibes = new ArrayList<HelpShibe>();
		
		shibeBobTimer = System.nanoTime();
		
		selectSound = Resources.getSound("Select.ogg");
		
	}
	
	public int checkForCurrentOption() {
		int temp = -1;
		for (int i = 0; i < optionTexts.length; i++) {
			if (optionTexts[i].getCurrentlyOver() == true) {
				temp = i;
			}
		}
		return temp;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		
		Input input = container.getInput();
		
		//Get the current option
		currentOption = checkForCurrentOption();
		
		//Make the current option sway
		for (int i = 0; i < optionTexts.length; i++) {
			if (optionTexts[i].getCurrentlyOver() == true && optionTexts[i].getSwaying() == false) {
				optionTexts[i].enterSway(Text.SWAY_NORMAL, Text.TRANSITION_FAST, Text.SWAY_INTENSITY_INTENSE);
				selectSound.play();
			}
			else if (optionTexts[i].getCurrentlyOver() == false && optionTexts[i].getSwaying() == true) {
				optionTexts[i].exitSway(Text.TRANSITION_FAST);
			}
		}
		
		//If the mouse is clicked, check if any of the texts are currently being hovered over, and if so, perform an action
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			switch (currentOption) {
				
				//Start
				case 0:
					sbg.enterState(3, new DissolveTransitionOut(DissolveTransitionOut.boxTypes.NORMAL_BOXES, true), new DissolveTransitionIn(DissolveTransitionIn.boxTypes.NORMAL_BOXES, true));
					break;
				
				//Options
				case 1:
					if (optionsPanel == null)
						optionsPanel = new Panel(50, 50, 300, 500, Panel.THIN_BORDER, Panel.TV_ANIMATION, Panel.FAST_SPEED);
					else
						if (optionsPanel.getOpened() == true)
							optionsPanel.closePanel();
					break;
				
				//Help
				case 2:
					helpShibes.add(new HelpShibe(0, 0));
					break;
				
				//Exit
				case 3:
					container.exit();
					break;
					
			}
			
		}
		
		if (optionsPanel != null)
			if (optionsPanel.getClosed() == true)
				optionsPanel = null;
		
		//Bob the shibe
		long elapsed = (System.nanoTime() - shibeBobTimer) / 1000000;
		if (elapsed > shibeBobTimerLength) {
			shibeBobTimer = System.nanoTime();
			if (shibeOffset == 0)
				shibeOffset = 15;
			else
				shibeOffset = 0;
		}
		
		//Color the hovered-over text
		for (int i = 0; i < optionTexts.length; i++) {
			optionTexts[i].setColor(Color.white);
			if (i == currentOption)
				optionTexts[i].setColor(Color.cyan);
			optionTexts[i].update(container, delta);
		}
		
		if (optionsPanel != null)
			optionsPanel.update();
		
		for (int i = 0; i < helpShibes.size(); i++)
			helpShibes.get(i).update(delta);
		
		for (int i = 0; i < helpShibes.size(); i++)
			if (helpShibes.get(i).getReadyToBeDeleted() == true)
				helpShibes.remove(i);
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		
		background.draw(0, 0, 1);
		
		menuShibe.draw((Main.WIDTH - menuShibe.getWidth()*2), (Main.HEIGHT - menuShibe.getHeight()*2 + shibeOffset), 2);
		
		for (int i = 0; i < optionTexts.length; i++)
			optionTexts[i].render(g);
		
		if (optionsPanel != null)
			optionsPanel.render(g);
		
		for (int i = 0; i < helpShibes.size(); i++)
			if (helpShibes.get(i) != null)
				helpShibes.get(i).render(g);
		
	}
	
	@Override
	public int getID() {
		return 2;
	}
	
	
	
}