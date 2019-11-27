package com.goibibo.libs.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.drawToBitmap
import androidx.databinding.BindingAdapter
import com.goibibo.libs.utils.BitmapUtils

class ScratchConstraintLayoutView : ConstraintLayout {
    private var mX = 0f
    private var mY = 0f
    private var mScratchBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private var mErasePath: Path? = null
    private var mTouchPath: Path? = null
    private var mBitmapPaint: Paint? = null
    var erasePaint: Paint? = null
        private set
    private var mDrawable: BitmapDrawable? = null
    private var mRevealListener: IRevealListener? = null
    private var mRevealPercent = 0f
    private var mThreadCount = 0
    private var scratchLayoutResourceId = 0

    @StringDef("revealed")
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ScratchedState {
        companion object {
            var REVEALED = "revealed"
        }
    }

    constructor(context: Context) : super(context) {
        this.init()
    }

    constructor(context: Context, set: AttributeSet?) : super(context, set) {
        this.init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init()
    }

    fun setStrokeWidth(multiplier: Int) {
        erasePaint!!.strokeWidth = multiplier.toFloat() * 12.0f
    }

    private fun init() {
        mTouchPath = Path()
        erasePaint = Paint()
        erasePaint!!.isAntiAlias = true
        erasePaint!!.isDither = true
        erasePaint!!.color = -65536
        erasePaint!!.style = Paint.Style.STROKE
        erasePaint!!.strokeJoin = Paint.Join.BEVEL
        erasePaint!!.strokeCap = Paint.Cap.ROUND
        setStrokeWidth(6)
        mErasePath = Path()
        mBitmapPaint = Paint(4)
        post {
            if (this@ScratchConstraintLayoutView.childCount > 0) {
                getChildAt(0).visibility = View.INVISIBLE
            }
        }
    }

    private fun drawScratchView() {
        if (mScratchBitmap != null) {
            mCanvas = Canvas(mScratchBitmap!!)
            val rect = Rect(0, 0, mScratchBitmap!!.width, mScratchBitmap!!.height)
            if (mDrawable != null) {
                mDrawable!!.bounds = rect
            }
            mDrawable = BitmapDrawable(context.resources, mScratchBitmap)
            if (mDrawable != null) {
                mDrawable!!.draw(mCanvas!!)
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mScratchBitmap != null) {
            canvas.drawBitmap(mScratchBitmap!!, 0.0f, 0.0f, mBitmapPaint)
            canvas.drawPath(mErasePath!!, erasePaint!!)
        }
    }

    private fun touch_start(x: Float, y: Float) {
        mErasePath!!.reset()
        mErasePath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    fun clear() {
        val bounds = layoutBounds
        var left = bounds[0]
        var top = bounds[1]
        var right = bounds[2]
        var bottom = bounds[3]
        val width = right - left
        val height = bottom - top
        val centerX = left + width / 2
        val centerY = top + height / 2
        left = centerX - width / 2
        top = centerY - height / 2
        right = left + width
        bottom = top + height
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mCanvas!!.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        checkRevealed()
        this.invalidate()
    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= 4.0f || dy >= 4.0f) {
            mErasePath!!.quadTo(mX, mY, (x + mX) / 2.0f, (y + mY) / 2.0f)
            mX = x
            mY = y
            drawPath()
        }
        mTouchPath!!.reset()
        mTouchPath!!.addCircle(mX, mY, 30.0f, Path.Direction.CW)
    }

    private fun drawPath() {
        if (mCanvas != null) {
            mErasePath!!.lineTo(mX, mY)
            mCanvas!!.drawPath(mErasePath!!, erasePaint!!)
            mTouchPath!!.reset()
            mErasePath!!.reset()
            mErasePath!!.moveTo(mX, mY)
            checkRevealed()
        }
    }

    fun reveal() {
        this.clear()
    }

    private fun touch_up() {
        drawPath()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!scratchEnabled) {
            return super.onTouchEvent(event)
        }
        val x = event.x
        val y = event.y
        when (event.action) {
            0 -> {
                touch_start(x, y)
                this.invalidate()
            }
            1 -> {
                touch_up()
                this.invalidate()
            }
            2 -> {
                touch_move(x, y)
                this.invalidate()
            }
        }
        return true
    }

    val color: Int
        get() = erasePaint!!.color

