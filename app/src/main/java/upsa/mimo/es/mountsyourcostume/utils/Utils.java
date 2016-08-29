package upsa.mimo.es.mountsyourcostume.utils;

import android.content.Context;

import java.io.File;
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
}
