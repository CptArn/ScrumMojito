package be.studyfindr.entities;

/**
 * Placeholder for login response.
 */
public class LoginResponse {

    // privates
    private String AccessToken;
    private long Id;

    /**
     * Creates a new instance of LoginResponse
     */
    public LoginResponse(){};

    /**
     * Creates a new instance of LoginResponse
     * @param AccessToken access token
     * @param id facebook id
     */
    public LoginResponse(String AccessToken, long id){
        this.AccessToken = AccessToken;
        this.Id = id;
    }

    /**
     * Gets the access token.
     * @return access token, null if unknown
     */
    public String getAccessToken(){
        return this.AccessToken;
    }

    /**
     * Gets the Facebook id
     * @return Facebook id
     */
    public long getId(){
        return this.Id;
    }

    /**
     * Sets the access token
     * @param accessToken new access token
     */
    public void setAccessToken(String accessToken){
        this.AccessToken = accessToken;
    }

    /**
     * Sets the id
     * @param id new id
     */
    public void setId(long id){
        this.Id = id;
    }
}
