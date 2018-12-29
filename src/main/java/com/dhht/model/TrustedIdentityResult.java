package com.dhht.model;

import lombok.Data;

@Data
public class TrustedIdentityResult {
    private boolean passed;
    private String message;
    private String memo;
}
