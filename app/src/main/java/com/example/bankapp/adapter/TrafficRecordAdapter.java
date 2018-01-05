package com.example.bankapp.adapter;

import android.content.Context;

import com.example.bankapp.R;
import com.example.bankapp.base.adapter.BaseAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerViewHolder;
import com.example.bankapp.modle.TrafficRecord;

import java.util.List;

/**
 * 违章查询列表
 *
 * @author Guanluocang
 *         created at 2018/1/3 18:14
 */

public class TrafficRecordAdapter extends BaseAdapter<TrafficRecord> {

    public TrafficRecordAdapter(Context context, List<TrafficRecord> datas) {
        super(context, R.layout.layout_traffic_record, datas);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder viewHolder, TrafficRecord item, int pos) {
        viewHolder.getTextView(R.id.tv_trafficRecordCar).setText(item.getmTrafficRecordCar());
        viewHolder.getTextView(R.id.tv_trafficRecordInfo).setText(item.getmTrafficRecordInfo());
        viewHolder.getTextView(R.id.tv_trafficRecordMoney).setText(item.getmTrafficRecordMoney());
        viewHolder.getTextView(R.id.tv_trafficRecordData).setText(item.getmTrafficRecordData());
    }
}
