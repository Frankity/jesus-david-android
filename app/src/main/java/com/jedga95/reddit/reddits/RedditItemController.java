package com.jedga95.reddit.reddits;

import android.content.Context;
import android.widget.Toast;

import com.jedga95.reddit.R;
import com.jedga95.reddit.reddits.db.DatabaseHelper;
import com.jedga95.reddit.reddits.json.RedditItemAPI;
import com.jedga95.reddit.reddits.json.model.RedditData;
import com.jedga95.reddit.reddits.json.model.RedditItem;
import com.jedga95.reddit.reddits.json.model.RedditItems;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedditItemController {

    private ArrayList<RedditItem> mRedditItems = new ArrayList<>();
    private DatabaseHelper mDbHelper;

    public interface ResponseCallback {
        void onFinishedParsing();
        void onFailedParsing();
    }

    public RedditItemController() {
    }

    public void parseRedditEntries(final Context context,
                                   final ResponseCallback callback, final boolean forceParse) {
        /**
         * Check database for existing entries
         */
        mDbHelper = new DatabaseHelper(context.getApplicationContext());
        if (!mDbHelper.getAllEntries().isEmpty() && !forceParse) {
            mRedditItems = mDbHelper.getAllEntries();

            if (callback != null) {
                callback.onFinishedParsing();
            }
            return;
        }

        /**
         * Creating an object of our API interface
         */
        RedditItemAPI.ApiService api = RedditItemAPI.getApiService();

        /**
         * Calling JSON
         */
        Call<RedditData> call = api.getRedditItems();
        call.enqueue(new Callback<RedditData>() {
            @Override
            public void onResponse(Call<RedditData> call, Response<RedditData> response) {
                mDbHelper.clearAll();
                mRedditItems.clear();

                ArrayList<RedditItems> items = response.body().getData().getItemList();
                for(int i = 0; i < items.size(); i++) {
                    final RedditItem item = items.get(i).getItem();
                    mRedditItems.add(item);

                    mDbHelper.addRedditEntry(item);
                }

                if (callback != null) {
                    callback.onFinishedParsing();
                }
            }

            @Override
            public void onFailure(Call<RedditData> call, Throwable t) {
                if(callback != null) {
                    callback.onFailedParsing();
                }
            }
        });
    }

    public ArrayList<RedditItem> getRedditItems() {
        return mRedditItems;
    }
}
