package upsa.mimo.es.mountsyourcostume.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.helpers.VolleyErrorHelper;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestGetCostumes;

public class SearchCostumeFragment extends Fragment {

    private final String TAG = "SEARCH_COSTUME";

    //private SQLiteDatabase db;
    @BindView(R.id.recycler_view_search)
    RecyclerView recyclerView;

    @BindView(R.id.search_container)
    ViewGroup container;
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
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void setupRecyclerView(){

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void requestGetCostumes(String category){
        if(category!=null){

        }

        MyApplication.getCloudPersistance().getCostumes(new RequestGetCostumes.OnResponseGetCostumes() {
            @Override
            public void onResponseGetCostumes(JSONArray response) {
                Log.d(TAG, "Response de SaveCostume: " + response.toString());
                MyApplication.hideProgressDialog();
            }

            @Override
            public void onErrorResposeGetCostumes(VolleyError error) {
                Log.d(TAG, "Error de SaveCostume: " + error.toString());// + " Message: " + error.networkResponse.data.toString());
                String message = VolleyErrorHelper.getMessage(error,getActivity());
                MyApplication.showMessageInSnackBar(container, message);
                MyApplication.hideProgressDialog();

            }
        });
    }
}
