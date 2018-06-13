package com.dhht.model;

import java.util.List;
import java.util.Set;

public class Role {
    private String id;

    private String name;

    private boolean isSystem;

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    private List<String> resourceIds;

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }



//    public Set<String> getResource() {
//        return resource;
//    }
//
//    public void setResource(Set<String> resource) {
//        this.resource = resource;
//    }


//private String resources;
//    public String getResources() {
//        return resources;
//    }
//
//    public void setResources(String resources) {
//        this.resources = resources;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

//    public int getIsSystem() {
//        return isSystem;
//    }
//
//    public void setIsSystem(int isSystem) {
//        this.isSystem = isSystem;
//    }
}