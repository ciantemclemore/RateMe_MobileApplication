package Models;

import com.google.gson.annotations.SerializedName;

public class BadRequest {
    @SerializedName("Response")
    public String response;
    @SerializedName("Error")
    public String error;
}
