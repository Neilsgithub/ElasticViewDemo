package com.example.elasticlistviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView listView;
	private String [] arr = {"a","b","c","d","e","f","g","h","i","j","k"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.imageView1).setOnTouchListener(new BounceTouchListener(this));
	}

	
}
