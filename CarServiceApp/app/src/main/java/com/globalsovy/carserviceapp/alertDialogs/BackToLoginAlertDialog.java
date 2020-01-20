package com.globalsovy.carserviceapp.alertDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.R;

public class BackToLoginAlertDialog {

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

        leave.setText("confirm");

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent login = new Intent(activity.getApplicationContext(), LoginActivity.class);
                activity.startActivity(login);
                activity.finish();
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
