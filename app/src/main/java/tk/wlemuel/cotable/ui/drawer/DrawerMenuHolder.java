package tk.wlemuel.cotable.ui.drawer;

import android.view.View;
import android.widget.IconTextView;
import android.widget.TextView;

import tk.wlemuel.cotable.R;


public class DrawerMenuHolder {
    private IconTextView drawerLeftListItemIcon;
    private TextView drawerLeftListItemText;

    public DrawerMenuHolder(View view) {
        drawerLeftListItemIcon = (IconTextView) view.findViewById(R.id.drawer_left_list_item_icon);
        drawerLeftListItemText = (TextView) view.findViewById(R.id.drawer_left_list_item_text);
    }

    public TextView getDrawerLeftListItemText() {
        return drawerLeftListItemText;
    }

    public IconTextView getDrawerLeftListItemIcon() {
        return drawerLeftListItemIcon;
    }
}
