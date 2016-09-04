package upsa.mimo.es.mountsyourcostume.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.dialogs.DialogChooseOptionCamera;
import upsa.mimo.es.mountsyourcostume.events.MessageOptionCameraEvent;
import upsa.mimo.es.mountsyourcostume.helpers.VolleyErrorHelper;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestSaveCostume;
import upsa.mimo.es.mountsyourcostume.helpers.request.ResponseGeneral;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.utils.Utils;

public class SaveCostumeFragment extends Fragment {

    private static final int CODE_PERMISSION_CAMERA = 80;

    private final String TAG = "SAVE_COSTUME";

    //Fields
    private String name;
    private String category;
    private String materials;
    private String steps;
    private int prize;

    SQLiteDatabase db;

    //For photo
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_PHOTO_BITMAP = "photoBitmap";

    //  private ViewGroup container;
    //For savedinstance
    private Bitmap photoBitmap;

    private File actuallyPhotoFile;
    @BindView(R.id.save_costume_container)
    ViewGroup container;
    //   private EditText editTextSteps;
    @BindView(R.id.edit_text_prize)
    EditText editTextPrize;
    @BindView(R.id.image_view_costume)
    ImageView imageViewPhoto;
    //  private ImageView imageViewPhoto;
    @BindView(R.id.edit_text_name)
    EditText editTextName;
    //  private EditText editTextName;
    @BindView(R.id.edit_text_materials)
    EditText editTextMaterials;
    //  private EditText editTextMaterials;
    @BindView(R.id.edit_text_steps)
    EditText editTextSteps;

    @BindView(R.id.spinner)
    Spinner spinner;

    @OnClick(R.id.floating_button_save_favourites)
    void floatingSaveFavourites(){
        saveInFavourites();
    }

    @OnClick(R.id.floating_button_save_cloud)
    void floatingSaveCloud(){
        saveInCloud();
    }

    @OnClick(R.id.image_view_costume)
    void imageViewCostume(){
        createDialogForPhoto();
    }

   // private EditText editTextPrize;


    public SaveCostumeFragment() {
        // Required empty public constructor
    }


