package upsa.mimo.es.mountsyourcostume.model;


import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by User on 18/07/2016.
 */
public class User {
    private String name;
    private transient String photoURL;
    private String tokenForBD;  //email for google and
    private transient int socialNetwork;

    public User(){
    }

    public User(String name, String photoURL, String tokenForBD){
        this.name = name;
        this.photoURL = photoURL;
        this.tokenForBD = tokenForBD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getTokenForBD() {
        return tokenForBD;
    }

    public void setTokenForBD(String tokenForBD) {
        this.tokenForBD = tokenForBD;
    }

    public int getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(int socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public static User getFromJson(JSONObject response){
        Gson gson = new Gson();
        User user = gson.fromJson(response.toString(),User.class);
        return user;
    }

}
