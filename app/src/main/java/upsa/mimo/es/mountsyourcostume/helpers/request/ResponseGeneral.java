package upsa.mimo.es.mountsyourcostume.helpers.request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 01/09/2016.
 */
public class ResponseGeneral {
    public static final int OK = 0;
    public static final int JSON_MALFORMED_CODE = 1;
    public static final int USER_EXIST = 2;
    public static final int USER_NOT_FOUND = 3;
    public static final int UNMODIFIED = 4;
    public static final int NOT_FOUND = 5;
    public static final int UNSUPPORTED_FORMAT = 6;
    public static final int COSTUME_EXIST = 7;
    public static final int COSTUME_NOT_FOUND = 8;

    private int code;
    private String message;

    public ResponseGeneral(){

    }
    public ResponseGeneral(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseGeneral getFromVolleyError(VolleyError error){
        //  Log.d("SAVE COSTUME","error: " + VolleyErrorHelper.getMessage(error,getActivity()));
        JSONObject jsonObject = null;
        ResponseGeneral responseGeneral = null;
        try {
           // Log.d(TAG,"String data: " + error.networkResponse.toString());
          //  Log.d(TAG, "Error de SaveCostume: " + error.toString() + " Message: " + error.getMessage() +" OTRA; ");
            String data = null;

            data = new String(error.networkResponse.data);

       //     Log.d(TAG,"String data: " + data);
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject!=null){
            responseGeneral = ResponseGeneral.getFromJson(jsonObject);
         //   Log.d(TAG, "Error de SaveCostume: " + error.toString() + " Message: " + error.getMessage() +" OTRA; " + responseGeneral.getCode());
        }
        return responseGeneral;
    }

    public static ResponseGeneral getFromJson(JSONObject response){
        Gson gson = new Gson();
        ResponseGeneral responseGeneral = gson.fromJson(response.toString(), ResponseGeneral.class);
        return responseGeneral;
    }

}
