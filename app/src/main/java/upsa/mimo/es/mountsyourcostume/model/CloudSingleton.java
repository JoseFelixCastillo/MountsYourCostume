package upsa.mimo.es.mountsyourcostume.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class CloudSingleton {
    private static CloudSingleton instance;
    private RequestQueue mRequestQueue;
    private static Context context;

    private CloudSingleton(Context context) {
        this.context = context;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized CloudSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new CloudSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
