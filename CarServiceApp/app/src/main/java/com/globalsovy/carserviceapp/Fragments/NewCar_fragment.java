package com.globalsovy.carserviceapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Adapters.PageAdapter;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class NewCar_fragment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ConstraintLayout addCarPhoto;
    ImageView carPhoto;

    Spinner brandList;
    ArrayList<String> brands;
    ArrayAdapter<String> brandAdapter;

    Spinner modelList;
    ArrayList<String> models;
    ArrayAdapter<String> modelAdapter;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

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
        addCarPhoto = parent.findViewById(R.id.addPhotoCarLayout);
        carPhoto = parent.findViewById(R.id.currentCarPhoto);

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

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            carPhoto.setImageBitmap(bitmap);
            carPhoto.setVisibility(View.VISIBLE);
            cursor.close();

        }
    }
    public void setSpinnerSelected() {
        brandList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long l) {
                getCarModels(brands.get(index));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        modelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            brandAdapter = new ArrayAdapter<String>(getContext(),
            R.layout.spinner_item,R.id.brandItem,brands);
                brandList.setAdapter(brandAdapter);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
            public void onErrorResponse(VolleyError error) {
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

}


