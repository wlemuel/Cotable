package tk.wlemuel.cotable.activity.blog.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.base.BaseRecycleAdapter;
import tk.wlemuel.cotable.model.Blog;
import tk.wlemuel.cotable.utils.HtmlUtils;

/**
 * BlogAdapter
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogAdapter
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class BlogAdapter extends BaseRecycleAdapter {

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_blog, null);
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }

    @Override
    protected void onBindItemViewHolder(BaseRecycleAdapter.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;

        Blog item = (Blog) mData.get(position);
        vh.mTitle.setText(HtmlUtils.filter(item.getTitle()));
        vh.mSummary.setText(HtmlUtils.filter(item.getSummary()));
        vh.mAuthorName.setText(item.getAuthor_name());
        vh.mUpdated.setText(item.getUpdated());
        vh.mComments.setText(
                vh.mComments.getContext().getString(
                        R.string.blog_item_comments_default, item.getComments())
        );
        vh.mReads.setText(
                vh.mComments.getContext().getString(
                        R.string.blog_item_reading_default, item.getReads())
        );

    }

    static class ViewHolder extends BaseRecycleAdapter.ViewHolder {
        public TextView mTitle, mSummary, mAuthorName, mUpdated, mComments, mReads;
        public CircleImageView mAuthorAvatar;

        public ViewHolder(int viewType, View view) {
            super(viewType, view);

            mTitle = (TextView) view.findViewById(R.id.blog_title);
            mSummary = (TextView) view.findViewById(R.id.blog_summary);
            mAuthorName = (TextView) view.findViewById(R.id.blog_author_name);
            mUpdated = (TextView) view.findViewById(R.id.blog_updated);
            mComments = (TextView) view.findViewById(R.id.blog_comments);
            mReads = (TextView) view.findViewById(R.id.blog_reads);
            mAuthorAvatar = (CircleImageView) view.findViewById(R.id.blog_author_avatar);
        }
    }
}
