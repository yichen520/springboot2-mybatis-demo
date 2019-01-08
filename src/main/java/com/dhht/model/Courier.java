package com.dhht.model;


import lombok.Data;

@Data
public class Courier {
    private String id;

    private String recipientsId;

    private String sealId;

    private String courierNo;

    private String courierType;

    private String postal;

    private String address;

}