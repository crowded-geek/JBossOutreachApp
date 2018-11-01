package com.crowded_geek.jbossoutreachapp.model;

public class Contributor {
    private String avatarUrl, name;
    private int contibutions;

    public Contributor() {

    }

    public Contributor(String avatarUrl, String name, int contibutions) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.contibutions = contibutions;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContibutions() {
        return contibutions;
    }

    public void setContibutions(int contibutions) {
        this.contibutions = contibutions;
    }
}
