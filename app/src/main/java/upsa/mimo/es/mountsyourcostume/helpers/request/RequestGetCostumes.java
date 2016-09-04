package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestGetCostumes {

    private static final String URL_REQUEST_SAVE_COSTUME_1 = "/userc";
    private static final String URL_REQUEST_SAVE_COSTUME_2 = "/costumes";

    public interface OnResponseGetCostumes{
        void onResponseGetCostumes(JSONArray response);
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
         String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_2 + "/" +category;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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

        this.cloudSingleton.addToRequestQueue(jsonArrayRequest);

    }

    public void requestGetCostumes(){
        String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME_1 + "/" + MyApplication.getUser().getTokenForBD() + URL_REQUEST_SAVE_COSTUME_2;
        // String url = "http://52.29.230.208:9000/costumes/estoMismo";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (onResponseGetCostumes != null) {
                    onResponseGetCostumes.onResponseGetCostumes(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.d("GETCOSTUMES","Esto: " + VolleyErrorHelper.getMessage(error,context));
                if(onResponseGetCostumes!=null){
                    onResponseGetCostumes.onErrorResposeGetCostumes(error);
                }
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonArrayRequest);
    }

}
