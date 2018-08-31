package Entities;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import aurelienribon.tweenengine.TweenManager;
import Main.Resources;
import Main.Main;

public class Milkbone extends Object {
	
	
	
	private float targetX;
	private float targetY;
	
	private float spawnX;
	private float spawnY;
	
	private boolean mouseClicked;
	private boolean followingMouse;
	private boolean currentlyActive;	//currentlyActive's roles are to determine whether or not it can be drawn, if it can be clicked, and if it can die
	
	private ArrayList<MilkboneCrumb> milkboneCrumbs;
	private Shibe shibe;
	
	private TweenManager tweenManager;
	
	private SpriteSheet milkboneSpriteSheet;
	private int currentMilkboneSprite;
	private long spriteUpdateTimer;
	private int spriteUpdateTimerLength = 120;
	private long spriteUpdateTimerElapsed;
	
	private Rectangle hitBox;
	private float hitBoxWidth;
	private float hitBoxHeight;
	
	private ArrayList<MilkboneBroken> milkboneBroken;
	private boolean currentlyDead;
	
	private ArrayList<ObjectPulse> milkboneRespawnPulse;
	
	private long deadTimer;
	private int deadTimerLength = 1500;
	private long deadTimerElapsed;
	
	private Sound milkboneChompSound;
	private Sound milkboneBuildSound;
	
	
	
	public Milkbone(float x, float y) {
		
		super(x, y);
		
		spawnX = x;
		spawnY = y;
		
		milkboneCrumbs = new ArrayList<MilkboneCrumb>();
		milkboneBroken = new ArrayList<MilkboneBroken>();
		milkboneRespawnPulse = new ArrayList<ObjectPulse>();
		alpha = 1;
		
		milkboneSpriteSheet = Resources.getSpriteSheet("Milkbone.png");
		width = (milkboneSpriteSheet.getWidth()/milkboneSpriteSheet.getHorizontalCount());
		height = milkboneSpriteSheet.getHeight();
		
		hitBoxWidth = (float) (width*0.8);
		hitBoxHeight = (float) (height*0.8);
		hitBox = new Rectangle(x, y, hitBoxWidth, hitBoxHeight);
		hitBox.setCenterX(x);
		hitBox.setCenterY(y);
		
		spriteUpdateTimer = System.nanoTime();
		
		milkboneChompSound = Resources.getSound("Chomp.ogg");
		milkboneBuildSound = Resources.getSound("MilkboneBuild.ogg");
		
		tweenManager = new TweenManager();
		//Tween.to(this, ObjectAccessor.ALPHA, 500.0f).target(1).ease(Linear.INOUT).start(tweenManager);
		
	}
	
	
	
	public void attachShibe(Shibe shibe) { this.shibe = shibe; }
	
	public void clearMilkboneCrumbs() {
		for (int i = 0; i < milkboneCrumbs.size(); i++)
			milkboneCrumbs.remove(i);
	}
	
	//Used to get a specific coord relative to where the milkbone is
	public float getLocalXCoord(int value) {
		float temp = 0;
		switch (value) {
			
			//Top left
			case 1:
				temp = x - hitBoxWidth/2;
				break;
			
			//Top middle
			case 2:
				temp = x;
				break;
			
			//Top right
			case 3:
				temp = x + hitBoxWidth/2;
				break;
			
			//Left
			case 4:
				temp = x - hitBoxWidth/2;
				break;
				
			//Middle
			case 5:
				temp = x;
				break;
				
			//Right
			case 6:
				temp = x + hitBoxWidth/2;
				break;
				
			//Bottom left
			case 7:
				temp = x - hitBoxWidth/2;
				break;
				
			//Bottom middle
			case 8:
				temp = x;
				break;
				
			//Bottom right
			case 9:
				temp = x + hitBoxWidth/2;
				break;
				
		}
		return temp;
	}
	public float getLocalYCoord(int value) {
		float temp = 0;
		switch (value) {
			
			//Top left
			case 1:
				temp = y - hitBoxHeight/2;
				break;
			
			//Top middle
			case 2:
				temp = y - hitBoxHeight/2;
				break;
			
			//Top right
			case 3:
				temp = y - hitBoxHeight/2;
				break;
			
			//Left
			case 4:
				temp = y;
				break;
				
			//Middle
			case 5:
				temp = y;
				break;
				
			//Right
			case 6:
				temp = y;
				break;
				
			//Bottom left
			case 7:
				temp = y + hitBoxHeight/2;
				break;
				
			//Bottom middle
			case 8:
				temp = y + hitBoxHeight/2;
				break;
				
			//Bottom right
			case 9:
				temp = y + hitBoxHeight/2;
				break;
			
		}
		return temp;
	}
	
