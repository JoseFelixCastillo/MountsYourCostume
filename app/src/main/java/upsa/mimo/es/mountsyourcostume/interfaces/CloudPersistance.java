package upsa.mimo.es.mountsyourcostume.interfaces;

import java.util.ArrayList;

import upsa.mimo.es.mountsyourcostume.helpers.request.RequestCreateUser;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestGetCostumes;
import upsa.mimo.es.mountsyourcostume.helpers.request.RequestSaveCostume;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.User;

/**
 * Created by JoseFelix on 28/08/2016.
 */
public interface CloudPersistance {
    public void getUser();
    public void createUser(User user, RequestCreateUser.OnResponseCreateUser onResponseCreateUser);
    public void getCostumes(String category, RequestGetCostumes.OnResponseGetCostumes onResponseGetCostumes);
    public void saveCostume(Costume costume, RequestSaveCostume.OnResponseSaveCostume onResponseSaveCostume);
    public void deleteCostume(String name);
}
