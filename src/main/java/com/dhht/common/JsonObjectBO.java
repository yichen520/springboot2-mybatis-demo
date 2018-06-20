package com.dhht.common;

import com.alibaba.fastjson.JSONObject;
//import com.dhht.util.WebUtil;
import org.springframework.web.bind.annotation.RequestMapping;

public class JsonObjectBO {
    private int code;  //业务状态（编码）
    private String message;  //反馈信息
    private JSONObject data;  //业务数据

    // 成功
    private static final Integer SUCCESS = 1;
    // 成功
    private static final Integer EXCEPTION = 0;
    // 异常 失败
    private static final Integer FAIL = -1;


    public JsonObjectBO(int code, String message, JSONObject data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public JsonObjectBO() {
    }

    public static JsonObjectBO error(String msg) {
        JsonObjectBO ResponseBo = new JsonObjectBO();
        ResponseBo.setCode(FAIL);
        ResponseBo.setMessage(msg);
        return ResponseBo;
    }

    public static JsonObjectBO ok(String msg) {
        JsonObjectBO ResponseBo = new JsonObjectBO();
        ResponseBo.setCode(SUCCESS);
        ResponseBo.setMessage(msg);
        return ResponseBo;
    }
    public static JsonObjectBO success(String msg,JSONObject data) {
        JsonObjectBO ResponseBo = new JsonObjectBO();
        ResponseBo.setCode(SUCCESS);
        ResponseBo.setData(data);
        ResponseBo.setMessage(msg);
        return ResponseBo;
    }
    public static JsonObjectBO exception(String msg) {
        JsonObjectBO ResponseBo = new JsonObjectBO();
        ResponseBo.setCode(EXCEPTION);
        ResponseBo.setMessage(msg);
        return ResponseBo;
    }

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

//    public void writeJson(Object object) {
//        WebUtil.writeJson(object);
//    }
}
