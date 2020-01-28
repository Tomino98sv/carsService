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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Add_Photos extends Fragment {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView toolbarTitle;
    private ImageView toolbarBtn;

    LinearLayout alreadyPicked;
    Button saveImages;
    Button pickImages;

    int idcar;
    private List<String> photoPaths;

    private MySharedPreferencies mySharedPreferencies;
    private RequestQueue myQueue;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_add_photos, container, false);

        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());

        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        alreadyPicked = parent.findViewById(R.id.pictureGallery);
        saveImages = parent.findViewById(R.id.sendImages);
        pickImages = parent.findViewById(R.id.pickImages);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(Car_Details_fragment.class);
            }
        });
        toolbarTitle.setText("Add photos");
        toolbarBtn.setVisibility(View.GONE);

        idcar = ((MainActivity)getActivity()).getCurrentIdCar();
        photoPaths = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(((MainActivity)getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((MainActivity)getActivity()), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }

        pickImages.setOnClickListener(new View.OnClickListener() {
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

        saveImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Sending "+ photoPaths.size() +" images");
                progressDialog.show();
                for (int i=0;i<photoPaths.size();i++) {
                    sendPhotoRequest(photoPaths.get(i),i+1);
                }
            }
        });

        return parent;
    }

    public static final int PICK_IMAGE = 1;

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if (resultCode == RESULT_OK && data != null) {
//            if(requestCode == 1){
//                photoPaths = new ArrayList<String>();
//                String[] imagesPath = data.getStringExtra("data").split("\\|");
//                try{
//                    alreadyPicked.removeAllViews();
//                }catch (Throwable e){
//                    e.printStackTrace();
//                }
//                for (int i=0;i<imagesPath.length;i++){
//                    photoPaths.add(imagesPath[i]);
//                    Bitmap yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
//                    ImageView imageView = new ImageView(getContext());
//                    imageView.setImageBitmap(yourbitmap);
//                    imageView.setAdjustViewBounds(true);
//                    alreadyPicked.addView(imageView);
//                }
//            }
//        }
//
//    }

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

            photoPaths.add(imagePath);
            alreadyPicked.removeAllViews();
            for (int i=0;i<photoPaths.size();i++){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmapPhoto = BitmapFactory.decodeFile(photoPaths.get(i), options);
                bitmapPhoto = ThumbnailUtils.extractThumbnail(bitmapPhoto,
                        254, 254);
                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bitmapPhoto);
                imageView.setAdjustViewBounds(true);
                alreadyPicked.addView(imageView);
            }
            cursor.close();



        }
    }

    public void sendPhotoRequest(String imagePath, final int position) {
        String url = mySharedPreferencies.getIp()+"/sendimage";
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing() && position == photoPaths.size()){
                            progressDialog.cancel();
                            ((MainActivity)getActivity()).changeFragment(Car_Details_fragment.class);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        smr.addStringParam("carid", String.valueOf(idcar));
        smr.addFile("image", imagePath);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myQueue.add(smr);
    }
}
