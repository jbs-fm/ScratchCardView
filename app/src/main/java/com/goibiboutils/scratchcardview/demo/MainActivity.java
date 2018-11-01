package com.goibiboutils.scratchcardview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


  }

  public void onTextViewDemoClick(View v) {
    startActivity(new Intent(this, DemoClothingActivity.class));
  }

  public void onImageViewDemoClick(View v) {
    startActivity(new Intent(this, CaptchaActivity.class));
  }

}
