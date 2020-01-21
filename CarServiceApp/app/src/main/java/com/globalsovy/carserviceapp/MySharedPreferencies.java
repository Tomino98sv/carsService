package com.globalsovy.carserviceapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.globalsovy.carserviceapp.POJO.Credencials;
import com.globalsovy.carserviceapp.POJO.UserInfo;

public class MySharedPreferencies {

    SharedPreferences loginData;

    public MySharedPreferencies(Context context) {
        loginData = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }

    public void fillLoginData(Credencials credencials, UserInfo userInfo) {

        System.out.println(credencials.toString());
        System.out.println(userInfo.toString());

        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("login",credencials.getLogin());
        editor.putString("token",credencials.getToken());
        editor.putInt("id",userInfo.getId());
        editor.putString("first_name",userInfo.getFirst_name());
        editor.putString("last_name",userInfo.getLast_name());
        editor.putString("email",userInfo.getEmail());
        editor.putString("password",userInfo.getPassword());
        editor.putBoolean("confirmed",userInfo.isConfirmed());
        editor.apply();
    }

    public String getLogin() {
        return loginData.getString("login","default");
    }
    public String getToken() {
        return loginData.getString("token","default");
    }

    public int getIdLogin() {
        return loginData.getInt("id",0);
    }
    public String getFnameLogin() {
        return loginData.getString("first_name","default");
    }
    public String getLnameLogin() {
        return loginData.getString("last_name","default");
    }
    public String getEmailLogin() {
        return loginData.getString("email","default");
    }
    public String getPassword() {
        return loginData.getString("password","default");
    }
    public boolean getConfirmedLogin() {
        return loginData.getBoolean("confirmed",false);
    }


    public void setLogin(String login) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("login",login);
        editor.apply();
    }
    public void setToken(String token) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("token",token);
        editor.apply();
    }

    public void setIdLogin(int id) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putInt("id",id);
        editor.apply();
    }
    public void setFnameLogin(String fname) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("first_name",fname);
        editor.apply();
    }
    public void setLnameLogin(String lname) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("last_name",lname);
        editor.apply();
    }
    public void setEmailLogin(String email) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("email",email);
        editor.apply();
    }
    public void setPassword(String password) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("password",password);
        editor.apply();
    }
    public void setConfirmedLogin(boolean confirmedLogin) {
        SharedPreferences.Editor editor = loginData.edit();
        editor.putBoolean("confirmed",confirmedLogin);
        editor.apply();
    }

    public String getIp() {
        return "http://itsovy.sk:1203";
    }
}
