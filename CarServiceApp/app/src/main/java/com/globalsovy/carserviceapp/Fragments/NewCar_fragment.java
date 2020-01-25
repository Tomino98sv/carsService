package com.globalsovy.carserviceapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.app.Activity.RESULT_OK;

public class NewCar_fragment extends Fragment {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView toolbarTitle;
    private ImageView toolbarBtn;

    private ConstraintLayout addCarPhoto;
    private ImageView carPhoto;

    private Spinner brandList;
    private ArrayList<String> brands;
    private ArrayAdapter<String> brandAdapter;

    private Spinner modelList;
    private ArrayList<String> models;
    private ArrayAdapter<String> modelAdapter;

    private Spinner transmissionList;
    private String[] transmissions = {"Manual","Automatic"};
    private ArrayAdapter<String> transmissionAdapter;

    private Spinner fuelList;
    private String[] fuels = {"Gasoline","Diesel","Liquified Petroleum","Natural Gas","Ethanol","Bio-diesel","Eletric"};
    private ArrayAdapter<String> fuelAdapter;

    private Spinner vintageList;
    private ArrayList<Integer> vintages;
    private ArrayAdapter<Integer> vintageAdapter;

    private Spinner volumeList;
    private ArrayList<Double> volumes;
    private ArrayAdapter<Double> volumeAdapter;

    private EditText colorHex;
    private EditText spzEditText;
    private EditText milageEditText;
    private ImageView pickedColor;
    private String photoPath = "";

    private MySharedPreferencies mySharedPreferencies;
    private RequestQueue myQueue;
    private Button sendNewCar;

    private ProgressDialog progressDialog;

    private int idcar;
    private int idCarPhoto;

    private String pickBrand="";
    private String pickModel="";
    private String pickcolor="";
    private int pickvintage=0;
    private int pickkilom=0;
    private String pickSPZ="";
    private String pickfuel="";
    private boolean picktrans=false;
    private double pickvolume=0.0;

    private boolean millageFilled = false;
    private boolean spzFilled = false;
    private boolean colorFilled = false;

