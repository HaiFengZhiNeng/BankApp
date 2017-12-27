package com.example.bankapp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.base.adapter.BaseAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerViewHolder;
import com.example.bankapp.modle.MoneyCurrent;

import java.util.List;

import butterknife.BindView;

/**
 * 活期理财
 *
 * @author Guanluocang
 *         created at 2017/12/25 10:50
 */

public class MoneyCurrentApdater extends BaseAdapter<MoneyCurrent> {

    public MoneyCurrentApdater(Context context, List<MoneyCurrent> datas) {
        super(context, R.layout.layout_money_current, datas);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder viewHolder, MoneyCurrent item, int pos) {
        viewHolder.getTextView(R.id.tv_urrentType).setText(item.getCurrentType());//类型
        viewHolder.getTextView(R.id.tv_currentPercentage).setText(item.getCurrentperentage());//比率
        viewHolder.getTextView(R.id.tv_urrentTypeTwo).setText(item.getCurrentperentageTwo());//类型二
        viewHolder.getTextView(R.id.tv_currentTime).setText(item.getCurrentTime());// 理财时间
        viewHolder.getTextView(R.id.tv_currentAmount).setText(item.getCurrentAmount()); // 理财金额
    }

}
