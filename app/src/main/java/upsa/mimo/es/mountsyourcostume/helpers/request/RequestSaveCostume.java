package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

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
        void onResponseSaveCostume(JSONArray response);
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

       // JSONObject body = createBody(costume);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
              //  headers.put("Content-Type", "application/json; charset=utf-8");
               // headers.put("Accept-Language", "es");

                return headers;
            }

            @Override
            public byte[] getBody() {
                return createBody(costume);
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };


        this.cloudSingleton.addToRequestQueue(jsonArrayRequest);

    }

    private byte[] createBody(Costume costume){

        byte[] body = null;
        Gson gson = new Gson();
        String jsonString = gson.toJson(costume);

        body = jsonString.getBytes();


        return body;

    }
}
