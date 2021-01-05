package com.example.linkusapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.linkusapp.R;

public class SearchAddress extends Activity {
    private WebView webView;
    //    private TextView addressText;
    private Handler handler;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent); //OK
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
//        addressText = findViewById(R.id.address_text);

        // WebView 초기화
//        init_webView();
//
//        // 핸들러를 통한 JavaScript 이벤트 반응
//        handler = new Handler();
        webView = (WebView) findViewById(R.id.address_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        webView.loadUrl("http://ec2-15-164-129-208.ap-northeast-2.compute.amazonaws.com:3000/");

    }


//    public class WebChromeClient extends android.webkit.WebChromeClient {
//
//        @Override
//        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//            WebView newWebView = new WebView(SearchAddress.this);
//            WebSettings webSettings = newWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//
//            final Dialog dialog = new Dialog(SearchAddress.this);
//            dialog.setContentView(newWebView);
//
//            ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);
//            dialog.show();
//
//            newWebView.setWebChromeClient(new WebChromeClient(){
//                @Override
//                public void onCloseWindow(WebView window) {
//                    dialog.dismiss();
//                }
//            });
//            ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
//            resultMsg.sendToTarget();
//            return true;
//        }
//    }
//    public void init_webView() {
//        // WebView 설정
//        webView = (WebView) findViewById(R.id.address_webView);
//        // JavaScript 허용
//        webView.getSettings().setJavaScriptEnabled(true);
//        // JavaScript의 window.open 허용
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setSupportMultipleWindows(true);
//        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
//        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
//        // web client 를 chrome 으로 설정
//        webView.setWebChromeClient(new WebChromeClient());
//        // webview url load. php 파일 주소
//        webView.loadUrl("http://ec2-15-164-129-208.ap-northeast-2.compute.amazonaws.com:3000/");
//    }
//    private class AndroidBridge {
//        @JavascriptInterface
//        public void setAddress(final String arg1, final String arg2, final String arg3) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
////                    addressText.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
//                    Bundle extra = new Bundle();
//                    Intent intent = new Intent();
//                    extra.putString("data", String.format("%s",arg3));
//                    intent.putExtras(extra);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    // WebView를 초기화 하지않으면 재사용할 수 없음
////                    init_webView();
////                    finish();
//                }
//            });
//        }
//    }


}
