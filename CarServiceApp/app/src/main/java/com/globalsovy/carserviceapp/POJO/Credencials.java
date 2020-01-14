package com.globalsovy.carserviceapp.POJO;

public class Credencials {

    String login;
    String token;

    public Credencials(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Credencials{" +
                "login='" + login + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
