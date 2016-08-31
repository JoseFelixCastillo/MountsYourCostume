package upsa.mimo.es.mountsyourcostume.helpers.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import upsa.mimo.es.mountsyourcostume.helpers.CloudDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.User;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class RequestCreateUser {

    private static final String TAG = RequestCreateUser.class.getSimpleName();
    private static final String URL_REQUEST_SAVE_COSTUME = "/user";

    public interface OnResponseCreateUser{
        void onResponseCreateUser(JSONObject response);
        void onErrorResponseCreateUser(VolleyError error);
    }

    private OnResponseCreateUser onResponseCreateUser;
    private CloudSingleton cloudSingleton;
    private Context context;

    public RequestCreateUser(CloudSingleton cloudSingleton, OnResponseCreateUser onResponseCreateUser, Context context){
        this.cloudSingleton = cloudSingleton;
        this.onResponseCreateUser = onResponseCreateUser;
        this.context = context;
    }

    public void requestCreateUser(User user){
        String url = CloudDBHelper.URL + URL_REQUEST_SAVE_COSTUME;
        JSONObject body = createBody(user);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Response create user: " + response.toString());
                if(onResponseCreateUser!=null){
                    onResponseCreateUser.onResponseCreateUser(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error create user: " + error.getMessage());
                if(onResponseCreateUser!=null){
                    onResponseCreateUser.onErrorResponseCreateUser(error);
                }
            }
        });

        this.cloudSingleton.addToRequestQueue(jsonObjectRequest);

    }

    private JSONObject createBody(User user){
        JSONObject body = null;
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        Log.d("CREATE USER1","Viendo -string: " + jsonString);
        try {
            body = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
       /* Log.d("CREATE USER","Viendo: " + body.toString());
        JSONArray jsonArray = null;
        try {
           jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
*/

    }
}
