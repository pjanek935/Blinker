package ui;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import main.ResourcesManager;

public class DialogBox {
	
	private IEntity entity;
	private boolean visible = true;
	private Sprite dialogBox;
	private Rectangle pointRectangle;
	private Text text;
	private Line line;
	
	public DialogBox(IEntity entity, String string, MyHUD hud){
		this.entity = entity;
		
		float[] co = 
				ResourcesManager.getInstance().camera.getCameraSceneCoordinatesFromSceneCoordinates(
						entity.getX(), entity.getY());
		float cameraX = co[0];
		float cameraY = co[1];
		
		pointRectangle = new Rectangle(cameraX - entity.getWidth()/2, cameraY + entity.getHeight()/2, 4, 4,
				ResourcesManager.getInstance().vbom);
		pointRectangle.setColor(Color.RED);
		hud.attachChild(pointRectangle);
		
		dialogBox = new Sprite(1024/2f + 350, 552/2f + 100, ResourcesManager.getInstance().dialog_box, ResourcesManager.getInstance().vbom);
		text = new Text(0, 0, ResourcesManager.getInstance().font, "1234567890qwertyuiopasdfghjklzxcvbnm"
				+ "QWERTYUIOPASDFGHJKLZXCVBNMêóœ³æñÊÓŒ£¯ÆÑ 1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNMêóœ³æñÊÓŒ£¯ÆÑ ",
				ResourcesManager.getInstance().vbom);
		text.setText(string);
		dialogBox.attachChild(text);
		text.setPosition(-(text.getWidth()/2 - dialogBox.getWidth()) - 4, text.getHeight()/2);
		
		
		line = new Line(pointRectangle.getX(), pointRectangle.getY(),
				dialogBox.getX() + dialogBox.getWidth()/2 - 4, dialogBox.getY() - dialogBox.getHeight()/2 + 4, ResourcesManager.getInstance().vbom);
		line.setColor(Color.RED);
		line.setLineWidth(2);
		hud.attachChild(line);
		hud.attachChild(dialogBox);
		
		dialogBox.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(isVisible()){
					updatePosition();
				}
				
			}
		});
		
	}
	
	public void change(IEntity entity, String string){
		this.entity = entity;
		
		float[] co = 
				ResourcesManager.getInstance().camera.getCameraSceneCoordinatesFromSceneCoordinates(
						entity.getX(), entity.getY());
		float cameraX = co[0];
		float cameraY = co[1];
		
		pointRectangle.setPosition(cameraX - entity.getWidth()/2, cameraY + entity.getHeight()/2);
		
		line.setPosition(pointRectangle.getX(), pointRectangle.getY(),
				dialogBox.getX() + dialogBox.getWidth()/2 - 4, dialogBox.getY() - dialogBox.getHeight()/2 + 4);
		
		text.setText(string);
		text.setPosition(-(text.getWidth()/2 - dialogBox.getWidth()) - 4, text.getHeight()/2);
	}
	
	public void updatePosition(){
		float[] co = 
				ResourcesManager.getInstance().camera.getCameraSceneCoordinatesFromSceneCoordinates(
						entity.getX(), entity.getY());
		float cameraX = co[0];
		float cameraY = co[1];
		
		pointRectangle.setPosition(cameraX - entity.getWidth()/2, cameraY + entity.getHeight()/2);
		line.setPosition(pointRectangle.getX(), pointRectangle.getY(),
				dialogBox.getX() + dialogBox.getWidth()/2 - 4, dialogBox.getY() - dialogBox.getHeight()/2 + 4);
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		dialogBox.setVisible(visible);
		line.setVisible(visible);
		text.setVisible(visible);
		pointRectangle.setVisible(visible);
	}
	
	public boolean isVisible(){
		return visible;
	}
}
