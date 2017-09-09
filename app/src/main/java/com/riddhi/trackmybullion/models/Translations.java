package com.riddhi.trackmybullion.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translations {

    @SerializedName("de")
    @Expose
    private String de;
    @SerializedName("es")
    @Expose
    private String es;
    @SerializedName("fr")
    @Expose
    private String fr;
    @SerializedName("ja")
    @Expose
    private String ja;
    @SerializedName("it")
    @Expose
    private String it;
    @SerializedName("br")
    @Expose
    private String br;
    @SerializedName("pt")
    @Expose
    private String pt;

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

}
