package com.dhht.common;

import com.alibaba.fastjson.JSONObject;

public class JsonObjectBO {
    private int code;  //业务状态（编码）
    private String message;  //反馈信息
    private JSONObject data;  //业务数据

    /**
     * 业务状态（编码）
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * 业务状态（编码）
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 反馈信息
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 反馈信息
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 业务数据
     * @return the data
     */
    public JSONObject getData() {
        return data;
    }

    /**
     * 业务数据
     * @param data the data to set
     */
    public void setData(JSONObject data) {
        this.data = data;
    }

    /**
     * 输出该对象的Json格式字符串
     */
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);

        if(data != null){
            json.put("data", data);
        }else{
            json.put("data", "");
        }

        return json.toString();
    }
}
