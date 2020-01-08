package com.globalsovy.carserviceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ExitAlertDialog {

    public void showDialog(final Activity activity, String title, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView titleView =dialog.findViewById(R.id.titleExitAlert);
        TextView msgView = dialog.findViewById(R.id.msgExitAlert);
        TextView cancel = dialog.findViewById(R.id.cancelExitDialog);
        TextView leave = dialog.findViewById(R.id.leaveExitDialog);

        titleView.setText(title);
        msgView.setText(msg);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
                System.exit(0);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
