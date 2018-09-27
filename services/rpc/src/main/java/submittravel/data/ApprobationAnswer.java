package submittravel.data;

import javax.xml.bind.annotation.XmlElement;

public class ApprobationAnswer {
    private String resultmessage ="";

    @XmlElement
    public String getResultmessage() { return resultmessage; }
    public void setResultmessage(String resultmessage) { this.resultmessage = resultmessage; }
}
