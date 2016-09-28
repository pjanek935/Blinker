package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.andengine.entity.text.Text;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import janowiak.blinker.R;

public class DataBaseConnection {
	
	public class ScoreRecord{
		public String name;
		public int score;
		public ScoreRecord(String name, int score){
			this.name = name;
			this.score = score;
		}
	}
	
	public class CustomComparator implements Comparator<ScoreRecord> {
	    @Override
	    public int compare(ScoreRecord o1, ScoreRecord o2) {
	        if(o1.score > o2.score){
	        	return -1;
	        }else if(o1.score < o2.score){
	        	return 1;
	        }else{
	        	return 0;
	        }
	    }
	}
	
	public int getRecords(ArrayList<ScoreRecord> records, Text errorTextField){
		try {
			URL url;
			url = new URL("http://janki.y0.pl/highscores.php");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			StringBuilder sb = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String json;
			while((json = bufferedReader.readLine())!= null){
	            sb.append(json+"\n");
			}
			
			JSONArray array = new JSONArray(sb.toString());
			for(int i=0; i<array.length(); i++){
				String name = array.getJSONObject(i).getString("name");
				String score = array.getJSONObject(i).getString("score");
				int scoreInt = Integer.parseInt(score);
				records.add(new ScoreRecord(name, scoreInt));
			}
			Collections.sort(records, new CustomComparator());
			return 1;
		} catch (MalformedURLException e) {
			errorTextField.setText("b³¹d URL");
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			errorTextField.setText(ResourcesManager.getInstance().getResources().getString(R.string.database_error));
			e.printStackTrace();
			return -1;
		} catch (JSONException e) {
			errorTextField.setText("b³¹d JSON");
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public void addScore(String name, int score){
		HTTPURLConnection service = new HTTPURLConnection();
		HashMap postDataParams=new HashMap<String, String>();
		postDataParams.put("name", name);
		postDataParams.put("score", Integer.toString(score));
		String response = service.ServerData("http://janki.y0.pl/addrecord.php", postDataParams);
		Log.d("mine", "Response: " + response);
	}

}
