package upsa.mimo.es.mountsyourcostume.model;

/**
 * Created by User on 18/07/2016.
 */
public class User {
    private String name;
    private String photoURL;
    private String email;
    private int socialNetwork;

    public User(){

    }

    public User(String name, String photoURL){
        this.name = name;
        this.photoURL = photoURL;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(int socialNetwork) {
        this.socialNetwork = socialNetwork;
    }
}
