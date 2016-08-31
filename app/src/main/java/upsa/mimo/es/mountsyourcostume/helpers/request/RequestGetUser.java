package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.helpers.VolleyErrorHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestGetUser {

    private static final String URL_REQUEST_SAVE_COSTUME = "/users";

    public interface OnResponseGetUser{
        void onResponseGetUser(JSONArray response);
        void onErrorResposeGetUser(VolleyError error);
    }

    private OnResponseGetUser onResponseGetUser;
    private CloudSingleton cloudSingleton;

    public RequestGetUser(OnResponseGetUser onResponseGetUser, CloudSingleton cloudSingleton){
        this.onResponseGetUser = onResponseGetUser;
        this.cloudSingleton = cloudSingleton;
    }

    public void requestGetUser(final Context context, Costume costume){
         String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (onResponseGetUser != null) {
                    onResponseGetUser.onResponseGetUser(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error,context);
                /*if(onResponseGetUser!=null){
                    onResponseGetUser.onErrorResposeGetUser(error);
                }*/
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonArrayRequest);

    }
}
