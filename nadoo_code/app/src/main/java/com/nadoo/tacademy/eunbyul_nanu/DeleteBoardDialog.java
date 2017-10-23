package com.nadoo.tacademy.eunbyul_nanu;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * Created by Kyoonho on 2017-06-13.
 */

public class DeleteBoardDialog extends Dialog {
    private ImageButton delete_btn, cancel_btn;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*다이알로그 외부 화면 흐리게 */
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.delete_board_dialog);

        delete_btn = (ImageButton)findViewById(R.id.delete_btn);
        cancel_btn = (ImageButton)findViewById(R.id.cancel_btn);


        if (mLeftClickListener != null && mRightClickListener != null) {
            cancel_btn.setOnClickListener(mLeftClickListener);
            delete_btn.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            delete_btn.setOnClickListener(mRightClickListener);
        } else {

        }

    }
    public DeleteBoardDialog(Context context, View.OnClickListener leftListenter, View.OnClickListener rightListenter){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListenter;
        this.mRightClickListener = rightListenter;
    }

}
