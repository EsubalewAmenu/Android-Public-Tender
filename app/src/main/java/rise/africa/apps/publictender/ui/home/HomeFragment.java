package rise.africa.apps.publictender.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rise.africa.apps.publictender.R;
import rise.africa.apps.publictender.shared.Common;
import rise.africa.apps.publictender.shared.PaginationListener;
import rise.africa.apps.publictender.shared.RecyclerViewAdapter;
import rise.africa.apps.publictender.shared.TenderItem;

public class HomeFragment extends Fragment {

    // A banner ad is placed in every 8th position in the RecyclerView.
    public static final int ITEMS_PER_AD = 8;
    // The RecyclerView that holds and displays banner ads and menu items.
    private RecyclerView recyclerView;
    // List of banner ads and MenuItems that populate the RecyclerView.
    private List<Object> recyclerViewItems = new ArrayList<>();
    RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    SwipeRefreshLayout swipeRefresh;
Common common = new Common();

    public boolean isLastPage = false;
    private int totalPage = 10;

    SearchView searchView;
    MenuItem myActionMenuItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        recyclerView = root.findViewById(R.id.recycler_view);
        swipeRefresh = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // load more fron the api
        common.searchQuery = "";
        common.OFFSET = 0;
        recyclerViewItems = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getContext(),
                recyclerViewItems);
        recyclerView.setAdapter(adapter);

        common.updateTenders(getContext(), adapter);

            }
        });
        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true);

        // Specify a linear layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        // Update the RecyclerView item's list with menu items and banner ads.


        // Specify an adapter.
        adapter = new RecyclerViewAdapter(getContext(),
                recyclerViewItems);
        recyclerView.setAdapter(adapter);

        common.updateTenders(getContext(), adapter);

        //        addLoading();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        recyclerView.addOnScrollListener(new PaginationListener((LinearLayoutManager) layoutManager) {
            @Override
            protected void loadMoreItems() {
                common.isLoading = true;
//                currentPage++;
                common.updateTenders(getContext(), adapter);

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return common.isLoading;
            }
        });

        return root;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);


        myActionMenuItem = menu.findItem( R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                System.out.println("search query is on frag" + query);
                common.searchQuery = query;
                common.OFFSET = 0;

                recyclerViewItems = new ArrayList<>();
                adapter = new RecyclerViewAdapter(getContext(),
                        recyclerViewItems);
                recyclerView.setAdapter(adapter);

                common.updateTenders(getContext(), adapter);

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }


    private void addLoading() {
//        isLoaderVisible = true;
        recyclerViewItems.add(new Object());
    }
    @Override
    public void onResume() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }

}