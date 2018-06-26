/**
 * 
 */
package com.dhht.sync.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 同步的数据
 * @author zxl
 *
 */
public class DataFromOut implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String id; 
	//数据
    private String data; 
	//数据类型
    private int dataType;	
	//操作类型
    private int operateType;
    //操作时间
    private Date dateTime;
    //附加数据
    private String fujiaData;


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

	public String getData() {
    	return data;
    }

	public void setData(String data) {
    	this.data = data;
    }

	public String getFujiaData() {
    	return fujiaData;
    }

	public void setFujiaData(String fujiaData) {
    	this.fujiaData = fujiaData;
    }
}
