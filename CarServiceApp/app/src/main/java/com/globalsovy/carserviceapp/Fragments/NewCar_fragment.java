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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class NewCar_fragment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ConstraintLayout addCarPhoto;
    ImageView carPhoto;

    Spinner dropList;
    String[] brands = new String[]{"Ferrari","BMW","Mazda"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_new_car, container, false);
        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        dropList = parent.findViewById(R.id.brandList);
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

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item,R.id.brandItem,brands);
        dropList.setAdapter(brandAdapter);

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
}


