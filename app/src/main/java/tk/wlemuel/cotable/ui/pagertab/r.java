package tk.wlemuel.cotable.ui.pagertab;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import tk.wlemuel.cotable.utils.ViewUtils;

/**
 * r
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc r
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public final class r {

    public r() {
        // a(a);
    }

    static final void a(SparseArray sparsearray) {
        sparsearray.put(0, new q(0));
        q q1 = new q(1);
        q q2 = new q(4);
        q q3 = new q(5);
        q q4 = new q(11);
        q q5 = new q(7);
        q q6 = new q(8);
        q q7 = new q(10);
        q q8 = new q(12);
        q q9 = new q(13);
        q q10 = new q(14);
        sparsearray.put(1, q1);
        sparsearray.put(4, q2);
        sparsearray.put(5, q3);
        sparsearray.put(11, q4);
        sparsearray.put(12, q8);
        sparsearray.put(7, q5);
        sparsearray.put(8, q6);
        sparsearray.put(10, q7);
        sparsearray.put(13, q9);
        sparsearray.put(14, q10);
        q1.a(q2);
        q1.a(q3);
        q1.a(q4);
        q1.a(q8);
        q1.a(q10);
        q4.a(q5);
        sparsearray.put(2, new q(2));
        q q11 = new q(3);
        sparsearray.put(3, q11);
        q11.a(q9);
        q q12 = new q(6);
        sparsearray.put(6, q12);
        q11.a(q12);
    }

    public static void a(q q1, TextView tv, View view) {
        if (q1 != null && tv != null && view != null) {
            int j = q1.b();
            boolean flag = q1.c();
            tv.setVisibility(j > 0 ? View.VISIBLE : View.GONE);
            view.setVisibility(flag ? View.VISIBLE : View.GONE);
            if (j > 0)
                tv.setText(String.valueOf(Math.min(99, j)));
        } else {
            ViewUtils.a(tv, View.GONE);
            ViewUtils.a(view, View.GONE);
        }
    }
}
