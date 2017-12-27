package com.example.bankapp.adapter;

import android.content.Context;
import android.view.View;

import com.example.bankapp.R;
import com.example.bankapp.base.adapter.BaseAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerViewHolder;
import com.example.bankapp.modle.RegisterLineUp;

import java.util.List;

/**
 * 挂号服务
 *
 * @author Guanluocang
 *         created at 2017/12/25 11:03
 */

public class RegisterLineUpAdapter extends BaseAdapter<RegisterLineUp> {

    private OnApplyClickListener onApplyClickListener;

    public void OnApplyClick(OnApplyClickListener onApplyClickListener) {
        this.onApplyClickListener = onApplyClickListener;
    }

    //点击事件和长按事件
    public interface OnApplyClickListener {
        void onItemClick(View view, int position);
    }

    public RegisterLineUpAdapter(Context context, List<RegisterLineUp> registerLineUpList) {
        super(context, R.layout.layout_register_lineup_item, registerLineUpList);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder viewHolder, RegisterLineUp item, final int pos) {
        if ("1".equals(item.getType())) {//现金业务
            viewHolder.getTextView(R.id.tv_bussinessType).setText("现金业务");
        } else if ("2".equals(item.getType())) {//贷款业务
            viewHolder.getTextView(R.id.tv_bussinessType).setText("贷款业务");
        } else if ("3".equals(item.getType())) {//理财业务
            viewHolder.getTextView(R.id.tv_bussinessType).setText("理财业务");
        } else if ("4".equals(item.getType())) {//支票业务
            viewHolder.getTextView(R.id.tv_bussinessType).setText("支票业务");
        } else if ("5".equals(item.getType())) {//对公业务
            viewHolder.getTextView(R.id.tv_bussinessType).setText("对公业务");
        }
        viewHolder.getTextView(R.id.tv_lineNum).setText(item.getLineNum());
        viewHolder.getTextView(R.id.tv_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onApplyClickListener != null) {
                    onApplyClickListener.onItemClick(view, pos);
                }
            }
        });
    }

}
