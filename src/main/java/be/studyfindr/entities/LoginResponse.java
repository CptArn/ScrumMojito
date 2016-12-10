package be.studyfindr.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anthony on 20/11/2016.
 */
public class LoginResponse {
    private String AccessToken;
    private long Id;

    public LoginResponse(){};

    public LoginResponse(String AccessToken, long id){
        this.AccessToken = AccessToken;
        this.Id = id;
    }

    public String getAccessToken(){
        return this.AccessToken;
    }

    public long getId(){
        return this.Id;
    }

    public void setAccessToken(String accessToken){
        this.AccessToken = accessToken;
    }

    public void setId(long id){
        this.Id = id;
    }
}
