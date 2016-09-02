package upsa.mimo.es.mountsyourcostume.helpers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestCreateUser;
import upsa.mimo.es.mountsyourcostume.helpers.request.ResponseGeneral;

/**
 * Created by User on 31/08/2016.
 */
public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);
        }
        else if (isServerProblem(error)) {
            return handleServerError(error, context);
        }
        else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    /**
     * Determines whether the error is related to network
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }
    /**
     * Determines whether the error is related to server
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 400:
                case 404:
                   try {
                       String dataString = new String(response.data);
                       JSONObject json = new JSONObject(dataString);
                       ResponseGeneral responseGeneral = ResponseGeneral.getFromJson(json);
                       if(responseGeneral.getCode()==ResponseGeneral.USER_NOT_FOUND){
                           createUser();
                           return context.getString(R.string.user_not_found);
                       }
                       return responseGeneral.getMessage();
                      /* switch (responseGeneral.getCode()){
                           case ResponseGeneral.JSON_MALFORMED_CODE:
                               return context.getString(R.string.miss_param);
                           case ResponseGeneral.COSTUME_EXIST:
                               return context.getString(R.string.costume_exist);
                           case ResponseGeneral.COSTUME_NOT_FOUND:
                               return context.getString(R.string.costume_not_found);
                           case ResponseGeneral.USER_EXIST:
                               return context.getString(R.string.user_exist);
                           case ResponseGeneral.USER_NOT_FOUND:
                               return context.getString(R.string.user_not_found);
                       }*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request

                default:
                    return context.getResources().getString(R.string.generic_server_down);
            }
        }
        return context.getResources().getString(R.string.generic_error);
    }

    private static void createUser(){
        MyApplication.getCloudPersistance().createUser(MyApplication.getUser(), new RequestCreateUser.OnResponseCreateUser() {
            @Override
            public void onResponseCreateUser(JSONObject response) {
                // Log.d(TAG, "Response de SaveCostume: " + response.toString());
                MyApplication.hideProgressDialog();
            }

            @Override
            public void onErrorResponseCreateUser(VolleyError error) {
                //  Log.d("SAVE COSTUME","error: " + VolleyErrorHelper.getMessage(error,getActivity()));
                MyApplication.hideProgressDialog();
            }
        });
    }
}
