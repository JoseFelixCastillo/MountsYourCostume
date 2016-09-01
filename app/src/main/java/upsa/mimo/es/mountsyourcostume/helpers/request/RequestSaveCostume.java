package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestSaveCostume {

    private static final String TAG = RequestSaveCostume.class.getSimpleName();
    private static final String URL_REQUEST_SAVE_COSTUME_1 = "/userc";
    private static final String URL_REQUEST_SAVE_COSTUME_2 = "/costume";
    public interface OnResponseSaveCostume{
        void onResponseSaveCostume(JSONObject response);
        void onErrorResposeSaveCostume(VolleyError error);
    }

    private OnResponseSaveCostume onResponseSaveCostume;
    private CloudSingleton cloudSingleton;
    private Context context;

    public RequestSaveCostume(CloudSingleton cloudSingleton, OnResponseSaveCostume onResponseSaveCostume, Context context){
        this.onResponseSaveCostume = onResponseSaveCostume;
        this.cloudSingleton = cloudSingleton;
        this.context = context;
    }

    public void requestSaveCostume(final Costume costume){
        String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_1 + "/" + MyApplication.getUser().getTokenForBD() + URL_REQUEST_SAVE_COSTUME_2;

        JSONObject body = createBody(costume);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseSaveCostume != null) {
                    onResponseSaveCostume.onResponseSaveCostume(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(onResponseSaveCostume!=null){
                    onResponseSaveCostume.onErrorResposeSaveCostume(error);
                }
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject createBody(Costume costume){

        JSONObject body = null;
        Gson gson = new Gson();
        String jsonString = gson.toJson(costume);

        try {
            body = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;

    }
}
