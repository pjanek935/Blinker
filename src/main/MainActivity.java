package main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import janowiak.blinker.R;

public class MainActivity extends Activity implements /*ConnectionRequestListener,*/ OnClickListener{

	private boolean isConnected = false;
	private EditText nameEditText;
	private Button singleButton;
	private Button multiButton;
    private ProgressDialog progressDialog;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		nameEditText = (EditText)findViewById(R.id.editText1);
		singleButton = (Button)findViewById(R.id.singleButton);
		multiButton = (Button)findViewById(R.id.multiButton);
		singleButton.setOnClickListener(this);
		multiButton.setOnClickListener(this);
		
		init();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//theClient.remiiConnectionRequestListener(this);
		
	}
	
	private void init(){
		
		
    }
	

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.singleButton){
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("roomId", 1);
			startActivity(intent);
		}else if(v.getId() == R.id.multiButton){
			String userName = nameEditText.getText() + "";
			Log.d("mine", "User name: " + userName);
			Utils.showToastAlert(MainActivity.this, "Connecting wiht name " + userName);
			Utils.userName = userName;
			
		}
	}

}
