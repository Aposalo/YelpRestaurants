package lucene;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import GUI.ResultsRestaurants;
import GUI.ResultsReviews;
import GUI.SearchRestaurants;
import GUI.SearchReview;

public class Caller extends SearchMain {
	
	
	public void searchFor(int choice) {
		indexInitializer();
		if (choice == 1) {
			SearchRestaurants restaurants = new SearchRestaurants();
			restaurants.setRestaurants();
		}
		else if (choice == 2) {
			SearchReview review = new SearchReview();
			review.setReviews();
		}
	}
	
	public void callReviews (String restaurantName,String keywords,String city,String address,int sortChoice,boolean goodAndBadReviews,
			boolean oldAndNewReviews,boolean nameSearch,boolean keywordSearch,boolean citySearch,boolean addressSearch) throws IOException, ParseException, InvalidTokenOffsetsException {
		indexInitializer();
		if (nameSearch||keywordSearch||citySearch||addressSearch) {
			Reviews reviews = new Reviews(restaurantName,keywords,city,address,nameSearch,keywordSearch,citySearch,addressSearch);
			reviews.searchReview(a);
		}
		if (goodAndBadReviews || oldAndNewReviews) {
			Subset set = new Subset();
			set.setSubsetReview(goodAndBadReviews,oldAndNewReviews);
		}
		else {
			indexSort = indexSubset;
		}
		SortClass sort = new SortClass();
		sort.sort(2,sortChoice);
		ResultsReviews results = new ResultsReviews();
		results.result();
	}
	
	public void callRestaurants (String state,String city,String address,String text,
			int sortChoice,boolean differentCities,boolean differentStarNumbers,boolean differentAddresses,
			boolean geographicalSearch,boolean textSearch) throws IOException, NumberFormatException, ParseException {
		indexInitializer();
		if (geographicalSearch||textSearch) {
			Restaurants restaurants = new Restaurants(state,city,address,text,
			geographicalSearch,textSearch);
			restaurants.searchRestaurant();
		}
		
		if (differentCities||differentStarNumbers||differentAddresses) {
			Subset set = new Subset();
			set.setSubsetRestaurant(differentCities,differentStarNumbers,differentAddresses);
		}
		else {
			indexSort = indexSubset;
		}
		SortClass sort = new SortClass();
		sort.sort(1,sortChoice);
		ResultsRestaurants results = new ResultsRestaurants();
		results.result();
	}

}
