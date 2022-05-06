package rise.africa.apps.publictender.shared;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import rise.africa.apps.publictender.R;
import rise.africa.apps.publictender.ReaderActivity;
import rise.africa.apps.publictender.ui.home.HomeFragment;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}
 * or the {@link AdViewHolder}.</p>
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The banner ad view type.
    private static final int BANNER_AD_VIEW_TYPE = 1;

    // The banner ad view type.
    private static final int LOADER_VIEW_TYPE = 2;

    // An Activity's Context.
    private final Context context;

    // The list of banner ads and menu items.
    private final List<Object> recyclerViewItems;

    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link TenderItem} and {@link AdView} types.
     */
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    public void addItems(List<Object> postItems) {
        recyclerViewItems.addAll(postItems);
        notifyDataSetChanged();
    }

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */
    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemTitle;
        private TextView menuItemSource;
        private TextView menuItemIsOpen;
        private TextView menuItemClosingDate;

        MenuItemViewHolder(View view) {
            super(view);
            menuItemTitle = view.findViewById(R.id.menu_item_title);
            menuItemIsOpen = view.findViewById(R.id.menu_item_is_open);
            menuItemClosingDate = view.findViewById(R.id.item_closing_date);
            menuItemSource = view.findViewById(R.id.item_source);
        }
        public void addItems(List<Object> postItems) {
            recyclerViewItems.addAll(postItems);
            notifyDataSetChanged();
        }
    }

    /**
     * The {@link AdViewHolder} class.
     */
    public class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View view) {
            super(view);
        }

        public void addItems(List<Object> postItems) {
            recyclerViewItems.addAll(postItems);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
System.out.println("position IS " +position+"  getItemCount() "+ getItemCount());
        if(recyclerViewItems.get(position).getClass() == TenderItem.class) return MENU_ITEM_VIEW_TYPE;
        else if(recyclerViewItems.get(position).getClass() == AdView.class) return BANNER_AD_VIEW_TYPE;
        else //if (recyclerViewItems.get(position).getClass() == Object.class) {
            return LOADER_VIEW_TYPE;
//        }
//        else return MENU_ITEM_VIEW_TYPE;


//        if(position % HomeFragment.ITEMS_PER_AD == 0) return BANNER_AD_VIEW_TYPE;
//        else if (position == getItemCount()-1) {
//            return LOADER_VIEW_TYPE;
//        }
//        else return MENU_ITEM_VIEW_TYPE;

//        return (position % HomeFragment.ITEMS_PER_AD == 0) ? BANNER_AD_VIEW_TYPE
//                : MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a banner ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.menu_item_container, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);
            case LOADER_VIEW_TYPE:
                View prgressbarView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_loading, viewGroup, false);
//                return new MenuItemViewHolder(prgressbarView);
                return new ProgressHolder(
                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup    , false));
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                View bannerLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.banner_ad_container,
                        viewGroup, false);
                return new AdViewHolder(bannerLayoutView);
        }
    }
    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }

        @Override
        protected void clear() {
        }

        public void addItems(List<Object> postItems) {
            recyclerViewItems.addAll(postItems);
            notifyDataSetChanged();
        }}
    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
                TenderItem tenderItem = (TenderItem) recyclerViewItems.get(position);

                // Add the menu item details to the menu item view.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    menuItemHolder.menuItemTitle.setText(Html.fromHtml(tenderItem.getTitle(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    menuItemHolder.menuItemTitle.setText(Html.fromHtml(tenderItem.getTitle()));
                }
                menuItemHolder.menuItemIsOpen.setText("This tender is "+tenderItem.getIs_open());
                menuItemHolder.menuItemSource.setText("Published on " + tenderItem.getSource());

                if (tenderItem.getIs_open().equals("Open")) {
                    menuItemHolder.menuItemClosingDate.setText("Will be closed on : " + tenderItem.getClosing_date() + " ( " + tenderItem.getDays_left() + " Days left)");
                    menuItemHolder.menuItemIsOpen.setTextColor(Color.rgb(0, 255, 0));
                }
                else{
                    menuItemHolder.menuItemClosingDate.setText("Closed at : "+tenderItem.getClosing_date());
                menuItemHolder.menuItemIsOpen.setTextColor(Color.rgb(255, 0, 0));
        }

                menuItemHolder.menuItemTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        System.out.println("chosed tender id is "+tenderItem.getId());

                        Intent intent = new Intent(context, ReaderActivity.class);
                        intent.putExtra("id", tenderItem.getId());
                        intent.putExtra("title", tenderItem.getTitle());
                        intent.putExtra("is_open", tenderItem.getIs_open());
                        intent.putExtra("source", tenderItem.getSource());
                        intent.putExtra("closing_date", tenderItem.getClosing_date());
                        intent.putExtra("days_left", tenderItem.getDays_left());
                        context.startActivity(intent);
                    }
                });
                break;
            case LOADER_VIEW_TYPE:
                break;
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                AdViewHolder bannerHolder = (AdViewHolder) holder;
                AdView adView = (AdView) recyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;
                // The AdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // AdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled AdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the banner ad to the ad view.
                adCardView.addView(adView);
        }
    }

}
