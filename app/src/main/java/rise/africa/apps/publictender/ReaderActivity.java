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

    private TextView desc, tv, tender_status;
    public ProgressBar progressBar;
    String id, title, is_open, closing_date, source, status, days_left;
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
        tender_status = (TextView) findViewById(R.id.tender_status);
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
        closing_date = getIntent().getStringExtra("closing_date");
        source = getIntent().getStringExtra("source");
        status = getIntent().getStringExtra("is_open");
        days_left = getIntent().getStringExtra("days_left");

        tender_status.setText("This tender is "+status+ " Published on " + source+"\n");
        if (status.equals("Open"))
            tender_status.append("Will be closed on : " +closing_date + " ( "+ days_left+" Days left)");
        else
            tender_status.append("Closed at : "+closing_date);

        common.setter(this, title, closing_date, desc);

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