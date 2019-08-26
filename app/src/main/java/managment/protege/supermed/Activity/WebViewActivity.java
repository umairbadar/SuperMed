package managment.protege.supermed.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Fragment.JoinUs;
import managment.protege.supermed.R;

public class WebViewActivity extends AppCompatActivity {

    WebView mWebView;
    private static final String url = "https://www.supermed.pk/api/keenuPaymentProcess/%s/%s";
    private static KProgressHUD labHud;
    private static final String TAG = "WebViewActivity";
    private int count = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(WebViewActivity.this, Main_Apps.class);
                startActivity(intent);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        labHud = KProgressHUD.create(Main_Apps.getMainActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow);

        if (getIntent() != null) {

            mWebView = findViewById(R.id.webView);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);


            if (getIntent().getStringExtra(JoinUs.LINK) == null) {

                getSupportActionBar().setTitle("SuperMed Payment");

                String orderId = getIntent().getStringExtra("orderid");
                String orderAmount = getIntent().getStringExtra("orderamount");
                String type = getIntent().getStringExtra("Type");


                String appendedUrl = String.format(url, orderId, type);
                Log.d("url", appendedUrl);
                mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                mWebView.setWebViewClient(new WebViewClient() {


                                              @Override
                                              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                  view.loadUrl(url);
                                                  return true;
                                              }

                                              @Override
                                              public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                  labHud.show();
                                                  super.onPageStarted(view, url, favicon);
                                              }

                                              @Override
                                              public void onPageFinished(WebView view, String url) {
                                                  super.onPageFinished(view, url);
                                                  labHud.dismiss();
                                                  mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                                              }
                                          }


                );
                mWebView.loadUrl(appendedUrl);
            } else {
            }
        }
    }


    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processHTML(String html) {
            Log.d(TAG, html);
            count += 1;
            if (html.contains("Success") && count == 10) {
                WebViewActivity.this.finishAffinity();
                startActivity(new Intent(WebViewActivity.this, Main_Apps.class));
            } else if (count == 10) {
            }
        }
    }
}
