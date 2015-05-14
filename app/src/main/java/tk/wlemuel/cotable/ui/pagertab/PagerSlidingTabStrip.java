package tk.wlemuel.cotable.ui.pagertab;

/**
 * PagerSlidingTabStrip
 *
 * @desc PagerSlidingTabStrip
 * @author Steve Lemuel
 * @version 0.1
 * @created 2015/05/14
 * @updated 2015/05/14
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import tk.wlemuel.cotable.R;

public class PagerSlidingTabStrip extends HorizontalScrollView implements
        ViewPager.OnPageChangeListener {

    private static final int ATTRS[];

    static {
        ATTRS = new int[]{0x1010095, 0x1010098};
    }

    private int checkedTextColor;
    private int currentPosition;
    private float currentPositionOffset;
    private int dividerColor;
    private int dividerPadding;
    private Paint dividerPaint;
    private int dividerWidth;
    private int indicatorColor;
    private int indicatorHeight;
    private int lastScrollX;
    private Locale locale;
    private OnTabClickListener onTabClickListener;
    private ViewPager pager;
    private Paint rectPaint;
    private int scrollOffset;
    private int tabCount;
    private int tabPadding;
    private LinearLayout.LayoutParams tabViewLayoutParams;
    private LinearLayout tabsContainer;
    private boolean textAllCaps;
    private int unCheckedTextColor;
    private int underlineColor;
    private int underlineHeight;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        currentPosition = 0;
        currentPositionOffset = 0.0F;
        indicatorColor = Color.parseColor("#2196F3");// 0xff01d9ae;
        underlineColor = Color.parseColor("#DADADA");
        dividerColor = 0;
        checkedTextColor = R.color.tab_strip_text_selected;
        unCheckedTextColor = R.color.tab_strip_text_unselected;
        textAllCaps = true;
        scrollOffset = 52;
        indicatorHeight = 4;
        underlineHeight = 1;
        dividerPadding = 12;
        tabPadding = 0;
        dividerWidth = 1;
        lastScrollX = 0;
        onTabClickListener = null;
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(-1, -1));
        addView(tabsContainer);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(1, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(1, indicatorHeight,
                dm);
        underlineHeight = (int) TypedValue.applyDimension(0, underlineHeight,
                dm);
        dividerPadding = (int) TypedValue.applyDimension(1, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(1, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(1, dividerWidth, dm);
        context.obtainStyledAttributes(attrs, ATTRS).recycle();
        TypedArray td = context.obtainStyledAttributes(attrs,
                R.styleable.PagerSlidingTabStrip);
        indicatorColor = td.getColor(0, indicatorColor);
        underlineColor = td.getColor(1, underlineColor);
        dividerColor = td.getColor(2, dividerColor);
        checkedTextColor = td.getResourceId(4, R.color.tab_strip_text_selected);
        unCheckedTextColor = td.getResourceId(4,
                R.color.tab_strip_text_unselected);
        indicatorHeight = td.getDimensionPixelSize(5, indicatorHeight);
        underlineHeight = td.getDimensionPixelSize(6, underlineHeight);
        dividerPadding = td.getDimensionPixelSize(7, dividerPadding);
        tabPadding = td.getDimensionPixelSize(8, tabPadding);
        scrollOffset = td.getDimensionPixelSize(9, scrollOffset);
        textAllCaps = td.getBoolean(12, textAllCaps);
        td.recycle();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);
        tabViewLayoutParams = new LinearLayout.LayoutParams(0, -1, 1.0F);
        if (locale == null)
            locale = getResources().getConfiguration().locale;
    }

    private void addTab(final int position, View view) {
        view.setFocusable(true);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == position
                        && onTabClickListener != null)
                    onTabClickListener.onTabClicked(position);
                else
                    pager.setCurrentItem(position, true);
            }
        });
        view.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(view, position, tabViewLayoutParams);
    }

    @SuppressLint("InflateParams")
    private void addTabView(int position, String title) {
        LinearLayout ly = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.tab_layout_main, null);
        ((TextView) ly.findViewById(R.id.tab_title_label)).setText(title);
        addTab(position, ly);
    }

    private void scrollToChild(int i, int j) {
        if (tabCount != 0) {
            int k = j + tabsContainer.getChildAt(i).getLeft();
            if (i > 0 || j > 0)
                k -= scrollOffset;
            if (k != lastScrollX) {
                lastScrollX = k;
                scrollTo(k, 0);
            }
        }
    }

    private void setChooseTabViewTextColor(int idx) {
        int count = tabsContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            TextView tv = (TextView) tabsContainer
                    .getChildAt(i).findViewById(R.id.tab_title_label);
            if (i == idx)
                tv.setTextColor(getResources().getColor(checkedTextColor));
            else
                tv.setTextColor(getResources().getColor(unCheckedTextColor));
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(int dividerPadding) {
        this.dividerPadding = dividerPadding;
        invalidate();
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        invalidate();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setUnderlineHeight(int underlineHeight) {
        this.underlineHeight = underlineHeight;
        invalidate();
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++)
            addTabView(i, pager.getAdapter().getPageTitle(i).toString());

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < 16)
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        else
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        currentPosition = pager.getCurrentItem();
                        setChooseTabViewTextColor(currentPosition);
                        scrollToChild(currentPosition, 0);
                    }
                });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode() && tabCount != 0) {
            int height = getHeight();
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0.0F, height - underlineHeight,
                    tabsContainer.getWidth(), height, rectPaint);
            rectPaint.setColor(indicatorColor);
            View view = tabsContainer.getChildAt(currentPosition);
            float left = view.getLeft();
            float right = view.getRight();
            float l = left;
            if (currentPositionOffset > 0.0F && currentPosition < -1 + tabCount) {
                View v = tabsContainer.getChildAt(1 + currentPosition);
                l = v.getLeft() * currentPositionOffset + left
                        * (1.0F - currentPositionOffset);
                right = v.getRight() * currentPositionOffset + right
                        * (1.0F - currentPositionOffset);
            }
            canvas.drawRect(l, height - indicatorHeight, right, height,
                    rectPaint);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i == 0)
            scrollToChild(pager.getCurrentItem(), 0);
    }

    @Override
    public void onPageScrolled(int i, float f, int j) {
        currentPosition = i;
        currentPositionOffset = f;
        scrollToChild(i, (int) (f * tabsContainer.getChildAt(i).getWidth()));
        invalidate();
    }

    @Override
    public void onPageSelected(int i) {
        setChooseTabViewTextColor(i);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(((SavedState) state).getSuperState());
        currentPosition = 0;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedstate = new SavedState(super.onSaveInstanceState());
        savedstate.currentPosition = currentPosition;
        return savedstate;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setCheckedTextColorResource(int checkedTextColor) {
        this.checkedTextColor = checkedTextColor;
        invalidate();
    }

    public void setDividerColorResource(int dividerColor) {
        this.dividerColor = getResources().getColor(dividerColor);
        invalidate();
    }

    public void setIndicatorColorResource(int indicatorColor) {
        this.indicatorColor = getResources().getColor(indicatorColor);
        invalidate();
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        this.onTabClickListener = listener;
    }

    public void setUnderlineColorResource(int underlineColor) {
        this.underlineColor = getResources().getColor(underlineColor);
        invalidate();
    }

    public void setViewPager(ViewPager vp) {
        pager = vp;
        if (vp.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        } else {
            notifyDataSetChanged();
            return;
        }
    }

    public View getChild(int idx) {
        return tabsContainer.getChildAt(idx);
    }

    public View getBadgeView(int i) {
        return tabsContainer.getChildAt(i).findViewById(R.id.con);
    }

    public void updateTab(int i, q q) {
        LinearLayout tab = (LinearLayout) tabsContainer.getChildAt(i);
        ImageView iv = (ImageView) tab.findViewById(R.id.tab_new_indicator);
        r.a(q, (TextView) tab.findViewById(R.id.tab_new_msg_label), iv);
    }

    public interface OnTabClickListener {

        void onTabClicked(int i);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }
        };
        int currentPosition;

        private SavedState(Parcel parcel) {
            super(parcel);
            currentPosition = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(currentPosition);
        }
    }
}
