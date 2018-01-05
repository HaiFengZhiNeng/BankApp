package com.example.bankapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bankapp.R;

/**
 * 显示二维码
 *@author Guanluocang
 *created at 2017/12/29 11:34
*/
public class FindQRCodeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView tvLeft, tvRight;


    public FindQRCodeDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_find_qr, null);
        setContentView(view);
        (tvLeft) = view.findViewById(R.id.tv_left);
        (tvRight) = view.findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (dm.widthPixels * 0.8);
        window.setAttributes(lp);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                dismiss();
                break;
            case R.id.tv_right:
                dismiss();
                break;
        }
    }
}
