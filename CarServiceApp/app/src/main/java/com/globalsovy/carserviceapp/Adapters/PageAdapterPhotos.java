package com.globalsovy.carserviceapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.Car_Details_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarImage;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.alertDialogs.BackToLoginAlertDialog;
import com.globalsovy.carserviceapp.alertDialogs.DeleteImageDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class PageAdapterPhotos extends PagerAdapter {

    List<CarImage> carPhotoUrls;
    LayoutInflater inflater;
    MySharedPreferencies mySharedPreferencies;
    Context context;
    int idcar;
    Activity activity;
    Fragment fragment;
    HashMap<Integer, Bitmap> alreadyDownloaded;

    public PageAdapterPhotos(List<CarImage> carPhotoUrls, Context context, int idcar, Activity activity, Fragment fragment) {
        super();
        this.carPhotoUrls = carPhotoUrls;
        this.context = context;
        mySharedPreferencies = new MySharedPreferencies(context);
        this.idcar = idcar;
        this.activity = activity;
        this.fragment = fragment;
        alreadyDownloaded = new HashMap<>();
    }

    @Override
    public int getCount() {
        System.out.println("getCount");
        return carPhotoUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        System.out.println("INSTATIATEitem");
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_photo,container,false);
        CarImage currentCarImage = carPhotoUrls.get(position);

        final ImageView carImage;

        carImage = view.findViewById(R.id.photo);

        if (alreadyDownloaded.get(currentCarImage.getId()) == null){
            if (currentCarImage.getUrl().equals("getProfile")){
                SendHttpReqeustForProfile getProfil = new SendHttpReqeustForProfile(carImage,position);
                getProfil.execute();
            }else {
                SendHttpReqeustForImage getImg = new SendHttpReqeustForImage(carImage,position);
                getImg.execute();
            }
        }else {
            carImage.setImageBitmap(alreadyDownloaded.get(currentCarImage.getId()));
        }

        container.addView(view,0);
        if (currentCarImage.getId() != -1) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteImageDialog dialog = new DeleteImageDialog();
                    dialog.showDialog(activity,"Delete this image?","Image will be removed from this car",carPhotoUrls.get(position).getId(),position,fragment);
                }
            });
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    private class SendHttpReqeustForImage extends AsyncTask<String, Void, Bitmap> {

        CarImage image;
        ImageView carImage;
        HttpURLConnection connection;
        InputStream input;
        int position;

        SendHttpReqeustForImage(ImageView carImage, int position) {
            this.image = carPhotoUrls.get(position);
            this.carImage = carImage;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap myBitmap=null;
            try {
                //ipconfig;
                URL url = new URL(image.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return myBitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null){
                alreadyDownloaded.put(carPhotoUrls.get(position).getId(),result);
                carImage.setImageBitmap(result);
                connection.disconnect();
                this.cancel(true);
            }else {
                carImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_car));
            }
        }
    }

    private class SendHttpReqeustForProfile extends AsyncTask<String, Void, Bitmap> {

        ImageView carImage;
        HttpURLConnection connection;
        InputStream input;
        int position;

        SendHttpReqeustForProfile(ImageView carImage, int position) {
            this.carImage = carImage;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap myBitmap = null;
            try {
                //ipconfig;
                URL url = new URL(mySharedPreferencies.getIp() + "/getcarprofileimage?idcar=" + idcar);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            alreadyDownloaded.put(carPhotoUrls.get(position).getId(),result);
            carImage.setImageBitmap(result);
            connection.disconnect();
            this.cancel(true);
        }
    }

}