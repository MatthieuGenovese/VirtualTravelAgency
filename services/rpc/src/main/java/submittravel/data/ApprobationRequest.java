package submittravel.data;

import javax.xml.bind.annotation.XmlElement;

public class ApprobationRequest {
    private String messageTravel="";
    private boolean choix = true;



    @XmlElement(required = true)
    public String getMessageTravel() { return messageTravel; }
    public void setMessageTravel(String messageTravel) { this.messageTravel = messageTravel; }

    @XmlElement(required = true)
    public boolean getChoix() { return choix; }
    public void setChoix(boolean choix) { this.choix = choix; }
}
