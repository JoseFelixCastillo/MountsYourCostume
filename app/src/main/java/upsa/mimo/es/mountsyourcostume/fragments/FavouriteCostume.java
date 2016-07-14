package upsa.mimo.es.mountsyourcostume.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.activities.CostumeActivity;
import upsa.mimo.es.mountsyourcostume.adapters.CostumeAdapter;
import upsa.mimo.es.mountsyourcostume.helpers.CostumeDBHelper;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.CostumeSQLiteOpenHelper;


public class FavouriteCostume extends Fragment implements CostumeAdapter.OnItemClickListener{

    private final String TAG = "FAVOURITE_COSTUME";

    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private ViewGroup container;



    public FavouriteCostume() {
        // Required empty public constructor
    }


    public static FavouriteCostume newInstance() {
        FavouriteCostume fragment = new FavouriteCostume();
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
        this.inflater = inflater;
        this.container = container;
        return inflater.inflate(R.layout.fragment_favourite_costume, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
           // loadUI();
            setupRecyclerView();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        drawList();

    }

    private void loadUI(){
       // recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_favourite);
    }

    private void setupRecyclerView(){

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_favourite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private ArrayList<Costume> getCostumesFromSQLite(){
        CostumeSQLiteOpenHelper costumeDB = CostumeSQLiteOpenHelper.getInstance(getActivity(), CostumeSQLiteOpenHelper.DATABASE_NAME, null, CostumeSQLiteOpenHelper.DATABASE_VERSION);

        db = costumeDB.getReadableDatabase();  //Luego si eso cambiamos el readable por el write

        ArrayList<Costume> costumes = CostumeDBHelper.getCostumes(db);
        if(!costumes.isEmpty()) {
            Log.d(TAG, "costume: " + costumes.get(0).getName());
        }
        else{
            Log.d(TAG, "vacia");
        }
        return costumes;
    }

    private void drawList(){

        final List<Costume> costumesFromSQLite = getCostumesFromSQLite();
        if(!costumesFromSQLite.isEmpty()) {
            final CostumeAdapter costumeAdapter = new CostumeAdapter(costumesFromSQLite, this, getActivity());
            Log.d(TAG, "pasando a drawlist");
            recyclerView.setAdapter(costumeAdapter);
        }
    }

    @Override
    public void onItemClick(View view, Costume costume) {
        Intent intent = new Intent(getContext(), CostumeActivity.class);
       // Costume costume = costumesFromSQLite.get(recyclerView.getChildAdapterPosition(v));
        intent.putExtra(CostumeActivity.EXTRA_ITEM, costume);

       // View view = inflater.inflate(R.layout.activity_costume,container,false);
        ImageView image = (ImageView) view.findViewById(R.id.image_costume);
        if(image!=null){
            Log.d(TAG,"imagen no nula en onclick");
        }
        else{
            Log.d(TAG,"image NULA en onclicck");
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                view.findViewById(R.id.image_costume),
                "image_transition");

        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
