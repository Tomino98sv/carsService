package com.globalsovy.carserviceapp.Adapters;

import android.app.ProgressDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.Car_Details_fragment;
import com.globalsovy.carserviceapp.Fragments.MyCars_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PageAdapter extends PagerAdapter {

    List<CarItem> carItems;
    LayoutInflater inflater;
    Context context;
    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    public PageAdapter(List<CarItem> carItems, Context context) {
        super();
        mySharedPreferencies = new MySharedPreferencies(context);
        myQueue = Volley.newRequestQueue(context);
        this.carItems = carItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return carItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.car_item,container,false);

        ImageView carImage;
        TextView brand,model;

        carImage = view.findViewById(R.id.carImage);
        brand = view.findViewById(R.id.brand);
        model = view.findViewById(R.id.model);

        new SendHttpReqeustForImage(carItems.get(position).getId(),carImage).execute();

        brand.setText(carItems.get(position).getBrand());
        model.setText(carItems.get(position).getModel());

        container.addView(view,0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).changeFragment(Car_Details_fragment.class);
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
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
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
        }
    }

}

