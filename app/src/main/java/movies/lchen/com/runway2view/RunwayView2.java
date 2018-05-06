package movies.lchen.com.runway2view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AnimationUtils;

/**
 * Created on 2018/05/05
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class RunwayView2 extends SurfaceView implements SurfaceHolder.Callback {

    private float DEFAULT_SCALE = 1.45f;
    /**
     * 默认宽度
     */
    private int DEFAULT_WIDTH = 834;
    /**
     * 默认高度
     */
    private int DEFAULT_HEIGHT = 574;

    private int mWidth;

    private int mHeight;

    private DrawThread mDrawThread;
    private RoadView mRoadView;

    public RunwayView2(Context context) {
        super(context);
        init();
    }

    public RunwayView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RunwayView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRoadView = new RoadView(getResources().getDisplayMetrics().density);
        mDrawThread = new DrawThread();
        SurfaceHolder surfaceHolder = getHolder();
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        surfaceHolder.addCallback(this);
        mDrawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (mDrawThread) {
            mDrawThread.mSurface = holder;
            mDrawThread.notify();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (mDrawThread) {
            mDrawThread.mSurface = holder;
            mDrawThread.notify();
            while (mDrawThread.mActive) {
                try {
                    mDrawThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        holder.removeCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (heightSpecSize * DEFAULT_SCALE), heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) (widthSpecSize / DEFAULT_SCALE));
        }
    }

    public void prepare() {
        synchronized (mDrawThread) {
            mDrawThread.mRunning = true;
            mDrawThread.notify();
        }
    }

    public void start() {
        if(mRoadView != null) {
        }
    }

    public void pause() {
        synchronized (mDrawThread) {
            mDrawThread.mRunning = false;
            mDrawThread.notify();
        }
    }

    public void destroy() {
        synchronized (mDrawThread) {
            mDrawThread.mQuit = true;
            mDrawThread.notify();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 绘制线程
     */
    private class DrawThread extends Thread {

        private SurfaceHolder mSurface;
        private boolean mRunning, mActive, mQuit;
        private Canvas mCanvas;

        @Override
        public void run() {
            super.run();
            while (true) {
                synchronized (this) {
                    if (!processDrawThreadState()) {
                        return;
                    }
                    //动画开始时间
                    final long startTime = AnimationUtils.currentAnimationTimeMillis();
                    //处理画布并进行绘制
                    processDrawCanvas(mCanvas);
                    //绘制时间
                    final long drawTime = AnimationUtils.currentAnimationTimeMillis() - startTime;
                    //处理一下线程需要的睡眠时间
                    processDrawThreadSleep(drawTime);
                }
            }
        }

        private boolean processDrawThreadState() {
            while (mSurface == null || !mRunning) {
                if (mActive) {
                    mActive = false;
                    notify();
                }
                if (mQuit) {
                    return false;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!mActive) {
                mActive = true;
                notify();
            }
            return true;
        }

        private void processDrawCanvas(Canvas mCanvas) {
            try {
                mCanvas = mSurface.lockCanvas();
                if (mCanvas != null) {
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    drawSurface(mCanvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    mSurface.unlockCanvasAndPost(mCanvas);
                }
            }
        }

        private void drawSurface(Canvas canvas) {
            if (mWidth == 0 || mHeight == 0) {
                return;
            }
            //绘制
            if (mRoadView != null) {
                mRoadView.setViewSize(mWidth, mHeight);
                mRoadView.draw(canvas, mWidth, mHeight);
            }
        }

        private void processDrawThreadSleep(long drawTime) {

            final long needSleepTime = 16 - drawTime;

            if (needSleepTime > 0) {
                try {
                    Thread.sleep(needSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
