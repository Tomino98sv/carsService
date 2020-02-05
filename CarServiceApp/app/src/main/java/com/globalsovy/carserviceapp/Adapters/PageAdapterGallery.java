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

import com.globalsovy.carserviceapp.Fragments.Car_Details_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.alertDialogs.DeleteCarDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageAdapterGallery extends PagerAdapter {

    ArrayList<String> carPhotos;
    Context context;
    Activity activity;
    HashMap<String, Bitmap> alreadyDownloaded;

    public PageAdapterGallery(ArrayList<String> carPhotos, Context context, Activity activity){
        super();
        this.carPhotos = carPhotos;
        this.context = context;
        this.activity = activity;
        alreadyDownloaded = ((MainActivity)activity).getAlreadyDownloadedApp();
    }

    @Override
    public int getCount() {
        return carPhotos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_photo,container,false);

        ImageView carImage;
        carImage = view.findViewById(R.id.photo);
        carImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.close));
        if (alreadyDownloaded.get(carPhotos.get(position)) == null){
            new SendHttpReqeustForImage(carPhotos.get(position),carImage,activity).execute();
        }else {
            Bitmap thumbImage= ThumbnailUtils.extractThumbnail(alreadyDownloaded.get(carPhotos.get(position)),
                    512, 512);
            carImage.setImageBitmap(thumbImage);
        }

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    private class SendHttpReqeustForImage extends AsyncTask<String, Void, Bitmap> {

        String urlString;
        ImageView carImage;
        HttpURLConnection connection;
        InputStream input;
        Activity activity;

        SendHttpReqeustForImage(String urlString, ImageView carImage,Activity activity) {
            this.urlString = urlString;
            this.carImage = carImage;
            this.activity = activity;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap myBitmap=null;
            try {
                //ipconfig;
                URL url = new URL(urlString);
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
            if (result != null){
                alreadyDownloaded.put(urlString,result);
                ((MainActivity)activity).setAlreadyDownloadedApp(alreadyDownloaded);
                Bitmap thumbImage= ThumbnailUtils.extractThumbnail(result,
                        512, 512);
                carImage.setImageBitmap(thumbImage);
                connection.disconnect();
                this.cancel(true);
            }else {
                carImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_car));
            }
        }
    }

}
