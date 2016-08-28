package upsa.mimo.es.mountsyourcostume.interfaces;

import java.util.ArrayList;

import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by User on 18/07/2016.
 */
public interface LocalPersistance {
    public ArrayList<Costume> getCostumes();
    public long saveCostume(Costume costume);
    public int deleteCostume(String name);
}


