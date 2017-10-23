package com.nadoo.tacademy.eunbyul_nanu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Kyoonho on 2017-06-06.
 */

public class





CertificationDialog extends Dialog {

    ImageButton  register_btn, cancel_btn;
    EditText certification_number;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*다이알로그 외부 화면 흐리게 */
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_custom_dialog);

        certification_number = (EditText) findViewById(R.id.certifiation_number);
        register_btn = (ImageButton)findViewById(R.id.register_button);
        cancel_btn = (ImageButton)findViewById(R.id.cancel_button);

        if (mLeftClickListener != null && mRightClickListener != null) {
            cancel_btn.setOnClickListener(mLeftClickListener);
            register_btn.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            cancel_btn.setOnClickListener(mLeftClickListener);
        } else {

        }

    }
    public CertificationDialog(Context context, View.OnClickListener leftListenter, View.OnClickListener rightListenter){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListenter;
        this.mRightClickListener = rightListenter;
    }
}
