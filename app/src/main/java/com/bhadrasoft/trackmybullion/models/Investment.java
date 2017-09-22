package com.bhadrasoft.trackmybullion.models;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Investment implements Serializable, Parcelable
{

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    public final static Parcelable.Creator<Investment> CREATOR = new Creator<Investment>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Investment createFromParcel(Parcel in) {
            Investment instance = new Investment();
            instance.date = ((String) in.readValue((String.class.getClassLoader())));
            instance.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.weight = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Investment[] newArray(int size) {
            return (new Investment[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1790128532169828615L;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(amount);
        dest.writeValue(weight);
    }

    public int describeContents() {
        return  0;
    }

}
