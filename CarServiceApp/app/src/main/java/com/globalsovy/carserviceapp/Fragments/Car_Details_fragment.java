package com.globalsovy.carserviceapp.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Adapters.PageAdapter;
import com.globalsovy.carserviceapp.Adapters.PageAdapterPhotos;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarDetails;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.alertDialogs.BackToLoginAlertDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Car_Details_fragment extends Fragment {

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    CarDetails carDetails;

    TextView brand;
    TextView model;
    TextView spz;
    TextView vintage;
    TextView kilometrage;
    TextView fuel;
    TextView transmission;
    TextView color;
    ImageView pickedColor;
    TextView getPdfManual;
    TextView volume;

    ViewPager carPics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_car__details,container,false);
        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());

        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        carPics = parent.findViewById(R.id.carPhoto);
        brand = parent.findViewById(R.id.brandVstCar);
        model = parent.findViewById(R.id.modelVstCar);
        spz = parent.findViewById(R.id.spzVstCar);
        vintage = parent.findViewById(R.id.vintageVstCar);
        kilometrage = parent.findViewById(R.id.kilometreVstCar);
        fuel = parent.findViewById(R.id.fuelVstCar);
        transmission = parent.findViewById(R.id.transmissionVstCar);
        color = parent.findViewById(R.id.colorVstCar);
        pickedColor = parent.findViewById(R.id.pickedColorOnVstCar);
        getPdfManual = parent.findViewById(R.id.pdfManual);
        volume = parent.findViewById(R.id.volumeVstCar);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyCars_fragment.class);
            }
        });
        toolbarBtn.setVisibility(View.GONE);

        requestForPhoto(((MainActivity)getActivity()).getCurrentIdCar());
        reqeustCarDetails(((MainActivity)getActivity()).getCurrentIdCar());
        return parent;
    }
    public void reqeustCarDetails(final int carId) {
        String url = mySharedPreferencies.getIp()+"/getcarinfo";
        JsonObjectRequest carDetailsReq = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("RESPONSE");
                try {
                    carDetails = new CarDetails(
                            response.getInt("id"),
                            response.getInt("userid"),
                            response.getInt("carid"),
                            response.getString("SPZ"),
                            response.getString("color"),
                            response.getInt("vintage"),
                            response.getInt("kilometrage"),
                            response.getInt("profileImgID"),
                            response.getString("fuel"),
                            response.getInt("manualtrans")==1,
                            response.getDouble("volume"),
                            response.getString("brand"),
                            response.getString("model"),
                            response.getString("docPath")
                            );
                    System.out.println(carDetails.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                setFields();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = new String(error.networkResponse.data,"UTF-8");
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_LONG).show();
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
                    body.put("id",carId);
                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        carDetailsReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(carDetailsReq);
    }

    public void setFields() {
        toolbarTitle.setText(carDetails.getModel());
        brand.setText(carDetails.getBrand());
        model.setText(carDetails.getModel());
        spz.setText(carDetails.getSPZ());
        vintage.setText(String.valueOf(carDetails.getVintage()));
        kilometrage.setText(String.valueOf(carDetails.getKilometrage()));
        fuel.setText(carDetails.getFuel());
        volume.setText(String.valueOf(carDetails.getVolume()));
        if (carDetails.isManualtrans()){
            transmission.setText("Manual");
        }else {
            transmission.setText("Automatic");
        }
        color.setText(carDetails.getColor());
        try {
            pickedColor.setBackgroundColor(Color.parseColor(carDetails.getColor()));
        }catch (Exception e){
            e.getMessage();
            pickedColor.setBackgroundColor(R.drawable.fill_hard);
        }
        getPdfManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"downloading pdf manual",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void requestForPhoto(final int idcar){
        String url = mySharedPreferencies.getIp()+"/getcarimages";
        JsonArrayRequest carPicsRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<String> carPhotos = new ArrayList<>();
                carPhotos.add("getProfile");
                try {
                    for (int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        carPhotos.add(jsonObject.getString("path"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                PageAdapterPhotos pageAdapterPhotos = new PageAdapterPhotos(carPhotos,getContext(),idcar);
                carPics.setAdapter(pageAdapterPhotos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                List<String> carPhotos = new ArrayList<>();
                carPhotos.add("getProfile");
                PageAdapterPhotos pageAdapterPhotos = new PageAdapterPhotos(carPhotos,getContext(),idcar);
                carPics.setAdapter(pageAdapterPhotos);
                try {
                    String message = new String(error.networkResponse.data,"UTF-8");
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_LONG).show();
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
                    body.put("idcar",idcar);
                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        carPicsRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(carPicsRequest);
    }

}
