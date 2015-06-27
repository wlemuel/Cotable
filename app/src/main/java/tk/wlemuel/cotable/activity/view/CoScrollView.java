package tk.wlemuel.cotable.activity.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Johnnyz Zhang on 2015/6/26.
 */
public class CoScrollView  extends ScrollView {
    private float mLastMotionY;
    private int mInitialScrollCheckY;
    private boolean mIsMoving;
    private static final int SCROLL_CHECK_DELAY = 100;

    private ScrollDirectionListener mScrollDirectionListener;

    public CoScrollView(Context context) {
        super(context);
    }

    public CoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollDirectionListener(ScrollDirectionListener listener) {
        mScrollDirectionListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScrollDirectionListener != null) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;

            switch (action) {
                case MotionEvent.ACTION_MOVE :
                    if (mIsMoving) {
                        int yDiff = (int) (event.getY() - mLastMotionY);
                        if (yDiff < 0) {
                            mScrollDirectionListener.onScrollDown();
                        } else if (yDiff > 0) {
                            mScrollDirectionListener.onScrollUp();
                        }
                        mLastMotionY = event.getY();
                    } else {
                        mIsMoving = true;
                        mLastMotionY = event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (mIsMoving) {
                        mIsMoving = false;
                        startScrollCheck();
                    }
                    break;

                default :
                    mIsMoving = false;
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private void startScrollCheck() {
        mInitialScrollCheckY = getScrollY();
        post(mScrollTask);
    }

    private final Runnable mScrollTask = new Runnable() {
        @Override
        public void run() {
            if (mInitialScrollCheckY == getScrollY()) {
                if (mScrollDirectionListener != null) {
                    mScrollDirectionListener.onScrollCompleted();
                }
            } else {
                mInitialScrollCheckY = getScrollY();
                postDelayed(mScrollTask, SCROLL_CHECK_DELAY);
            }
        }
    };


    public boolean canScrollUp() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && canScrollVertically(-1);
    }
    public boolean canScrollDown() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && canScrollVertically(1);
    }
}

