import model.Bid;
import model.OpenAuction;
import model.OpenAuctions;
import model.Site;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import org.apache.flink.util.Collector;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class TestQuery5 {
    private JAXBContext context;
    static long  backup1 ;
    String bid = null;
    @Before
    public void init() throws JAXBException {
        System.out.println("SimpleProducer :");
        this.context = JAXBContext.newInstance(Site.class);
        Unmarshaller unmarshaller = this.context.createUnmarshaller();
        Object unmarshallered = unmarshaller.unmarshal(new File("Bid.xml"));
        Site site = (Site) unmarshallered;
        for (OpenAuctions s : site.getOpenAuctions()) {
            if (s.getOpenauction() != null) {
                for (OpenAuction os : s.getOpenauction()) {
                    if (os.getListbids() != null) {
                        bid = os.getAuction() + "," + os.getListbids().getBid() + "," + os.getListbids().getDateTime();
                        System.out.println(bid);
                        ProducerBid.createproducer(bid);
                    }
                }
            }
        }

    }
    @Test
    public void Query5() throws Exception {


                final String LOCAL_ZOOKEEPER_HOST = "localhost:2181";
                final String LOCAL_KAFKA_BROKER = "localhost:9092";
                final String BID_GROUP = "Bidgroup";
                String topicName = "BidProducerTopic";
                ZoneId zoneId = ZoneId.systemDefault();
                backup1 = LocalDateTime.now().atZone(zoneId).toEpochSecond();
                Properties kafkaProps = new Properties();
                kafkaProps.setProperty("zookeeper.connect", LOCAL_ZOOKEEPER_HOST);
                kafkaProps.setProperty("bootstrap.servers", LOCAL_KAFKA_BROKER);
                kafkaProps.setProperty("group.id", BID_GROUP);
                //always read the Kafka topic from the start
                kafkaProps.setProperty("auto.offset.reset", "earliest");
                // create a Kafka consumer
                StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
                FlinkKafkaConsumer011<Bid> consumer = new FlinkKafkaConsumer011<>(topicName, new BidSchema(), kafkaProps);
                consumer.assignTimestampsAndWatermarks(new MyAssignerWithPunctuatedWatermarks());
                DataStream<Bid> stream = env.addSource(consumer);
                DataStream<Tuple3<Long, Integer, Integer>> hourlybids = stream.keyBy((Bid bid) -> bid.getItemid())
                        .timeWindow(Time.minutes(60))
                        .process(new MyProcessWindowFunction());
                DataStream<Tuple3<Long, Integer, Integer>> hourlyMax = hourlybids
                        .timeWindowAll(Time.minutes(60))
                        .maxBy(2);
                hourlyMax.print();
                try {
                    env.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }





    }
    private  class MyAssignerWithPunctuatedWatermarks implements AssignerWithPunctuatedWatermarks<Bid> {
        @Nullable
        @Override
        public Watermark checkAndGetNextWatermark(Bid bid, long l) {
            return new Watermark(l - 1500);
        }

        @Override
        public long extractTimestamp(Bid bid, long l) {

            backup1 = backup1 + 1000 ;
            // System.out.println(ldt.atZone(zoneId).toEpochSecond()*1000);
            return (backup1)* 1000 ;
        }
    }
    private class MyProcessWindowFunction extends ProcessWindowFunction<Bid, Tuple3<Long, Integer, Integer>, Integer, TimeWindow> {

        @Override
        public void process(Integer integer, Context context, Iterable<Bid> iterable, Collector<Tuple3<Long, Integer, Integer>> collector) throws Exception {
            Integer count = 0;
            for (Bid in : iterable) {
                count++;
            }
            collector.collect(new Tuple3(context.window().getEnd(), integer, count));
        }
    }

}




