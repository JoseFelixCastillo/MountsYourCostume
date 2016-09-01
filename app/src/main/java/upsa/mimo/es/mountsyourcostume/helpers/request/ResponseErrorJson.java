package upsa.mimo.es.mountsyourcostume.helpers.request;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by User on 01/09/2016.
 */
public class ResponseErrorJson extends ResponseGeneral{

    private JSONObject errors;

    public JSONObject getErrors() {
        return errors;
    }

    public void setErrors(JSONObject errors) {
        this.errors = errors;
    }

    public static ResponseErrorJson getFromJson(JSONObject response){
        Gson gson = new Gson();
        ResponseErrorJson responseErrorJson = gson.fromJson(response.toString(),ResponseErrorJson.class);
        return responseErrorJson;
    }
}
