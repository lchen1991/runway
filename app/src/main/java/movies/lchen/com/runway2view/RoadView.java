package movies.lchen.com.runway2view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Looper;
import android.view.animation.LinearInterpolator;

public class RoadView {

    private Canvas mCanvas;

    private int mWidth;
    private int mHeight;

    private int wayWidth;
    private int wayHeight;

    private int clipWidth = 50;
    private int clipheight = 50;

    private int wayLeft;
    private int wayTop;
    private int wayRight;
    private int wayBottom;

    private float degree = 45.0f;

    private LinearGradient mLineLGEdgeLeft = null;

    private LinearGradient mLineLGEdgeRight = null;

    private int[] mlineColors =
            {Color.parseColor("#1A45C7BD"),
                    Color.parseColor("#45C7BD"),
                    Color.parseColor("#1A45C7BD")};

    private LinearGradient mLineLG = null;

    private LinearGradient mLineLG0 = null;

    private LinearGradient mLineLG1 = null;

    private LinearGradient mLineLG2 = null;

    private LinearGradient mLineLG3 = null;

    private LinearGradient mLineLG4 = null;

    private LinearGradient mLineLG5 = null;

    volatile int phase = 0;

    int dashWidth = 18;
    /**
     * 路线高度
     */
    int dashHeight = 48;
    /**
     * 路线间隔
     */
    int dashGap = 60;

    private Paint paint;

    private Path mPathWay;

    private Camera camera;

    private Matrix matrixCamera;

    private ObjectAnimator animator = ObjectAnimator.ofInt(this, "phase", 0, dashHeight + dashGap);

    public RoadView(float density) {
        init(density);
    }

    private void init(float density) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPathWay = new Path();
        mPathWay.moveTo(wayWidth, 0);
        mPathWay.lineTo(0, 0);
        mPathWay.lineTo(wayWidth, wayHeight);
        mPathWay.lineTo(0, wayHeight);

        camera = new Camera();

        matrixCamera = new Matrix();

        float newZ = -density * 6;
        camera.setLocation(0, 0, newZ);

        animator.setDuration(1200);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    public void setViewSize(int width, int height) {

        mWidth = width;
        mHeight = height;

        wayWidth = width - clipWidth;
        wayHeight = height - clipheight;

        wayLeft = clipWidth;
        wayTop = clipheight;
        wayRight = wayWidth;
        wayBottom = wayHeight;

        if(mLineLGEdgeLeft == null) {
            mLineLGEdgeLeft = new LinearGradient(0, 0, 50, wayHeight,
                    mlineColors, null, Shader.TileMode.CLAMP);
        }

        if(mLineLGEdgeRight == null) {
            mLineLGEdgeRight = new LinearGradient(0, 0, 50 , wayHeight,
                    mlineColors, null, Shader.TileMode.CLAMP);
        }
    }

    @SuppressWarnings("unused")
    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void draw(Canvas canvas, int width, int height) {

        mCanvas = canvas;

        int centerX = width / 2;
        int centerY = height / 2;

        paint.reset();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        // 划背景
//        canvas.save();
//        paint.setColor(Color.TRANSPARENT);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(new RectF(0,0, wayWidth + 50, wayHeight + 50), paint);
//        canvas.restore();

        //旋转图层
//        camera.save();
//        matrixCamera.reset();
//        camera.rotateX(degree);
//        camera.getMatrix(matrixCamera);
//        camera.restore();
//        matrixCamera.preTranslate(-centerX, -centerY);
//        matrixCamera.postTranslate(centerX, centerY);

        canvas.save();
//        canvas.concat(matrixCamera);

        paint.setStrokeWidth(6);
        paint.setShader(mLineLGEdgeLeft);
        canvas.drawLine(50, 0, 50, wayHeight, paint);
        paint.setShader(mLineLGEdgeRight);
        canvas.drawLine(wayWidth, 0, wayWidth, wayHeight, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dashWidth);

        int r = wayHeight % (dashHeight + dashGap);
        int count = wayHeight / (dashHeight + dashGap);
        int dashCount = r == 0 ? count : count + 2;
        for (int i = -1; i < dashCount; i++) {
            int startX = width / 2;
            int startY = phase + (dashHeight + dashGap) * i;
            int stopX = width / 2;
            int stopY = phase + (dashHeight + dashGap) * i + dashHeight;

            //去超出部分
            if (startY < wayTop - 80) {
                if (stopY > wayTop - 80) {
                    startY = wayTop - 80;
                } else {
                    startY = wayTop;
                    stopY = wayTop;
                }
            }
            if (startY > wayHeight - 16) {
                startY = wayHeight - 16;
            }
            if (stopY > wayHeight - 16) {
                stopY = wayHeight - 16;
            }
            //划线
            if (i == -5) {
                if (mLineLG == null) {
                    mLineLG = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#0045C7BD"),
                            Color.parseColor("#0045C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG);
            } else if (i == -4) {
                if (mLineLG0 == null) {
                    mLineLG0 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#1A45C7BD"),
                            Color.parseColor("#1A45C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG0);
            } else if (i == -3) {
                if (mLineLG1 == null) {
                    mLineLG1 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#1A45C7BD"),
                            Color.parseColor("#3345C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG1);
            } else if (i == -2) {
                if (mLineLG2 == null) {
                    mLineLG2 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#3345C7BD"),
                            Color.parseColor("#3345C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG1);
            } else if (i == -1) {
                if (mLineLG3 == null) {
                    mLineLG3 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#3345C7BD"),
                            Color.parseColor("#6345C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG3);
            } else if (i == 0) {
                if (mLineLG4 == null) {
                    mLineLG4 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#6345C7BD"),
                            Color.parseColor("#E645C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG4);
            } else if (i == 1) {
                if (mLineLG5 == null) {
                    mLineLG5 = new LinearGradient(
                            startX, startY, stopX, stopY, Color.parseColor("#E645C7BD"),
                            Color.parseColor("#F245C7BD"), Shader.TileMode.CLAMP);
                }
                paint.setShader(mLineLG5);
            }
            else {
                paint.setShader(null);
                paint.setColor(Color.parseColor("#E645C7BD"));
            }
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        canvas.restore();
    }
}