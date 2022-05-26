package rise.africa.apps.publictender.ui.bookmark;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rise.africa.apps.publictender.R;
import rise.africa.apps.publictender.shared.DB;
import rise.africa.apps.publictender.shared.RecyclerViewAdapter;
import rise.africa.apps.publictender.shared.TenderItem;
import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    RecyclerView bookmark_recyclerView;
    DB db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        bookmark_recyclerView = (RecyclerView) root.findViewById(R.id.bookmark_recyclerView);
        bookmark_recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        bookmark_recyclerView.setLayoutManager(layoutManager);
        db = new DB(getContext());
        Cursor res = db.getSelect("*", "bookmarks","1");
        res.moveToFirst();
        List<Object> bookMarks = new ArrayList<>();

        while(res.isAfterLast() == false){
            bookMarks.add(new TenderItem(
                    res.getString(1),
                    res.getString(2),
                    res.getString(4),
                    "bookmarked",
                    res.getString(3),
                    "0"
            ));

            res.moveToNext();
        }
        System.out.println("test size dasf " + bookMarks.size());
        if (bookMarks.size() == 0) {

            root = inflater.inflate(R.layout.no_bookmark, container, false);

        }
        else
            setUpAdapter(bookMarks);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void setUpAdapter(List<Object> bookMarks) {
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

        adapter = new RecyclerViewAdapter(getContext(),
                bookMarks);
        bookmark_recyclerView.setAdapter(adapter);
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
//        bookmark_recyclerView.setAdapter(adapter);


//        adapterRecentRecipe.addItems(bookMarks);
    }

}