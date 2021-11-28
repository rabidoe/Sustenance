package com.enhanced.sustenance.ui.pantry_fragment;

import android.os.Parcel;
import android.os.Parcelable;

import com.enhanced.sustenance.utils.Enums;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodObject implements Parcelable {
    private static final String TAG = "FoodObject";
    SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yy");
    private final String name;
    private String qty;
    private Enums.UNITS unit;
    private Date expiry;
    private Enums.CATEGORIES category;
    private Enums.PACKAGING packaging;
    private String isle;
    private String note;

    public FoodObject(String name) {
        this.name = name;
    }

    public FoodObject(String name, String qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        if (unit == null) {
            return "";
        }
        return unit.toString();
    }


    public String getExpiryAsString() {
        return sdf.format(expiry);
    }

    public Enums.CATEGORIES getCategory() {
        return category;
    }

    public Enums.PACKAGING getPackaging() {
        return packaging;
    }

    public String getIsle() {
        return isle;
    }

    public String getNote() {
        return note;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setUnit(Enums.UNITS unit) {
        this.unit = unit;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public void setExpiryFromStrings(String dd, String mm, String yy) {
        String str = dd + "/" + mm + "/" + yy;
        try {
            this.expiry = sdf.parse(str);
        } catch (Exception ignored) {
        }
    }

    public void expiryFromStringFormat(String str) {
        if (!str.equalsIgnoreCase("")) {
            String[] strings = str.split("/");
            setExpiryFromStrings(strings[0], strings[1], strings[2]);
        } else {
            expiry = null;
        }
    }

    public void setCategory(Enums.CATEGORIES category) {
        this.category = category;
    }

    public void setPackaging(Enums.PACKAGING packaging) {
        this.packaging = packaging;
    }

    public void setIsle(String isle) {
        this.isle = isle;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(qty);
        dest.writeString(String.valueOf(unit));
        dest.writeString(sdf.format(expiry));
        dest.writeString(String.valueOf(category));
        dest.writeString(String.valueOf(packaging));
        dest.writeString(isle);
        dest.writeString(note);
    }

    public FoodObject(Parcel in) {
        name = in.readString();
        qty = in.readString();
        unit = Enums.UNITS.getEnum(in.readString());
        expiryFromStringFormat(in.readString());
        category = Enums.CATEGORIES.getEnum(in.readString());
        packaging = Enums.PACKAGING.getEnum(in.readString());
        isle = in.readString();
        note = in.readString();
    }

    public static final Parcelable.Creator<FoodObject> CREATOR =
            new Parcelable.Creator<FoodObject>() {
                public FoodObject createFromParcel(Parcel in) {
                    return new FoodObject(in);
                }

                public FoodObject[] newArray(int size) {
                    return new FoodObject[size];
                }
            };
}
