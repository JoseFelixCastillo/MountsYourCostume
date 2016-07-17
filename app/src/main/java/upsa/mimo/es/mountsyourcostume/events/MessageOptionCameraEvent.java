package upsa.mimo.es.mountsyourcostume.events;

/**
 * Created by JoseFelix on 17/07/2016.
 */
public class MessageOptionCameraEvent {

    private final String message;

    public MessageOptionCameraEvent(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }


}
