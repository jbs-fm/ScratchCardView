package com.goibiboutils.scratchcardview.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.goibibo.libs.views.ScratchConstraintLayoutView;

public class ConstraintLayoutScratchActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    setContentView(R.layout.activity_constraint_scratch);

    ScratchConstraintLayoutView scratchRelativeLayoutView = findViewById(R.id.scratch_card);
    scratchRelativeLayoutView.setStrokeWidth(20);


    /**
     Using Inflated View
     */
    /*final View scratchView = inflater.inflate(R.layout.lyt_scratch, scratchRelativeLayoutView, true);
    scratchRelativeLayoutView.setScratchView(scratchView, scratchRelativeLayoutView);*/

    /**
     * Opening in already revealed state
     */
    //scratchRelativeLayoutView.setScratchView(ScratchConstraintLayoutView.ScratchedState.REVEALED);

    /**
     * Using Raw View
     */
    scratchRelativeLayoutView.setScratchView(R.layout.lyt_scratch);


    scratchRelativeLayoutView.setRevealListener(new ScratchConstraintLayoutView.IRevealListener() {
      @Override
      public void onRevealed(ScratchConstraintLayoutView tv) {
        // on reveal
      }

      @Override
      public void onRevealPercentChangedListener(ScratchConstraintLayoutView siv, float percent) {
        // on percent change
      }
    });
  }

}
