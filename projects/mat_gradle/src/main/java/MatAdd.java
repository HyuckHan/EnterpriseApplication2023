import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public final class MatAdd {

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: JavaMatrixAdd <file>");
			System.exit(1);
		}

		SparkSession spark = SparkSession
			.builder()
			.appName("JavaMatrixAdd")
			.getOrCreate();

		JavaRDD<String> mat1 = spark.read().textFile(args[0]).javaRDD();
		JavaRDD<String> mat2 = spark.read().textFile(args[1]).javaRDD();

		JavaPairRDD<String, Integer> m1elements = mat1.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) {
				String[] arr = s.split(" ");	
				return new Tuple2(arr[0] + " " + arr[1], Integer.parseInt(arr[2]));
			}
		});

		JavaPairRDD<String, Integer> m2elements = mat2.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) {
				String[] arr = s.split(" ");	
				return new Tuple2(arr[0] + " " + arr[1], Integer.parseInt(arr[2]));
			}
		});


		JavaPairRDD<String, Integer> elements = m1elements.union(m2elements);

		JavaPairRDD<String, Integer> rst = elements.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer x, Integer y) {
				return x + y;
			}
		});

		rst.saveAsTextFile(args[args.length - 1]);
		spark.stop();
	}
}
