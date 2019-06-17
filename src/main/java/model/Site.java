package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name="site")
public class Site {

    @XmlElement(name = "open_auctions")
    ArrayList<OpenAuctions> openAuctions;

    public ArrayList<OpenAuctions> getOpenAuctions() {
        return openAuctions;
    }

    public Site() {
    }
    public Site(ArrayList<OpenAuctions> openAuctions) {
        this.openAuctions = openAuctions;
    }
    @Override
    public String toString() {
        return "model.Site{" +
                "openAuctions=" + openAuctions +
                '}';
    }
}
