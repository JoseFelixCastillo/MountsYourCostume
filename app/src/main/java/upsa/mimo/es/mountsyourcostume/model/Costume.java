package upsa.mimo.es.mountsyourcostume.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by JoseFelix on 29/04/2016.
 */
public class Costume implements Parcelable{

    private String name;
    private String category;
    private String materials;
    private String steps;
    private int prize;
    private String uri_image;

    public Costume(String name, String category, String materials, String steps, int prize, String uri_image) {
        this.name = name;
        this.category = category;
        this.materials = materials;
        this.steps = steps;
        this.prize = prize;
        this.uri_image = uri_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getUri_image() {
        return uri_image;
    }

    public void setImage(String uri_image) {
        this.uri_image = uri_image;
    }

    //methods for doing costume parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.materials);
        dest.writeString(this.steps);
        dest.writeInt(this.prize);
        dest.writeString(this.uri_image);
    }

    protected Costume(Parcel in){
        this.name = in.readString();
        this.category = in.readString();
        this.materials = in.readString();
        this.steps = in.readString();
        this.prize = in.readInt();
        this.uri_image = in.readString();
    }

    //Quizas no se use no lo se, comprobar
    public static final Parcelable.Creator<Costume> CREATOR = new Parcelable.Creator<Costume>() {
        public Costume createFromParcel(Parcel source) {
            return new Costume(source);
        }

        public Costume[] newArray(int size) {
            return new Costume[size];
        }
    };
}