    public static SaveCostumeFragment newInstance() {
        SaveCostumeFragment fragment = new SaveCostumeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save_costume, container, false);

        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
        //    loadUI();
            initSpinner();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView()!=null){

        }
        if(savedInstanceState!=null){
            if(savedInstanceState.getParcelable(KEY_PHOTO_BITMAP)!=null){
                imageViewPhoto.setImageBitmap((Bitmap) savedInstanceState.getParcelable(KEY_PHOTO_BITMAP));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(actuallyPhotoFile!=null){
            Log.d(TAG, "eliminando foto en ondestroy");
            actuallyPhotoFile.delete();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(imageViewPhoto.getDrawable()!=null){
            photoBitmap = ((BitmapDrawable) imageViewPhoto.getDrawable()).getBitmap();
            outState.putParcelable(KEY_PHOTO_BITMAP, photoBitmap);
        }

    }
    //For spinner
    private void initSpinner(){
      //  Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categorys, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Methods for camera
    private void doPhotoWithCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = null; // create a file to save the image
        try {
            fileUri = Uri.fromFile(createTempFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doPhotoWithCamera  " + fileUri.getEncodedPath());

        if(fileUri!=null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else{
            Log.d(TAG, "file URI null");
        }
    }

    private boolean checkPermissionCamera(){
        if(Build.VERSION.SDK_INT >=23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CODE_PERMISSION_CAMERA);
                return false;
            }
        }
        else{
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "entro a los permisos");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODE_PERMISSION_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "entro al ok de los permisos");
                    doPhotoWithCamera();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                //La imagen esta en la memoria cache pero quiero tenerla en memoria normal asi que lo copio en un fichero normal

                Bitmap imageBitmap = BitmapFactory.decodeFile(actuallyPhotoFile.getPath());

                setPicToImageView(imageBitmap);

            } else if (resultCode == getActivity().RESULT_CANCELED) {

            } else {

            }
        }
        if(requestCode == PICK_IMAGE_REQUEST){

            if(resultCode == getActivity().RESULT_OK){
                if(data!=null){
                    Uri uri = data.getData();

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(bitmap!=null) {
                        setPicToImageView(bitmap);

                    }
                }
            }
            else if(resultCode == getActivity().RESULT_CANCELED){

            }
            else{

            }
        }
    }
    private File createTempFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File tempFile = File.createTempFile(timeStamp, ".jpg", getActivity().getCacheDir());
        tempFile.setWritable(true, false);
        //if the file contains image, we erase that image that we will not need more and replace it with the new image
        if(actuallyPhotoFile!=null) {
            actuallyPhotoFile.delete();
        }
        actuallyPhotoFile = tempFile;
        return tempFile;
    }

    //Change dimension of the image
    private void setPicToImageView(Bitmap immutableBitmap){
        // Get the dimensions of the View
        int targetW = imageViewPhoto.getWidth();
        int targetH = imageViewPhoto.getHeight();


        Bitmap output = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) targetW / immutableBitmap.getWidth(), (float) targetH / immutableBitmap.getHeight());
        canvas.drawBitmap(immutableBitmap, m, new Paint());
        Log.d(TAG, "Mutableeeeeeee");
        imageViewPhoto.setImageBitmap(output);

    }

    private void chooseGalleryImage(){
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void saveInCloud(){

        if(getFields()&&category!=null){
            File file = null;
            try {
                file = persistImageFromImageView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(file!=null) {

                Costume costume = new Costume(name,category,materials,steps,prize,file.getPath());

                MyApplication.showProgressDialog(getActivity());
                requestSaveCostume(costume);

            }
            else{
                MyApplication.showMessageInSnackBar(container,getActivity().getString(R.string.need_image));
            }
        }
        else{
            MyApplication.showMessageInSnackBar(container,getActivity().getString(R.string.fill_fields));

        }
    }

    private void requestSaveCostume(final Costume costume){

        MyApplication.getCloudPersistance().saveCostume(costume, new RequestSaveCostume.OnResponseSaveCostume() {
            @Override
            public void onResponseSaveCostume(JSONObject response) {

                ResponseGeneral responseGeneral = ResponseGeneral.getFromJson(response);
                MyApplication.showMessageInSnackBar(container, responseGeneral.getMessage());

                MyApplication.hideProgressDialog();
            }

            @Override
            public void onErrorResposeSaveCostume(VolleyError error) {
                String message = VolleyErrorHelper.getMessage(error,getActivity());
                if(!(message==getActivity().getString(R.string.user_not_found))){
                    MyApplication.showMessageInSnackBar(container, message);
                    MyApplication.hideProgressDialog();
                }
            }
        });
    }

    private void saveInFavourites(){
        if(getFields()&&category!=null){
            File file = null;
            try {
                file = persistImageFromImageView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(file!=null) {

                Costume costume = new Costume(name,category,materials,steps,prize,file.getPath());
                long rows = MyApplication.getLocalPersistance().saveCostume(costume);

                if (rows > 0) {
                    MyApplication.showMessageInSnackBar(container,getActivity().getString(R.string.save_ok));
                } else {

                }
            }
            else{
                MyApplication.showMessageInSnackBar(container,getActivity().getString(R.string.need_image));
            }
        }
        else{
            MyApplication.showMessageInSnackBar(container,getActivity().getString(R.string.fill_fields));

        }
    }
    private void setFileToActuallyPhotoFile(File persistentFile){
        if(actuallyPhotoFile!=null){
            actuallyPhotoFile.delete();
            actuallyPhotoFile=persistentFile;
        }
    }

    private boolean getFields(){
        String name = editTextName.getText().toString();
        String materials = editTextMaterials.getText().toString();
        String steps = editTextSteps.getText().toString();
        String prizeString = editTextPrize.getText().toString();



        if((name.matches(""))||(materials.matches(""))||(steps.matches(""))||prizeString.matches("")){

            Log.d(TAG,"campos malos");
            return false;
        }
        else{
            //campos validos
            this.name = name;
            this.materials = materials;
            this.steps = steps;
            this.prize = Integer.parseInt(prizeString);
            Log.d(TAG,"campos buenos");
            return true;
        }
    }

    //devuelve un archivo del imageView
    private File persistImageFromImageView() throws Exception{
        if(imageViewPhoto.getDrawable()!=null) {
           File file = Utils.createFile(getActivity());


            Bitmap image = ((BitmapDrawable) imageViewPhoto.getDrawable()).getBitmap();


            return Utils.persistImageFromBitmap(file,image);
        }
        return null;
    }


    private void createDialogForPhoto(){

        DialogChooseOptionCamera dialogChooseOptionCamera = DialogChooseOptionCamera.newInstance();
        FragmentManager fm = getChildFragmentManager();
        dialogChooseOptionCamera.show(fm,DialogChooseOptionCamera.TAG);
        Log.d(TAG,"Llego al dialogforphoto");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageOptionCameraEvent event) {
        String[] stringArray = getResources().getStringArray(R.array.photo_options);
        if(event.getMessage().contains(stringArray[0])){
            if(checkPermissionCamera()) {
                doPhotoWithCamera();
            }

        }
        else if(event.getMessage().contains(stringArray[1])){
            chooseGalleryImage();
        }
    }


}
