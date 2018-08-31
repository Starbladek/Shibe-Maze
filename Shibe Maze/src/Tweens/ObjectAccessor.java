package Tweens;

import Entities.Object;
import aurelienribon.tweenengine.TweenAccessor;

public class ObjectAccessor implements TweenAccessor<Object> {
	
	public static final int ALPHA = 0;
	public static final int POSITION_XY = 1;

	@Override
	public int getValues(Object target, int tweenType, float[] returnValues) {
		
		switch(tweenType) {
		case ALPHA:
			returnValues[0] = target.getAlpha();
			return 1;
		case POSITION_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		default:
			assert false;
			return -1;
		}
		
	}

	@Override
	public void setValues(Object target, int tweenType, float[] newValues) {
		
		switch(tweenType) {
		case ALPHA:
			target.setAlpha(newValues[0]);
			break;
		case POSITION_XY:
			target.setX(newValues[0]);
			target.setY(newValues[1]);
			break;
		default:
			assert false;
		}
		
	}
	
}