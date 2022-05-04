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

        common.setter(this, title, published_date, desc);

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


    }

}