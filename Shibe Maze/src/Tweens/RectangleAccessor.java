package Tweens;

import org.newdawn.slick.geom.Rectangle;

import aurelienribon.tweenengine.TweenAccessor;

public class RectangleAccessor implements TweenAccessor<Rectangle> {
	
	public static final int POSITION = 0;
	
	@Override
	public int getValues(Rectangle target, int tweenType, float[] returnValues) {
		
		switch(tweenType) {
		case POSITION:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		default:
			assert false;
			return -1;
		}
		
	}

	@Override
	public void setValues(Rectangle target, int tweenType, float[] newValues) {
		
		switch(tweenType) {
		case POSITION:
			target.setX(newValues[0]);
			target.setY(newValues[1]);
			break;
		default:
			assert false;
		}
		
	}
	
}