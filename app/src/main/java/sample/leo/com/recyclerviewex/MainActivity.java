package sample.leo.com.recyclerviewex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import sample.leo.com.recyclerviewex.adapter.RecyclerAdapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter<String> mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<String> mDatas = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new RecyclerAdapter<>(this, getData());
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisiableItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mRefreshLayout.isRefreshing()) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 1 == mAdapter.getItemCount()) {
                        mRefreshLayout.setEnabled(false);
                        mAdapter.setStatus(RecyclerAdapter.STATUS_LOADING);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                mRefreshLayout.setEnabled(true);
                                mAdapter.setStatus(RecyclerAdapter.STATUS_NORMAL);
                                mAdapter.notifyDataSetChanged();
                            }
                        }, 3000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }


    private ArrayList<String> getData() {
        for (int i = 1; i <= 20; i++) {
            mDatas.add("this is item " + i);
        }
        return mDatas;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                getData();
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setEnabled(true);
            }
        }, 3000);
    }
}
