package tk.wlemuel.cotable.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ViewUtils
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc ViewUtils
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public final class ViewUtils {

    public static float a(TextView tv) {
        float f = 0.0F;
        if (tv != null) {
            float f1 = 0.0f;
            CharSequence text = tv.getText();
            if (!TextUtils.isEmpty(text)) {
                f1 = tv.getPaint().measureText(text.toString());
            }
            int i = tv.getPaddingLeft();
            int j = tv.getPaddingRight();
            f = f1 + (float) (j + i);
        }
        return f;
    }

    public static int a(Context context) {
        return (int) TypedValue.applyDimension(1, 40F, context.getResources()
                .getDisplayMetrics());
    }

    public static void a(Activity activity, int[] ai) {
        if (ai != null && activity != null) {
            int size = ai.length;
            for (int i = 0; i < size; i++) {
                View view = activity.findViewById(ai[i]);
                if (view != null)
                    view.setOnClickListener((View.OnClickListener) activity);
            }
        }
    }

    public static void a(Dialog dialog, int[] ai) {
        if (ai != null && dialog != null) {
            int size = ai.length;
            for (int j = 0; j < size; j++) {
                View view = dialog.findViewById(ai[j]);
                if (view != null)
                    view.setOnClickListener((View.OnClickListener) dialog);
            }
        }
    }

    public static void a(View view) {
        if (view != null && (view.getParent() instanceof ViewGroup))
            ((ViewGroup) view.getParent()).removeView(view);
    }

    public static void a(View view, int i) {
        if (view != null)
            view.setVisibility(i);
    }

    public static void a(View view, View.OnClickListener listener, int[] ai) {
        if (ai != null && view != null) {
            int size = ai.length;
            for (int j = 0; j < size; j++) {
                View view1 = view.findViewById(ai[j]);
                if (view1 != null)
                    view1.setOnClickListener(listener);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap b(View view) {
        Bitmap bitmap = null;
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0),
                View.MeasureSpec.makeMeasureSpec(0, 0));
        int i = view.getMeasuredWidth();
        int j = view.getMeasuredHeight();
        view.layout(0, 0, i, j);
        view.setDrawingCacheEnabled(false);
        view.setWillNotCacheDrawing(true);
        view.setDrawingCacheEnabled(false);
        view.setWillNotCacheDrawing(true);
        if (i > 0 && j > 0) {
            bitmap = Bitmap.createBitmap(i, j,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            if (Build.VERSION.SDK_INT >= 11)
                view.setLayerType(1, null);
            view.draw(canvas);
        }
        return bitmap;
    }
}
