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

    private List<String> resourceIds;

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