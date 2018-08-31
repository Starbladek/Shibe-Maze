package Utilities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import Tweens.TextAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;

public class Text {
	
	
	
	public static final int SWAY_FAST = 10;
	public static final int SWAY_NORMAL = 4;
	public static final int SWAY_SLOW = 2;
	
	public static final int TRANSITION_IMMEDIATE = 0;
	public static final float TRANSITION_FAST = 0.5f;
	public static final int TRANSITION_NORMAL = 1;
	public static final int TRANSITION_SLOW = 3;
	
	public static final int SWAY_INTENSITY_INTENSE = 4;
	public static final int SWAY_INTENSITY_NORMAL = 1;
	public static final float SWAY_INTENSITY_MELLOW = 0.3f;
	
	public static enum BorderType {
		NO_BORDER,
		THIN_BORDER
	};
	private BorderType borderType;
	
	public static enum PlacementType {
		CORNER,
		MIDDLE
	};
	private PlacementType placementType;
	
	private String text;
	private TrueTypeFont font;
	
	private float x;
	private float y;
	
	private Color color;
	private float alpha;
	
	private boolean currentlyOver;
	
	private boolean swaying;
	private float swayIntensity;
	private int swaySpeed;
	private int swayType;
	private int sinValue;
	private int cosValue;
	
	private Rectangle hitBox;
	private float hitBoxWidth;
	private float hitBoxHeight;
	
	private TweenManager tweenManager;
	
	
	
	public Text(float x, float y, String text, TrueTypeFont font, Color color, BorderType borderType, PlacementType placementType) {
		
		this.text = text;
		this.font = font;
		
		this.x = x;
		this.y = y;
		
		this.alpha = 255;
		this.color = color;
		
		this.swayType = 0;
		this.borderType = borderType;
		this.placementType = placementType;
		
		hitBoxWidth = font.getWidth(text);
		hitBoxHeight = (float) (font.getHeight(text)*0.7);
		hitBox = new Rectangle(x - hitBoxWidth/2, y - hitBoxHeight/2, hitBoxWidth, hitBoxHeight);
		
		tweenManager = new TweenManager();
		
	}
	public Text(float x, float y, String text, TrueTypeFont font, Color color, BorderType borderType, PlacementType placementType, float alpha) {
		
		this.text = text;
		this.font = font;
		
		this.x = x;
		this.y = y;
		
		this.alpha = alpha;
		this.color = color;
		
		this.swayType = 0;
		this.borderType = borderType;
		this.placementType = placementType;
		
		hitBoxWidth = font.getWidth(text);
		hitBoxHeight = (float) (font.getHeight(text)*0.7);
		hitBox = new Rectangle(x - hitBoxWidth/2, y - hitBoxHeight/2, hitBoxWidth, hitBoxHeight);
		
		tweenManager = new TweenManager();
		
	}
	public Text(float x, float y, String text, TrueTypeFont font, Color color, BorderType borderType, PlacementType placementType, float alpha, int swayType) {
		
		this.text = text;
		this.font = font;
		
		this.x = x;
		this.y = y;
		
		this.alpha = alpha;
		this.color = color;
		
		this.swayType = swayType;
		this.borderType = borderType;
		this.placementType = placementType;
		
		hitBoxWidth = font.getWidth(text);
		hitBoxHeight = (float) (font.getHeight(text)*0.7);
		hitBox = new Rectangle(x - hitBoxWidth/2, y - hitBoxHeight/2, hitBoxWidth, hitBoxHeight);
		
		tweenManager = new TweenManager();
		
	}
	
	
	
	public float getX() { return x; }
	public void setX(float value) { x = value; }
	
	public float getY() { return y; }
	public void setY(float value) { y = value; }
	
	public float getAlpha() { return alpha; }
	public void setAlpha(float value) { alpha = value; }
	
	public String getText() { return text; }
	public void setText(String newText) { text = newText; }
	
	public boolean getSwaying() { return swaying; }
	public void setSwaying(boolean value) { swaying = value; }
	
	public float getSwayIntensity() { return swayIntensity; }
	public void setSwayIntensity(float value) { swayIntensity = value; }
	
	public int getSwaySpeed() { return swaySpeed; }
	public void setSwaySpeed(int value) { swaySpeed = value; }
	
