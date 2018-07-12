package com.dhht.model;

public class Minitor {
    private String id;

    private Integer minitor;

    private String description;

    private Byte rank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getMinitor() {
        return minitor;
    }

    public void setMinitor(Integer minitor) {
        this.minitor = minitor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getRank() {
        return rank;
    }

    public void setRank(Byte rank) {
        this.rank = rank;
    }
}