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
public class RequestGetCostumes {

    private static final String URL_REQUEST_SAVE_COSTUME = "http://costumedb.herokuapp.com/users";

    public interface OnResponseGetCostumes{
        void onResponseGetCostumes(JSONObject response);
        void onErrorResposeGetCostumes(VolleyError error);
    }

    private OnResponseGetCostumes onResponseGetCostumes;
    private CloudSingleton cloudSingleton;

    public RequestGetCostumes(OnResponseGetCostumes onResponseGetCostumes, CloudSingleton cloudSingleton){
        this.onResponseGetCostumes = onResponseGetCostumes;
        this.cloudSingleton = cloudSingleton;
    }

    public void requestGetCostumes(Context context, Costume costume){
        // String url =

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_REQUEST_SAVE_COSTUME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseGetCostumes != null) {
                    onResponseGetCostumes.onResponseGetCostumes(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(onResponseGetCostumes!=null){
                    onResponseGetCostumes.onErrorResposeGetCostumes(error);
                }
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

}
