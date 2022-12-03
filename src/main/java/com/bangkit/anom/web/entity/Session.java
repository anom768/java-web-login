package com.bangkit.anom.web.entity;

public class Session {

    private String id;

    private String id_user;

    public Session(String id, String id_user) {
        this.id = id;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
