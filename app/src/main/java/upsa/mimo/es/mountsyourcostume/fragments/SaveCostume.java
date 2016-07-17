package upsa.mimo.es.mountsyourcostume.fragments;


import android.Manifest;
import android.content.Context;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import upsa.mimo.es.mountsyourcostume.dialogs.DialogChooseOptionCamera;
import upsa.mimo.es.mountsyourcostume.events.MessageOptionCameraEvent;
import upsa.mimo.es.mountsyourcostume.helpers.CostumeDBHelper;
import upsa.mimo.es.mountsyourcostume.model.CostumeSQLiteOpenHelper;

public class SaveCostume extends Fragment {

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
    void FloatingSaveFavourites(){
        saveInFavourites();
    }

    @OnClick(R.id.floating_button_save_cloud)
    void FloatingSaveCloud(){
        saveInCloud();
    }

    @OnClick(R.id.image_view_costume)
    void ImageViewCostume(){
        createDialogForPhoto();
    }

   // private EditText editTextPrize;


    public SaveCostume() {
        // Required empty public constructor
    }


    public static SaveCostume newInstance() {
        SaveCostume fragment = new SaveCostume();
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
            if(savedInstanceState.getParcelable("photoBitmap")!=null){
                imageViewPhoto.setImageBitmap((Bitmap) savedInstanceState.getParcelable("photoBitmap"));
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
            outState.putParcelable("photoBitmap", photoBitmap);
        }

    }

 /*   private void loadUI(){

        editTextName = (EditText) getView().findViewById(R.id.edit_text_name);
        editTextMaterials = (EditText) getView().findViewById(R.id.edit_text_materials);
        editTextSteps = (EditText) getView().findViewById(R.id.edit_text_steps);
        editTextPrize = (EditText) getView().findViewById(R.id.edit_text_prize);

        container = (ViewGroup) getView().findViewById(R.id.save_costume_container);

        imageViewPhoto = (ImageView) getView().findViewById(R.id.image_view_costume);
        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }*/

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
/*    private boolean checkPermissionCameraAndStorage(){
        if(Build.VERSION.SDK_INT >=23) {
            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    && (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)) {
                Log.d(TAG,"permisos entro al primer true");
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CODE_PERMISSION_CAMERA);
                return false;
            }
        }
        else{
            return true;
            }
    }*/
    private boolean checkPermissionCamera(){
        if(Build.VERSION.SDK_INT >=23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"permisos entro al primer true");
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
            Log.d(TAG, "resultCode del captureimage    "+ resultCode);
            if (resultCode == getActivity().RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
               /* if(data!=null) {
                    Log.d(TAG, "Image saved to:\n" + data.getData());
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageViewPhoto.setImageBitmap(imageBitmap);
                }
                //La imagen esta en la memoria cache pero quiero tenerla en memoria normal asi que lo copio en un fichero normal
*/
                Bitmap imageBitmap = BitmapFactory.decodeFile(actuallyPhotoFile.getPath());
                Log.d(TAG, "URI camera: "+ actuallyPhotoFile.getPath());
                //imageViewPhoto.setImageBitmap(imageBitmap);
               // Picasso.with(getContext()).load(actuallyPhotoFile.getPath()).into(imageViewPhoto);
                setPicToImageView(imageBitmap);

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Log.d(TAG, "Cancelado en onActivityResult de capture image  ");
            } else {
                // Image capture failed, advise user
                Log.d(TAG, "Algo raro en onActivityResult de capture image");
            }
        }
        if(requestCode == PICK_IMAGE_REQUEST){
            Log.d(TAG, "entramos a pickimageRequest");
            if(resultCode == getActivity().RESULT_OK){

                if(data!=null){
                    Uri uri = data.getData();
                    Log.d(TAG, "URI gallery: "+ uri.getPath());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(bitmap!=null) {
                        setPicToImageView(bitmap);
                        //imageViewPhoto.setImageBitmap(bitmap);
                    }
                }
            }
            else if(resultCode == getActivity().RESULT_CANCELED){
                Log.d(TAG, "Cancelado en onActivityResult de Pick Image");
            }
            else{
                Log.d(TAG, "Algo raro en onActivityResult de Pick Image");
            }
        }

    }

  /*  private  Uri getOutputMediaFileUri() throws IOException {
      //  Log.d(TAG, "getOutPutMediFileUri    " + createFile().getAbsolutePath());
        return Uri.fromFile(createTempFile());
    }*/

/*    private File createFile() throws IOException {


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outPutDir = getActivity().getCacheDir();
        return File.createTempFile(timeStamp, ".jpg", outPutDir);
    }*/


   private File createFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File directory =  getActivity().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory,timeStamp);

        return file;
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



  /*  private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = imageViewPhoto.getWidth();
        int targetH = imageViewPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //Deprecated but in the android guide still in the example
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        //bitmap.set
        imageViewPhoto.setImageBitmap(bitmap);
    }*/

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


    }

    private void saveInFavourites(){
//Ver tambien categoria

        if(getFields()&&category!=null){
            File file = null;
            try {
                file = persistImageFromImageView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(file!=null) {
                //setFileToActuallyPhotoFile(file);

                CostumeSQLiteOpenHelper costumeDB = CostumeSQLiteOpenHelper.getInstance(getActivity(), CostumeSQLiteOpenHelper.DATABASE_NAME, null, CostumeSQLiteOpenHelper.DATABASE_VERSION);

                db = costumeDB.getWritableDatabase();
                long rows = CostumeDBHelper.insertCostume(db, name, category, materials, steps, prize, file.getPath());
                if (rows > 0) {
                    Log.d(TAG, "ha habido inserciones");
                    Snackbar snackbar = Snackbar.make(container,"Se guardo correctamente",Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Log.d(TAG, "error insertando posiblemente");
                }
            }
            else{
                Snackbar snackbar = Snackbar.make(container,"Se necesita una imagen",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        else{
            Snackbar snackbar = Snackbar.make(container,"Rellene todos los campos",Snackbar.LENGTH_LONG);
            snackbar.show();
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
        //prize = Integer.parseInt(editTextPrize.getText().toString());


        if((name.matches(""))||(materials.matches(""))||(steps.matches(""))||prizeString.matches("")){ //Precio verlo mejor y falta imagen
          //  showDialog("Rellene todos los campos");
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

    private void showDialog(String message){

    }

    //devuelve un archivo del imageView
    private File persistImageFromImageView() throws Exception{

        if(imageViewPhoto.getDrawable()!=null) {
            File file = createFile();
            OutputStream outputStream;

            Bitmap image = ((BitmapDrawable) imageViewPhoto.getDrawable()).getBitmap();
            outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            return file;
        }
        return null;
    }

   /* private String getRealUriFromUriGallery(Uri uri){
        String result;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        //ContentResolver.op
        return result;

    }*/

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
            doPhotoWithCamera();

        }
        else if(event.getMessage().contains(stringArray[1])){
            chooseGalleryImage();
        }
    }


}
