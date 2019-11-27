package com.goibiboutils.scratchcardview.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

  public void onRelativeLayoutDemoClick(View view) {
    startActivity(new Intent(this, RelativeLayoutScratchActivity.class));
  }

  public void onConstraintLayoutDemoClick(View view) {
    startActivity(new Intent(this, ConstraintLayoutScratchActivity.class));
  }
}
