package model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bidder")
public class Bidder {
 @XmlElement(name= "bid")
   Double bid;
   @XmlElement(name ="time")
   String dateTime;

    public Bidder() {
    }

    public Bidder(Double bid, String dateTime) {
        this.bid = bid;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "model.Bidder{" +
                "bid=" + bid +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }

    public Double getBid() {
        return bid;
    }

    public String getDateTime() {
        return dateTime;
    }
}
