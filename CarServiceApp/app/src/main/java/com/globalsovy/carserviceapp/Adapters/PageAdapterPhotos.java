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
import java.util.List;

public class PageAdapterPhotos extends PagerAdapter {

    List<CarImage> carPhotoUrls;
    LayoutInflater inflater;
    MySharedPreferencies mySharedPreferencies;
    Context context;
    int idcar;
    Activity activity;

    public PageAdapterPhotos(List<CarImage> carPhotoUrls, Context context, int idcar, Activity activity) {
        super();
        this.carPhotoUrls = carPhotoUrls;
        this.context = context;
        mySharedPreferencies = new MySharedPreferencies(context);
        this.idcar = idcar;
        this.activity = activity;
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

        final ImageView carImage;

        carImage = view.findViewById(R.id.photo);

        if (carPhotoUrls.get(position).getUrl().equals("getProfile")){
            SendHttpReqeustForProfile getProfil = new SendHttpReqeustForProfile(carImage);
            getProfil.execute();
        }else {
            SendHttpReqeustForImage getImg = new SendHttpReqeustForImage(carPhotoUrls.get(position),carImage);
            getImg.execute();
        }
        container.addView(view,0);
        if (carPhotoUrls.get(position).getId() != -1) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteImageDialog dialog = new DeleteImageDialog();
                    dialog.showDialog(activity,"Delete this image?","Image will be removed from this car",carPhotoUrls.get(position).getId());
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

        SendHttpReqeustForImage(CarImage image, ImageView carImage) {
            this.image = image;
            this.carImage = carImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap ThumbImage=null;
            try {
                //ipconfig;
                URL url = new URL(image.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                final int THUMBSIZE = 254;

                ThumbImage = ThumbnailUtils.extractThumbnail(myBitmap,
                        THUMBSIZE, THUMBSIZE);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return ThumbImage;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null){
                carImage.setImageBitmap(result);
                connection.disconnect();
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        SendHttpReqeustForProfile(ImageView carImage) {
            this.carImage = carImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap ThumbImage=null;
            try {
                //ipconfig;
                URL url = new URL(mySharedPreferencies.getIp() + "/getcarprofileimage?idcar=" + idcar);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                final int THUMBSIZE = 254;

                ThumbImage = ThumbnailUtils.extractThumbnail(myBitmap,
                        THUMBSIZE, THUMBSIZE);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ThumbImage;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            carImage.setImageBitmap(result);
            connection.disconnect();
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.cancel(true);
        }
    }

}