package model;

public class Bid {

    Integer itemid;
    Double bid;
    long dateTime;

    public Bid(Integer itemid, Double bid, Integer dateTime) {
        this.itemid = itemid;
        this.bid = bid;
        this.dateTime = dateTime;
    }

    public static Bid fromString(String line) {
        String[] tokens = line.split(",");
        if (tokens.length != 3) {
            throw new RuntimeException("Invalid record: " + line);
        }

        Bid bid = new Bid();

        try {
            bid.itemid = Integer.parseInt(tokens[0]);
            bid.bid = Double.parseDouble(tokens[1]);
            bid.dateTime = Long.parseLong(tokens[2]);

        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return bid;
    }

    @Override
    public String toString() {
        return itemid +","+ bid +","+ dateTime;
    }

    public Bid() {
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }


}

