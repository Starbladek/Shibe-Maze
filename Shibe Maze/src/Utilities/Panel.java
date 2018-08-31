package Utilities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Panel {
	
	
	
	public static final int FAST_SPEED = 25;
	public static final int NORMAL_SPEED = 50;
	public static final int SLOW_SPEED = 100;
	
	public static final int THIN_BORDER = 50;
	public static final int NORMAL_BORDER = 100;
	public static final int THICK_BORDER = 200;
	
	public static final int ABRASIVE_ANIMATION = 0;
	public static final int SMOOTH_ANIMATION = 2;
	public static final int TV_ANIMATION = 4;
	
	
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	private int borderWidth;
	private int animationType;
	
	private Rectangle rect1;
	private Rectangle rect2;
	
	private boolean opening;
	private boolean closing;
	private boolean opened;
	private boolean closed;	//This is the readyToBeDeleted variable, but for the sake of theming, I named it closed
	
	private float[] panelWidthAbrasiveStages = { 0.05f, 0.1f, 0.3f, 0.9f, 1f, 1.1f, 1.15f, 1.1f, 1f };
	private float[] panelHeightAbrasiveStages = { 0.05f, 0.4f, 0.9f, 1f, 0.2f, 0.15f, 0.2f, 0.6f, 1f };
	
	private float[] panelWidthSmoothStages = { 0.05f, 0.1f, 0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 0.95f, 1f };
	private float[] panelHeightSmoothStages = { 0.05f, 0.1f, 0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 0.95f, 1f };
	
	private float[] panelWidthTVStages = { 0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.07f, 0.12f, 0.22f, 0.4f, 0.7f, 0.9f, 0.95f, 0.99f, 1f };
	private float[] panelHeightTVStages = { 0.1f, 0.5f, 0.9f, 0.95f, 0.99f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
	
	private float[][] animationChoices = new float[][] {
			panelWidthAbrasiveStages,
			panelHeightAbrasiveStages,
			panelWidthSmoothStages,
			panelHeightSmoothStages,
			panelWidthTVStages,
			panelHeightTVStages
	};
	
	private int currentStage;
	
	private long animationTimer;
	private int frameLength;
	
	
	
	public Panel(float x, float y, float width, float height, int borderWidth, int animationType, int speed) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.borderWidth = borderWidth/2;
		this.animationType = animationType;
		this.frameLength = speed;
		
		//The outer rectangle border
		rect1 = new Rectangle(0, 0, 0, 0);
		//The inner rectangle filling
		rect2 = new Rectangle(0, 0, 0, 0);
		
		openPanel();
		
	}
	
	public void openPanel() {
		if (opening == true || closing == true)
			return;
		opening = true;
		animationTimer = System.nanoTime();
	}
	public void closePanel() {
		if (opening == true || closing == true)
			return;
		opened = false;
		closing = true;
	}
	
	public boolean getOpened() { return opened; }
	public boolean getClosed() { return closed; }
	
	public void update() {
		
		if (opening) {
			long elapsed = (System.nanoTime() - animationTimer) / 1000000;
			if (elapsed > frameLength) {
				
				currentStage++;
				animationTimer = System.nanoTime();
				
				rect1.setWidth(width*animationChoices[animationType][currentStage]);
				rect1.setX(x + (width-rect1.getWidth()) / 2 );
				
				rect1.setHeight(height*animationChoices[animationType+1][currentStage]);
				rect1.setY(y + (height-rect1.getHeight()) / 2 );
				
				rect2.setWidth((width-borderWidth)*animationChoices[animationType][currentStage]);
				rect2.setX(x + (width-rect2.getWidth()) / 2 );
				
				rect2.setHeight((height-borderWidth)*animationChoices[animationType+1][currentStage]);
				rect2.setY(y + (height-rect2.getHeight()) / 2 );
				
				if (currentStage >= animationChoices[animationType].length - 1) {
					opening = false;
					opened = true;
				}
				
			}
		}
		
		if (closing) {
			
			long elapsed = (System.nanoTime() - animationTimer) / 1000000;
			if (elapsed > frameLength) {
				
				currentStage--;
				animationTimer = System.nanoTime();
				
				rect1.setWidth(width*animationChoices[animationType][currentStage]);
				rect1.setX(x + (width-rect1.getWidth()) / 2 );
				
				rect1.setHeight(height*animationChoices[animationType+1][currentStage]);
				rect1.setY(y + (height-rect1.getHeight()) / 2 );
				
				rect2.setWidth((width-borderWidth)*animationChoices[animationType][currentStage]);
				rect2.setX(x + (width-rect2.getWidth()) / 2 );
				
				rect2.setHeight((height-borderWidth)*animationChoices[animationType+1][currentStage]);
				rect2.setY(y + (height-rect2.getHeight()) / 2 );
				
				if (currentStage <= 0) {
					closing = false;
					closed = true;
				}
				
			}
			
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.gray);
		g.fill(rect1);
		g.setColor(Color.white);
		g.fill(rect2);
	}
	
}