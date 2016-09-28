package player;

public interface Element {
	public float getXInScreenSpace();
	public float getYInScreenSpace();
	public void setPositionInScreenSpace(float x, float y);
	public float getXInWorldSpace();
	public float getYInWorldSpace();
	public void setPositionInWorldSpace(float x, float y);
}
