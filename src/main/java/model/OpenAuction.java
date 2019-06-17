package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "open_auction")
public class OpenAuction
{
    public Bidder getListbids() {
        return listbids;
    }

    @XmlElement(name = "bidder")
   Bidder listbids;

    public long getAuction() {
        return auction;
    }

    @XmlAttribute(name = "id")
    long auction;

    public OpenAuction() {
    }
    public OpenAuction(Bidder listbids, long auction) {
        this.listbids = listbids;
        this.auction = auction;
    }
    @Override
    public String toString() {
        return "model.OpenAuction{" +
                ", bidder=" + listbids +
                ", auction=" + auction +
                '}';
    }


}
