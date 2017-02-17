package co.crossroadsapp.overwatch.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import co.crossroadsapp.overwatch.ControlManager;
import co.crossroadsapp.overwatch.R;
import co.crossroadsapp.overwatch.data.UserData;
import co.crossroadsapp.overwatch.network.NetworkEngine;
import co.crossroadsapp.overwatch.network.OverwatchFetchUserProfileNetwork;
import co.crossroadsapp.overwatch.utils.Constants;
import co.crossroadsapp.overwatch.utils.TravellerLog;
import co.crossroadsapp.overwatch.utils.Util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by karagdi on 1/13/17.
 */
public class LoginFragment extends PreChooseGameTagFragment {
    final String finalInfoUrl = "https://" + Constants.DOMAIN + "/api/v1/a/user/addConsole?consoleType=pc";
    //final String finalInfoUrl = "https://" + Constants.DOMAIN + "/api/v1/auth/login";
    private View _load_view;
    private TextView _loading_text;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.login_layout_fragment, container, false);
        this._activity = getActivity();
        launchWebView(v);
        return v;
    }

    private void showLoading(String messages) {
        if (this._load_view != null) {
            this._load_view.setVisibility(View.VISIBLE);
        }
        if (this._loading_text != null) {
            this._loading_text.setText(messages);
        }
    }

    private void hideLoading() {
        if (this._load_view != null) {
            this._load_view.setVisibility(View.GONE);
        }
        if (this._loading_text != null) {
            this._loading_text.setText("");
        }
    }

    private void launchWebView(final View v) {
        final WebView wv = ((WebView) v.findViewById(R.id.login_webview));
        if (wv != null) {
            wv.setVisibility(View.GONE);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.clearFormData();
            wv.getSettings().setSavePassword(false);
            wv.getSettings().setSaveFormData(false);
            wv.setWebViewClient(new LoginWebViewClient());
            Activity activity = (Activity) v.getContext();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            this._load_view = v.findViewById(R.id.load_view);
            this._loading_text = (TextView) v.findViewById(R.id.loading_text);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoading("Loading...");
                    //wv.loadUrl(finalInfoUrl);
                    CookieSyncManager.createInstance(wv.getContext());
                    loadCookieToWebView(wv);
                    CookieSyncManager.getInstance().sync();
                    HashMap<String, String> headerMap = new HashMap<>();
                    String cookies = CookieManager.getInstance().getCookie(Constants.DOMAIN);
                    headerMap.put("Cookie", "connect.sid=s%3AuOpOgpSshFoKf0oKqP5ot7Yat1aC443-.kl32Vp65Sjc3WaEh7%2Fv38ISGBLfTGMwVUNrkoj77tB8; domain=overwatch-staging.herokuapp.com");
                    wv.loadUrl(finalInfoUrl);

                }
            });
        }
    }

    private void loadCookieToWebView(WebView wv)
    {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(wv, true);
        }

        List<Cookie> cookies = NetworkEngine.getmInstance(wv.getContext()).getAllCookies();

        cookieManager.removeAllCookie();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
                cookieManager.setCookie(cookie.getDomain(), cookieString);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
    }

    private void fetchProfile(Context ctx) {
        OverwatchFetchUserProfileNetwork loginNetwork = new OverwatchFetchUserProfileNetwork(ctx);
        loginNetwork.addObserver(this);
        try {
            loginNetwork.fetchProfile();
        } catch (JSONException e) {
            update(loginNetwork, null);
        }
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private class LoginWebViewClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().getPath();
            if (url != null && url.contains("success")) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //showLoading("Please wait...");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            /*
             * Once webview is fully loaded, set the mContent background to be transparent
             * and make visible the 'x' image.
             */
            //_content.setBackgroundColor(Color.TRANSPARENT);
            if (url.contains("success")) {
                view.setVisibility(View.GONE);
                /*String cookies = CookieManager.getInstance().getCookie(url);
                TravellerLog.w(this, "Cookie: " + cookies);
                Util.setDefaults("cookie", cookies, view.getContext());*/
                showLoading("Fetching profile, please wait...");
                //fetchUserProfile(view.getContext());
                //new PullUserProfile(view.getContext()).execute(cookies);
                fetchProfile(view.getContext());
            } else {
                hideLoading();
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    public InputStream httpGetConnect(String urlString, String cookie, boolean saveCookie, Map<String, String> headers) throws IOException, Exception {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + urlString);
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(Constants.CONTENT_TYPE, "application/json");
            if (cookie != null)
                conn.setRequestProperty(Constants.COOKIE_CAPITAL, cookie);
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    conn.setRequestProperty(key, headers.get(key));
                }
            }
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Post failed with error code " + status + " request url: " + urlString);
            }
            if (saveCookie) {
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(Constants.COOKIES_HEADER);
                if (cookiesHeader != null) {
                }
            }
            return conn.getInputStream();
        } finally {
            if (conn != null) {
                //conn.disconnect();
            }
        }
    }

    private UserData user;

    private class PullUserProfile extends AsyncTask<String, Void, Boolean> {
        private Context _context;

        public PullUserProfile(Context context) {
            this._context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params == null || params.length != 1) {
                return false;
            }

            String cookies = params[0];
            try {
                InputStream is = httpGetConnect("https://overwatch-staging.herokuapp.com/api/v1/a/user/self", cookies, true, null);
                if (is == null) {
                    throw new NullPointerException("Request class name to get user profile failed");
                }
                InputStreamReader isr = new InputStreamReader(is);
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(isr);
                String read = br.readLine();
                String dimitare = "";
                while (read != null) {
                    //System.out.println(read);
                    sb.append(read);
                    read = br.readLine();

                    dimitare = sb.toString();
                }
                TravellerLog.w(this, "dimitare: " + dimitare);
                JSONObject response = new JSONObject(dimitare);
                user = new UserData();
                user.toJson(response);
                //user.setUser("Dimitare");
                //user.setImageUrl("https://s3-us-west-2.amazonaws.com/frienduel-assets/metaimages/icn_avatar.png");
                //user.setAuthenticationId(Constants.LOGIN);
                /*JsonFactory jf = new JsonFactory();
                JsonParser jp = null;
                if (sb != null && sb.length() > 0) {
                    jp = jf.createParser(sb.toString());

                } else {
                    jp = jf.createParser(is);
                }*/
                ControlManager.getmInstance().setUserdata(user);
                Util.updateCooieFromBackend(this._context, cookies);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final Activity activity = (Activity) getContext();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //refreshSuccess((ProfileRespond) result);
                    exitLoginPage(activity);
                }
            });
        }
    }

