package com.jedga95.reddit.reddits.json.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jesdga95 on 21/01/17.
 */
public class RedditItems {
    @SerializedName("data")
    @Expose
    private RedditItem item;

    /**
     * @return The current item
     */
    public RedditItem getItem() {
        return item;
    }

    /**
     * @param item The item to be set
     */
    public void setItem(RedditItem item) {
        this.item = item;
    }
}
