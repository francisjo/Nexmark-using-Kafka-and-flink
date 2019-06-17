package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "open_auctions")
public class OpenAuctions {
    @XmlElement(name = "open_auction")
    ArrayList<OpenAuction> openauction;

    public ArrayList<OpenAuction> getOpenauction() {
        return openauction;
    }

    public OpenAuctions() {
    }
    public OpenAuctions(ArrayList<OpenAuction> openauction) {
        this.openauction = openauction;

    }
    @Override
    public String toString() {
        return "model.OpenAuctions{" +
                "openauction=" + openauction +
                '}';
    }
}
