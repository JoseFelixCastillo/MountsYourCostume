package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 04/09/2016.
 */
public class RequestDeleteCostume {
    private static final String TAG = RequestSaveCostume.class.getSimpleName();
    private static final String URL_REQUEST_SAVE_COSTUME_1 = "/userc";
    private static final String URL_REQUEST_SAVE_COSTUME_2 = "/costume";
    public interface OnResponseDeleteCostume{
        void onResponseDeleteCostume(JSONObject response);
        void onErrorResposeDeleteCostume(VolleyError error);
    }

    private OnResponseDeleteCostume onResponseDeleteCostume;
    private CloudSingleton cloudSingleton;
    private Context context;

    public RequestDeleteCostume(CloudSingleton cloudSingleton, OnResponseDeleteCostume onResponseDeleteCostume, Context context){
        this.onResponseDeleteCostume = onResponseDeleteCostume;
        this.cloudSingleton = cloudSingleton;
        this.context = context;
    }

    public void requestDeleteCostume(final String name){
        String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_1 + "/" + MyApplication.getUser().getTokenForBD() + URL_REQUEST_SAVE_COSTUME_2 + "/"+name;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseDeleteCostume != null) {
                    onResponseDeleteCostume.onResponseDeleteCostume(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(onResponseDeleteCostume!=null){
                    onResponseDeleteCostume.onErrorResposeDeleteCostume(error);
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept-Language", "es");
                return params;
            }
        };

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

}
