package com.goibiboutils.scratchcardview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.goibibo.libs.views.ScratchRelativeLayoutView;

public class RelativeLayoutScratchActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rl_scratch);
    ScratchRelativeLayoutView scratchImageView = findViewById(R.id.sample_image);
    scratchImageView.setStrokeWidth(20);

    scratchImageView.setRevealListener(new ScratchRelativeLayoutView.IRevealListener() {
      @Override
      public void onRevealed(ScratchRelativeLayoutView tv) {
        // on reveal
      }

      @Override
      public void onRevealPercentChangedListener(ScratchRelativeLayoutView siv, float percent) {
        // on percent change
      }
    });
  }
}
