package upsa.mimo.es.mountsyourcostume.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.activities.CostumeActivity;
import upsa.mimo.es.mountsyourcostume.adapters.CostumeAdapter;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.model.Costume;


public class FavouriteCostumeFragment extends Fragment implements CostumeAdapter.OnItemClickListener{

    private final String TAG = "FAVOURITE_COSTUME";

   // private SQLiteDatabase db;
    @BindView(R.id.recycler_view_favourite)
    RecyclerView recyclerView;

  //  private LayoutInflater inflater;
    @BindView(R.id.favourite_container)
    ViewGroup container;



    public FavouriteCostumeFragment() {
        // Required empty public constructor
    }


    public static FavouriteCostumeFragment newInstance() {
        FavouriteCostumeFragment fragment = new FavouriteCostumeFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourite_costume, container, false);
        ButterKnife.bind(this,view);
        return view;
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

       // recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_favourite);
        recyclerView.setHasFixedSize(true);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
        else if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private ArrayList<Costume> getCostumesFromLocalPersistance(){

        ArrayList<Costume> costumes = MyApplication.getLocalPersistance().getCostumes();

        if(!costumes.isEmpty()) {
            Log.d(TAG, "costume: " + costumes.get(0).getName());
        }
        else{
            Log.d(TAG, "vacia");
        }
        return costumes;
    }

    private void drawList(){

        final List<Costume> costumesFromSQLite = getCostumesFromLocalPersistance();
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
