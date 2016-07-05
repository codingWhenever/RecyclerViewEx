package sample.leo.com.recyclerviewex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import sample.leo.com.recyclerviewex.R;

/**
 * Created by leo on 2016/7/5.
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOADING = 2;
    private static final int ITEM_TYPE_NORMAL = 1;//普通文本
    private static final int ITEM_TYPE_FOOTER = 2;//footer
    private int load_more_status = 1;
    private ArrayList<T> mData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(Context context, ArrayList<T> data) {
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NORMAL) {
            final View itemView = mLayoutInflater.inflate(R.layout.layout_item, parent, false);
            final RecyclerView.ViewHolder viewHolder = new NormalViewHolder(itemView);
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(itemView, viewHolder.getLayoutPosition());
                    }
                });
            }
            return viewHolder;
        } else {
            View itemView = mLayoutInflater.inflate(R.layout.layout_footer_view, parent, false);
            RecyclerView.ViewHolder viewHolder = new FooterViewHolder(itemView);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position + 1 == getItemCount()) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            switch (load_more_status) {
                case STATUS_NORMAL:
                    footer.tv_text.setVisibility(View.VISIBLE);
                    footer.tv_text.setText("上拉加载更多");
                    footer.mProgressBar.setVisibility(View.GONE);
                    break;
                case STATUS_LOADING:
                    footer.tv_text.setVisibility(View.GONE);
                    footer.mProgressBar.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            NormalViewHolder normal = (NormalViewHolder) holder;
            normal.tv_content.setText(mData.get(position).toString());
        }
    }


    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(int status) {
        this.load_more_status = status;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View item, int pos);
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;

        public NormalViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_text;
        public ProgressBar mProgressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_loading);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
        }
    }
}
