package com.jedga95.reddit;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jedga95.reddit.reddits.json.model.RedditItem;

/**
 * A fragment representing a single Reddit detail screen.
 * This fragment is either contained in a {@link RedditListActivity}
 * in two-pane mode (on tablets) or a {@link RedditDetailActivity}
 * on handsets.
 */
public class RedditDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item passed by the list activity
     */
    public static final String ARG_SERIAL_ITEM = "item_serial";

    /**
     * The content this fragment is presenting.
     */
    private RedditItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RedditDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SERIAL_ITEM)) {

            mItem = (RedditItem) getArguments().getSerializable(ARG_SERIAL_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
                    .findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getDisplayName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reddit_detail, container, false);

        // Show the content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.reddit_detail)).setText(mItem.getDescription());
        }

        return rootView;
    }
}
