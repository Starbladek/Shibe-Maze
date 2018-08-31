package Entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Main.Resources;

public class MilkboneBroken extends Object {
	
	
	
	private Milkbone milkbone;
	
	private Image bmL;
	private float bmLX;
	private float bmLY;
	private float bmLRotateAngle;
	
	private Image bmR;
	private float bmRX;
	private float bmRY;
	private float bmRRotateAngle;
	
	private float verticalVelocity;
	
	private int phase;
	private long phaseTimer;
	private int[] phaseTimerLength = {1000, 3500, 3000};
	private long phaseTimerElapsed;
	private boolean phase1Occurred;
	private boolean phase2Occurred;
	
	private Sound milkboneBreak1;
	private Sound milkboneBreak2;
	
	
	
	public MilkboneBroken(float x, float y, Milkbone milkbone) {
		
		super(x, y);
		
		alpha = 1;
		
		//bmL = Resources.getImage("MilkboneBrokenLeft.png");
		try {
			bmL = new Image("res/IMAGES/Milkbone/MilkboneBrokenLeft.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		bmLX = x;
		bmLY = y;
		bmLRotateAngle = (float) (-1 * ((Math.random()*5) + 5) );
		
		//bmR = Resources.getImage("MilkboneBrokenRight.png");
		try {
			bmR = new Image("res/IMAGES/Milkbone/MilkboneBrokenRight.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		bmRX = x;
		bmRY = y;
		bmRRotateAngle = (float) (Math.random()*6 + 8);
		
		this.milkbone = milkbone;
		
		verticalVelocity = 3;
		
		phaseTimer = System.nanoTime();
		
		milkboneBreak1 = Resources.getSound("MilkboneBreak1.ogg");
		milkboneBreak2 = Resources.getSound("MilkboneBreak2.ogg");
		
	}
	
	
	
	public void update(int delta) {
		
		phaseTimerElapsed = (System.nanoTime() - phaseTimer) / 1000000;
		if (phaseTimerElapsed > phaseTimerLength[phase]) {
			if (phase < 2)
				phase++;
		}
		
		switch (phase) {
		case 0:
			//About to break
			break;
		case 1:
			//Break, and then freeze for a second
			if (phase1Occurred == false) {
				milkboneBreak1.play();
				int rand = (int) Math.ceil(Math.random()*3 + 5);
				milkbone.explodeCrumbs((x + bmL.getWidth()/2), (y + bmL.getHeight()/2), rand, MilkboneCrumb.ForceType.SMALL);
				bmLX -= 8;
				bmRX += 8;
				phase1Occurred = true;
			}
			break;
		case 2:
			
			if (phase2Occurred == false) {
				milkboneBreak2.play();
				int rand = (int) Math.ceil(Math.random()*8 + 4);
				milkbone.explodeCrumbs((x + bmL.getWidth()/2), (y + bmL.getHeight()/2), rand, MilkboneCrumb.ForceType.MEDIUM);
				phase2Occurred = true;
			}
			
			bmLX -= 3;
			bmRX += 3;
			
			bmLY -= verticalVelocity;
			bmRY -= verticalVelocity;
			
			bmL.rotate(bmLRotateAngle);
			bmR.rotate(bmRRotateAngle);
			
			verticalVelocity -= 0.2;
			
			if (bmLY > Main.Main.HEIGHT && bmRY > Main.Main.HEIGHT) {
				readyToBeDeleted = true;
			}
			
			break;
		}
		
	}
	
	public void render(Graphics g) {
		g.drawImage(bmL, bmLX, bmLY, new Color(255, 255, 255, alpha));
		g.drawImage(bmR, bmRX, bmRY, new Color(255, 255, 255, alpha));
	}
	
}