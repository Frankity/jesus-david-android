package com.jedga95.reddit.reddits.json.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jesdga95 on 21/01/17.
 */

public class RedditData {
    @SerializedName("data")
    @Expose
    private DataContainer mainDataContainer;

    /**
     * @return All the items from the query
     */
    public DataContainer getData() {
        return mainDataContainer;
    }


    public class DataContainer {
        @SerializedName("children")
        @Expose
        private ArrayList<RedditItems> itemList = new ArrayList<>();

        /**
         * @return {@link com.jedga95.reddit.reddits.json.model.RedditItems} list for the query
         */
        public ArrayList<RedditItems> getItemList() {
            return itemList;
        }
    }
}