    private boolean customProfileImg = false;
    private boolean carAdded = false;
    private boolean carPhotoAdded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_new_car, container, false);

        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());

        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        brandList = parent.findViewById(R.id.brandList);
        modelList = parent.findViewById(R.id.modelList);
        transmissionList = parent.findViewById(R.id.transmissionList);
        fuelList = parent.findViewById(R.id.fuelList);
        vintageList = parent.findViewById(R.id.vintageList);
        volumeList = parent.findViewById(R.id.volumeList);
        colorHex = parent.findViewById(R.id.colorList);
        pickedColor = parent.findViewById(R.id.colorPicked);
        spzEditText = parent.findViewById(R.id.spzList);
        milageEditText = parent.findViewById(R.id.milageList);

        addCarPhoto = parent.findViewById(R.id.addPhotoCarLayout);
        carPhoto = parent.findViewById(R.id.currentCarPhoto);
        sendNewCar = parent.findViewById(R.id.addNewCarButton);
        sendNewCar.setEnabled(false);
        sendNewCar.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
        sendNewCar.setTextColor(getResources().getColor(R.color.buttonLoginColor));

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyCars_fragment.class);
            }
        });
        toolbarTitle.setText("New car");
        toolbarBtn.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(((MainActivity)getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((MainActivity)getActivity()), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }

        addCarPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        sendNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    pickcolor = colorHex.getText().toString();
                    pickSPZ = spzEditText.getText().toString();
                    pickkilom = Integer.valueOf(milageEditText.getText().toString());
                    if (!photoPath.equals("")){
                        sendPhotoRequest();
                        customProfileImg =true;
                    }
                    sendnewCarRequest();
            }
        });
        setSpinnerOptions();
        setSpinnerSelected();
        getCarBrands();
        return parent;
    }

    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            String[] filePath = { MediaStore.Images.Media.DATA};
            Cursor cursor = ((MainActivity)getActivity()).getContentResolver().query(pickedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            photoPath = imagePath;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmapPhoto = BitmapFactory.decodeFile(imagePath, options);

            carPhoto.setImageBitmap(bitmapPhoto);
            carPhoto.setVisibility(View.VISIBLE);
            cursor.close();

        }
    }
    public void setSpinnerSelected() {
        brandList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long l) {
                getCarModels(brands.get(index));
                pickBrand = brands.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        modelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                pickModel = models.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        transmissionList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                picktrans = transmissions[index].equals("Manual");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fuelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                pickfuel = fuels[index];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        vintageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                pickvintage = vintages.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        volumeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                pickvolume = volumes.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void setSpinnerOptions() {
        vintages = new ArrayList<>();
        volumes = new ArrayList<>();

        int startYear = 1960;
        for (int i=0;i<61;i++){
            vintages.add(Integer.valueOf(startYear+i));
        }
        double startVolume = 0.8;
        for (double i=startVolume;i<4.1;i+=0.1){
            double result = Math.round(i*10);
            result = result/10;
            volumes.add(Double.valueOf(result));
        }

        pickedColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(),pickedColor.getSolidColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        pickedColor.setBackgroundColor(color);
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));
                        colorHex.setText(hexColor);
                    }
                });
                colorPicker.show();
            }
        });
        colorHex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.equals("")){
                    colorFilled = false;
                }else {
                    colorFilled = true;
                }
                try {
                    pickedColor.setBackgroundColor(Color.parseColor(editable.toString()));
                }catch (Exception e){
                    e.printStackTrace();
                    colorFilled = false;
                }
                if (colorFilled && millageFilled && spzFilled){
                    sendNewCar.setEnabled(true);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    sendNewCar.setTextColor(getResources().getColor(R.color.white));
                }
                if (!colorFilled || !spzFilled || !millageFilled){
                    sendNewCar.setEnabled(false);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    sendNewCar.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                }
            }
        });
        spzEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (String.valueOf(editable).equals("")){
                    spzFilled = false;
                }else {
                    spzFilled = true;
                }
                if (colorFilled && millageFilled && spzFilled){
                    sendNewCar.setEnabled(true);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    sendNewCar.setTextColor(getResources().getColor(R.color.white));
                }
                if (!colorFilled || !spzFilled || !millageFilled){
                    sendNewCar.setEnabled(false);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    sendNewCar.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                }
            }
        });
        milageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (String.valueOf(editable).equals("")){
                    millageFilled = false;
                }else {
                    millageFilled = true;
                }
                if (colorFilled && millageFilled && spzFilled){
                    sendNewCar.setEnabled(true);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    sendNewCar.setTextColor(getResources().getColor(R.color.white));
                }
                if (!colorFilled || !spzFilled || !millageFilled){
                    sendNewCar.setEnabled(false);
                    sendNewCar.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    sendNewCar.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                }
            }
        });

        transmissionAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,R.id.brandItem,transmissions);
        fuelAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,R.id.brandItem,fuels);
        vintageAdapter = new ArrayAdapter<Integer>(getContext(), R.layout.spinner_item,R.id.brandItem,vintages);
        volumeAdapter = new ArrayAdapter<Double>(getContext(), R.layout.spinner_item,R.id.brandItem,volumes);

        transmissionList.setAdapter(transmissionAdapter);
        fuelList.setAdapter(fuelAdapter);
        vintageList.setAdapter(vintageAdapter);
        volumeList.setAdapter(volumeAdapter);

    }
    public void getCarBrands() {
        String URL = mySharedPreferencies.getIp()+"/getbrands";

        JsonArrayRequest brandRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                brands = new ArrayList<>();
                for (int i =0; i<response.length();i++){
                    try {
                        JSONObject brand = response.getJSONObject(i);
                        brands.add(brand.getString("brand"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            brandAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,R.id.brandItem,brands);
                brandList.setAdapter(brandAdapter);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                brands = new ArrayList<>();
            }
        }) {
        };

        brandRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(brandRequest);
    }
    public void getCarModels(final String brand) {
        String URL = mySharedPreferencies.getIp()+"/getmodels";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                models = new ArrayList<>();
                for (int i =0; i<response.length();i++){
                    try {
                        JSONObject model = response.getJSONObject(i);
                        models.add(model.getString("model"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                modelAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_item,R.id.brandItem,models);
                modelList.setAdapter(modelAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                ((MainActivity)getActivity()).changeFragment(NoCars_fragment.class);
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
                    body.put("brand",brand);
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
    public void sendnewCarRequest() {
        String URL = mySharedPreferencies.getIp()+"/addcar";
        JsonObjectRequest sendNewCar = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    carAdded = true;
                    idcar = response.getInt("id");
                    if (customProfileImg){
                        setProfilePics();
                    }else{
                        ((MainActivity)getActivity()).changeFragment(MyCars_fragment.class);
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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();

                    JSONObject auth = new JSONObject();
                    auth.put("login",mySharedPreferencies.getLogin());
                    auth.put("token",mySharedPreferencies.getToken());
                    body.put("token",auth);
                    body.put("userID",mySharedPreferencies.getIdLogin());
                    body.put("brand",pickBrand);
                    body.put("model",pickModel);
                    body.put("color",pickcolor);
                    body.put("vintage",pickvintage);
                    body.put("kilometrage",pickkilom);
                    body.put("spz",pickSPZ);
                    body.put("fuel",pickfuel);
                    body.put("transmission",picktrans);
                    body.put("volume",pickvolume);

                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        sendNewCar.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(sendNewCar);
    }
    public void sendPhotoRequest() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sending image");
        progressDialog.show();
        String url = mySharedPreferencies.getIp()+"/sendimage";
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response from sending image "+response);
                        try {
                            JSONObject answer = new JSONObject(response);
                            idCarPhoto = answer.getInt("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (progressDialog.isShowing()){
                            progressDialog.cancel();
                        }
                        carPhotoAdded = true;
                        setProfilePics();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        smr.addStringParam("carid", String.valueOf(35));
        smr.addFile("image", photoPath);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myQueue.add(smr);
    }

    public void setProfilePics() {
        if (carPhotoAdded && carAdded){

            String URL = mySharedPreferencies.getIp()+"/setcarprofileimg";
            StringRequest setProfilePics = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                            ((MainActivity)getActivity()).changeFragment(MyCars_fragment.class);
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
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() {
                    try {
                        JSONObject body = new JSONObject();
                        body.put("picture",idCarPhoto);
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
}


