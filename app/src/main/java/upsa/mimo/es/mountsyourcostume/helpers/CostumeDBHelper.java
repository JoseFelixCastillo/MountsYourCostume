package upsa.mimo.es.mountsyourcostume.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import upsa.mimo.es.mountsyourcostume.interfaces.LocalPersistance;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.CostumeSQLiteOpenHelper;

/**
 * Created by JoseFelix on 16/05/2016.
 */
public class CostumeDBHelper implements LocalPersistance{

    private static final String TAG = "COSTUME_DBHELPER";

    private SQLiteDatabase db;
    private static CostumeDBHelper instance;

    private CostumeDBHelper(Context context){
        CostumeSQLiteOpenHelper sqLiteOpenHelper = CostumeSQLiteOpenHelper.getInstance(context,CostumeSQLiteOpenHelper.DATABASE_NAME,
                null,CostumeSQLiteOpenHelper.DATABASE_VERSION);

        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public static CostumeDBHelper newInstance(Context context){
        if(instance==null){
            instance = new CostumeDBHelper(context);
        }
        return instance;
    }

    public static void updateCostume(){

    }

    @Override
    public ArrayList<Costume> getCostumes() {
        String[] campos = new String[]{CostumeSQLiteOpenHelper.KEY_NAME, CostumeSQLiteOpenHelper.KEY_CATEGORY,
                CostumeSQLiteOpenHelper.KEY_MATERIALS,CostumeSQLiteOpenHelper.KEY_STEPS,CostumeSQLiteOpenHelper.KEY_PRIZE,
                CostumeSQLiteOpenHelper.KEY_URI_IMAGE};
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

    @Override
    public long saveCostume(Costume costume) {
        ContentValues newCostume = new ContentValues();
        newCostume.put(CostumeSQLiteOpenHelper.KEY_NAME, costume.getName());
        newCostume.put(CostumeSQLiteOpenHelper.KEY_CATEGORY, costume.getCategory());
        newCostume.put(CostumeSQLiteOpenHelper.KEY_MATERIALS, costume.getMaterials());
        newCostume.put(CostumeSQLiteOpenHelper.KEY_STEPS, costume.getSteps());
        newCostume.put(CostumeSQLiteOpenHelper.KEY_PRIZE, costume.getPrize());
        newCostume.put(CostumeSQLiteOpenHelper.KEY_URI_IMAGE, costume.getUri_image());

        long rows = db.insert(CostumeSQLiteOpenHelper.DATABASE_TABLE, null, newCostume);
        Log.d(TAG,"rows insertadas: " + rows);
        return rows;

    }

    @Override
    public int deleteCostume(String name) {

        int rows = db.delete(CostumeSQLiteOpenHelper.DATABASE_TABLE, CostumeSQLiteOpenHelper.KEY_NAME + " = ?", new String[] { name });
        String string = new String(CostumeSQLiteOpenHelper.DATABASE_TABLE +  CostumeSQLiteOpenHelper.KEY_NAME + " = ?" + new String[] { name });
        Log.d(TAG,"string eliminadas:  " + string);
        return rows;
    }
}
