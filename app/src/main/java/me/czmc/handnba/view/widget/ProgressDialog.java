package me.czmc.handnba.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import me.czmc.handnba.R;


/**
 * Created by czmc on 2016/9/22.
 */

public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context, R.style.progress_dialog);
        init();
    }


    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }
    private void init() {
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        msg.setText(getContext().getString(R.string.loading));
    }

    public static ProgressDialog show(Context context, CharSequence message) {
        ProgressDialog dialog =  new ProgressDialog(context);
        if(TextUtils.isEmpty(message)){
            dialog.show();
            return dialog;
        }
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(message);
        dialog.show();
        return dialog;
    }
    public static ProgressDialog show(Context context) {
        ProgressDialog dialog =  new ProgressDialog(context);
        dialog.show();
        return dialog;
    }
}