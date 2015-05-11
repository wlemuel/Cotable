package tk.wlemuel.cotable.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * FixedRecycleView
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc FixedRecycleView
 * @created 2015/05/11
 * @updated 2015/05/11
 */
public class FixedRecyclerView extends RecyclerView {
    public FixedRecyclerView(Context context) {
        super(context);

    }

    public FixedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);

    }
}