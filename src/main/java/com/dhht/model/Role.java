package com.dhht.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Role {
    private String id;

    private String name;

    private Boolean isSystem;

    private List<UserSimple> roleUser;


    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean system) {
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

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public List<UserSimple> getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(List<UserSimple> roleUser) {
        this.roleUser = roleUser;
    }

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

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isSystem=" + isSystem +
                ", roleUser=" + roleUser +
                ", resourceIds=" + resourceIds +
                '}';
    }

    //    public int getIsSystem() {
//        return isSystem;
//    }
//
//    public void setIsSystem(int isSystem) {
//        this.isSystem = isSystem;
//    }
}