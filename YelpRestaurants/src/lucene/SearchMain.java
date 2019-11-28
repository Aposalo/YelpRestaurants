package lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import GUI.SearchYelp;
import jdbc.Driver;


public class SearchMain {
	
	protected static StandardAnalyzer analyzer = new StandardAnalyzer();
	
	protected static Directory indexReview;
	protected static Directory indexRestaurants;
	protected static Directory indexMinMax = new RAMDirectory();
	protected static Directory indexSort = new RAMDirectory();
	protected static Directory indexSubset = new RAMDirectory();
	protected static Directory indexFinal = new RAMDirectory();
	
	protected static IndexWriterConfig configReview = new IndexWriterConfig(analyzer);
	protected static IndexWriterConfig configRestaurants = new IndexWriterConfig(analyzer);
	protected static IndexWriterConfig configMinMax = new IndexWriterConfig(analyzer);
	protected static IndexWriterConfig configSort = new IndexWriterConfig(analyzer);
	protected static IndexWriterConfig configSubset = new IndexWriterConfig(analyzer);
	protected static IndexWriterConfig configFinal = new IndexWriterConfig(analyzer);
	protected static String[] a;
	
	public void loadLibrary() throws IOException, SQLException {
		DocumentAdd add = new DocumentAdd();
		ResultSet Review = null;
		ResultSet Restaurants = null;
		Driver driver = new Driver();
		IndexWriter w = new IndexWriter(indexReview, configReview);
		System.out.println("1/3");
		Review = driver.run("select review_id,business_id,date,text,useful,stars from restaurants_review limit 600000");
		System.out.println("loading...");
		while (Review.next()) {
			add.addDoc(w,Review.getString("text") ,Review.getString("business_id"),Review.getDate("date"),Review.getInt("useful"),Review.getInt("stars"));
		}
		driver.close();
		System.out.println("2/3");
		Review = driver.run("select review_id,business_id,date,text,useful,stars from restaurants_review limit 1300000 OFFSET 600000");
		System.out.println("loading...");
		while (Review.next()) {
			add.addDoc(w,Review.getString("text"),Review.getString("business_id"),Review.getDate("date"),Review.getInt("useful"),Review.getInt("stars"));
		}
		driver.close();
		w.close();
		System.out.println("3/3");
		w = new IndexWriter(indexRestaurants, configRestaurants);
		System.out.println("loading...");
		Restaurants = driver.run("select * from restaurants");
		while (Restaurants.next()) {
			add.addRestaurantDoc(w,Restaurants.getString("id"),Restaurants.getString("name"),Restaurants.getString("address"),Restaurants.getString("city")
					,Restaurants.getString("state"),Restaurants.getFloat("latitude"),Restaurants.getFloat("longtitude"),Restaurants.getInt("review_count"),Restaurants.getFloat("stars"));
		}
		System.out.println("ready!!!");
		driver.close();
		w.close();
	}
	
	protected void indexInitializer() {
		indexMinMax = new RAMDirectory();
		indexSort = new RAMDirectory();
		indexSubset = new RAMDirectory();
		indexFinal = new RAMDirectory();
		configMinMax = new IndexWriterConfig(analyzer);
		configSort = new IndexWriterConfig(analyzer);
		configSubset = new IndexWriterConfig(analyzer);
		configFinal = new IndexWriterConfig(analyzer);
	}
	
	public static void main(String[] args) throws IOException{
		a = args;
		indexReview = FSDirectory.open(Paths.get("/yelp_restaurants/indexReview"));
		indexRestaurants = FSDirectory.open(Paths.get("/yelp_restaurants/indexRestaurants"));
		
		IndexWriter w = new IndexWriter(indexReview, configReview);
		w.close();
		
		w = new IndexWriter(indexRestaurants, configRestaurants);
        w.close();
        SearchYelp yelp = new SearchYelp();
        yelp.search();
	}
	
	
}
