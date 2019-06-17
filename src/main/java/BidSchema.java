import model.Bid;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class BidSchema implements DeserializationSchema<Bid>, SerializationSchema<Bid> {
    @Override
    public byte[] serialize(Bid element) {
        return element.toString().getBytes();
    }

    @Override
    public Bid deserialize(byte[] message) {
        return Bid.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(Bid nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Bid> getProducedType() {
        return TypeExtractor.getForClass(Bid.class);
    }

}
