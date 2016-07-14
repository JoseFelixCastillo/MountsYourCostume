package upsa.mimo.es.mountsyourcostume.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upsa.mimo.es.mountsyourcostume.R;

public class SearchCostume extends Fragment {

    private final String TAG = "SEARCH_COSTUME";

    //private SQLiteDatabase db;
    private RecyclerView recyclerView;

    public SearchCostume() {
        // Required empty public constructor
    }


    public static SearchCostume newInstance() {
        SearchCostume fragment = new SearchCostume();
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
        return inflater.inflate(R.layout.fragment_search_costume, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            loadUI();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void loadUI(){
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_search);
    }

    private void setupRecyclerView(){

    }
}
