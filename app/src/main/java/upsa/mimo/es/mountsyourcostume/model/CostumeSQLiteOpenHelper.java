package upsa.mimo.es.mountsyourcostume.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JoseFelix on 16/05/2016.
 */
public class CostumeSQLiteOpenHelper extends SQLiteOpenHelper {

    private static CostumeSQLiteOpenHelper instance;

    public static final String DATABASE_NAME = "costumeDataBase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "costumeTable";

    private static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_MATERIALS = "materials";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_PRIZE = "prize";
    public static final String KEY_URI_IMAGE = "uri_image";

    //SQL statement for create table costume  //Comprobar que sea con mayusculas
    String costumeSqlCreate = "CREATE TABLE " + DATABASE_TABLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT unique, "
            + KEY_CATEGORY + " TEXT, "
            + KEY_MATERIALS + " TEXT, "
            + KEY_STEPS + " TEXT, "
            + KEY_PRIZE + " INTEGER, "
            + KEY_URI_IMAGE + " TEXT)";


    public static synchronized CostumeSQLiteOpenHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        if(instance==null){
            instance = new CostumeSQLiteOpenHelper(context, name, factory, version);
        }
        return instance;
    }


    private CostumeSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
   // public CostumeSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
   //     super(context, name, factory, version);
  //  }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(costumeSqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Eliminamos la versión anterior de la tabla para simplificar este
        // ejemplo.
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // Creamos la nueva versión de la tabla   //Ver si esta bien
        db.execSQL(costumeSqlCreate);
    }
}
