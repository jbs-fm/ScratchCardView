package com.goibiboutils.scratchcardview.demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.goibibo.libs.views.ScratchConstraintLayoutView
import kotlinx.android.synthetic.main.activity_constraint_scratch.*

class ConstraintLayoutScratchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(R.layout.activity_constraint_scratch)
        val scratchRelativeLayoutView = findViewById<ScratchConstraintLayoutView>(R.id.scratch_card)
//        scratchRelativeLayoutView.setStrokeWidth(20)


//        container.setTransitionListener(object : MotionLayout.TransitionListener {
//            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//
//            }
//
//            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//
//            }
//
//            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//
//            }
//
//            override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
//                if (currentId == R.id.move) {
//                    scratchRelativeLayoutView.loadScratchViewAsBitmap()
//                    scratchRelativeLayoutView.scratchEnabled = true
//                }
//
//                if (currentId == R.id.start) {
//                    scratchRelativeLayoutView.scratchEnabled = false
//                    scratchRelativeLayoutView.resetScratchView()
//                }
//            }
//        })


        /**
         * Using Inflated View
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
        scratchRelativeLayoutView.setScratchView(R.layout.lyt_scratch, false)
        scratchRelativeLayoutView.setRevealListener(object : ScratchConstraintLayoutView.IRevealListener {
            override fun onRevealed(tv: ScratchConstraintLayoutView?) { // on reveal
            }

            override fun onRevealPercentChangedListener(siv: ScratchConstraintLayoutView?, percent: Float) { // on percent change
            }
        })
    }
}