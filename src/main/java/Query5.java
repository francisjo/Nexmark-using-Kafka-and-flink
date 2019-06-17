import model.Bid;
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

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

public class Query5 {

    static long  backup1 ;
    static final String LOCAL_ZOOKEEPER_HOST = "localhost:2181";
    static final String LOCAL_KAFKA_BROKER = "localhost:9092";
    static final String BID_GROUP = "Bidgroup";
    static String topicName = "BidProducerTopic";
    public static void main(String[] args) throws Exception {
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
        consumer.assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<Bid>() {

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
       });
                DataStream < Bid > stream = env.addSource(consumer);
                DataStream<Tuple3<Long,Integer,Integer>> hourlybids = stream.keyBy((Bid bid) -> bid.getItemid())
                .timeWindow(Time.minutes(60))
                .process(new ProcessWindowFunction<Bid, Tuple3<Long,Integer,Integer>, Integer, TimeWindow>() {
                    @Override
                    public void process(Integer integer, Context context, Iterable<Bid> iterable, Collector<Tuple3<Long,Integer,Integer>> collector) throws Exception {
                        Integer count = 0;
                        for (Bid in : iterable) {
                                     count++;
                                 }
                                 collector.collect(new Tuple3(context.window().getEnd(), integer, count));

                    }
                });
          DataStream<Tuple3<Long,Integer,Integer>> hourlyMax = hourlybids
            .timeWindowAll(Time.minutes(60))
              .maxBy(2);
        hourlyMax.print();



        env.execute();



    }


}
