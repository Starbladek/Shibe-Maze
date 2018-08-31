package Entities;

public abstract class Object {
	
	
	
	protected float x;
	protected float y;
	
	protected float dx;
	protected float dy;
	
	protected int width;
	protected int height;
	
	protected float alpha;
	
	protected float rotation;
	
	protected boolean readyToBeDeleted;
	
	//To be used for velocity calculating:
	protected float x1;
	protected float y1;
	protected float velocity;
	
	
	
	public Object(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getDistanceTo(Object obj) {
		
		float dist;
		
		float e1x = x;
		float e1y = y;
		
		float e2x = obj.x;
		float e2y = obj.y;
		
		float dx = e1x - e2x;
		float dy = e1y - e2y;
		
		dist = (float) Math.sqrt(dx*dx + dy*dy);
		
		return dist;
		
	}
	
	public float getAlpha() { return alpha; }
	public void setAlpha(float value) { alpha = value; }
	
	public float getX() { return x; }
	public void setX(float value) { x = value; }
	
	public float getY() { return y; }
	public void setY(float value) { y = value; }
	
	public int getWidth() { return width; }
	public void setWidth(int value) { width = value; }
	
	public int getHeight() { return height; }
	public void setHeight(int value) { height = value; }
	
	public boolean getReadyToBeDeleted() { return readyToBeDeleted; }
	public void setReadyToBeDeleted(boolean value) { readyToBeDeleted = value; }
	
	public float getXVelocity() {
		float dist = x - x1;
		return dist;
	}
	
	public float getYVelocity() {
		float dist = y - y1;
		return dist;
	}
	
	public float getXYVelocity() {
		float dx = x - x1;
		float dy = y - y1;
		float dist = (float) Math.sqrt(dx*dx + dy*dy);
		return dist;
	}
	
}