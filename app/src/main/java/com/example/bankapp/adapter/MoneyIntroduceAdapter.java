package com.example.bankapp.adapter;

import android.content.Context;

import com.example.bankapp.R;
import com.example.bankapp.base.adapter.BaseAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerViewHolder;
import com.example.bankapp.modle.LocalMoneyService;

import java.util.List;

/**
 * 理财介绍
 *
 * @author Guanluocang
 *         created at 2017/12/25 11:01
 */

public class MoneyIntroduceAdapter extends BaseAdapter<LocalMoneyService> {

    public MoneyIntroduceAdapter(Context context, List<LocalMoneyService> moneyIntroduceList) {
        super(context, R.layout.layout_money_introduce, moneyIntroduceList);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder viewHolder, LocalMoneyService item, int pos) {
        viewHolder.getTextView(R.id.tv_moneyIntroduceQuestion).setText(item.getIntroduceQuestion());
        viewHolder.getTextView(R.id.tv_moneyIntroduceAnswer).setText(item.getIntroduceAnswer());
        viewHolder.getTextView(R.id.tv_moneyIntroduceAction).setText(item.getIntroduceAction());
        viewHolder.getTextView(R.id.tv_moneyIntroduceExpression).setText(item.getIntroduceExpression());
    }

}
