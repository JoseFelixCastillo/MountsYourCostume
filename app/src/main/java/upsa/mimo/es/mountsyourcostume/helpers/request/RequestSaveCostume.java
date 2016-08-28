package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestSaveCostume {

    private static final String URL_REQUEST_SAVE_COSTUME = "http://costumedb.herokuapp.com/user";
    public interface OnResponseSaveCostume{
        void onResponseSaveCostume(JSONObject response);
        void onErrorResposeSaveCostume(VolleyError error);
    }

    private OnResponseSaveCostume onResponseSaveCostume;
    private CloudSingleton cloudSingleton;

    public RequestSaveCostume(OnResponseSaveCostume onResponseSaveCostume, CloudSingleton cloudSingleton){
        this.onResponseSaveCostume = onResponseSaveCostume;
        this.cloudSingleton = cloudSingleton;
    }

    public void requestSaveCostume(Context context, Costume costume){
       // String url =
        JSONObject body = createBody(costume);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_REQUEST_SAVE_COSTUME, body, new Response.Listener<JSONObject>() {
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
        });

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject createBody(Costume costume){


        return null;
    }
}
