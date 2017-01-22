package com.jedga95.reddit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jedga95.reddit.reddits.RedditItemController;
import com.jedga95.reddit.reddits.json.model.RedditItem;
import com.jedga95.reddit.ui.TextDrawable;
import com.jedga95.reddit.ui.util.ColorGenerator;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * An activity representing a list of Reddits. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RedditDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RedditListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * The item controller
     */
    private RedditItemController mRedditItemController;

    /**
     * The loading animation view
     */
    private AVLoadingIndicatorView mLoadingView;


    /**
     * The no connection layout
     */
    private View mNoConnectionLayout;

    /**
     * The recycler view list
     */
    private RecyclerView mRecyclerView;

    /**
     * The swipe to refresh layout
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mLoadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);

        mNoConnectionLayout = findViewById(R.id.no_connection_layout);
        mNoConnectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView();
                //parseEntries(true);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.reddit_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parseEntries(true);
            }
        });

        mRedditItemController = new RedditItemController();

        if (findViewById(R.id.reddit_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        parseEntries(false);
    }

    private void parseEntries(final boolean forceParse) {
        mRedditItemController.parseRedditEntries(this, new RedditItemController.ResponseCallback() {
            @Override
            public void onFinishedParsing() {
                assert mRecyclerView != null;
                setupRecyclerView(mRecyclerView);

                // If we're on two panel view, click on the first item of the list
                if (mTwoPane) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView
                                    .findViewHolderForAdapterPosition(0).itemView.performClick();
                        }
                    });
                }

                dismissLoadingView();
            }

            @Override
            public void onFailedParsing() {
                if (mRecyclerView.getAdapter() == null) {
                    mNoConnectionLayout.setVisibility(View.VISIBLE);
                    mNoConnectionLayout.animate().alpha(1f).setListener(null);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_fetching, Toast
                            .LENGTH_SHORT).show();
                }
                dismissLoadingView();
            }
        }, forceParse);
        if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.animate().alpha(1f).setListener(null);
        mNoConnectionLayout.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mNoConnectionLayout.setVisibility(View.GONE);
                parseEntries(true);
                super.onAnimationEnd(animation);
            }
        });
    }

    private void dismissLoadingView() {
        mLoadingView.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingView.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(
                new SimpleItemRecyclerViewAdapter(mRedditItemController.getRedditItems()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<RedditItem> mValues;
        private ColorGenerator mGenerator;
        private TextDrawable.IBuilder mDrawableBuilder;
        private int mLastPosition = -1;

        public SimpleItemRecyclerViewAdapter(List<RedditItem> items) {
            mValues = items;

            mGenerator = ColorGenerator.MATERIAL;
            mDrawableBuilder = TextDrawable.builder()
                    .beginConfig()
                    .endConfig()
                    .rect();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reddit_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final RedditItem item = mValues.get(position);
            holder.mItem = item;
            holder.mTitleView.setText(item.getDisplayName());
            holder.mDescriptionView.setText(item.getShortDescription());
            if (!TextUtils.isEmpty(item.getIconImgUrl())) {
                Picasso.with(RedditListActivity.this)
                        .load(item.getIconImgUrl()).into(holder.mThumbView);
            } else {
                int color = mGenerator.getColor(item.getDisplayName());
                holder.mThumbView.setImageDrawable(mDrawableBuilder.build(item.getDisplayName()
                        .substring(0, 1).toUpperCase(), color));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(
                                RedditDetailFragment.ARG_SERIAL_ITEM, holder.mItem);
                        RedditDetailFragment fragment = new RedditDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.reddit_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RedditDetailActivity.class);
                        intent.putExtra(RedditDetailFragment.ARG_SERIAL_ITEM, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });

            // Set the view to scale in
            setAnimation(holder.itemView, position);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.itemView.clearAnimation();
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > mLastPosition) {
                TranslateAnimation anim = new TranslateAnimation((viewToAnimate.getRight() -
                        viewToAnimate.getLeft()) / 2, 0, 0, 0);
                anim.setInterpolator(new OvershootInterpolator(1.2f));
                anim.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                viewToAnimate.startAnimation(anim);
                mLastPosition = position;
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mThumbView;
            public final TextView mTitleView;
            public final TextView mDescriptionView;
            public RedditItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mThumbView = (ImageView) view.findViewById(R.id.thumb);
                mTitleView = (TextView) view.findViewById(R.id.title);
                mDescriptionView = (TextView) view.findViewById(R.id.description);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }
}
