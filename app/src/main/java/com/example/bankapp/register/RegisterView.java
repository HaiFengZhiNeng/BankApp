package com.example.bankapp.register;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.adapter.RegisterLineUpAdapter;
import com.example.bankapp.animator.RegisterAnimation;
import com.example.bankapp.animator.SlideInOutBottomItemAnimator;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.RegisterLineUp;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.registerService.RegisterServiceView;
import com.example.bankapp.util.TimeUtil;
import com.example.bankapp.view.ChatLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 挂号服务
 *
 * @author Guanluocang
 *         created at 2017/12/7 11:33
 */
public class RegisterView extends PresenterActivity<RegisterPresenter> implements IRegisiterView {

    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_registerPersonalForeign)
    TextView tvRegisterPersonalForeign;//个人有外汇
    @BindView(R.id.tv_registerPersonal)
    TextView tvRegisterPersonal;//个人无外汇
    @BindView(R.id.tv_registerPublic)
    TextView tvRegisterPublic;//对公
    @BindView(R.id.tv_registerAppointment)
    TextView tvRegisterAppointment;//预约
    @BindView(R.id.tv_registerSelectLineUp)
    TextView tvRegisterSelectLineUp;//预约取号
    @BindView(R.id.tv_registerSelectAppointment)
    TextView tvRegisterSelectAppointment;// 立即排队
    @BindView(R.id.rv_registerLineUp)
    RecyclerView rvRegisterLineUp;// 立即排队Recycleview
    @BindView(R.id.tv_updataTime)
    TextView tvUpdataTime;//更新时间
    @BindView(R.id.ll_appointment)
    LinearLayout llAppointment;// 立即排队 布局
    @BindView(R.id.ll_linup)
    LinearLayout llLinup;//预约取号 布局

    //Adapter
    private RegisterLineUpAdapter registerLineUpAdapter;
    private List<RegisterLineUp> registerLineUpList = new ArrayList<>();

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_register_view;
    }


    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initData();
    }

    //初始化数据
    private void initData() {

        //布局格式
        rvRegisterLineUp.setLayoutManager(new ChatLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvRegisterLineUp.setItemAnimator(new SlideInOutBottomItemAnimator(rvRegisterLineUp));
        //进场动画
        rvRegisterLineUp.setLayoutAnimation(RegisterAnimation.getInstance());
        //模拟数据
        registerLineUpList.add(new RegisterLineUp("1", "4"));
        registerLineUpList.add(new RegisterLineUp("2", "12"));
        registerLineUpList.add(new RegisterLineUp("3", "7"));
        registerLineUpList.add(new RegisterLineUp("4", "3"));
        registerLineUpList.add(new RegisterLineUp("5", "8"));
        //设置适配器
        registerLineUpAdapter = new RegisterLineUpAdapter(RegisterView.this, registerLineUpList);
        rvRegisterLineUp.setAdapter(registerLineUpAdapter);
        //立即申请
        registerLineUpAdapter.OnApplyClick(new RegisterLineUpAdapter.OnApplyClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast("申请成功");
            }
        });

        //设置当前时间
        tvUpdataTime.setText(TimeUtil.formatDateTime(TimeUtil.gainCurrentDate(), "yyyy-MM-dd HH:mm:ss") + "");

        registerLineUpAdapter.OnApplyClick(new RegisterLineUpAdapter.OnApplyClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPresenter.doAnswer("请拿取您的号码，在您前面还有" + registerLineUpList.get(position).getLineNum() + "人正在排队，请您耐心等待");
//                startActivity(RegisterServiceView.class);
            }
        });
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_EXIT);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BasePresenter.ACTION_OTHER_FINISH);
        registerReceiver(businessReceiver, filter);
    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(businessReceiver);
    }

    private BroadcastReceiver businessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BasePresenter.ACTION_OTHER_FINISH)) {
                onExit();
            }
        }
    };

    @OnClick({R.id.tv_goBack, R.id.tv_registerPersonalForeign, R.id.tv_registerPersonal, R.id.tv_registerPublic, R.id.tv_registerAppointment, R.id.tv_registerSelectLineUp, R.id.tv_registerSelectAppointment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_registerPersonalForeign://个人有外汇
                mPresenter.doSelectTextColor("PersonalForeign");
                break;
            case R.id.tv_registerPersonal://个人无外汇
                mPresenter.doSelectTextColor("Personal");
                break;
            case R.id.tv_registerPublic://对公
                mPresenter.doSelectTextColor("Public");
                break;
            case R.id.tv_registerAppointment://预约
                mPresenter.doSelectTextColor("Appointment");
                break;
            case R.id.tv_registerSelectAppointment://预约取号
                mPresenter.doSelectRegisterItem("Appointment");
                break;
            case R.id.tv_registerSelectLineUp://立即排队
                mPresenter.doSelectRegisterItem("LineUp");
                break;
        }
    }

    //头部业务类型选中颜色
    @Override
    public void setSelectTextColor(int selectColor, int unSelectColor, String which) {
        switch (which) {
            case "PersonalForeign":
                tvRegisterPersonalForeign.setTextColor(selectColor);
                tvRegisterPersonal.setTextColor(unSelectColor);
                tvRegisterPublic.setTextColor(unSelectColor);
                tvRegisterAppointment.setTextColor(unSelectColor);
                break;
            case "Personal":
                tvRegisterPersonalForeign.setTextColor(unSelectColor);
                tvRegisterPersonal.setTextColor(selectColor);
                tvRegisterPublic.setTextColor(unSelectColor);
                tvRegisterAppointment.setTextColor(unSelectColor);
                break;
            case "Public":
                tvRegisterPersonalForeign.setTextColor(unSelectColor);
                tvRegisterPersonal.setTextColor(unSelectColor);
                tvRegisterPublic.setTextColor(selectColor);
                tvRegisterAppointment.setTextColor(unSelectColor);
                break;
            case "Appointment":
                tvRegisterPersonalForeign.setTextColor(unSelectColor);
                tvRegisterPersonal.setTextColor(unSelectColor);
                tvRegisterPublic.setTextColor(unSelectColor);
                tvRegisterAppointment.setTextColor(selectColor);
                break;
        }
    }

    //Item 选择 预约取号 立即排队
    @Override
    public void setRegisterSelectItem(int selectColor, int unSelectColor, String which) {
        switch (which) {
            case "Appointment"://预约取号
                tvRegisterSelectAppointment.setTextColor(selectColor);
                tvRegisterSelectLineUp.setTextColor(unSelectColor);
                tvRegisterSelectAppointment.setBackgroundResource(R.drawable.layout_register_select_right_pressed);
                tvRegisterSelectLineUp.setBackgroundResource(R.drawable.layout_register_select_left);
                llAppointment.setVisibility(View.VISIBLE);
                llLinup.setVisibility(View.GONE);
                break;
            case "LineUp"://立即排队
                tvRegisterSelectAppointment.setTextColor(unSelectColor);
                tvRegisterSelectLineUp.setTextColor(selectColor);
                tvRegisterSelectAppointment.setBackgroundResource(R.drawable.layout_register_select_right);
                tvRegisterSelectLineUp.setBackgroundResource(R.drawable.layout_register_select_left_pressed);
                llAppointment.setVisibility(View.GONE);
                llLinup.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 复写的方法
     */
    @Override
    protected void startMotion() {

    }

    @Override
    protected void stopMotion() {

    }

    @Override
    protected void onEventLR() {

    }

    @Override
    public void special(String result, SpecialType type) {

    }

    @Override
    public void spakeMove(SpecialType specialType, String result) {

    }

    @Override
    public void doAiuiAnwer(String anwer) {

    }

    @Override
    public void refHomePage(String question, String finalText) {

    }

    @Override
    public void refHomePage(String question, News news) {

    }

    @Override
    public void refHomePage(String question, Radio radio) {

    }

    @Override
    public void refHomePage(String question, Poetry poetry) {

    }

    @Override
    public void refHomePage(String question, Cookbook cookbook) {

    }

    @Override
    public void refHomePage(String question, EnglishEveryday englishEveryday) {

    }

    @Override
    public void doCallPhone(String phoneNumber) {

    }

    @Override
    public void doDance() {

    }

}
