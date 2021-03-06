package com.dhht.model;

import java.util.List;

public class  Resource {
    private String id;

    private Boolean isRequired;

    private String description;

    private String url;

    private String parentId;

    private String icon;

    private Integer menuSort;

    private Boolean isMenu;

    private List<Resource> children;

    public Resource(){

    }

    public Resource(String id, Boolean isRequired, String description, String url, String parentId, String icon, Integer menuSort, Boolean isMenu) {
        this.id = id;
        this.isRequired = isRequired;
        this.description = description;
        this.url = url;
        this.parentId = parentId;
        this.icon = icon;
        this.menuSort = menuSort;
        this.isMenu = isMenu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public boolean getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }
}