package com.globalsovy.carserviceapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class Details_New_Appointment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    Spinner myCarsSpinner;
    ArrayAdapter<String> adapterMyCars;
    ArrayList<String> listForSpinner;
    ArrayList<CarItem> myCars;

    EditText notes;
    ConstraintLayout addPic;
    Button saveNewAppointment;
    LinearLayout linearLayoutPics;

    TextView datePick;

    HashMap <Integer,String> urlPhotos;
    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    ProgressDialog progressDialog;

    String date = "";
    String time = "";
    int carId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_details_new_appointment, container, false);

        urlPhotos = new HashMap <>();
        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());
        progressDialog = new ProgressDialog(getContext());

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        toolbar = getActivity().findViewById(R.id.toolbar);
        myCarsSpinner = parent.findViewById(R.id.spinnerMyCars);
        notes = parent.findViewById(R.id.note);
        addPic = parent.findViewById(R.id.addPhotoAppointmentLayout);
        saveNewAppointment = parent.findViewById(R.id.saveAppointment);
        linearLayoutPics = parent.findViewById(R.id.appointmentImages);
        datePick = parent.findViewById(R.id.datePicked);


        myCars = ((MainActivity)getActivity()).getMyCars();
        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        date = ((MainActivity)getActivity()).getNewAppointmentDate();
        time = ((MainActivity)getActivity()).getNewAppointmentTime();

        String[] array = date.split("\\.",-1);
        String dayMonthYear = array[2]+"."+array[1]+"."+array[0];

        datePick.setText(dayMonthYear+" - "+time);

        toolbarTitle.setText("New Appointment");
        toolbarBtn.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(New_Appointment.class);
            }
        });
        if (ContextCompat.checkSelfPermission(((MainActivity)getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((MainActivity)getActivity()), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPicsMethod();
            }
        });

        saveNewAppointment.setEnabled(false);
        saveNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAppointmentRequest();
            }
        });

        setSpinner();
        setTextWatcher();
        return parent;
    }

    public void setSpinner(){
        listForSpinner = new ArrayList<>();
        for (CarItem carItem:myCars){
            listForSpinner.add(carItem.getBrand()+" "+carItem.getModel());
        }
        adapterMyCars = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,R.id.brandItem,listForSpinner);
        myCarsSpinner.setAdapter(adapterMyCars);

        myCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                carId = myCars.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addPicsMethod() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            String[] filePath = { MediaStore.Images.Media.DATA};
            Cursor cursor = ((MainActivity)getActivity()).getContentResolver().query(pickedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();

            addToView(imagePath);
        }
    }

    public void setTextWatcher() {
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0){
                    saveNewAppointment.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    saveNewAppointment.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                    saveNewAppointment.setEnabled(false);
                }else {
                    saveNewAppointment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    saveNewAppointment.setTextColor(getResources().getColor(R.color.white));
                    saveNewAppointment.setEnabled(true);
                }
            }
        });
    }

    public void addToView(String imgPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapPhoto = BitmapFactory.decodeFile(imgPath, options);
        bitmapPhoto = ThumbnailUtils.extractThumbnail(bitmapPhoto,254,254);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout =  inflater.inflate(R.layout.appoint_car_photo_item, null, false);
        ConstraintLayout container = (ConstraintLayout) layout.findViewById(R.id.appoint_pics_parent);
        ImageView imageView = layout.findViewById(R.id.appointPics);
        imageView.setImageBitmap(bitmapPhoto);

        final int id = ViewCompat.generateViewId();
        container.setId(id);

        ImageView remove = layout.findViewById(R.id.removePics);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutPics.removeView(getActivity().findViewById(id));
                urlPhotos.remove(id);

            }
        });
        urlPhotos.put(id,imgPath);
        linearLayoutPics.addView(container);
    }
    public void sendAppointmentRequest() {
        String url = mySharedPreferencies.getIp()+"/createappointment";
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        JsonObjectRequest createAppoint = new JsonObjectRequest(Request.Method.POST, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idApp = response.getInt("insertid");
                            int position = 0;
                            for (Map.Entry<Integer,String> entry : urlPhotos.entrySet()){
                                sendImageRequest(position,idApp,entry.getValue());
                                position++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
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
                    body.put("date",date);
                    body.put("time",time);
                    body.put("carid",carId);
                    body.put("message",notes.getText());

                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };



        createAppoint.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myQueue.add(createAppoint);
    }
    public void sendImageRequest(final int position, final int idAppointment, final String pathImage) {

        String url = mySharedPreferencies.getIp()+"/insertappimage";
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing() && position == urlPhotos.size()-1){
                            progressDialog.cancel();
                            ((MainActivity)getActivity()).changeFragment(MyAppointments_fragment.class);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        Map<String,String> images = new HashMap<>();

        smr.addStringParam("id", String.valueOf(idAppointment));
        smr.addFile("image", pathImage);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myQueue.add(smr);
    }

}

