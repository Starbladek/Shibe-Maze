package Tweens;

import Utilities.Text;
import aurelienribon.tweenengine.TweenAccessor;

public class TextAccessor implements TweenAccessor<Text> {
	
	public static final int POSITION_X = 0;
	public static final int POSITION_Y = 1;
	public static final int POSITION_XY = 2;
	public static final int ALPHA = 3;
	public static final int SWAY_INTENSITY = 4;
	
	@Override
	public int getValues(Text target, int tweenType, float[] returnValues) {
		
		switch(tweenType) {
		case POSITION_X:
			returnValues[0] = target.getX();
			return 1;
		case POSITION_Y:
			returnValues[0] = target.getY();
			return 1;
		case POSITION_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		case ALPHA:
			returnValues[0] = target.getAlpha();
			return 1;
		case SWAY_INTENSITY:
			returnValues[0] = target.getSwayIntensity();
			return 1;
		default:
			assert false;
			return -1;
		}
		
	}

	@Override
	public void setValues(Text target, int tweenType, float[] newValues) {
		
		switch(tweenType) {
		case POSITION_X:
			target.setX(newValues[0]);
			break;
		case POSITION_Y:
			target.setY(newValues[0]);
			break;
		case POSITION_XY:
			target.setX(newValues[0]);
			target.setY(newValues[1]);
			break;
		case ALPHA:
			target.setAlpha(newValues[0]);
			break;
		case SWAY_INTENSITY:
			target.setSwayIntensity(newValues[0]);
			break;
		default:
			assert false;
		}
		
	}
	
}