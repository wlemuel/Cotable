package tk.wlemuel.cotable.ui.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.model.DrawerMenu;

/**
 * DrawerMenuAdapter
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc DrawerMenuAdapter
 * @created 2015/05/14
 * @updated 2015/05/14
 */
public class DrawerMenuAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<DrawerMenu> mItemsData;

    public DrawerMenuAdapter(Context context, List<DrawerMenu> listItemData) {
        mInflater = LayoutInflater.from(context);
        mItemsData = listItemData;

    }

    @Override
    public Object getItem(int position) {
        return mItemsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerMenuHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_main_drawer_left_list_item, null);
            holder = new DrawerMenuHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DrawerMenuHolder) convertView.getTag();
        }

        DrawerMenu item = mItemsData.get(position);
        holder.getIconTextView().setText(item.getIcon());
        holder.getTextView().setText(item.getText());

        return convertView;
    }

    @Override
    public int getCount() {
        return mItemsData.size();
    }

}