	public Color getColor() { return color; }
	public void setColor(Color newColor) { color = newColor; }
	
	public boolean getCurrentlyOver() { return currentlyOver; }
	public void setCurrentlyOver(boolean value) { currentlyOver = value; }
	
	
	
	public void enterSway(int swaySpeed, float length, float intensity) {
		tweenManager.killTarget(this);
		swaying = true;
		this.swaySpeed = swaySpeed;
		Tween.to(this, TextAccessor.SWAY_INTENSITY, (length*1000)).target(intensity).ease(Linear.INOUT).start(tweenManager);
	}
	
	public void exitSway(float length) {
		tweenManager.killTarget(this);
		swaying = false;
		Tween.to(this, TextAccessor.SWAY_INTENSITY, (length*1000)).target(0).ease(Linear.INOUT).start(tweenManager);
	}
	
	
	
	public void update(GameContainer container, int delta) {
		
		tweenManager.update(delta);
		
		switch (placementType) {
		case CORNER:
			hitBox.setCenterX(x + (hitBoxWidth/2));
			hitBox.setCenterY(y + (hitBoxHeight/2));
			break;
		case MIDDLE:
			hitBox.setCenterX(x);
			hitBox.setCenterY(y);
			break;
		}
		
		Input input = container.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		if ( mouseX >= hitBox.getMinX() && mouseX <= hitBox.getMaxX() && mouseY >= hitBox.getMinY() && mouseY <= hitBox.getMaxY() )
			currentlyOver = true;
		else
			currentlyOver = false;
		
	}
	
	public void render(Graphics g) {
		
		//Setting the text to a new color every update is the only way I can think of to change the alpha of the text
		Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha);
		
		int width = font.getWidth(text);
		int height = font.getHeight(text);
		
		float yOffset = 0;
		float xOffset = 0;
		
		//Side to side
		if (swayType == 0) {
			cosValue += swaySpeed*2;
			if (cosValue >= 360)
				cosValue = 0;
			xOffset = (float) (swayIntensity * ( Math.sin(Math.toRadians(cosValue)) ));
		}
		//Up and down
		else if (swayType == 1) {
			sinValue += swaySpeed;
			if (sinValue >= 360)
				sinValue = 0;
			yOffset = (float) (swayIntensity * ( Math.sin(Math.toRadians(sinValue)) ));
		}
		//Do the hustle
		else if (swayType == 2) {
			cosValue += swaySpeed*2;
			if (cosValue >= 360)
				cosValue = 0;
			xOffset = (float) (swayIntensity * ( Math.sin(Math.toRadians(cosValue)) ));
			sinValue += swaySpeed;
			if (sinValue >= 360)
				sinValue = 0;
			yOffset = (float) (swayIntensity * ( Math.sin(Math.toRadians(sinValue)) ));
		}
		
		
		
		switch (placementType) {
		
		case CORNER:
			//Black borders of the text
			if (borderType == BorderType.THIN_BORDER) {
				font.drawString(x + xOffset + 1, y + yOffset, text, Color.black);
				font.drawString(x + xOffset - 1, y + yOffset, text, Color.black);
				font.drawString(x + xOffset, y + yOffset + 1, text, Color.black);
				font.drawString(x + xOffset, y + yOffset - 1, text, Color.black);
			}
			//Actual text
			font.drawString(x + xOffset, y + yOffset, text, color2);
			break;
			
		case MIDDLE:
			//Black borders of the text
			if (borderType == BorderType.THIN_BORDER) {
				font.drawString((x - width/2) + xOffset + 1, (y - height/2) + yOffset, text, Color.black);
				font.drawString((x - width/2) + xOffset - 1, (y - height/2) + yOffset, text, Color.black);
				font.drawString((x - width/2) + xOffset, (y - height/2) + yOffset + 1, text, Color.black);
				font.drawString((x - width/2) + xOffset, (y - height/2) + yOffset - 1, text, Color.black);
			}
			//Actual text
			font.drawString((x - width/2) + xOffset, (y - height/2) + yOffset, text, color2);
			break;
			
		}
		
		g.setColor(new Color(255, 0, 0, 40));
		//g.fill(hitBox);
		
	}
	
}