package com.globalsovy.carserviceapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.Gallery_fragment;
import com.globalsovy.carserviceapp.Fragments.MyAppointments_fragment;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.Appointment;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.alertDialogs.CancelAppointmentDialog;
import com.globalsovy.carserviceapp.alertDialogs.DeleteImageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class RecycleViewAdapterAppItems extends RecyclerView.Adapter<RecycleViewAdapterAppItems.ViewHolder> {

    private ArrayList<Appointment> listOfAppointment;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Appointment appointment = listOfAppointment.get(position);
        holder.dateAndTime.setText(appointment.getDate()+" - "+appointment.getTime());
        holder.brandAndModel.setText(appointment.getBrand()+" "+appointment.getModel());
        holder.notes.setText(appointment.getMessage());

        ArrayList<String> urlImages = appointment.getUrlImages();
        holder.picturesContainer.removeAllViews();
        for (int i=0; i<urlImages.size();i++){
            ImageView imageView = new ImageView(context);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)activity).setCurrentApp(listOfAppointment.get(position));
                    ((MainActivity)activity).changeFragment(Gallery_fragment.class);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,150);
            params.setMargins(2,2,2,2);
            imageView.setLayoutParams(params);
            holder.picturesContainer.addView(imageView);
            if (alreadyDownloaded.get(urlImages.get(i)) != null){
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(alreadyDownloaded.get(urlImages.get(i)),128,128);
                imageView.setImageBitmap(thumbnail);
            }else {
                imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_close_black_24dp));
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

                CancelAppointmentDialog dialog = new CancelAppointmentDialog();
                dialog.showDialog(activity,((MainActivity)activity).getFragment(),appointment.getId(),position,appointment.getUrlImages());
//                cancelAppRequest(appointment.getId(),position,appointment.getUrlImages());
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date appDate = simpleDateFormat.parse(appointment.getDate());
            if (appDate.compareTo(today) < 0){ //appDate is after today
                holder.cancelAppointment.setEnabled(false);
                holder.cancelAppointment.setVisibility(View.GONE);
                holder.expandedItem.setBackgroundColor(activity.getResources().getColor(R.color.buttonLoginColor));
                holder.simpleItem.setBackgroundColor(activity.getResources().getColor(R.color.buttonLoginColor));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
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


    public void cancelAppRequest(final int idAppointment, final int position, final ArrayList<String> eraseOldBitMap) {
        String url = mySharedPreferencies.getIp()+"/cancelappointment";
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Canceling,Removing,Cleaning");
        progressDialog.show();
        final StringRequest cancelApp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                for (String url: eraseOldBitMap){
                    if (alreadyDownloaded.containsKey(url)){
                        alreadyDownloaded.remove(url);
                    }
                }
                ((MainActivity)activity).setAlreadyDownloadedApp(alreadyDownloaded);
                ((MyAppointments_fragment)((MainActivity)activity).getFragment()).removeAndRebuildList(position);
                if (progressDialog.isShowing()){
                    progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("appointmentID",idAppointment);
                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
        };

        cancelApp.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(cancelApp);
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
                        128, 128);
                carImage.setImageBitmap(thumbImage);
                connection.disconnect();
                this.cancel(true);
            }else {
                carImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_car));
            }
        }
    }
}

