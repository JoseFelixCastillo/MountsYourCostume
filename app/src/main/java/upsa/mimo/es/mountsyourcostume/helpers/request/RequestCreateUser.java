package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.User;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestCreateUser {

    private static final String URL_REQUEST_SAVE_COSTUME = "http://costumedb.herokuapp.com/user";
    public interface OnResponseCreateUser{
        void onResponseCreateUser(JSONObject response);
        void onErrorResposeCreateUser(VolleyError error);
    }

    private OnResponseCreateUser onResponseCreateUser;
    private CloudSingleton cloudSingleton;

    public RequestCreateUser(OnResponseCreateUser onResponseCreateUser, CloudSingleton cloudSingleton){
        this.onResponseCreateUser = onResponseCreateUser;
        this.cloudSingleton = cloudSingleton;
    }

    public void requestCreateUser(Context context, User user){
        // String url =
        JSONObject body = createBody(user);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_REQUEST_SAVE_COSTUME, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onResponseCreateUser != null) {
                    onResponseCreateUser.onResponseCreateUser(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(onResponseCreateUser!=null){
                    onResponseCreateUser.onErrorResposeCreateUser(error);
                }
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject createBody(User user){


        return null;
    }
}
