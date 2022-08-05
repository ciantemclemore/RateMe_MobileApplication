package Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieSearch {
    @SerializedName("Search")
    public ArrayList<Movie> movieSearchResults;
    @SerializedName("totalResults")
    public String totalResults;
    @SerializedName("Response")
    public String response;
}
