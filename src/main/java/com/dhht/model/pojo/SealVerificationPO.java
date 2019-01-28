package com.dhht.model.pojo;

import com.dhht.model.Seal;
import com.dhht.model.SealAgent;
import com.dhht.model.SealVerification;
import lombok.Data;

import java.util.List;

@Data
public class SealVerificationPO {
    private Seal seal;
    private SealVerification sealVerification;
    private List<SealAgent> sealAgent;
}
