package com.globalsovy.carserviceapp.alertDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.Car_Details_fragment;
import com.globalsovy.carserviceapp.Fragments.MyCars_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DeleteImageDialog {

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;
    Dialog dialog;
    int positionInArray;
    Fragment fragment;

    public void showDialog(final Activity activity, String title, String msg, final int idImg, final int positionInArray, final Fragment fragment) {

        mySharedPreferencies = new MySharedPreferencies(activity.getApplicationContext());
        myQueue = Volley.newRequestQueue(activity.getApplicationContext());

        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.positionInArray = positionInArray;
        this.fragment =fragment;


        TextView titleView =dialog.findViewById(R.id.titleExitAlert);
        TextView msgView = dialog.findViewById(R.id.msgExitAlert);
        TextView cancel = dialog.findViewById(R.id.cancelExitDialog);
        TextView delete = dialog.findViewById(R.id.leaveExitDialog);
        TextView setProfile = dialog.findViewById(R.id.specialOption);

        setProfile.setVisibility(View.VISIBLE);
        titleView.setText(title);
        msgView.setText(msg);
        setProfile.setText("Set as profile picture");
        delete.setText("Delete");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForDeletePhoto(idImg,activity);
            }
        });

        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setProfilePics(idImg,((MainActivity)activity).getCurrentIdCar());
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


    public void requestForDeletePhoto(final int idImg, final Activity activity){
        String url = mySharedPreferencies.getIp()+"/deleteimage";
        StringRequest deleteImg = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(activity.getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                ((Car_Details_fragment)fragment).adapterRebuild(positionInArray);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String errorMess = "Ecample";
                try {
                    errorMess = new String(error.networkResponse.data,"UTF-8");
                    System.out.println("errorMess "+errorMess);
                    Toast.makeText(activity.getBaseContext(),errorMess,Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("id",idImg);
                    String bodyString = body.toString();
                    System.out.println("BODY"+bodyString);
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        deleteImg.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(deleteImg);
    }

    public void setProfilePics(final int idImg, final int idcar) {

            String URL = mySharedPreferencies.getIp()+"/setcarprofileimg";
            StringRequest setProfilePics = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(com.android.volley.error.VolleyError error) {
                    try {
                        String message = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(fragment.getContext(),message,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(fragment.getContext(),"check your internet connection",Toast.LENGTH_LONG).show();
                    }
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
                        body.put("picture",idImg);
                        body.put("car",idcar);
                        String bodyString = body.toString();
                        return bodyString == null ? null : bodyString.getBytes("utf-8");
                    } catch (UnsupportedEncodingException | JSONException uee) {
                        return null;
                    }
                }
            };

            setProfilePics.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            myQueue.add(setProfilePics);
    }
}



