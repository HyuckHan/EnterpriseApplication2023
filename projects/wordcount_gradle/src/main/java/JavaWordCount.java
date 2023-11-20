import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaWordCount {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: JavaWordCount <in-file> <out-file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession
            .builder()
            .appName("JavaWordCount")
            .getOrCreate();

        JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

        FlatMapFunction<String, String> fmf = new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) {
                return Arrays.asList(s.split(" ")).iterator();
            }
        };
        JavaRDD<String> words = lines.flatMap(fmf);

        PairFunction<String, String, Integer> pf = new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2(s, 1);
            }
        };
        JavaPairRDD<String, Integer> ones = words.mapToPair(pf);

        Function2<Integer, Integer, Integer> f2 = new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer x, Integer y) {
                return x + y;
            }
        };
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(f2);

        counts.saveAsTextFile(args[1]);
        spark.stop();
    }
}
