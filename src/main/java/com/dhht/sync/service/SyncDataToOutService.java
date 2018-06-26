package com.dhht.sync.service;


import com.alibaba.fastjson.JSON;
import com.dhht.dao.JsonDataMapper;
import com.dhht.model.JsonData;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.UUIDUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;


@Service
public class SyncDataToOutService {
	@Resource
	private JsonDataMapper resultToOutDao;

	@Autowired
	private FtpConfig ftpConfig;

	//当前时间
	private long currentTime = 0;
	//当前时间要保存文件的总量
	private int totalInCurrentTim = 0;
	
	/**
	 * 保存结果
	 * @param dataType
	 * @param text
	 */
	public void saveResult(int dataType,int operateType,int result,String text) {
		JsonData resultToOut = new JsonData();
		resultToOut.setId(UUIDUtil.generate());
		resultToOut.setDealResult(result);
		resultToOut.setCreateTime(new Date());
		resultToOut.setDataType(dataType);
		resultToOut.setOperateType(operateType);
		resultToOut.setJsonData(strToByteArray(text));
		resultToOutDao.insert(resultToOut);
		writeJsonFile(resultToOut);
	}

	private void writeJsonFile(Object dataObj) {
		String objJson = JSON.toJSONString(dataObj);

		File file = new File(ftpConfig.getTEMP_DATA_DIR()+ "/" + generateFileName());

		FileOutputStream fileOutputStream = null;
		FileChannel channel = null;
		BufferedOutputStream outputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			//进行加锁，以便上传部分的代码能够判断文件是否已经输出完毕。可能有更好的解决方案
			channel = fileOutputStream.getChannel();
//    		channel.lock();

			outputStream = new BufferedOutputStream(fileOutputStream);
			outputStream.write(objJson.getBytes("UTF-8"));
			outputStream.flush();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				channel.close();
				fileOutputStream.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取文件名。为保证同一时间文件名不重复，在当前时间的毫秒数后面再加3位自增数作为文件名返回
	 * @return
	 */
	synchronized private String generateFileName() {
		long datetime = Calendar.getInstance().getTimeInMillis();
		if(datetime == currentTime) {//获取到的时间与上次操作的时间相同，数量自增，否则归零
			totalInCurrentTim++;
		}
		else {
			currentTime = datetime;
			totalInCurrentTim = 0;
		}
		return String.valueOf(datetime) + String.format("%03d", totalInCurrentTim);
	}

	/**
	 * 发送图片到内网
	 *
	 * @param
	 */
//	public void saveImg(String imgPath) {
//
//		byte[] imgData = null;
//		try {
//			imgData = FileUtils.fileToBytes(new File(imgParentDir + "/" + imgPath));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileTooBigException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String imgDataStr = Base64.encode(imgData);
//
//		save(imgDataStr, SyncDataType.IMAGE, SyncOperateType.SAVE, imgPath);
//	}
//
//	/**
//	 * 发送图片到内网
//	 *
//	 * @param imgPath 图片相对路径
//	 */
//	public void deleteImg(String imgPath) {
//		save("", SyncDataType.IMAGE, SyncOperateType.DELETE, imgPath);
//	}
//
//	/**
//	 * 将要同步的数据转JSON后输出到文件中缓存
//	 * @param fileName 输出后的文件名，按数据对象的ID进行给定，以免重复
//	 * @param dataObj 要输出的数据对象
//	 */
//	private void writeJsonFile(Object dataObj) {
//		String objJson = JSON.toJSONString(dataObj);
//		File file = new File(tempFileDir + "/" + generateFileName());
//
//		FileOutputStream fileOutputStream = null;
//		FileChannel channel = null;
//		BufferedOutputStream outputStream = null;
//		try {
//			fileOutputStream = new FileOutputStream(file);
//			//进行加锁，以便上传部分的代码能够判断文件是否已经输出完毕。可能有更好的解决方案
//			channel = fileOutputStream.getChannel();
////    		channel.lock();
//
//			outputStream = new BufferedOutputStream(fileOutputStream);
//			outputStream.write(objJson.getBytes("UTF-8"));
//			outputStream.flush();
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				outputStream.close();
//				channel.close();
//				fileOutputStream.close();
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//		}
//
//	}

	public static byte[] strToByteArray(String str) {
		if (str == null) {
			return null;
		}
		byte[] byteArray = str.getBytes();
		return byteArray;
	}
}
