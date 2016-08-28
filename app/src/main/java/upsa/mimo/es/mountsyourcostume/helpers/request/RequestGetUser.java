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
public class RequestGetUser {

    private static final String URL_REQUEST_SAVE_COSTUME = "http://costumedb.herokuapp.com/users";

    public interface OnResponseGetUser{
        void onResponseGetUser(JSONObject response);
        void onErrorResposeGetUser(VolleyError error);
    }

    private OnResponseGetUser onResponseGetUser;
    private CloudSingleton cloudSingleton;

    public RequestGetUser(OnResponseGetUser onResponseGetUser, CloudSingleton cloudSingleton){
        this.onResponseGetUser = onResponseGetUser;
        this.cloudSingleton = cloudSingleton;
    }

    public void requestGetUser(Context context, Costume costume){
        // String url =

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_REQUEST_SAVE_COSTUME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseGetUser != null) {
                    onResponseGetUser.onResponseGetUser(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(onResponseGetUser!=null){
                    onResponseGetUser.onErrorResposeGetUser(error);
                }
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }
}
