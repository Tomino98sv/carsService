package com.globalsovy.carserviceapp.alertDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class BackToLoginAlertDialog {

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    Dialog dialog;
    Activity activity;

    public void showDialog(final Activity activity, String title, String msg) {
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mySharedPreferencies = new MySharedPreferencies(activity.getApplicationContext());
        myQueue = Volley.newRequestQueue(activity.getApplicationContext());

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
                logout();
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

    public void logout() {
        String URL = mySharedPreferencies.getIp()+"/logout";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Intent login = new Intent(activity.getApplicationContext(), LoginActivity.class);
                mySharedPreferencies.setToken("logouted");
                activity.startActivity(login);
                activity.finish();
                Toast.makeText(activity.getApplicationContext(),"See you soon",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(),"Error "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("login",mySharedPreferencies.getLogin());
                    body.put("token",mySharedPreferencies.getToken());

                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(stringRequest);
    }
}
