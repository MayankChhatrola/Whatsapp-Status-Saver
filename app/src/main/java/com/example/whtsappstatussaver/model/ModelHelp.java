package com.example.whtsappstatussaver.model;

public class ModelHelp {
    private String shelp;
    private int simage;
    private String detailshelp;

    public String getShelp() {
        return this.shelp;
    }

    public int getSimage() {
        return this.simage;
    }

    public ModelHelp setShelp(String str) {
        this.shelp = str;
        return this;
    }

    public ModelHelp setSimage(int i) {
        this.simage = i;
        return this;
    }

    public String getDetailshelp() {
        return  this.detailshelp;
    }

    public ModelHelp setDetailshelp(String str) {
        this.detailshelp = str;
        return this;
    }


}
