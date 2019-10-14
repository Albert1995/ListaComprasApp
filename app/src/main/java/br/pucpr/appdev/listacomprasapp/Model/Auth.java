package br.pucpr.appdev.listacomprasapp.Model;

public class Auth {
    String auth, token, uuid;

    public Auth() {
    }

    public Auth(String auth, String token, String uuid) {
        this.auth = auth;
        this.token = token;
        this.uuid = uuid;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
