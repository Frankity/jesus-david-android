package com.jedga95.reddit.reddits.json.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jedga95.reddit.reddits.json.RedditItemAPI;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;

/**
 * Created by jesdga95 on 21/01/17.
 */

public class RedditItem implements Serializable {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("icon_img")
    @Expose
    private String iconImgUrl;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("banner_img")
    @Expose
    private String bannerImageUrl;

    @SerializedName("public_description")
    @Expose
    private String description;

    @SerializedName("description_html")
    @Expose
    private String content;


    private String shortDescription;

    public RedditItem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getContent() {
        // Switch relative paths to full absolute urls
        final String relativeHtml = StringEscapeUtils.unescapeHtml(content);
        final String absoluteHtml
                = relativeHtml
                        .replace("href=\"//", "href=\"" + RedditItemAPI.HTTP_BASE + "/")
                        .replace("href=\"/", "href=\"" + RedditItemAPI.BASE_URL + "/");
        return absoluteHtml;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortDescription() {
        // Limit description to 500 chars, for faster UI rendering
        return StringEscapeUtils
                .unescapeHtml(description.substring(0, Math.min(description.length(), 500)));
    }

    @Override
    public String toString() {
        return "RedditItem{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
