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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.Appointment;
import com.globalsovy.carserviceapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RecycleViewAdapterAppItems extends RecyclerView.Adapter<RecycleViewAdapterAppItems.ViewHolder> {

    private ArrayList<Appointment> listOfAppointment;
//    private HashMap<Integer,ViewHolder> items;
    Context context;
    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;
    Activity activity;
    HashMap<String,Bitmap> alreadyDownloaded;

    Animation scaleUp;
    Animation scaleDown;


    public RecycleViewAdapterAppItems(ArrayList<Appointment> listOfAppointment, Context context, Activity activity) {
        this.listOfAppointment = listOfAppointment;
        this.context = context;
        mySharedPreferencies = new MySharedPreferencies(context);
        myQueue = Volley.newRequestQueue(context);
        this.activity = activity;
        this.alreadyDownloaded = ((MainActivity)activity).getAlreadyDownloadedApp();

//        items = new HashMap<>();
        scaleUp = AnimationUtils.loadAnimation(context,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(context,R.anim.scale_down);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Appointment appointment = listOfAppointment.get(position);
//        items.put(position,holder);
        holder.dateAndTime.setText(appointment.getDate()+" - "+appointment.getTime());
        holder.brandAndModel.setText(appointment.getBrand()+" "+appointment.getModel());
        holder.notes.setText(appointment.getMessage());

        ArrayList<String> urlImages = appointment.getUrlImages();
        for (int i=0; i<urlImages.size();i++){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(2,2,2,2);
            imageView.setLayoutParams(params);
            holder.picturesContainer.addView(imageView);
            if (alreadyDownloaded.get(urlImages.get(i)) != null){
                imageView.setImageBitmap(alreadyDownloaded.get(urlImages.get(i)));
            }else {
                new SendHttpReqeustForImage(urlImages.get(i),imageView,activity).execute();
            }
        }

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appointment.isExpandV()){
                    holder.expand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    holder.expandedItem.startAnimation(scaleDown);
                    holder.expandedItem.setVisibility(View.VISIBLE);
                    appointment.setExpandV(true);
                }else {
                    holder.expand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
//                    holder.expandedItem.startAnimation(scaleUp);
                    holder.expandedItem.setVisibility(View.GONE);
                    appointment.setExpandV(false);
                }
            }
        });

        holder.cancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("Get item Count recycle view "+listOfAppointment.size());
        return listOfAppointment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        LinearLayout simpleItem;
        TextView dateAndTime;
        TextView brandAndModel;
        ImageView expand;

        LinearLayout expandedItem;
        TextView notes;
        LinearLayout picturesContainer;
        TextView cancelAppointment;


        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parentItemAppointment);
            simpleItem = itemView.findViewById(R.id.simpleItem);;
            dateAndTime= itemView.findViewById(R.id.dateAndtime);;
            brandAndModel= itemView.findViewById(R.id.brandAndmodel);;
            expand = itemView.findViewById(R.id.expand);

            expandedItem= itemView.findViewById(R.id.expandItem);;
            notes= itemView.findViewById(R.id.note);;
            picturesContainer= itemView.findViewById(R.id.picturesApint);;
            cancelAppointment= itemView.findViewById(R.id.cancelAppointment);
        }
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
            Bitmap ThumbImage=null;
            try {
                //ipconfig;
                URL url = new URL(urlString);
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
            if (result != null){
                alreadyDownloaded.put(urlString,result);
                ((MainActivity)activity).setAlreadyDownloadedApp(alreadyDownloaded);
                carImage.setImageBitmap(result);
                connection.disconnect();
                this.cancel(true);
            }else {
                carImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_car));
            }
        }
    }
}

