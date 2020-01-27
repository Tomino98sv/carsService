package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.Adapters.PageAdapter;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyCars_fragment extends Fragment {

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ViewPager viewPager;
    PageAdapter adapter;
    List<CarItem> cars;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_my_cars,container,false);
        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(getContext());

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        viewPager = parent.findViewById(R.id.view_Pager);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbarTitle.setText("My Cars");
        toolbarBtn.setImageResource(R.drawable.add);
        toolbarBtn.setVisibility(View.VISIBLE);

        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("add car clicked");
                ((MainActivity)getActivity()).changeFragment(NewCar_fragment.class);
            }
        });

        getCars();

        viewPager.setPadding(50,50,50,50);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((MainActivity)getActivity()).setCurrentIdCar(cars.get(position).getId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return parent;
    }

    public void getCars() {
        String URL = mySharedPreferencies.getIp()+"/getcars";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cars = new ArrayList<>();
                for (int i =0; i<response.length();i++){
                    try {
                        JSONObject car = response.getJSONObject(i);
                        CarItem carItem = new CarItem(
                                car.getInt("id"),
                                car.getString("brand"),
                                car.getString("model")
                        );
                        System.out.println(carItem.toString());
                        cars.add(carItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(cars.toArray().toString());
                adapter = new PageAdapter(cars,getContext());
                viewPager.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
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
                    body.put("login",mySharedPreferencies.getLogin());
                    body.put("token",mySharedPreferencies.getToken());

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
