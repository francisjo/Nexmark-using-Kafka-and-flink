import model.OpenAuction;
import model.OpenAuctions;
import model.Site;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Properties;

public class Producersimple {

    public static void main(String[] args) throws JAXBException {
        JAXBContext context;
        context = JAXBContext.newInstance(Site.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object unmarshallered = unmarshaller.unmarshal(new File("Bid.xml"));
        Site site = (Site) unmarshallered;
        for (OpenAuctions s : site.getOpenAuctions()) {
            if (s.getOpenauction() != null) {
                for (OpenAuction os : s.getOpenauction()) {
                    if (os.getListbids() != null) {
                        String bid = null;

                        bid =os.getAuction()+","+os.getListbids().getBid()+"," + os.getListbids().getDateTime();
                        System.out.println(bid);
                        //Bid bid = new Bid(os.getAuction(), os.getListbids().getBid(), os.getListbids().getDateTime());
                       creatproducer(bid);
                    }
                }
            }
        }
    }
    private static void creatproducer(String bid)
    {

        String topicName = "BidProducerTopic";
        String key = "Key1";
       // String value = "Value-1";
        //read file
//        File file = new File("bid.xml");
//        FileInputStream fstream = new FileInputStream(file);
//        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//        String msg = null;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,bid);
        producer.send(record);
        producer.close();
        System.out.println("SimpleProducer Completed.");

    }

}

//

//        Properties properties = new Properties();
//        properties.put(JAXBSerializerConfig.JAXB_FORMATTED_OUTPUT_CONF, "true");
//        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.toString());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JAXBSerializer.class.toString());
//        Producer<Integer, Site> producer = new KafkaProducer<>(properties);


/****
 for (model.OpenAuctions s : site.getOpenAuctions())
 {
 if(!s.openauction.isEmpty())
 {
 for(model.OpenAuction os : s.getOpenauction())
 {
 if(os.bidder != null)
 {
 model.Bid bid = new model.Bid(os.getAuction(),os.getBidder().getBid(),os.getBidder().getDateTime());
 System.out.println(bid);
 }
 }
 }

 }
 */


/***
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 factory.setNamespaceAware(true);
 DocumentBuilder builder;
 Document doc = null;
 try {
 builder = factory.newDocumentBuilder();
 doc = builder.parse("file.xml");
 XPathFactory xpathFactory = XPathFactory.newInstance();
 XPath xpath = xpathFactory.newXPath();
 Duke duke = getEmployeeNameById(doc, xpath);
 }
 */
        /**
         // set up streaming execution environment
         StreamExecutionEnvironment env =
         StreamExecutionEnvironment.getExecutionEnvironment();
         env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
         // configure the Kafka consumer
         Properties kafkaProps = new Properties();
         kafkaProps.setProperty("zookeeper.connect", LOCAL_ZOOKEEPER_HOST);
         kafkaProps.setProperty("bootstrap.servers", LOCAL_KAFKA_BROKER);
         kafkaProps.setProperty("group.id", BID_GROUP);

         //always read the Kafka topic from the start
         kafkaProps.setProperty("auto.offset.reset", "earliest");
         FlinkKafkaProducer<IN> producer = new FlinkKafkaProducer<IN>(kafkaProps);
         producer.
         // create a Kafka consumer
         FlinkKafkaConsumer011<TaxiRide> consumer = new FlinkKafkaConsumer<>(
         "cleansedRides",
         new TaxiRideSchema(),
         kafkaProps);
         // assign a timestamp extractor to the consumer
         //consumer.assignTimestampsAndWatermarks(new TaxiRideTSExtractor());

         // create a TaxiRide data stream
         DataStream<TaxiRide> rides = env.addSource(consumer);
         */

/**
 private static Duke getEmployeeNameById(Document doc, XPath xpath)
 {
 Duke duke = null;
 try {
 XPathExpression expr = xpath.compile("/duke/age/");
 }
 }
 */