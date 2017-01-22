package com.jedga95.reddit.reddits.json.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jesdga95 on 21/01/17.
 */

public class RedditItem implements Serializable {

    @SerializedName("icon_img")
    @Expose
    private String iconImgUrl;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("banner_img")
    @Expose
    private String bannerImageUrl;

    @SerializedName("description")
    @Expose
    private String description;

    private String shortDescription;

    public RedditItem() {
    }

    public String getIconImgUrl() {
        return iconImgUrl;
    }

    public void setIconImgUrl(String iconImgUrl) {
        this.iconImgUrl = iconImgUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return description.substring(0, Math.min(description.length(), 500));
    }

    @Override
    public String toString() {
        return "RedditItem{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
