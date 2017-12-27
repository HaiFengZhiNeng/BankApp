/*
 * Created by 岱青海蓝信息系统(北京)有限公司 on 17-5-26 下午3:53
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
 * Created by Administrator on 2016/8/8.
 * 查看图片或删除图片 PopupWindow
 */
public class PictureHandlePopupWindow extends PopupWindow {
    private Activity context;

    public PictureHandlePopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_pic_handle, null);
        Button btnBigPic = (Button) view.findViewById(R.id.btn_big_picture);
        Button btnDelete = (Button) view.findViewById(R.id.btn_delete);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnBigPic.setOnClickListener(itemsOnClick);
        btnDelete.setOnClickListener(itemsOnClick);
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