	public void kill(GameContainer container) {
		
		if (currentlyDead == false && currentlyActive == true) {
			currentlyDead = true;
			deadTimer = System.nanoTime();
			
			followingMouse = false;
			container.setMouseGrabbed(false);
			
			milkboneBroken.add(new MilkboneBroken((x - width/2), (y - height/2), this));
			
			Mouse.setCursorPosition((int) x, (int) (Main.HEIGHT - y));
			setPos(spawnX, spawnY);
			
			currentMilkboneSprite = 0;
			
			//alpha = 0;
			//Tween.to(this, ObjectAccessor.ALPHA, 1000.0f).target(1).ease(Linear.INOUT).start(tweenManager);
		}
		
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
		hitBox.setCenterX(x);
		hitBox.setCenterY(y);
	}
	
	public void setSpawn(float newX, float newY) {
		spawnX = newX;
		spawnY = newY;
	}
	
	public void explodeCrumbs(float posX, float posY, int amount, MilkboneCrumb.ForceType forceType) {
		for (int i = 0; i < amount; i++)
			milkboneCrumbs.add(new MilkboneCrumb(posX, posY, forceType));
	}
	
	public boolean getCurrentlyActive() { return currentlyActive; }
	public void setCurrentlyActive(boolean value) { currentlyActive = value; }
	
	public int getCurrentMilkboneSprite() { return currentMilkboneSprite; }
	public void setCurrentMilkboneSprite(int value) { currentMilkboneSprite = value; }
	
	
	
