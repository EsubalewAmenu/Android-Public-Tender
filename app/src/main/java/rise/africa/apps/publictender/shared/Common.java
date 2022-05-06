package rise.africa.apps.publictender.shared;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Common {
    public static final int ITEMS_PER_AD = 8;
    public int OFFSET = 0;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/4177191030";
    public boolean isLoading = false;
    String BASE_URL = "https://192.168.0.27:8082/wp/ds/wp-json/ds_tender/";
    String USERNAME = "test", PAZZWORD = "QQ!!qq11";

    public void getTenderDetail(Activity activity, String id, TextView description, TextView tv, ProgressBar progressBar, String title){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        String url = BASE_URL+"v1/tender/" + id;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        System.out.println("work! " + response);
//                        description.setText(response);
                        parseTenderDetailJson(activity,response,description, tv, progressBar, title);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! " + error);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", USERNAME);
                params.put("password", PAZZWORD);

                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void updateTenders(Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        handleSSLHandshake();

        if (OFFSET>-1) {
    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = BASE_URL+"v1/tenders/" +
                    (OFFSET * PaginationListener.PAGE_SIZE) + "/" + PaginationListener.PAGE_SIZE + "/35,39,25,52,89,91,88,96,100,83,84";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
//                            System.out.println("work! " + response);
                            parseTendersJson(context, response, adapter);
                            OFFSET++;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("That didn't work! " + error);
                }
            })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username", USERNAME);
                    params.put("password", PAZZWORD);

                    return params;
                }
            };

// Add the request to the RequestQueue.
            queue.add(stringRequest);
            isLoading = false;
        }
    }, 1500);
}
    }
    public void parseTendersJson(Context context, String jsonString, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){

        if (jsonString != null) {
            List<Object> items = new ArrayList<>();
            try {
                JSONObject jsonObj = new JSONObject(jsonString);

                // Getting JSON Array node
                JSONArray datas = jsonObj.getJSONArray("tenders");

                // looping through All Contacts
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject c = datas.getJSONObject(i);
                    TenderItem tenderItem = new TenderItem(c.getString("id"), c.getString("title"), c.getString("source_name"), c.getString("tender_status"), c.getString("closing_date"), c.getString("days_left"));
                    items.add(tenderItem);
                }
                if(datas.length()<PaginationListener.PAGE_SIZE) {
                    OFFSET = -100;
                }
                if (items.size()>0) {
                    items = addBannerAds(items, context);
                    ((RecyclerViewAdapter) adapter).addItems(items);
                    loadBannerAds(items);
//            loadBannerAds(items);
                }
            } catch (final JSONException e) {
                System.out.println("Json parsing error on common update tender: " + e.getMessage());
            }

        }

    }
    public void parseTenderDetailJson(Activity activity, String jsonString, TextView description, TextView tv, ProgressBar progressBar, String title){

        if (jsonString != null) {
            List<Object> items = new ArrayList<>();
            try {
                JSONObject jsonObj = new JSONObject(jsonString);

                // Getting JSON Array node
                JSONObject data = jsonObj.getJSONObject("tender");
                if(data.has("content")) {
                    setter(activity, title, data.getString("content"), description);

                    tv.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            } catch (final JSONException e) {
                System.out.println("Json parsing error on common update tender: " + e.getMessage());
            }

        }

    }
    /**
     * Adds banner ads to the items list.
     */
    private List<Object> addBannerAds(List<Object> items, Context context) {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = 0; i <= items.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            items.add(i, adView);
        }
        return items;
    }

    /**
     * Sets up and loads the banner ads.
     */
    private void loadBannerAds(List<Object> items) {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(0, items);
    }

    /**
     * Loads the banner ads in the items list.
     */
    private void loadBannerAd(final int index, List<Object> items) {

        if (index >= items.size()) {
            return;
        }

        Object item = items.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad"
                    + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        // The previous banner ad loaded successfully, call this method again to
                        // load the next ad in the items list.
                        loadBannerAd(index + ITEMS_PER_AD, items);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // The previous banner ad failed to load. Call this method again to load
                        // the next ad in the items list.
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e(
                                "MainActivity",
                                "The previous banner ad failed to load with error: "
                                        + error
                                        + ". Attempting to"
                                        + " load the next banner ad in the items list.");
                        loadBannerAd(index + ITEMS_PER_AD, items);
                    }
                });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public void setter(Activity activity, String title, String content, TextView desc) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activity.setTitle(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                desc.setText(Html.fromHtml("<b>"+title+"</b><br/><br/>"+content.trim(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                activity.setTitle(Html.fromHtml(title));
                desc.setText(Html.fromHtml(title+"<br/><br/>"+content.trim()));
            }

//            checkBookmark(id);
        }catch (Exception ds){}

    }
}
