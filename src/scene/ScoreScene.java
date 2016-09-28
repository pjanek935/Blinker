package scene;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import janowiak.blinker.R;
import main.DataBaseConnection;
import main.DataBaseConnection.ScoreRecord;
import main.UserData;
import scene.SceneManager.SceneType;

public class ScoreScene extends BaseScene{
	
	
	
	private Text loadingScoreText;

	@Override
	public void createScene() {
		Line line = new Line(200, 552, 200, 0, vbom);
		line.setLineWidth(4);
		line.setColor(Color.WHITE);
		attachChildAtForeground2(line);
		
		Line line2 = new Line(1024 - 200, 552, 1024 - 200, 0, vbom);
		line2.setLineWidth(4);
		line2.setColor(Color.WHITE);
		attachChildAtForeground2(line2);
		
		Text nameText = new Text(350, 520, resourcesManager.font,  resourcesManager.getResources().getString(R.string.name), vbom);
		attachChildAtForeground2(nameText);
		
		Text scoreText = new Text(1024 - 350, 520, resourcesManager.font,  resourcesManager.getResources().getString(R.string.score), vbom);
		attachChildAtForeground2(scoreText);
		
		Line line3 = new Line(200, 500, 1024 - 200, 500, vbom);
		line3.setLineWidth(4);
		line3.setColor(Color.WHITE);
		attachChildAtForeground2(line3);
		
		for(int i=0; i<9; i++){
			Line tmpLine = new Line(200, 450 - i*50, 1024 - 200, 450 - i*50, vbom);
			tmpLine.setLineWidth(1);
			tmpLine.setColor(Color.WHITE);
			attachChildAtForeground2(tmpLine);
		}
		
		loadingScoreText = new Text(1024/2, 552/2, resourcesManager.smallFont, "qwertyuiopasdfghjklzxcvbnmQWERTYUIOP"
				+ "ASDFGHJKLZXCVBNM", vbom);
		loadingScoreText.setText( resourcesManager.getResources().getString(R.string.loading_scores));
		attachChildAtForeground2(loadingScoreText);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<ScoreRecord> records = new ArrayList<ScoreRecord>();
				DataBaseConnection dbConnection = new DataBaseConnection();
				int error = dbConnection.getRecords(records, loadingScoreText);
				
				if(error == -1){
					return;
				}
				
				int totalScore = 0;
				for(int i=0; i<5; i++){
					totalScore += UserData.getInstance().getBestStageScore(i);
				}
				String userName = UserData.getInstance().getUserName();
				boolean sameRecordExist = false;
				for(ScoreRecord r : records){
					if(r.name.equals(userName) && r.score == totalScore){
						sameRecordExist = true;
					}
				}
				if(records.size() < 10 && totalScore > 0 && !sameRecordExist){
					dbConnection.addScore(UserData.getInstance().getUserName(), totalScore);
					records.clear();
					records = new ArrayList<DataBaseConnection.ScoreRecord>();
					dbConnection.getRecords(records, loadingScoreText);
				}else{
					if(totalScore > records.get(records.size()-1).score && !sameRecordExist){
						dbConnection.addScore(UserData.getInstance().getUserName(), totalScore);
						records.clear();
						records = new ArrayList<DataBaseConnection.ScoreRecord>();
						dbConnection.getRecords(records, loadingScoreText);
					}
				}
					
				for(int i=0; i<records.size(); i++){
					final Text nText = new Text(0, 470 - 50*i, resourcesManager.smallFont, records.get(i).name, vbom);
					nText.registerUpdateHandler(new IUpdateHandler() {
						@Override
						public void reset() {}
						@Override
						public void onUpdate(float pSecondsElapsed) {
							if(nText.getX() < 350){
								nText.setX(nText.getX() + 16);
							}else{
								nText.unregisterUpdateHandler(this);
							}
						}
					});
					attachChildAtForeground2(nText);
					
					final Text sText = new Text(1024, 470 - 50*i, resourcesManager.smallFont, ""+records.get(i).score, vbom);
					sText.registerUpdateHandler(new IUpdateHandler() {
						@Override
						public void reset() {}
						@Override
						public void onUpdate(float pSecondsElapsed) {
							if(sText.getX() > 1024 - 350){
								sText.setX(sText.getX() - 16);
							}else{
								sText.unregisterUpdateHandler(this);
							}
						}
					});
					attachChildAtForeground2(sText);
					
				}
				loadingScoreText.setVisible(false);
					
			}
		});
		
		t.start();
		
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadStageSelectSceneFromScoreScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SCORE;
	}

	@Override
	public void disposeScene() {
		
	}

}
