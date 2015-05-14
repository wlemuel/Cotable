package tk.wlemuel.cotable.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

import tk.wlemuel.cotable.utils.TLog;

/**
 * ApiHttpClient
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc ApiHttpClient
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class ApiHttpClient {

    public static final String TAG = ApiHttpClient.class.getSimpleName();

    private static final String HOST = BaseApi.HOST;
    private static final String DEV_HOST = BaseApi.HOST;
    private static final String DEV_API_URL = BaseApi.DEFAULT_API_URL;
    private static final String REAL_API_URL = BaseApi.DEFAULT_API_URL;
    private static final String DEF_API_URL = DEV_API_URL;

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    public static AsyncHttpClient client;
    private static String API_URL;
    private static boolean isUseSSL;

    // Set the API_URL
    static {
        API_URL = DEF_API_URL;
        isUseSSL = false;
    }

    public ApiHttpClient() {

    }

    /**
     * Returns the client.
     *
     * @return the client
     */
    public static AsyncHttpClient getHttpClient() {
        if (client == null) setHttpClient(new AsyncHttpClient());
        return client;
    }


    /**
     * Set the httpclient with heads
     *
     * @param c client
     */
    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        client.addHeader("Host", HOST);
        client.addHeader("Connection", "keep-alive");
        client.setUserAgent(ApiClientHelper.getUserAgent());
    }

    /**
     * Change the host for the current client.
     *
     * @param host the host
     */
    public static void setClientHost(String host) {
        getHttpClient();
        client.removeHeader("Host");
        client.addHeader("Host", host);
    }

    /**
     * Reset the host.
     */
    public static void resetClientHost() {
        setClientHost(HOST);
    }


    /**
     * Set the user agent for the client
     *
     * @param userAgent user-agent
     */
    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    /**
     * Return the API_URL
     *
     * @return {@code API_URL}
     */
    public static String getApiUrl() {
        return API_URL;
    }

    /**
     * Set the API_URL
     *
     * @param apiUrl given apiurl
     */
    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    /**
     * Reset the API_URL
     */
    public static void resetApiUrl() {
        API_URL = DEF_API_URL;
    }

    /**
     * Set using the ssl or not.
     *
     * @param isUse boolean
     */
    public static void setIsUseSSL(boolean isUse) {
        isUseSSL = isUse;
    }

    /**
     * Cancel all the requests invoked by the {@code context}
     *
     * @param context context
     */
    public static void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }

    /**
     * Combine the API_URL and the relative url.
     *
     * @param relativeUrl relative url
     * @return the final url.
     */
    public static String getAbsoluteApiUrl(String relativeUrl) {

        String url = String.format((isUseSSL ? HTTPS : HTTP) + API_URL, relativeUrl);
        TLog.log(TAG, "Request: " + url);
        return url;
    }

    /**
     * Perform a HTTP GET request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param params      additional GET parameters to send with the request.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void get(String relativeUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(relativeUrl), params, handler);
        TLog.log(TAG, "GET " + relativeUrl + " & " + params.toString());
    }

    /**
     * Perform a HTTP GET request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void get(String relativeUrl, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(relativeUrl), handler);
        TLog.log(TAG, "GET " + relativeUrl);
    }

    /**
     * Perform a HTTP GET request with parameters.
     *
     * @param url     the URL to send the request to.
     * @param handler the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void getDirect(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
        TLog.log(TAG, "GET " + url);
    }

    /**
     * Perform a HTTP DELETE request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void delete(String relativeUrl, AsyncHttpResponseHandler handler) {
        client.delete(getAbsoluteApiUrl(relativeUrl), handler);
        TLog.log(TAG, "DELETE " + relativeUrl);
    }


    /**
     * Perform a HTTP POST request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param params      additional GET parameters to send with the request.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void post(String relativeUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(relativeUrl), params, handler);
        TLog.log(TAG, "POST " + relativeUrl + " & " + params.toString());
    }


    /**
     * Perform a HTTP POST request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void post(String relativeUrl, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(relativeUrl), handler);
        TLog.log(TAG, "POST " + relativeUrl);
    }

    /**
     * Perform a HTTP POST request with parameters.
     *
     * @param url     the URL to send the request to.
     * @param handler the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void postDirect(String url, AsyncHttpResponseHandler handler) {
        client.post(url, handler);
        TLog.log(TAG, "POST " + url);
    }

    /**
     * Perform a HTTP PUT request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param params      additional GET parameters to send with the request.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void put(String relativeUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(relativeUrl), params, handler);
        TLog.log(TAG, "PUT " + relativeUrl + " & " + params.toString());
    }

    /**
     * Perform a HTTP PUT request with parameters.
     *
     * @param relativeUrl the URL to send the request to.
     * @param handler     the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public static void put(String relativeUrl, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(relativeUrl), handler);
        TLog.log(TAG, "PUT " + relativeUrl);
    }


}
