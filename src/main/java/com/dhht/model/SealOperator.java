package com.dhht.model;

import lombok.Data;

/**
 * Created by imac_dhht on 2018/7/17.
 */
@Data
public class SealOperator {

    private String id;

    private Seal seal;

    private SealGetPerson sealGetPerson;

    private SealOperationRecord sealOperationRecord;

    private String operatorPhoto; //经办人照片

    private String positiveIdCardScanner;//身份证正面扫描件

    private String reverseIdCardScanner;//身份证反面扫描件

    private String proxy; //委托书

    private Integer pageNum;

    private Integer pageSize;

    private String electronicSealURL; //电子印模数据

    private String sealScannerURL;//实物印章印模扫描件

    private String businessScanner;//营业执照扫描件




}