	public void update(GameContainer container, int delta) {
		
		tweenManager.update(delta);
		
		Input input = container.getInput();
		
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		if (currentlyActive == true && currentlyDead == false) {
		
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) == true && mouseClicked == false && followingMouse == false
					&& mouseX > (x - hitBoxWidth/2) && mouseX < (x + hitBoxWidth/2)
					&& mouseY > (y - hitBoxHeight/2) && mouseY < (y + hitBoxHeight/2)) {
				mouseClicked = true;
				followingMouse = true;
				container.setMouseGrabbed(true);
				Mouse.setCursorPosition((int) x, (int) (Main.HEIGHT - y));
			}
			else if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) == true && mouseClicked == false && followingMouse == true) {
				mouseClicked = true;
				followingMouse = false;
				container.setMouseGrabbed(false);
				Mouse.setCursorPosition((int) x, (int) (Main.HEIGHT - y));
			}
			else if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) == false && mouseClicked == true) {
				mouseClicked = false;
			}
			
			if (followingMouse == true) {
				
				//Update milkbone position to mouse's position
				targetX = mouseX;
				targetY = mouseY;
				
				dx = targetX - x;
				dy = targetY - y;
				
				x += dx*0.2;
				y += dy*0.2;
				
				dx = 0;
				dy = 0;
				
				//Update hitbox to be directly on the milkbone
				hitBox.setCenterX(x);
				hitBox.setCenterY(y);
				
				//Check if milkbone is touching shibe
				if ((hitBox.intersects(shibe.hitBox) || hitBox.contains(shibe.hitBox)) && currentlyActive == true) {
					
					milkboneChompSound.play();
					
					currentlyActive = false;
					shibe.setChomp(true);
					shibe.setCurrentShibeSprite(5);
					
					explodeCrumbs(x, y, 5, MilkboneCrumb.ForceType.MEDIUM);
					
					mouseClicked = false;
					followingMouse = false;
					container.setMouseGrabbed(false);
					Mouse.setCursorPosition((int) x, (int) (Main.HEIGHT - y));
					
				}
				
			}
		
		}
		
		if (currentlyDead == true) {
			deadTimerElapsed = (System.nanoTime() - deadTimer) / 1000000;
			if (deadTimerElapsed > deadTimerLength) {
				currentlyDead = false;
			}
		}
		
		//Update the milkbone rebuild animation
		spriteUpdateTimerElapsed = (System.nanoTime() - spriteUpdateTimer) / 1000000;
		if (spriteUpdateTimerElapsed > spriteUpdateTimerLength && currentlyDead == false && currentlyActive == true) {
			if (currentMilkboneSprite < (milkboneSpriteSheet.getHorizontalCount() - 1)) {
				if (currentMilkboneSprite == 0)
					milkboneBuildSound.play();
				spriteUpdateTimer = System.nanoTime();
				currentMilkboneSprite++;
				//Add a pulse when it hits the last frame in the animation
				if (currentMilkboneSprite == (milkboneSpriteSheet.getHorizontalCount()-1))
					milkboneRespawnPulse.add(new ObjectPulse( x, y, milkboneSpriteSheet, (milkboneSpriteSheet.getHorizontalCount()-1) ));
			}
		}
		
		//Update the crumbs (when they're available)
		for (int i = 0; i < milkboneCrumbs.size(); i++)
			milkboneCrumbs.get(i).update(delta);
		
		//Delete the crumbs (when they're ready to be deleted)
		for (int i = 0; i < milkboneCrumbs.size(); i++)
			if (milkboneCrumbs.get(i).readyToBeDeleted == true)
				milkboneCrumbs.remove(i);
		
		//Update the broken milkbones (when they're available)
		for (int i = 0; i < milkboneBroken.size(); i++)
			milkboneBroken.get(i).update(delta);
		
		//Delete the broken milkbones (when they're ready to be deleted)
		for (int i = 0; i < milkboneBroken.size(); i++)
			if (milkboneBroken.get(i).readyToBeDeleted == true)
				milkboneBroken.remove(i);
		
		//Update the pulses (when they're available)
		for (int i = 0; i < milkboneRespawnPulse.size(); i++)
			milkboneRespawnPulse.get(i).update(delta);
		
		//Delete the pulses (when they're ready to be deleted)
		for (int i = 0; i < milkboneRespawnPulse.size(); i++)
			if (milkboneRespawnPulse.get(i).readyToBeDeleted == true)
				milkboneRespawnPulse.remove(i);
		
	}
	
	public void render(Graphics g) {
		
		//render the bone
		if (currentlyActive == true) {
			milkboneSpriteSheet.startUse();
			g.setColor(new Color(255, 255, 255, alpha));
			milkboneSpriteSheet.getSubImage(currentMilkboneSprite, 0).drawEmbedded((x - width/2), (y - height/2), width, height);
			milkboneSpriteSheet.endUse();
		}
		
		//render the crumbs (when they're available)
		for (int i = 0; i < milkboneCrumbs.size(); i++)
			if (milkboneCrumbs.get(i) != null)
				milkboneCrumbs.get(i).render(g);
		
		//render the broken bone (if it's available)
		for (int i = 0; i < milkboneBroken.size(); i++)
			if (milkboneBroken.get(i) != null)
				milkboneBroken.get(i).render(g);
		
		//render the pulse (if it's available)
		for (int i = 0; i < milkboneRespawnPulse.size(); i++)
			if (milkboneRespawnPulse.get(i) != null)
				milkboneRespawnPulse.get(i).render(g);
		
		g.setColor(new Color (255, 0, 0, 50));
		//g.fill(hitBox);
		
	}
	
}