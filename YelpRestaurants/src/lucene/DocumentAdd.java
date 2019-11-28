package lucene;

import java.io.IOException;
import java.sql.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.NumericUtils;

public class DocumentAdd {
	
	public void addDoc(IndexWriter w, String text, String business,Date date,int useful,int stars) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("text", text, Field.Store.YES));

        doc.add(new StringField("business", business, Field.Store.YES));
        Field dateField = new StringField(
        	    "date", 
        	    DateTools.dateToString(date, DateTools.Resolution.MINUTE),
        	    Field.Store.YES);
        
        String aa = DateTools.dateToString(date, DateTools.Resolution.MINUTE);
        aa = aa.substring(0, 10);
        long date2 = Integer.parseInt(aa);
        doc.add(new SortedNumericDocValuesField("date", date2));
        
        doc.add(dateField);
        
        doc.add(new IntPoint("useful", useful));
		doc.add(new StoredField("useful", useful));
		doc.add(new SortedNumericDocValuesField("useful",(long) useful));
		
		doc.add(new FloatPoint("stars", stars));
		String temp = ""+stars;
		doc.add(new StringField("stars", temp, Field.Store.YES));
		doc.add(new StoredField("stars", stars));
		doc.add(new SortedNumericDocValuesField("stars",NumericUtils.floatToSortableInt(stars)));
        w.addDocument(doc);
        
    }
	
	
	public void addRestaurantDoc(IndexWriter w,String id,String name,String address,String city,String state,float latitude,float longtitude,int review_count,float stars) throws IOException {
		Document doc = new Document();
		doc.add(new StringField("id", id, Field.Store.YES));
		doc.add(new StringField("name", name, Field.Store.YES));
		doc.add(new StringField("address", address, Field.Store.YES));
		if (city!=(null)) {
			doc.add(new StringField("city", city, Field.Store.YES));
		}
		else {
			doc.add(new StringField("city", "No city", Field.Store.YES));
		}
		
		doc.add(new StringField("state", state, Field.Store.YES));
		
		doc.add(new FloatPoint("latitude", latitude));
		doc.add(new StoredField("latitude", latitude));
		doc.add(new SortedNumericDocValuesField("latitude",(long) latitude));
		
		doc.add(new FloatPoint("longtitude", longtitude));
		doc.add(new StoredField("longtitude", longtitude));
		doc.add(new SortedNumericDocValuesField("longtitude",(long) longtitude));
		
		doc.add(new IntPoint("review_count", review_count));
		doc.add(new StoredField("review_count", review_count));
		doc.add(new SortedNumericDocValuesField("review_count",(long) review_count));
		
		doc.add(new FloatPoint("stars", stars));
		String temp = ""+stars;
		doc.add(new StringField("stars", temp, Field.Store.YES));
		doc.add(new StoredField("stars", stars));
		doc.add(new SortedNumericDocValuesField("stars",NumericUtils.floatToSortableInt(stars)));
		w.addDocument(doc);
	}

}
