package rise.africa.apps.publictender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import rise.africa.apps.publictender.shared.Common;

public class ReaderActivity extends AppCompatActivity {

    private TextView desc, tv;
    public ProgressBar progressBar;
    String id, title, is_open, published_date, source;
    Button btnRetry;
    Common common = new Common();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        ImageButton zoomIn;
        ImageButton zoomOut;

        tv = (TextView) findViewById(R.id.tvWait);

        desc = (TextView) findViewById(R.id.desc);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
        is_open = getIntent().getStringExtra("is_open");
        published_date = getIntent().getStringExtra("published_date");
        source = getIntent().getStringExtra("source");

        setter(title, published_date);

        common.getTenderDetail(getApplicationContext(), id, desc);

        btnRetry = (Button) findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                common.getTenderDetail(getApplicationContext(), id, desc);
                tv.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.INVISIBLE);

            }
        });


    }

    private void setter(String title, String content) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                desc.setText(Html.fromHtml("<b>"+title+"</b><br/><br/>"+content.trim(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                setTitle(Html.fromHtml(title));
                desc.setText(Html.fromHtml(title+"<br/><br/>"+content.trim()));
            }

//            checkBookmark(id);
        }catch (Exception ds){}

    }
}