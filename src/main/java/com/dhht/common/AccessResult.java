package com.dhht.common;

public class AccessResult {
    /** 操作是否成功 */
    private int isSuccess = 0;

    /** 结果信息 */
    private String resultMsg = "";

    public AccessResult() {

    }

    public AccessResult(int isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.resultMsg = msg;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
