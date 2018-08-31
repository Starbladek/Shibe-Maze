package Tweens;

import LevelStuff.Level;
import aurelienribon.tweenengine.TweenAccessor;

public class LevelAccessor implements TweenAccessor<Level> {
	
	public static final int SCALE = 0;
	public static final int ALPHA = 1;

	@Override
	public int getValues(Level target, int tweenType, float[] returnValues) {
		
		switch(tweenType) {
		case SCALE:
			returnValues[0] = target.getScale();
			return 1;
		case ALPHA:
			returnValues[0] = target.getAlpha();
			return 1;
		default:
			assert false;
			return -1;
		}
		
	}

	@Override
	public void setValues(Level target, int tweenType, float[] newValues) {
		
		switch(tweenType) {
		case SCALE:
			target.setScale(newValues[0]);
			break;
		case ALPHA:
			target.setAlpha(newValues[0]);
			break;
		default:
			assert false;
		}
		
	}
	
}