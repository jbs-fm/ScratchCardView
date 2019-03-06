package com.goibiboutils.scratchcardview.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.goibibo.libs.views.ScratchRelativeLayoutView;

public class RelativeLayoutScratchActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    setContentView(R.layout.activity_rl_scratch);

    ScratchRelativeLayoutView scratchRelativeLayoutView = findViewById(R.id.scratch_card);
    scratchRelativeLayoutView.setStrokeWidth(20);


    /**
     Using Inflated View
     */
    /*final View scratchView = inflater.inflate(R.layout.lyt_scratch, scratchRelativeLayoutView, true);
    scratchRelativeLayoutView.setScratchView(scratchView, scratchRelativeLayoutView);*/

    /**
     * Opening in already revealed state
     */
    //scratchRelativeLayoutView.setScratchView(ScratchRelativeLayoutView.ScratchedState.REVEALED);

    /**
     * Using Raw View
     */
    scratchRelativeLayoutView.setScratchView(R.layout.lyt_scratch);


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
