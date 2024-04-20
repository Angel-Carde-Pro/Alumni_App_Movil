package com.gamesmindt.myapplication.Services;

import java.util.List;

public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private int usuario_id;
    private String username;
    private List<Authority> authorities;

    // Constructor
    public LoginResponse(String accessToken, String tokenType, int usuario_id, String username, List<Authority> authorities) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.usuario_id = usuario_id;
        this.username = username;
        this.authorities = authorities;
    }

    // Getters y Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    // Clase Authority interna
    public static class Authority {
        private String authority;

        // Constructor
        public Authority(String authority) {
            this.authority = authority;
        }

        // Getter y Setter
        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }
}
