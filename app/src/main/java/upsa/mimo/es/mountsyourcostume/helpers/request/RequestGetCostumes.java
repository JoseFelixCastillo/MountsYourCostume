package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestGetCostumes {

    private static final String URL_REQUEST_SAVE_COSTUME_1 = "/userc";
    private static final String URL_REQUEST_SAVE_COSTUME_2 = "/costumes";

    public interface OnResponseGetCostumes{
        void onResponseGetCostumes(JSONObject response);
        void onErrorResposeGetCostumes(VolleyError error);
    }

    private OnResponseGetCostumes onResponseGetCostumes;
    private CloudSingleton cloudSingleton;
    private Context context;

    public RequestGetCostumes(CloudSingleton cloudSingleton, OnResponseGetCostumes onResponseGetCostumes, Context context){
        this.onResponseGetCostumes = onResponseGetCostumes;
        this.cloudSingleton = cloudSingleton;
        this.context = context;
    }

    public void requestGetCostumes(String category){
         String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_2 + category;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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

    public void requestGetCostumes(){
        String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_1 + "/" + MyApplication.getUser().getTokenForBD() + "/"
                + URL_REQUEST_SAVE_COSTUME_2;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
