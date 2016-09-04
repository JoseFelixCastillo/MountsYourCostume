package upsa.mimo.es.mountsyourcostume.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.utils.Utils;

/**
 * Created by JoseFelix on 29/04/2016.
 */
public class Costume implements Parcelable{

    private String name;
    private String category;
    private String materials;
    private String steps;
    private int prize;

    private transient String uri_image;
   // @SerializedName("encodedImage")
    private String encodedImage;

    public Costume(String name, String category, String materials, String steps, int prize, String uri_image) {
        this.name = name;
        this.category = category;
        this.materials = materials;
        this.steps = steps;
        this.prize = prize;
        this.uri_image = uri_image;
        this.encodedImage="hola";
      //  this.encodedImage = encodedImageinBase64(uri_image);

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

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
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
        dest.writeString(this.encodedImage);
    }

    protected Costume(Parcel in){
        this.name = in.readString();
        this.category = in.readString();
        this.materials = in.readString();
        this.steps = in.readString();
        this.prize = in.readInt();
        this.uri_image = in.readString();
        this.encodedImage = in.readString();
    }

    public static final Parcelable.Creator<Costume> CREATOR = new Parcelable.Creator<Costume>() {
        public Costume createFromParcel(Parcel source) {
            return new Costume(source);
        }

        public Costume[] newArray(int size) {
            return new Costume[size];
        }
    };


    public static Costume getFromJsonObject(JSONObject response, Context context){
        Costume costume = null;
        try{
            Gson gson = new Gson();
            costume = gson.fromJson(response.toString(),Costume.class);
        }
        catch(Exception e){
            Log.d("COSTUME", "Error al parsear costume");
        }
        Log.d("COSTUME","MIRANDO COSTUME: " + costume.getEncodedImage());
        if(costume.getEncodedImage()!=null&&!costume.getEncodedImage().equals("hola")){

            File file = null;
            byte[] decodedString = Base64.decode(costume.getEncodedImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            try {
                file = Utils.createFile(context);
                Utils.persistImageFromBitmap(file,decodedByte);
            } catch (Exception e) {

                e.printStackTrace();
            }

            if(file!=null){
                costume.setImage(file.getPath());
            }
            else{
                Log.d("ERROR", "ERROR AL PARSEAR LA IMAGEN");
            }
        }
        else{
            Log.d("COSTUME","ENTRO ELSE: " + costume.getEncodedImage());
            Uri uri = Uri.parse("R.drawable.no_photo_available");
            costume.setImage(uri.getPath());
        }
        //Log.d("TAG", "ocupa: " + costume.getEncodedImage().getBytes().length);
        return costume;
    }

    private String encodedImageinBase64(String uri_image){
        Bitmap bm = BitmapFactory.decodeFile(uri_image);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b,Base64.DEFAULT);
        Log.d("COSTUME", "ENcoded image: " + encodedImage);
        Log.d("TAG", "ocupa: " + encodedImage.getBytes().length);
        return encodedImage;
       // Utils.persistImageFromBitmap()
    }




}
