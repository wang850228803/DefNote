package zinus.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private ImageButton addBtn;
	private ImageButton setBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addBtn = (ImageButton) findViewById(R.id.addBtn);
		setBtn = (ImageButton) findViewById(R.id.settingBtn);
		
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, SearchActivity.class));
			}
		});
		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SettingActivity.class));
			}
			
		});
	}
	
}
