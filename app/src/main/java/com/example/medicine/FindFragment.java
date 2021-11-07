package com.example.medicine;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class FindFragment extends Fragment {

    private LinearLayout layoutView;
    private WebView mWebView;
    private WebSettings mWebSettings;

    private Button btnFindHospital;
    private Button btnFindPharmacy;
    CookieManager cookieManager;
    BackPressCloseHandler backPressCloseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutView = (LinearLayout) inflater.inflate(R.layout.fragment_find, container, false);
        backPressCloseHandler = new BackPressCloseHandler(getActivity());


        // 웹뷰 시작
        mWebView = (WebView) layoutView.findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent e) {
                if(e.getAction() != KeyEvent.ACTION_DOWN){
                    return true;
                }
                if(e.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    if(mWebView.canGoBack()){
                        mWebView.goBack();
                    }else{
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }

        });

        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        // 세팅
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스크립트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부


        //찾기 버튼
        btnFindHospital = layoutView.findViewById(R.id.button_find_hospital);
        btnFindPharmacy = layoutView.findViewById(R.id.button_find_pharmacy);

        btnFindHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFindPharmacy.setBackgroundColor(getResources().getColor(R.color.findButton));
                btnFindHospital.setBackgroundColor(getResources().getColor(R.color.findButtonClick));
                mWebView.loadUrl(getString(R.string.url_find_hospital)); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
            }
        });

        btnFindPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFindHospital.setBackgroundColor(getResources().getColor(R.color.findButton));
                btnFindPharmacy.setBackgroundColor(getResources().getColor(R.color.findButtonClick));
                mWebView.loadUrl(getString(R.string.url_find_pharmacy)); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
            }
        });

        return layoutView;
    }

}