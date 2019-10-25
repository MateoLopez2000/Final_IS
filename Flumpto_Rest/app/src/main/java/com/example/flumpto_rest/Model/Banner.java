package com.example.flumpto_rest.Model;

import java.sql.Blob;

public class Banner {
    private String ID,Name,Link;
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }


}
