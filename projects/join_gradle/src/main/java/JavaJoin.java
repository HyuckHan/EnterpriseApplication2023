import scala.Tuple2;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaJoin {
	static class Product{
		String id;
		String price;
		String code;

		public String toString() {
			return "[" + id + "," + price + "," + code + "]";
		}
	}
	static class Code{
		String code;
		String desc;

		public String toString() {
			return "[" + code + "," + desc + "]";
		}
	}

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.err.println("Usage: JavaWordCount <in-file> <out-file>");
			System.exit(1);
		}

		SparkSession spark = SparkSession
			.builder()
			.appName("JavaJoin")
			.getOrCreate();

		JavaRDD<String> products = spark.read().textFile(args[0]).javaRDD();
		PairFunction<String, String, Product> pfA = new PairFunction<String, String, Product>() {
			public Tuple2<String,Product> call(String s) {
				StringTokenizer st = new StringTokenizer(s, "|");
				Product prod = new Product();
				prod.id = st.nextToken();
				prod.price = st.nextToken();
				prod.code = st.nextToken();
				return new Tuple2(prod.code, prod);
			}
		};
		JavaPairRDD<String,Product> pTuples = products.mapToPair(pfA);


		JavaRDD<String> codes = spark.read().textFile(args[1]).javaRDD();
		PairFunction<String, String, Code> pfB = new PairFunction<String, String, Code>() {
			public Tuple2<String,Code> call(String s) {
				StringTokenizer st = new StringTokenizer(s, "|");
				Code code = new Code();
				code.code = st.nextToken();
				code.desc = st.nextToken();
				return new Tuple2(code.code, code);
			}
		};
		JavaPairRDD<String,Code> cTuples = codes.mapToPair(pfB);

		JavaPairRDD<String,Tuple2<Product,Code>> joined = pTuples.join(cTuples);
		

		joined.saveAsTextFile("/tmp/joined");
		//codes.saveAsTextFile("/tmp/relation_b");

		spark.stop();
	}
}
