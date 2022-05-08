package rise.africa.apps.publictender.ui.bookmark;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rise.africa.apps.publictender.ReaderActivity;
import rise.africa.apps.publictender.R;
import rise.africa.apps.publictender.shared.DB;
import rise.africa.apps.publictender.shared.RecyclerViewAdapter;
import rise.africa.apps.publictender.shared.TenderItem;
import rise.africa.apps.publictender.shared.RecyclerViewAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    RecyclerView recyclerviewRecentRecipe;
    LinearLayoutManager recentRecipeVerticalLayout;
    RecyclerViewAdapter adapterRecentRecipe;
    DB db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerviewRecentRecipe = (RecyclerView) root.findViewById(R.id.recyclerView);
        recentRecipeVerticalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerviewRecentRecipe.setLayoutManager(recentRecipeVerticalLayout);

        ArrayList<TenderItem> bookMarks = null;//db.getAllBookmarks();
        if (bookMarks.size() == 0) {

            root = inflater.inflate(R.layout.no_bookmark, container, false);

        }
        else
            setUpAdapter(bookMarks);

        return root;
    }

    private void setUpAdapter(ArrayList<TenderItem> bookMarks) {
//        recyclerviewRecentRecipe.setLayoutManager(recentRecipeVerticalLayout);

//        adapterRecentRecipe = new RecyclerViewAdapter(bookMarks, new RecentRecipeAdapter.OnRecentRecipeItemListener() {
//            @Override
//            public void onItemClick(RecentRecipeItem item) {
//
//
//                Drawable drawable = item.tumbImage.getDrawable();
//                Bitmap bitmap = ((BitmapDrawable) drawable ).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] bitmapdata = stream.toByteArray();
//
//                Intent i=new Intent( getContext(), DetailScreen.class);
//                i.putExtra("recipe_id", item.id);
//
//                i.putExtra("bitmapdata",bitmapdata);
//
//                startActivity(i);
//
//            }
//        });


        // Set adapter on recycler view
        recyclerviewRecentRecipe.setAdapter(adapterRecentRecipe);


//        adapterRecentRecipe.addItems(bookMarks);
    }

}