package movies.lchen.com.runway2view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by chenlei on 2018/5/6.
 */

public class RoadView2 extends View {

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Path mPath;

    private LinearGradient mDashGradient;
    private LinearGradient mRoadLGEdgeLeft;
    private LinearGradient mRoadLGEdgeRight;

    private int mDashPhase;

    /**
     * 虚线段长度
     */
    private final static int DASH_ON_WIDTH = 80;

    /**
     * 线段之间空白长度
     */
    private final static int DASH_OFF_WIDTH = 40;

    private int mDashMinSize = DASH_ON_WIDTH + DASH_OFF_WIDTH;

    private PathEffect[] mPathEffects = new PathEffect[mDashMinSize];

    private boolean isRunning = false;

    private ObjectAnimator animator = ObjectAnimator.ofInt(this, "phase", 0, mDashMinSize);
    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            if (!isRunning) {
                animation.cancel();
            }
        }
    };

    private int[] mlineColors =
            {Color.parseColor("#1A45C7BD"),
                    Color.parseColor("#45C7BD"),
                    Color.parseColor("#1A45C7BD")};

    public RoadView2(Context context) {
        super(context);
        init();
    }

    public RoadView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoadView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addListener(mAnimatorListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getWidth();
        mHeight = getHeight();

//        mPath.moveTo(w / 2, 0);
//        mPath.lineTo(w / 2, h);

        if (mDashGradient == null) {
            mDashGradient = new LinearGradient(
                    0, 0, 0, h, Color.parseColor("#00000000"),
                    Color.parseColor("#45C7BD"), Shader.TileMode.CLAMP);

            mRoadLGEdgeLeft = new LinearGradient(0, 0, 0, h,
                    mlineColors, null, Shader.TileMode.CLAMP);

            mRoadLGEdgeRight = new LinearGradient(0, 0, 0, h,
                    mlineColors, null, Shader.TileMode.CLAMP);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        mPaint.setStrokeWidth(6);
        mPaint.setShader(mRoadLGEdgeLeft);
        canvas.drawLine(10, 0, 10, mHeight, mPaint);
        mPaint.setShader(mRoadLGEdgeRight);
        canvas.drawLine(mWidth - 10, 0, mWidth - 10, mHeight, mPaint);
        canvas.restore();

        canvas.save();
        mPath.quadTo(Math.round(mWidth), 0,mWidth / 2, mHeight);
//        mPath.lineTo(mWidth / 2, mHeight);
        mPaint.setStrokeWidth(30);
        mPaint.setShader(mDashGradient);
        mPaint.setPathEffect(getPathEffect());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

    }

    @SuppressWarnings("unused")
    public void setPhase(int phase) {
        this.mDashPhase = phase;
        invalidate();
    }

    private static PathEffect makeDash(float phase) {
        return new DashPathEffect(new float[]{DASH_ON_WIDTH, DASH_OFF_WIDTH}, phase);
    }

    private PathEffect getPathEffect() {
        mDashPhase %= mDashMinSize;
        PathEffect effect = mPathEffects[mDashPhase];
        if (effect == null) {
            effect = makeDash(-mDashPhase);
            mPathEffects[mDashPhase] = effect;
        }
        return effect;
    }

    public void start() {
        if (animator != null && !animator.isRunning()) {
            isRunning = true;
            animator.start();
        }
    }

    public void stop() {
        if (animator != null) {
            animator.end();
        }
    }

    public void destory() {
        try {
            if (animator != null) {
                isRunning = false;
                animator.cancel();
                animator = null;
            }
            mPathEffects = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
