package upsa.mimo.es.mountsyourcostume.helpers;

import android.content.Context;

import java.util.ArrayList;

import upsa.mimo.es.mountsyourcostume.helpers.request.RequestCreateUser;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestGetCostumes;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestSaveCostume;
import upsa.mimo.es.mountsyourcostume.interfaces.CloudPersistance;
import upsa.mimo.es.mountsyourcostume.model.CloudSingleton;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.User;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public class CloudDBHelper implements CloudPersistance {

    private CloudSingleton cloudSingleton;
    private Context context;
    public static CloudDBHelper instance;


    private CloudDBHelper(Context context){
        this.cloudSingleton = CloudSingleton.getInstance(context);
        this.context = context;
    }
    public static CloudDBHelper newInstance(Context context){
        if(instance==null){
            instance = new CloudDBHelper(context);
        }
        return instance;
    }


    @Override
    public void getUser() {

    }

    @Override
    public void createUser(User user, RequestCreateUser.OnResponseCreateUser onResponseCreateUser) {

    }

    @Override
    public void getCostumes(String category, RequestGetCostumes.OnResponseGetCostumes onResponseGetCostumes) {


    }

    @Override
    public void saveCostume(Costume costume, RequestSaveCostume.OnResponseSaveCostume onResponseSaveCostume) {

        RequestSaveCostume requestSaveCostume = new RequestSaveCostume(onResponseSaveCostume,cloudSingleton);
        requestSaveCostume.requestSaveCostume(context,costume);

    }

    @Override
    public void deleteCostume(String name) {

    }
}
