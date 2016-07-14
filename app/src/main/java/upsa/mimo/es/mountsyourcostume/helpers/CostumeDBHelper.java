package upsa.mimo.es.mountsyourcostume.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.CostumeSQLiteOpenHelper;

/**
 * Created by JoseFelix on 16/05/2016.
 */
public class CostumeDBHelper {

    private static final String TAG = "COSTUME_DBHELPER";

    public static long insertCostume(SQLiteDatabase db, String name, String category, String materials, String steps, int prize, String uri_image){

        ContentValues newCostume = new ContentValues();
        newCostume.put(CostumeSQLiteOpenHelper.KEY_NAME, name);
        newCostume.put(CostumeSQLiteOpenHelper.KEY_CATEGORY, category);
        newCostume.put(CostumeSQLiteOpenHelper.KEY_MATERIALS, materials);
        newCostume.put(CostumeSQLiteOpenHelper.KEY_STEPS, steps);
        newCostume.put(CostumeSQLiteOpenHelper.KEY_PRIZE, prize);
        newCostume.put(CostumeSQLiteOpenHelper.KEY_URI_IMAGE, uri_image);

        long rows = db.insert(CostumeSQLiteOpenHelper.DATABASE_TABLE, null, newCostume);
        Log.d(TAG,"rows insertadas: " + rows);
        return rows;
    }

    public static void updateCostume(){

    }
    public static int deleteCostumeByName(SQLiteDatabase db, String name){

      //  String where =  CostumeSQLiteOpenHelper.KEY_NAME  + " = ?" ;
      //  String[] whereArgs = new String[] { name };
      //  int rows = db.delete(CostumeSQLiteOpenHelper.DATABASE_NAME, where, whereArgs);
        int rows = db.delete(CostumeSQLiteOpenHelper.DATABASE_TABLE, CostumeSQLiteOpenHelper.KEY_NAME + " = ?", new String[] { name });
        String string = new String(CostumeSQLiteOpenHelper.DATABASE_TABLE +  CostumeSQLiteOpenHelper.KEY_NAME + " = ?" + new String[] { name });
        Log.d(TAG,"string eliminadas:  " + string);
        return rows;

    }

    public static ArrayList<Costume> getCostumes(SQLiteDatabase db){


        String[] campos = new String[]{CostumeSQLiteOpenHelper.KEY_NAME, CostumeSQLiteOpenHelper.KEY_CATEGORY, CostumeSQLiteOpenHelper.KEY_MATERIALS,
                CostumeSQLiteOpenHelper.KEY_STEPS,CostumeSQLiteOpenHelper.KEY_PRIZE,CostumeSQLiteOpenHelper.KEY_URI_IMAGE};
        Cursor c = db.query(CostumeSQLiteOpenHelper.DATABASE_TABLE, campos, null, null, null, null,
                null);

        // Recorremos los resultados para mostrarlos en pantalla
        //tvResultado.setText("");
        ArrayList<Costume> costumes = new ArrayList<>();
        if (c.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                String name = c.getString(0);
                String category = c.getString(1);
                String materials = c.getString(2);
                String steps = c.getString(3);
                int prize = c.getInt(4);
                String uri_image = c.getString(5);

                Costume costume = new Costume(name,category,materials,steps,prize,uri_image);
                costumes.add(costume);
                Log.d("Conseguimos: name: " + name, "category: " + category + "materials: " + materials + "steps: " + steps + "prize: " + prize + "uri_image: " + uri_image + "\n");
            } while (c.moveToNext());
        }
        c.close();

        return costumes;
    }
}
