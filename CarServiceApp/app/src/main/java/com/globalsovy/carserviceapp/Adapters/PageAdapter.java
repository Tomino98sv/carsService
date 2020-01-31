package com.globalsovy.carserviceapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.Car_Details_fragment;
import com.globalsovy.carserviceapp.Fragments.MyCars_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarImage;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.alertDialogs.DeleteCarDialog;
import com.globalsovy.carserviceapp.alertDialogs.DeleteImageDialog;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class PageAdapter extends PagerAdapter {

    List<CarItem> carItems;
    LayoutInflater inflater;
    Context context;
    MySharedPreferencies mySharedPreferencies;
    Fragment fragment;
    Activity activity;
    HashMap<Integer, Bitmap> alreadyDownloaded;

    public PageAdapter(List<CarItem> carItems, Context context, Activity activity, Fragment fragment) {
        super();
        mySharedPreferencies = new MySharedPreferencies(context);
        this.carItems = carItems;
        this.context = context;
        this.fragment = fragment;
        this.activity = activity;
        alreadyDownloaded = new HashMap<>();
    }

    @Override
    public int getCount() {
        System.out.println("getCount");
        return carItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container,final int position) {
        System.out.println("INSTATIATEitem");
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.car_item,container,false);
        final CarItem currentCarItem = carItems.get(position);

        ImageView carImage;
        TextView brand,model;

        carImage = view.findViewById(R.id.carImage);
        brand = view.findViewById(R.id.brand);
        model = view.findViewById(R.id.model);

        if (alreadyDownloaded.get(currentCarItem.getId()) == null){
            SendHttpReqeustForImage getProgil = new SendHttpReqeustForImage(currentCarItem.getId(),carImage);
            getProgil.execute();
        }else {
            carImage.setImageBitmap(alreadyDownloaded.get(currentCarItem.getId()));
        }



        brand.setText(currentCarItem.getBrand());
        model.setText(currentCarItem.getModel());
        container.addView(view,0);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).changeFragment(Car_Details_fragment.class);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DeleteCarDialog dialog = new DeleteCarDialog();
                dialog.showDialog(activity,
                        "Delete this Car?",currentCarItem.getBrand()+" "+currentCarItem.getModel()+"\n will be removed from your list of cars",
                        currentCarItem.getId(),
                        position,
                        fragment);
                return false;
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


    private class SendHttpReqeustForImage extends AsyncTask<String, Void, Bitmap> {

        int id;
        ImageView carImage;
        HttpURLConnection connection;
        InputStream input;

        SendHttpReqeustForImage(int id, ImageView carImage) {
            this.id = id;
            this.carImage = carImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap ThumbImage=null;
            try {
                //ipconfig;
                URL url = new URL(mySharedPreferencies.getIp() + "/getcarprofileimage?idcar=" + id);
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
            alreadyDownloaded.put(id,result);
            carImage.setImageBitmap(result);
            connection.disconnect();
            this.cancel(true);
        }
    }

}

