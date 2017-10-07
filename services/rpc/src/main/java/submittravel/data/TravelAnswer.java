package submittravel.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TravelAnswer {
    private String id_employe;
    private String answer ="";


    @XmlElement
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    @XmlElement
    public String getIdentifier() { return id_employe; }
    public void setIdentifier(String identifier) { this.id_employe = identifier; }
}
