package upsa.mimo.es.mountsyourcostume.helpers;

import android.content.Context;

import upsa.mimo.es.mountsyourcostume.helpers.request.RequestCreateUser;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestDeleteCostume;
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

   // public final static String URL = "http://bdcostumes.herokuapp.com";
    public final static String URL = "http://52.29.230.208:9000";


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
    public void createUser(User user, RequestCreateUser.OnResponseCreateUser onResponseCreateUser) {

        RequestCreateUser requestCreateUser = new RequestCreateUser(cloudSingleton,onResponseCreateUser,context);
        requestCreateUser.requestCreateUser(user);
    }

    @Override
    public void getCostumes(String category, RequestGetCostumes.OnResponseGetCostumes onResponseGetCostumes) {

        RequestGetCostumes requestGetCostumes= new RequestGetCostumes(cloudSingleton,onResponseGetCostumes,context);
        requestGetCostumes.requestGetCostumes(category);

    }

    @Override
    public void getCostumes(RequestGetCostumes.OnResponseGetCostumes onResponseGetCostumes) {
        RequestGetCostumes requestGetCostumes= new RequestGetCostumes(cloudSingleton,onResponseGetCostumes,context);
        requestGetCostumes.requestGetCostumes();

    }


    @Override
    public void saveCostume(Costume costume, RequestSaveCostume.OnResponseSaveCostume onResponseSaveCostume) {

        RequestSaveCostume requestSaveCostume = new RequestSaveCostume(cloudSingleton,onResponseSaveCostume,context);
        requestSaveCostume.requestSaveCostume(costume);

    }

    @Override
    public void deleteCostume(String name, RequestDeleteCostume.OnResponseDeleteCostume onResponseDeleteCostume) {
        RequestDeleteCostume requestDeleteCostume = new RequestDeleteCostume(cloudSingleton,onResponseDeleteCostume,context);
        requestDeleteCostume.requestDeleteCostume(name);
    }
}
