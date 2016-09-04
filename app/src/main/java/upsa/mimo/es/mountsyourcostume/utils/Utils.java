package upsa.mimo.es.mountsyourcostume.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JoseFelix on 29/08/2016.
 */
public class Utils {

    public static File createFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File directory =  context.getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory,timeStamp);

        return file;
    }

    public static File persistImageFromBitmap(File fileOut, Bitmap bitmap) throws Exception{
        OutputStream outputStream;

       // Bitmap image = ((BitmapDrawable) imageViewPhoto.getDrawable()).getBitmap();
        outputStream = new FileOutputStream(fileOut);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        return fileOut;
    }
}
