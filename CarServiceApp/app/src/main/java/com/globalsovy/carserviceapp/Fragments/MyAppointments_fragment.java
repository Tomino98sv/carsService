package com.globalsovy.carserviceapp.Fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Adapters.PageAdapterPhotos;
import com.globalsovy.carserviceapp.Adapters.RecycleViewAdapterAppItems;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.Appointment;
import com.globalsovy.carserviceapp.POJO.CarImage;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class MyAppointments_fragment extends Fragment {

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    RecyclerView recyclerView;
    RecycleViewAdapterAppItems adapter;
    ArrayList<Appointment> appointments;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_appointments, container, false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        recyclerView = parent.findViewById(R.id.myAppointmentRecView);
        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbarTitle.setText("My Appointments");
        toolbarBtn.setImageResource(R.drawable.add);
        toolbarBtn.setVisibility(View.VISIBLE);
        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(New_Appointment.class);
            }
        });

        getAppointments();

        return parent;
    }

    public void removeAndRebuildList(int position ){
        appointments.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void cancelAppointmentFromFragment(final int idAppointment, final int position, final ArrayList<String> eraseOldBitMap){
        adapter.cancelAppRequest(idAppointment,position,eraseOldBitMap);
    }

    public void getAppointments() {
        String url = mySharedPreferencies.getIp()+"/getappointments";

        JsonArrayRequest getAppointments = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                appointments = new ArrayList<>();
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject appointmentJSON = response.getJSONObject(i);
                        Appointment appointmentObject = new Appointment(
                                appointmentJSON.getInt("id"),
                                appointmentJSON.getString("date"),
                                appointmentJSON.getString("time"),
                                appointmentJSON.getString("message"),
                                appointmentJSON.getString("brand"),
                                appointmentJSON.getString("model")
                        );
                        appointments.add(appointmentObject);
                        getAppointmentImages(i,appointmentObject.getId(), response.length()-1);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = new String(error.networkResponse.data,"UTF-8");
                    if (message.equals("You don't have any appointments.")){
                        ((MainActivity)getActivity()).changeFragment(NoAppointments_fragment.class);
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
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
                    body.put("iduser",mySharedPreferencies.getIdLogin());
                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        getAppointments.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(getAppointments);
    }


    public void getAppointmentImages(final int positionArray, final int idAppointment, final int last) {
        final String url = mySharedPreferencies.getIp()+"/getappimages";

        final StringRequest getImages = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> urls = new ArrayList<>();

                response = response.replaceAll("[\"]",""); //odstran "
                response = response.replaceAll("[\\]]",""); // odstran ]
                response = response.replaceAll("[\\[]",""); // odstran [
                String[] array = response.split("\\,", -1); // rozdel podla ,
                for (int i = 0; i<array.length;i++){
                    urls.add(array[i]);
                }

                appointments.get(positionArray).setUrlImages(urls);
                if (positionArray == last) {

                    adapter = new RecycleViewAdapterAppItems(appointments,getContext(),getActivity());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = new String(error.networkResponse.data,"UTF-8");
                    if (!message.equals("Appointment images not found..")){
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"check your internet connection",Toast.LENGTH_LONG).show();
                }

                if (positionArray == last) {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    adapter = new RecycleViewAdapterAppItems(appointments,getContext(),getActivity());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                adapter.notifyDataSetChanged();
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
                    body.put("appointmentID",idAppointment);
                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        getImages.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(getImages);
    }
}
