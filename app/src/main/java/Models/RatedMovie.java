package Models;

public class RatedMovie extends Movie {

    public RatedMovie(Movie movie, int rating){
        this.userRating = rating;
        this.title = movie.title;
        this.poster = movie.poster;
    }

    private int userRating;
    public int getUserRating(){
        return this.userRating;
    }
}
