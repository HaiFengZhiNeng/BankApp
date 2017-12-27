package com.example.bankapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.adapter.MoneyIntroduceAdapter;
import com.example.bankapp.base.adapter.BaseRecyclerAdapter;
import com.example.bankapp.base.view.BaseFragment;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.modle.LocalMoneyService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 理财介绍
 *
 * @author Guanluocang
 *         created at 2017/12/21 15:04
 */
public class IntroduceFragment extends BaseFragment {

    @BindView(R.id.recycler_introduce)
    RecyclerView rvIntroduce;

    private List<LocalMoneyService> localMoneyServices = new ArrayList<>();
    private MoneyIntroduceAdapter moneyIntroduceAdapter;

    private IntroduceManager introduceManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_introducek;
    }

    @Override
    protected void initView(View view) {

        rvIntroduce.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvIntroduce.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
        moneyIntroduceAdapter = new MoneyIntroduceAdapter(getActivity(), localMoneyServices);
        rvIntroduce.setAdapter(moneyIntroduceAdapter);
        moneyIntroduceAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                showDialog(position);
            }
        });

    }

    @Override
    protected void initData() {
        introduceManager = new IntroduceManager();
    }

    @Override
    protected void setListener(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        queryListData();
    }

    /**
     * 删除数据
     */
    private void deleteData(int position) {
        if (!localMoneyServices.isEmpty()) {
            introduceManager.delete(localMoneyServices.get(position));
            moneyIntroduceAdapter.removeItem(localMoneyServices.get(position));
        }
    }

    /**
     * 删除所有数据
     */
    private void deleteAllData() {
        if (!localMoneyServices.isEmpty()) {
            introduceManager.deleteAll();
            moneyIntroduceAdapter.clear();
        }
    }

    /**
     * 查询数据
     */
    private void queryListData() {
        localMoneyServices = introduceManager.loadAll();
        if (localMoneyServices != null && localMoneyServices.size() > 0) {
            moneyIntroduceAdapter.refreshData(localMoneyServices);
        }
    }

    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 指定下拉列表的显示数据
        final String[] memu = {"删除此条", "删除所有"};
        // 设置一个下拉的列表选择项
        builder.setItems(memu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        try {
                            deleteData(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "成功删除一条数据",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        try {
                            deleteAllData();
                            Toast.makeText(getActivity(), "成功删除所有数据",
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

}
