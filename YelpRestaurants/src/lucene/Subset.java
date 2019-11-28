package lucene;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Subset extends SearchMain {

	private IndexWriter w;
	private DocumentAdd add = new DocumentAdd();
	
	void setSubsetRestaurant(boolean differentCities,boolean differentStarNumbers,boolean differentAddresses) throws IOException {
		Directory indexTemp2 = new RAMDirectory();
		IndexWriterConfig configTemp2 = new IndexWriterConfig(analyzer);
		if (differentCities && differentStarNumbers) {
			Directory indexTemp = new RAMDirectory();
			IndexWriterConfig configTemp = new IndexWriterConfig(analyzer);
			setSubsetRestaurantDifferent("city",indexSubset,indexTemp,configTemp);
			setSubsetRestaurantDifferent("stars",indexTemp,indexTemp2,configTemp2);
		}
		
		else if (differentCities) {
			setSubsetRestaurantDifferent("city",indexSubset,indexTemp2,configTemp2);
		}
		
		else if (differentStarNumbers) {
			setSubsetRestaurantDifferent("stars",indexSubset,indexTemp2,configTemp2);
		}
		
		if (differentAddresses) {
			if (differentCities||differentStarNumbers) {
				setSubsetRestaurantDifferent("address",indexTemp2,indexSort,configSort);
			}
			else {
				setSubsetRestaurantDifferent("address",indexSubset,indexSort,configSort);
			}
		}
		else {
			indexSort = indexTemp2;
		}
		
	}

	
	void setSubsetRestaurantDifferent(String search,Directory indexRead,Directory indexWrite,IndexWriterConfig configWrite) throws IOException {
		ArrayList <String>  searchArray = new ArrayList<String>();
		IndexReader reader = DirectoryReader.open( indexRead);
		IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 2000000);
        ScoreDoc[] hits = docs.scoreDocs;
	    w = new IndexWriter(indexWrite,configWrite);
        for(int i=0;i<hits.length;++i) {
        	int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            int size = searchArray.size();
            int j=0;
            boolean check=true;
            while(j<size&&check) {
        		if(d.get(search).equals(searchArray.get(j))){
        			check = false;
        		}
        		else {
        			j++;
        		}
            }
            if(check) {
            	searchArray.add(d.get(search));
                Query q2 = new TermQuery(new Term(search,d.get(search)));
            	IndexReader reader2 = DirectoryReader.open(indexRead);
            	IndexSearcher searcher2 = new IndexSearcher(reader2);
            	TopDocs docs2 = searcher2.search(q2, 1);
            	ScoreDoc[] hits2 = docs2.scoreDocs;
            	int docId2 = hits2[0].doc;
            	Document d2 = searcher2.doc(docId2);
            	add.addRestaurantDoc(w,d2.get("id"),d2.get("name"),d2.get("address"),d2.get("city"),d2.get("state"),
	              Float.parseFloat(d2.get("latitude")),Float.parseFloat(d2.get("longtitude")),Integer.parseInt(d2.get("review_count")),
	              Float.parseFloat(d2.get("stars")));
            }
        }
        w.close();
	}
	
	void setSubsetReview (boolean goodAndBadReviews, boolean oldAndNewReviews) throws IOException {
		
		if (goodAndBadReviews && oldAndNewReviews) {
			setSubsetReviewDifferent ("stars","date");
		}
		else if (goodAndBadReviews) {
			setSubsetReviewDifferent ("stars",null);
		}
		else if (oldAndNewReviews) {
			setSubsetReviewDifferent ("date",null);
		}
		
	}
	
	void setSubsetReviewDifferent (String search1,String search2) throws IOException {
		String min,max;
		String min2,max2;
		int hitMin,hitMax;
		Document d;
		Sort sort = new Sort(new SortedNumericSortField(search1, SortField.Type.LONG, true));
		IndexReader reader = DirectoryReader.open(indexMinMax);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 2000000,sort);
        ScoreDoc[] hits = docs.scoreDocs;
        if (hits.length == 0) {
        	indexSort = indexSubset;
        	return;
        }
        else {
        	hitMin = hits[hits.length-1].doc;
        }
        
        d = searcher.doc(hitMin);
        min = (d.get(search1));
        hitMax = hits[0].doc;
        d = searcher.doc(hitMax);
        max = (d.get(search1));
        Query queryMin = new TermQuery(new Term(search1,min));
        Query queryMax = new TermQuery(new Term(search1,max));
        BooleanQuery booleanQuery;
        if (search2!=null) {
        	System.out.println(search2);
        	sort = new Sort(new SortedNumericSortField(search2, SortField.Type.LONG, true));
        	reader = DirectoryReader.open(indexMinMax);
            searcher = new IndexSearcher(reader);
            docs = searcher.search(query, 2000000,sort);
            hits = docs.scoreDocs;
            hitMin = hits[hits.length-1].doc;
            d = searcher.doc(hitMin);
            min2 = (d.get(search2));
            hitMax = hits[0].doc;
            d = searcher.doc(hitMax);
            max2 = (d.get(search2));
            Query queryMin2 = new TermQuery(new Term(search2,min2));
            Query queryMax2 = new TermQuery(new Term(search2,max2));
            booleanQuery = new BooleanQuery.Builder()
            		.add(queryMax, Occur.SHOULD)
            		.add(queryMin, Occur.SHOULD)
            		.add(queryMax2, Occur.SHOULD)
            		.add(queryMin2, Occur.SHOULD).build();
        }
        else {
        	booleanQuery = new BooleanQuery.Builder()
            		.add(queryMax, Occur.SHOULD)
            		.add(queryMin, Occur.SHOULD).build();
        }
        
        reader = DirectoryReader.open(indexSubset);
        searcher = new IndexSearcher(reader);
        docs = searcher.search(booleanQuery, 2000000);
        hits = docs.scoreDocs; 
        w = new IndexWriter(indexSort,configSort);
        for(int i=0;i<hits.length;++i) {
        	 int docId = hits[i].doc;
             d = searcher.doc(docId);
             Date date = Date.valueOf(d.get("date").substring(0,4)+"-"+d.get("date").substring(4,6)+"-"+d.get("date").substring(6,8));
             add.addDoc(w, d.get("text"), d.get("business"),date,Integer.parseInt(d.get("useful")),Integer.parseInt(d.get("stars")));
        }
        w.close();
	}
	
}
