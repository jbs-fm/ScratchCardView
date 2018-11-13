package com.goibiboutils.scratchcardview.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goibibo.libs.views.ScratchRelativeLayoutView;

public class RelativeLayoutScratchActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    setContentView(R.layout.activity_rl_scratch);

    ScratchRelativeLayoutView scratchRelativeLayoutView = findViewById(R.id.lytScratch);
    scratchRelativeLayoutView.setStrokeWidth(20);

    final RelativeLayout parent = findViewById(R.id.parent);

    final View scratchView = inflater.inflate(R.layout.lyt_scratch, parent, true);
    scratchRelativeLayoutView.setScratchView(scratchView);
    scratchView.bringToFront();

    final LinearLayout lytHidden = findViewById(R.id.lytHidden);
    ((TextView) lytHidden.findViewById(R.id.txt)).setText("123434524");

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        parent.removeView(parent.getChildAt(1));
      }
    }, 100);

    scratchRelativeLayoutView.setRevealListener(new ScratchRelativeLayoutView.IRevealListener() {
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
