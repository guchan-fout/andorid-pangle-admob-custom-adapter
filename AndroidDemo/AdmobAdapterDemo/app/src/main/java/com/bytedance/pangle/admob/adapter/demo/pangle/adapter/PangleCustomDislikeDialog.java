package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bytedance.pangle.admob.adapter.demo.R;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTDislikeDialogAbstract;
import com.bytedance.sdk.openadsdk.dislike.TTDislikeListView;

import java.util.ArrayList;
import java.util.List;


public class PangleCustomDislikeDialog extends TTDislikeDialogAbstract {
    final List<FilterWord> mList;
    private OnDislikeItemClick mOnDislikeItemClick;

    public PangleCustomDislikeDialog(Context context, List<FilterWord> list) {
        super(context);

        this.mList = initData(list);
    }

    public void setOnDislikeItemClick(OnDislikeItemClick onDislikeItemClick) {
        mOnDislikeItemClick = onDislikeItemClick;
    }

    private List<FilterWord> initData(List<FilterWord> list) {
        List<FilterWord> data = new ArrayList<>();
        if (list != null) {
            for (FilterWord filterWord : list) {
                if (filterWord.hasSecondOptions()) {
                    data.addAll(initData(filterWord.getOptions()));
                } else {
                    data.add(filterWord);
                }
            }
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TTDislikeListView listView = (TTDislikeListView) findViewById(R.id.lv_dislike_custom);
        listView.setAdapter(new PangleCustomDislikeAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PangleCustomDislikeDialog.this.dismiss();

                if (mOnDislikeItemClick != null) {
                    FilterWord word = null;
                    try {
                        word = (FilterWord) parent.getAdapter().getItem(position);
                    } catch (Throwable ignore) {
                    }
                    mOnDislikeItemClick.onItemClick(word);
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.dislike_dialog;
    }

    @Override
    public int[] getTTDislikeListViewIds() {
        return new int[]{R.id.lv_dislike_custom};
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return null;
    }

    class PangleCustomDislikeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FilterWord word = (FilterWord) getItem(position);

            TextView textView = new TextView(getContext());
            textView.setPadding(40, 40, 40, 40);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setText(word.getName());

            return textView;
        }
    }

    public interface OnDislikeItemClick {
        void onItemClick(FilterWord filterWord);
    }

}
