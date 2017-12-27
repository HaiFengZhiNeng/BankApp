/*
 * Created by 岱青海蓝信息系统(北京)有限公司 on 17-5-22 下午3:10
 * Copyright (c) 2017. All rights reserved.
 */

package com.example.bankapp.view.takephoto;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.bankapp.R;
import com.example.bankapp.util.ScreenUtils;


/**
 * Created by cheng on 2016/8/30.
 * 拍照或从相册选择图片
 */
public class PictureSelectPopupWindow extends PopupWindow {

    private Activity context;

    public PictureSelectPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_pic_select, null);
        Button btnTakePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        Button btnPickPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnTakePhoto.setOnClickListener(itemsOnClick);
        btnPickPhoto.setOnClickListener(itemsOnClick);
        btnCancel.setOnClickListener(itemsOnClick);

        int width = (int) (ScreenUtils.getScreenWidth(context) * 0.9);
        setWidth(width);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        update();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0+
        context.getWindow().setAttributes(lp);
    }
}
