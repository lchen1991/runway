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

    private float mRoalOffset;

    private float mRoalOffsetLeft;

    private float mRoalOffsetRight;

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
            animator2.start();
            animator3.start();
            animator4.start();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animator2.end();
            animator3.end();
            animator4.end();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            animator2.cancel();
            animator3.cancel();
            animator4.cancel();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            if (!isRunning) {
                animation.cancel();
                animator2.cancel();
                animator3.cancel();
                animator4.cancel();
            }
        }
    };

    private ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "roalOffset", 0.4f, 0.7f, 0.4f);

    private ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "roalOffsetLeft", 0.1f, 0.4f, 0.1f);

    private ObjectAnimator animator4 = ObjectAnimator.ofFloat(this, "roalOffsetRight", 0.7f, 1, 0.7f);

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

        mPath = new Path();

        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addListener(mAnimatorListener);

        animator2.setDuration(2 * 60 * 1000);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        animator3.setDuration(2 * 60 * 1000);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setRepeatCount(ValueAnimator.INFINITE);

        animator4.setDuration(2 * 60 * 1000);
        animator4.setInterpolator(new LinearInterpolator());
        animator4.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getWidth();
        mHeight = getHeight();

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

        mPaint.reset();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);

        canvas.save();
        mPath.reset();
        mPath.moveTo(mWidth * mRoalOffsetLeft, 0);
        mPath.quadTo(mWidth / 5, mHeight * 0.2f, mWidth / 5, mHeight);
        mPaint.setShader(mRoadLGEdgeLeft);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        canvas.save();
        mPath.reset();
        mPath.moveTo(mWidth * mRoalOffsetRight, 0);
        mPath.quadTo((mWidth - (mWidth / 5)), mHeight * 0.2f, (mWidth - (mWidth / 5)), mHeight);
        mPaint.setShader(mRoadLGEdgeRight);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        canvas.save();
        mPath.reset();
        mPath.moveTo(mWidth * mRoalOffset, 0);
        mPath.quadTo(mWidth / 2, mHeight * 0.2f, mWidth / 2, mHeight);
        mPaint.setStrokeWidth(20);
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

    @SuppressWarnings("unused")
    public void setRoalOffset(float roalOffset) {
        this.mRoalOffset = roalOffset;
    }

    @SuppressWarnings("unused")
    public void setRoalOffsetLeft(float roalOffsetLeft) {
        this.mRoalOffsetLeft = roalOffsetLeft;
    }

    @SuppressWarnings("unused")
    public void setRoalOffsetRight(float roalOffsetRight) {
        this.mRoalOffsetRight = roalOffsetRight;
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