    fun setEraserMode() {
        erasePaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun setRevealListener(listener: IRevealListener?) {
        mRevealListener = listener
    }

    val isRevealed: Boolean
        get() = mRevealPercent == 1.0f

    @SuppressLint("StaticFieldLeak")
    private fun checkRevealed() {
        if (!isRevealed && mRevealListener != null) {
            val bounds = layoutBounds
            val left = bounds[0]
            val top = bounds[1]
            val width = bounds[2] - left
            val height = bounds[3] - top
            if (mThreadCount > 1) {
                Log.d("Captcha", "Count greater than 1")
                return
            }
            ++mThreadCount

            object : AsyncTask<Int?, Void?, Float>() {
                override fun doInBackground(vararg params: Int?): Float {

                    val var7: Float
                    var7 = try {
                        val left = params[0]
                        val top = params[1]
                        val width = params[2]
                        val height = params[3]
                        val croppedBitmap = Bitmap.createBitmap(mScratchBitmap!!, left!!, top!!, width!!, height!!)
                        BitmapUtils.getTransparentPixelPercent(croppedBitmap)
                    } finally {
                        mThreadCount--
                    }
                    return var7
                }

                public override fun onPostExecute(percentRevealed: Float) {
                    if (!isRevealed) {
                        val oldValue = mRevealPercent
                        mRevealPercent = percentRevealed
                        if (oldValue != percentRevealed) {
                            mRevealListener!!.onRevealPercentChangedListener(this@ScratchConstraintLayoutView, percentRevealed)
                        }
                        if (isRevealed) {
                            mRevealListener!!.onRevealed(this@ScratchConstraintLayoutView)
                        }
                    }
                }
            }.execute(*arrayOf(left, top, width, height))
        }
    }

    val layoutBounds: IntArray
        get() {
            val paddingLeft = this.paddingLeft
            val paddingTop = this.paddingTop
            val paddingRight = this.paddingRight
            val paddingBottom = this.paddingBottom
            val vwidth = this.width - paddingLeft - paddingRight
            val vheight = this.height - paddingBottom - paddingTop
            val centerX = vwidth / 2
            val centerY = vheight / 2
            val drawable = this.background
            val bounds = drawable.bounds
            var width = drawable.intrinsicWidth
            var height = drawable.intrinsicHeight
            if (width <= 0) {
                width = bounds.right - bounds.left
            }
            if (height <= 0) {
                height = bounds.bottom - bounds.top
            }
            if (height > vheight) {
                height = vheight
            }
            if (width > vwidth) {
                width = vwidth
            }
            width = vwidth
            height = vheight
            return intArrayOf(paddingLeft, paddingTop, paddingLeft + width, paddingTop + height)
        }

    interface IRevealListener {
        fun onRevealed(var1: ScratchConstraintLayoutView?)
        fun onRevealPercentChangedListener(var1: ScratchConstraintLayoutView?, var2: Float)
    }

    // region:: ScratchConstraintLayoutView
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    val scratchLayoutViewLoaded: Boolean
        get() {
            return scratchLayoutView != null
        }

    val scratchLayoutView: View?
        get() = findViewWithTag(SCRATCH_VIEW_TAG)

    var scratchEnabled = false

    @BindingAdapter(value = ["scratchViewLayoutFile", "makeScratchableImmediately"], requireAll = false)
    fun setScratchView(@LayoutRes layoutResource: Int, makeScratchableImmediately: Boolean = false) {
        scratchLayoutResourceId = layoutResource

        if (scratchLayoutViewLoaded) {
            removeView(scratchLayoutView)
        }

        val view = LayoutInflater.from(context).inflate(layoutResource, this@ScratchConstraintLayoutView, true)

        getChildAt(1).apply {
            id = View.generateViewId()
            tag = SCRATCH_VIEW_TAG
        }

        if (makeScratchableImmediately) {
            scratchEnabled = true
            postDelayed({
                loadScratchViewAsBitmap()
            }, 300)
        }
    }

    @SuppressLint("ResourceType")
    fun loadScratchViewAsBitmap() {
        scratchLayoutView?.let { scratchView ->

            mScratchBitmap = loadBitmapFromView(scratchView)
            hideScratchViewLayout()
            mDrawable = BitmapDrawable(context.resources, mScratchBitmap)
            mDrawable!!.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            setEraserMode()
            drawScratchView()
            if (this@ScratchConstraintLayoutView.childCount > 0) {
                getChildAt(0).visibility = View.VISIBLE
            }
        }
    }

    fun hideScratchViewLayout() {
        scratchLayoutView?.let {
            ConstraintSet().apply {
                clone(this@ScratchConstraintLayoutView)
                setVisibility(it.id, View.GONE)
                applyTo(this@ScratchConstraintLayoutView)
            }
        }
    }

    fun showScratchLayoutView() {
        scratchLayoutView?.let {
            ConstraintSet().apply {
                clone(this@ScratchConstraintLayoutView)
                setVisibility(it.id, View.VISIBLE)
                applyTo(this@ScratchConstraintLayoutView)
            }
        }
    }

    fun resetScratchView() {
        showScratchLayoutView()
    }

    /**
     * @param scratchedState pls use this to show already scratched view..
     */
    fun setScratchView(@ScratchedState scratchedState: String) {
        if (scratchedState == ScratchedState.REVEALED) {
            post {
                if (this@ScratchConstraintLayoutView.childCount > 0) {
                    getChildAt(0).visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadBitmapFromView(view: View): Bitmap {
        return view.drawToBitmap()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // endregion

    companion object {
        const val STROKE_WIDTH = 12.0f
        private const val TOUCH_TOLERANCE = 4.0f
        private const val SCRATCH_VIEW_TAG = "SCRATCH_VIEW_TAG"
    }
}