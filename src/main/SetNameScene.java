package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.text.Text;

import android.content.res.AssetManager;
import android.util.Log;
import janowiak.blinker.R;
import main.SceneManager.SceneType;
import menu.Button;

public class SetNameScene extends BaseScene{
	
	private ArrayList<String> cWords;
	private boolean readSuccess = false;

	@Override
	public void createScene() {
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				loadCWords();
			}
		});
		t.start();
		
		final Text enterNameText = new Text(1024/2, 500, resourcesManager.smallFont, "PRZESTAÑ SIÊ WYG£UPIAÆ I PODAJ SWOJE IMIÊ..."
				+ "PRZESTAÑ SIÊ WYG£UPIAÆ I PODAJ SWOJE IMIÊ...P", vbom);
		enterNameText.setText( resourcesManager.getResources().getString(R.string.enter_name));
		attachChildAtForeground2(enterNameText);
		
		final Text textField = new Text(1024/2, 400, ResourcesManager.getInstance().font, "QWERTYUIOPASDFGHJKLZXCVBNM", vbom);
		textField.setText("");
		attachChild(textField);
		final Text floor = new Text(textField.getX() + textField.getWidth()/2, textField.getY(), resourcesManager.font, "_", vbom);
		attachChild(floor);
		floor.registerUpdateHandler(new IUpdateHandler() {
			int counter = 0;
			@Override
			public void reset() {}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				counter ++;
				if(counter > 20){
					floor.setVisible(!floor.isVisible());
					counter = 0;
				}
				floor.setPosition(textField.getX() + textField.getWidth()/2 + floor.getWidth()/2 + 4, textField.getY());
			}
		});
		ArrayList<Button> buttons = new ArrayList<Button>();
		for(int i=0; i<26; i++){
			if(i<10){
				final Button b = new Button(100 + 90*i, 300, "letter", this) {
					@Override
					public void buttonPressed() {
						Text text = (Text)(this.getEntity());
						textField.setText(textField.getText() + text.getText().toString());
					}
				};
				buttons.add(b);
			}else if(i < 19){
				final Button b = new Button(100 + 16 + 90*(i-10), 300 - 64, "letter", this) {
					@Override
					public void buttonPressed() {
						Text text = (Text)(this.getEntity());
						textField.setText(textField.getText() + text.getText().toString());
					}
				};
				buttons.add(b);
			}else{
				final Button b = new Button(100 + 32 + 90*(i-19), 300 - 128, "letter", this) {
					@Override
					public void buttonPressed() {
						Text text = (Text)(this.getEntity());
						textField.setText(textField.getText() + text.getText().toString());
					}
				};
				buttons.add(b);
			}
			
		}
		Button deleteButton = new Button(900, 300 - 64, "<-", this) {
			@Override
			public void buttonPressed() {
				if(textField.getText().length() >= 2){
					textField.setText(textField.getText().subSequence(0, textField.getText().length()-1));
				}else if(textField.getText().length() == 1){
					textField.setText("");
				}
				
			}
		};
		Button confirmButton = new Button(1024/2, 100,  resourcesManager.getResources().getString(R.string.confirm), this) {
			@Override
			public void buttonPressed() {
				Log.d("mine", "readsucces: " + readSuccess);
				if(textField.getText().length()>=1 && cWords != null){
					String name = textField.getText().toString();
					boolean curseWord = checkWord(name);
					if(curseWord){
						enterNameText.setText( resourcesManager.getResources().getString(R.string.stop_joking_around));
					}else{
						UserData.getInstance().setUSerName(name);
						SceneManager.getInstance().loadStageSelectSceneFromScoreScene();
					}
					
				}
			}
		};
		((Text)(buttons.get(0).getEntity())).setText("Q");
		((Text)(buttons.get(1).getEntity())).setText("W");
		((Text)(buttons.get(2).getEntity())).setText("E");
		((Text)(buttons.get(3).getEntity())).setText("R");
		((Text)(buttons.get(4).getEntity())).setText("T");
		((Text)(buttons.get(5).getEntity())).setText("Y");
		((Text)(buttons.get(6).getEntity())).setText("U");
		((Text)(buttons.get(7).getEntity())).setText("I");
		((Text)(buttons.get(8).getEntity())).setText("O");
		((Text)(buttons.get(9).getEntity())).setText("P");
		((Text)(buttons.get(10).getEntity())).setText("A");
		((Text)(buttons.get(11).getEntity())).setText("S");
		((Text)(buttons.get(12).getEntity())).setText("D");
		((Text)(buttons.get(13).getEntity())).setText("F");
		((Text)(buttons.get(14).getEntity())).setText("G");
		((Text)(buttons.get(15).getEntity())).setText("H");
		((Text)(buttons.get(16).getEntity())).setText("J");
		((Text)(buttons.get(17).getEntity())).setText("K");
		((Text)(buttons.get(18).getEntity())).setText("L");
		((Text)(buttons.get(19).getEntity())).setText("Z");
		((Text)(buttons.get(20).getEntity())).setText("X");
		((Text)(buttons.get(21).getEntity())).setText("C");
		((Text)(buttons.get(22).getEntity())).setText("V");
		((Text)(buttons.get(23).getEntity())).setText("B");
		((Text)(buttons.get(24).getEntity())).setText("N");
		((Text)(buttons.get(25).getEntity())).setText("M");
		
	}
	
	public boolean checkWord(String word){
		word = word.toLowerCase();
		for(String cw : cWords){
			if(word.contains(cw)){
				Log.d("mine", "curseword: " + cw);
				return true;
			}
		}
		return false;
	}
	
	public void loadCWords(){
		cWords = new ArrayList<String>();
		try {
		     AssetManager am = ResourcesManager.getInstance().context.getAssets();
		     InputStream is = am.open("wulgaryzmy.txt");
		     BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		     String line;   
		     while ((line = br.readLine()) != null) {
		                cWords.add(new String(line));
		                Log.d("mine", line);
		     }
		     br.close() ;
		 }catch (IOException e) {
		    e.printStackTrace();           
		 }
		
		try {
		     AssetManager am = ResourcesManager.getInstance().context.getAssets();
		     InputStream is = am.open("swearWords.txt");
		     BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		     String line;   
		     while ((line = br.readLine()) != null) {
		                cWords.add(new String(line));
		                Log.d("mine", line);
		     }
		     readSuccess = true;
		     br.close() ;
		 }catch (IOException e) {
		    e.printStackTrace();           
		 }
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}
