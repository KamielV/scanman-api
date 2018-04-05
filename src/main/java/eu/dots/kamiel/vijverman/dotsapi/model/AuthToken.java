/*
 */
package eu.dots.kamiel.vijverman.dotsapi.model;

/**
 *
 * @author kamie_itw2x3t
 */
public class AuthToken {
    private String token;

    public AuthToken(){

    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
