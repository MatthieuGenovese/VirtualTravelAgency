package submittravel.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TravelAnswer {
    private String id_employe;
    private String answer ="";
    /*private String submitcar ="";
    private String submithostel ="";
    private String submitplane ="";*/



    /*@XmlElement
    public String getsubmitcar() { return submitcar; }
    public void setsubmitcar(String submitcar) { this.submitcar = submitcar; }

    @XmlElement
    public String getsubmithostel() { return submithostel; }
    public void setsubmithostel(String submithostel) { this.submithostel = submithostel; }

    @XmlElement
    public String getsubmitplane() { return submitplane; }
    public void setsubmitplane(String submitplane) { this.answer = submitplane; }*/

    @XmlElement
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    @XmlElement
    public String getIdentifier() { return id_employe; }
    public void setIdentifier(String identifier) { this.id_employe = identifier; }
}
