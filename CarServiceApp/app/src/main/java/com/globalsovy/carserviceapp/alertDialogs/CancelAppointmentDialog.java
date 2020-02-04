package com.globalsovy.carserviceapp.alertDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.MyAppointments_fragment;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;

import java.util.ArrayList;

public class CancelAppointmentDialog {

    Dialog dialog;

    public void showDialog(final Activity activity, final Fragment fragment, final int idAppointment, final int position, final ArrayList<String> eraseOldBitMap) {


        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.cancel_appointment_dialog);

        ImageView close =dialog.findViewById(R.id.dismissWindow);
        Button cancelApp = dialog.findViewById(R.id.cancelAppointmentFromDialog);

        cancelApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ((MyAppointments_fragment)fragment).cancelAppointmentFromFragment(idAppointment,position,eraseOldBitMap);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
