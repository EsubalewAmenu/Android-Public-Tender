package rise.africa.apps.publictender.filter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.internal.DebouncingOnClickListener;
import rise.africa.apps.publictender.MainActivity;
import rise.africa.apps.publictender.R;
import rise.africa.apps.publictender.shared.DB;


public class EcommerceFiltersFragment extends Fragment {


    LinearLayout linearLayout;

    Button applyFiltersButton;

    TextView textviewReset;

    ExpandableListView expandablelistviewFilter;

    MainActivity mContext;
    List<FilterHeader> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    EcommerceFIltersExpandableAdapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public EcommerceFiltersFragment() {
        // Required empty public constructor
    }


    public static EcommerceFiltersFragment newInstance() {
        EcommerceFiltersFragment fragment = new EcommerceFiltersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandablelistviewFilter = view.findViewById(R.id.expandablelistview_filter);
        linearLayout = view.findViewById(R.id.linearlayout_filter);
        applyFiltersButton = view.findViewById(R.id.applyFiltersBtn);
        textviewReset = view.findViewById(R.id.button_reset);
        prepareListData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<FilterHeader>();
        listDataChild = new HashMap<>();


        DB db;
        db = new DB(getContext());
        final Cursor categoriesCursor = db.getSelect("*", "categories", "main_category=1");

        if (categoriesCursor.moveToFirst()) {
            do {
                listDataHeader.add(new FilterHeader(categoriesCursor.getString(1), "All sizes"));

                final Cursor subCategoriesCursor = db.getSelect("*", "categories", "sub_category="+categoriesCursor.getString(0)+" order by category_name asc");
//System.out.println("subcategories of "+categoriesCursor.getString(1)+" = " + subCategoriesCursor.getCount());
                // Adding child data
                List<String> bySize = new ArrayList<>();
                if (subCategoriesCursor.moveToFirst()) {
                    do {
                        bySize.add(subCategoriesCursor.getString(1));
                    } while (subCategoriesCursor.moveToNext());
                }else{
                    bySize.add(categoriesCursor.getString(1));
                }
                listDataChild.put(categoriesCursor.getString(1), bySize); // Header, Child data

            } while (categoriesCursor.moveToNext());
        }

        // Adding child data
//        listDataHeader.add(new FilterHeader("Item Size", "All sizes"));
//        listDataHeader.add(new FilterHeader("Item Color", "Select"));
//        listDataHeader.add(new FilterHeader("Sort", "Select"));


        // Adding child data
//        List<String> bySize = new ArrayList<>();
//        bySize.add("Medium");
//        bySize.add("Small");
//        bySize.add("Large");
//        bySize.add("XSmall");
//        bySize.add("XLarge");
//
//
//
//        List<String> color = new ArrayList<>();
//        color.add("Black");
//        color.add("Red");
//        color.add("White");
//        color.add("Green");
//        color.add("Blue");
//
//        List<String> sort = new ArrayList<>();
//        sort.add("Cost ($): highest first");
//        sort.add("Cost ($): lowest first");
//
//
//        listDataChild.put(listDataHeader.get(0).getTitle(), bySize); // Header, Child data
//        listDataChild.put(listDataHeader.get(1).getTitle(), color);
//        listDataChild.put(listDataHeader.get(2).getTitle(), sort);

        listAdapter = new EcommerceFIltersExpandableAdapter(mContext, listDataHeader, listDataChild);
        expandablelistviewFilter.setAdapter(listAdapter);

        linearLayout.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                mContext.dismissFragment();
            }
        });

        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("listDataChild listDataChild"+listDataChild.get(listDataHeader.get(0).getTitle()));
                System.out.println("listDataChild listDataChild"+listAdapter.getCheckedStatusCombinedString());
                mContext.dismissFragment();
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mContext = (MainActivity) context;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // : Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
