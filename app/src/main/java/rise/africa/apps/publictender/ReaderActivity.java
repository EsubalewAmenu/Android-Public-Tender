package rise.africa.apps.publictender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import rise.africa.apps.publictender.shared.Common;
import rise.africa.apps.publictender.shared.DB;

public class ReaderActivity extends AppCompatActivity {

    private TextView desc, tv, tender_status;
    public ProgressBar progressBar;
    String id, title, is_open, closing_date, source, status, days_left;
    Button btnRetry;
    Common common = new Common();
    FloatingActionButton bookmark, fab;

    AdView mAdView;

    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        ImageButton zoomIn;
        ImageButton zoomOut;

        tv = (TextView) findViewById(R.id.tvWait);

                desc = (TextView) findViewById(R.id.desc);
        tender_status = (TextView) findViewById(R.id.tender_status);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        db = new DB(getApplicationContext());

        zoomIn = (ImageButton) findViewById(R.id.zoom_in);
        zoomOut = (ImageButton) findViewById(R.id.zoom_out);

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSize = desc.getTextSize() / getApplicationContext().getResources().getDisplayMetrics().density;

                if (textSize <= 30.0f) {
                    TextView textView = desc;
                    textView.setTextSize(0, textView.getTextSize() + 1.0f);
                    return;
                }
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float textSize = desc.getTextSize() / getApplicationContext().getResources().getDisplayMetrics().density;

                if (textSize >= 15.0f) {
                    TextView textView2 = desc;
                    textView2.setTextSize(0, textView2.getTextSize() - 1.0f);
                    return;
                }
            }
        });

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
//        is_open = getIntent().getStringExtra("is_open");
        closing_date = getIntent().getStringExtra("closing_date");
        source = getIntent().getStringExtra("source");
        status = getIntent().getStringExtra("is_open");
        days_left = getIntent().getStringExtra("days_left");

        tender_status.setText("This tender is "+status+ " Published on " + source+"\n");
        if (status.equals("Open"))
            tender_status.append("Will be closed on : " +closing_date + " ( "+ days_left+" Days left)");
        else
            tender_status.append("Closed at : "+closing_date);

        common.setter(this, id, title, closing_date, desc);

        common.getTenderDetail(this, id, desc, tv, progressBar, title);

        btnRetry = (Button) findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                common.getTenderDetail(getParent(), id, desc, tv, progressBar, title);
                tv.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.INVISIBLE);

            }
        });

        bookmark = (FloatingActionButton) findViewById(R.id.bookmark);
        checkBookmark(id);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getSelect("*", "bookmarks", "tenderId="+id).moveToFirst()) {
                    db.removeBookmark(id);
                    bookmark.setImageResource(R.drawable.ic_un_bookmark);
                    Toast.makeText(getApplicationContext(), "Bookmark Removed", Toast.LENGTH_SHORT).show();
                } else if (db.addBookmark(id, title, closing_date, source)) {
                    bookmark.setImageResource(R.drawable.ic_bookmark);
                    Toast.makeText(getApplicationContext(), "Bookmark Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + ReaderActivity.this.getPackageName()+"\n\n" + title);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void checkBookmark(String tenderId) {
        DB db = new DB(getApplicationContext());
        if (db.getSelect("*", "bookmarks", "tenderId="+id).moveToFirst()) {//isBookmarked
            bookmark.setImageResource(R.drawable.ic_bookmark);
        }
//        else {
//            bookmark.setImageResource(R.drawable.ic_un_bookmark);
//        }
    }
}