package tk.wlemuel.cotable.ui.drawer;

import android.view.View;
import android.widget.IconTextView;
import android.widget.TextView;

import tk.wlemuel.cotable.R;


public class DrawerMenuHolder {
    private IconTextView mIconTextView;
    private TextView mTextView;

    public DrawerMenuHolder(View view) {
        mIconTextView = (IconTextView) view.findViewById(R.id.drawer_left_list_item_icon);
        mTextView = (TextView) view.findViewById(R.id.drawer_left_list_item_text);
    }

    public TextView getTextView() {
        return mTextView;
    }

    public IconTextView getIconTextView() {
        return mIconTextView;
    }
}
