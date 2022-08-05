package Models;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("Source")
    String source;
    @SerializedName("Value")
    String value;

}
