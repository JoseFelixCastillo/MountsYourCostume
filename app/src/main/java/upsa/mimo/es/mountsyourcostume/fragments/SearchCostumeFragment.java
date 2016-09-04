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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.activities.CostumeActivity;
import upsa.mimo.es.mountsyourcostume.adapters.CostumeAdapter;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.VolleyErrorHelper;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestGetCostumes;
import upsa.mimo.es.mountsyourcostume.model.Costume;

public class SearchCostumeFragment extends Fragment implements CostumeAdapter.OnItemClickListener {

    private final String TAG = "SEARCH_COSTUME";

    //private SQLiteDatabase db;
    @BindView(R.id.recycler_view_search)
    RecyclerView recyclerView;

    @BindView(R.id.search_container)
    ViewGroup container;
    @OnClick(R.id.button_search)
    void buttonSearchCostume(){
        searchCostume();
    }
    @BindView(R.id.edit_text_search)
    EditText editTextSearch;

    public SearchCostumeFragment() {
        // Required empty public constructor
    }


    public static SearchCostumeFragment newInstance() {
        SearchCostumeFragment fragment = new SearchCostumeFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_costume, container, false);

        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){

            setHasOptionsMenu(true);
            setupRecyclerView();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void setupRecyclerView(){

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void requestGetCostumes(String category){
        MyApplication.showProgressDialog(getActivity());
        MyApplication.getCloudPersistance().getCostumes(category, new RequestGetCostumes.OnResponseGetCostumes() {
            @Override
            public void onResponseGetCostumes(JSONArray response) {
                drawList(parseCostumes(response));
            }

            @Override
            public void onErrorResposeGetCostumes(VolleyError error) {

                String message = VolleyErrorHelper.getMessage(error,getActivity());
                MyApplication.showMessageInSnackBar(container, message);
                MyApplication.hideProgressDialog();
            }
        });
    }

    private void requestGetCostumes(){
        MyApplication.showProgressDialog(getActivity());
        MyApplication.getCloudPersistance().getCostumes(new RequestGetCostumes.OnResponseGetCostumes() {
            @Override
            public void onResponseGetCostumes(JSONArray response) {
                drawList(parseCostumes(response));

            }

            @Override
            public void onErrorResposeGetCostumes(VolleyError error) {
                String message = VolleyErrorHelper.getMessage(error,getActivity());
                MyApplication.showMessageInSnackBar(container, message);
                MyApplication.hideProgressDialog();

            }
        });
    }
    private ArrayList<Costume> parseCostumes(JSONArray response){
        ArrayList<Costume> costumes = new ArrayList<>();
        for(int i=0;i<response.length();i++){
            try {
                JSONObject json = response.getJSONObject(i);
                Costume costume = Costume.getFromJsonObject(json,getActivity());
                costumes.add(costume);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyApplication.hideProgressDialog();
        return costumes;
    }

    private void drawList(ArrayList<Costume> costumes){

        if(costumes.isEmpty()){

        }
        else{
            final CostumeAdapter costumeAdapter = new CostumeAdapter(costumes, this, getActivity());

            recyclerView.setAdapter(costumeAdapter);
        }
    }
    private void searchCostume(){
        String category = editTextSearch.getText().toString();
        if(!category.matches("")){
            requestGetCostumes(category);
        }
        else{
            requestGetCostumes();
        }
    }


    @Override
    public void onItemClick(View view, Costume costume) {
        Intent intent = new Intent(getContext(), CostumeActivity.class);
        // Costume costume = costumesFromSQLite.get(recyclerView.getChildAdapterPosition(v));
        intent.putExtra(CostumeActivity.EXTRA_ITEM, costume);
        intent.putExtra(CostumeActivity.EXTRA_PERSISTANCE,CostumeActivity.CLOUD);

        // View view = inflater.inflate(R.layout.activity_costume,container,false);
        ImageView image = (ImageView) view.findViewById(R.id.image_costume);


        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                view.findViewById(R.id.image_costume),
                "image_transition");

        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
