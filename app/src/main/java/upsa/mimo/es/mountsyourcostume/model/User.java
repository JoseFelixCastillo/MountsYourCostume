package upsa.mimo.es.mountsyourcostume.model;

/**
 * Created by User on 18/07/2016.
 */
public class User {
    private String name;
    private String photoURL;

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
}
