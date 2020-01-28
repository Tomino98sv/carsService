package com.globalsovy.carserviceapp.alertDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.globalsovy.carserviceapp.R;

public class DeleteImageDialog {
    public void showDialog(final Activity activity, String title, String msg, final int idImg) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView titleView =dialog.findViewById(R.id.titleExitAlert);
        TextView msgView = dialog.findViewById(R.id.msgExitAlert);
        TextView cancel = dialog.findViewById(R.id.cancelExitDialog);
        TextView leave = dialog.findViewById(R.id.leaveExitDialog);

        titleView.setText(title);
        msgView.setText(msg);
        leave.setText("Delete");

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.dismiss();
//                activity.finish();
//                System.exit(0);
                Toast.makeText(activity.getBaseContext(),"imageDeleted id "+idImg,Toast.LENGTH_SHORT).show();
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

