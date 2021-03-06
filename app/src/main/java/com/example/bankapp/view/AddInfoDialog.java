package com.example.bankapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.listener.OnConfimListener;

/**
 * 添加信息dialog
 * Created by dell on 2017/9/13.
 */

public class AddInfoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnConfimListener mOnConfimListener;

    private EditText etContent;

    public AddInfoDialog(Context context, OnConfimListener onConfimListener) {
        super(context, R.style.chooseDialog_Animation);
        this.mContext = context;
        this.mOnConfimListener = onConfimListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_common_confirm, null);
        setContentView(view);
        etContent = (EditText) view.findViewById(R.id.et_content);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (dm.widthPixels * 0.8);
        window.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                String content = etContent.getText().toString();
                if(content == null || content.equals("")){
                    Toast.makeText(mContext, "请填写姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                    dismiss();
                    mOnConfimListener.onConfim(content);
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