/*    private void fetchProfile(final Activity activity) {
        FetchProfileHelper.TrimbleNetworkListener listener = new BaseNetworkHelper.TrimbleExtendedNetworkListener() {
            @Override
            public void onStart() {
                View v = getView();
                if (v != null) {
                    *//*TextView host_player_name = (TextView) v.findViewById(R.id.host_player_name);
                    if (host_player_name != null) {
                        host_player_name.setVisibility(View.VISIBLE);
                        host_player_name.setText(R.string.fetching_profile);
                    }*//*
                }
            }

            @Override
            public void onResults(final TrimbleHTTPResponse result) {
                if (result instanceof ProfileRespond) {
                    ProfileRespond profile = (ProfileRespond) result;
                    HostUser host = profile.getUser();
                    if (host != null) {
                        OverwatchUtils.updateHostUserCache(host);
                    }
                    if (activity == null || activity.isFinishing()) {
                        return;
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //refreshSuccess((ProfileRespond) result);
                            exitLoginPage(activity);
                        }
                    });
                } else {
                    onRequestError(new TrimbleException(TrimbleException.TRIMBLE_INTERNAL_EXCEPTION));
                }
                FetchProfileHelper.getInstance().cleanUpListeners();
            }

            @Override
            public void onRequestError(TrimbleException exception) {
                FragmentActivity activity = getActivity();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showError(getView());
                    }
                });
                FetchProfileHelper.getInstance().cleanUpListeners();
            }

            @Override
            public void onRequestProgress(int percentage) {

            }
        };
        FetchProfileHelper.fetchProfile(listener);
    }*/

    private void showError(View view) {
    }
}
