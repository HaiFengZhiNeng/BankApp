package com.example.bankapp.registerService;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.view.takephoto.PictureHandlePopupWindow;
import com.example.bankapp.view.takephoto.PictureSelectPopupWindow;
import com.jph.takephoto.model.TResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 挂号完善信息
 *
 * @author Guanluocang
 *         created at 2017/12/13 9:38
 */
public class RegisterServiceView extends PresenterActivity<RegisterServicePresenter> implements IRegisterServiceView {

    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.et_address)
    EditText etAddress;//取号网点
    @BindView(R.id.et_name)
    EditText etName;//办卡人姓名
    @BindView(R.id.et_telphone)
    EditText etTelphone;//电话
    @BindView(R.id.et_idcard)
    EditText etIdcard;//身份证
    @BindView(R.id.et_bank)
    EditText etBank;//银行卡号
    @BindView(R.id.iv_idCardPositive)
    ImageView ivIdCardPositive;//身份证正面
    @BindView(R.id.iv_idCardOpposite)
    ImageView ivIdCardOpposite;//身份证反面
    @BindView(R.id.iv_bankPositive)
    ImageView ivBankPositive;//银行卡正面
    @BindView(R.id.iv_bankOpposite)
    ImageView ivBankOpposite;//银行卡反面

    @BindView(R.id.rl_idCardPositive)
    RelativeLayout rlIdCardPositive;
    @BindView(R.id.rl_idCardOpposite)
    RelativeLayout rlIdCardOpposite;
    @BindView(R.id.rl_bankPositive)
    RelativeLayout rlBankPositive;
    @BindView(R.id.rl_bankOpposite)
    RelativeLayout rlBankOpposite;

    private Context mContext;

    private int TAG = 0;//  标记是哪张图片
    private String mIdCardPositive = "";
    private String mIdCardOpposite = "";
    private String mBankPositive = "";
    private String mBankOpposite = "";

    //选择图片
    private PictureSelectPopupWindow pictureSelectPopupWindow;
    //放大/删除图片
    private PictureHandlePopupWindow pictureHandlePopupWindow;
    private RegisterServiceTakePhoto registerServiceTakePhoto;

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_register_service_view;
    }

    @Override
    public RegisterServicePresenter createPresenter() {
        return new RegisterServicePresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
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
    protected void onViewCreated() {
        super.onViewCreated();
        initView();
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
    }

    @Override
    public void onPauseReceiver() {

    }

    private void initView() {
        mContext = this;
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_pic_select, null);
        registerServiceTakePhoto = RegisterServiceTakePhoto.of(contentView);
        pictureSelectPopupWindow = new PictureSelectPopupWindow(this, pictureSelectItemsOnClick);
        pictureHandlePopupWindow = new PictureHandlePopupWindow(this, pictureHandleItemsOnClick);

        pictureSelectPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pictureSelectPopupWindow.backgroundAlpha(1.0f);
            }
        });
        pictureHandlePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pictureHandlePopupWindow.backgroundAlpha(1.0f);
            }
        });
    }

    View.OnClickListener pictureSelectItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_pick_photo:   // 调用系统相机
                    registerServiceTakePhoto.onClick(view, getTakePhoto());
                    pictureSelectPopupWindow.dismiss();
                    break;
                case R.id.btn_take_photo:   // 调用系统相机
                    registerServiceTakePhoto.onClick(view, getTakePhoto());
                    pictureSelectPopupWindow.dismiss();
                    break;
                case R.id.btn_cancel:   // 取消
                    pictureSelectPopupWindow.dismiss();
                    break;
            }
        }
    };

    View.OnClickListener pictureHandleItemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_big_picture:
                    pictureHandlePopupWindow.dismiss();
                case R.id.btn_delete: // 删除图片
                    pictureHandlePopupWindow.dismiss();
                    deleteImage();
                    break;
                case R.id.btn_cancel:
                    pictureHandlePopupWindow.dismiss();
                    break;

            }
        }
    };

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        addImages(result.getImage().getCompressPath());
    }


    @OnClick({R.id.tv_goBack, R.id.iv_idCardPositive, R.id.iv_idCardOpposite, R.id.iv_bankPositive, R.id.iv_bankOpposite, R.id.rl_idCardPositive, R.id.rl_idCardOpposite, R.id.rl_bankPositive, R.id.rl_bankOpposite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.iv_idCardPositive:
                TAG = 1;
                if (!"".equals(mIdCardPositive)) {
                    pictureHandlePopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                    pictureHandlePopupWindow.backgroundAlpha(0.5f);
                }
                break;
            case R.id.iv_idCardOpposite:
                TAG = 2;
                if (!"".equals(mIdCardOpposite)) {
                    pictureHandlePopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                    pictureHandlePopupWindow.backgroundAlpha(0.5f);
                }
                break;
            case R.id.iv_bankPositive:
                TAG = 3;
                if (!"".equals(mBankPositive)) {
                    pictureHandlePopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                    pictureHandlePopupWindow.backgroundAlpha(0.5f);
                }
                break;
            case R.id.iv_bankOpposite:
                TAG = 4;
                if (!"".equals(mBankOpposite)) {
                    pictureHandlePopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                    pictureHandlePopupWindow.backgroundAlpha(0.5f);
                }
                break;

            case R.id.rl_idCardPositive:
                TAG = 1;
                pictureSelectPopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                pictureSelectPopupWindow.backgroundAlpha(0.5f);
                break;
            case R.id.rl_idCardOpposite:
                TAG = 2;
                pictureSelectPopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                pictureSelectPopupWindow.backgroundAlpha(0.5f);
                break;
            case R.id.rl_bankPositive:
                TAG = 3;
                pictureSelectPopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                pictureSelectPopupWindow.backgroundAlpha(0.5f);
                break;
            case R.id.rl_bankOpposite:
                TAG = 4;
                pictureSelectPopupWindow.showAtLocation(findViewById(R.id.ll_registerService), Gravity.BOTTOM, 0, 0);
                pictureSelectPopupWindow.backgroundAlpha(0.5f);
                break;
        }
    }

    /**
     * 添加图片
     *
     * @param path
     */
    public void addImages(String path) {
        if (TAG == 1) {
            if (TextUtils.isEmpty(mIdCardPositive)) {
                mIdCardPositive = path;
                ivIdCardPositive.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mIdCardPositive).into(ivIdCardPositive);
            }
        } else if (TAG == 2) {
            if (TextUtils.isEmpty(mIdCardOpposite)) {
                mIdCardOpposite = path;
                ivIdCardOpposite.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mIdCardOpposite).into(ivIdCardOpposite);
            }
        } else if (TAG == 3) {
            if (TextUtils.isEmpty(mBankPositive)) {
                mBankPositive = path;
                ivBankPositive.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mBankPositive).into(ivBankPositive);
            }
        } else if (TAG == 4) {
            if (TextUtils.isEmpty(mBankOpposite)) {
                mBankOpposite = path;
                ivBankOpposite.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mBankOpposite).into(ivBankOpposite);
            }
        }
    }

    /**
     * 删除图片
     */
    private void deleteImage() {
        if (TAG == 1) {
            mIdCardPositive = "";
            ivIdCardPositive.setVisibility(View.GONE);
        } else if (TAG == 2) {
            mIdCardOpposite = "";
            ivIdCardOpposite.setVisibility(View.GONE);
        } else if (TAG == 3) {
            mBankPositive = "";
            ivBankPositive.setVisibility(View.GONE);
        } else if (TAG == 4) {
            mBankOpposite = "";
            ivBankOpposite.setVisibility(View.GONE);
        }
    }

}
