package com.example.bankapp.adapter;

import android.content.Context;

import com.example.bankapp.R;
import com.example.bankapp.base.adapter.BaseAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerViewHolder;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.modle.LocalMoneyVideo;

import java.util.List;

/**
 * 理财与财经
 *
 * @author Guanluocang
 *         created at 2017/12/25 11:02
 */

public class MoneyIntroduceFinanceAdapter extends BaseAdapter<LocalMoneyVideo> {


    public MoneyIntroduceFinanceAdapter(Context context, List<LocalMoneyVideo> moneyIntroduceList) {
        super(context, R.layout.layout_money_introduce, moneyIntroduceList);

    }

    @Override
    protected void convert(BaseRecyclerViewHolder viewHolder, LocalMoneyVideo item, int pos) {
        viewHolder.getTextView(R.id.tv_moneyIntroduceItem).setText(item.getVideoName());
    }

}
