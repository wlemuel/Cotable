package tk.wlemuel.cotable.ui.pagertab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * q
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc q
 * @created 2015/05/10
 * @updated 2015/05/10
 */
public final class q implements Serializable {

    private static final long serialVersionUID = 0xe2d57122f51e480bL;
    private final int a;
    private int b;
    private boolean c;
    private int d;
    private List<q> e;

    public q(int i) {
        a = i;
        d = -1;
    }

    public q(q q1) {
        a = q1.a;
        b = q1.b;
        c = q1.c;
        d = q1.d;
    }

    public final int a() {
        return a;
    }

    final void a(int i) {
        b = i;
    }

    final void a(q q1) {
        if (e == null)
            e = new ArrayList();
        e.add(q1);
        q1.d = a;
    }

    final void a(boolean flag) {
        c = flag;
    }

    public final int b() {
        return b;
    }

    public final boolean c() {
        return b <= 0 && c;
    }

    final void d() {
        if (e != null && e.size() > 0) {
            b = 0;
            c = false;
            Iterator iterator = e.iterator();
            while (iterator.hasNext()) {
                q q1 = (q) iterator.next();
                b = b + q1.b;
                c = c | q1.c;
            }
        }
    }

    final int e() {
        return d;
    }
}
