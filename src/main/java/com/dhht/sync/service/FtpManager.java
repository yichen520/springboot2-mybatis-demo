package com.dhht.sync.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class FtpManager implements InitializingBean { 

	//FTP客户端
	private FTPClient ftpClient;
	//FTP配置文件
	@Autowired
	private FtpConfig ftpConfig;
	//ftp自动重连线程
	private Thread ftpAutoConnectThread;

	@Override
	public void afterPropertiesSet() throws Exception {

		ftpClient = new FTPClient();

		//开启ftp连接守护进程，断线后自动重连（也会建立第一次连接）
		ftpAutoConnectThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {//注意，sleep放置于此以免continue导致sleep失效
	                    Thread.sleep(10000);
                    } catch (InterruptedException e) {
	                    e.printStackTrace();
                    }
					//System.out.println("测试FTP连接状态");
		            if(ftpClient.isConnected()) {
		            	try {//防止第一次连接城后isConnected始终返回true导致无法重连，还有更好的办法？
		                    ftpClient.getStatus();
		                    continue;//如果能获取到状态，证明连接正常，不必重连
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		            }
		            try {
		            	//closeFtp();
		            	ftpClient = new FTPClient();
		        		connectFtp();
		            } catch (Throwable e) {
		                e.printStackTrace();
		            }
		            
		            
				}
			}
		});
		//ftpAutoConnectThread.start();
		
	}

	/**
	 * 获取ftp连接
	 * @return
	 * @throws Exception
	 */
	synchronized private void connectFtp() throws Exception{
		if(ftpClient.isConnected()) {
			return;
		}
	    ftpClient.connect(ftpConfig.getFTP_HOST(), ftpConfig.getFTP_PORT());
	    ftpClient.login(ftpConfig.getFTP_USER(), ftpConfig.getFTP_PWD());
	    ftpClient.setBufferSize(100000);
	    ftpClient.setDataTimeout(0);
	    ftpClient.setDefaultTimeout(0);
	    int reply = ftpClient.getReplyCode();      
	    if (FTPReply.isPositiveCompletion(reply)) {      
	        ftpClient.changeWorkingDirectory(ftpConfig.getFTP_PATH());
	    }
	    else {
	    	ftpClient.disconnect();
	    }
	}

//	/**
//	 * 关闭ftp连接
//	 */
//	private void closeFtp(){
//	  try {
//	      if (ftpClient != null && ftpClient.isConnected()) {
//	    	  ftpClient.logout();
//	    	  ftpClient.disconnect();
//	      }
//	  }catch (Throwable e){
//	    e.printStackTrace();
//	  }   
//	}
	
	/**
	 * ftp上传文件
	 * @param f：要上传的文件（不能为文件夹）
	 * @return true:上传成功 、false：上传失败
	 */
	public boolean upload(File f) {
		if(f.isDirectory()) {
			return false;
		}
		
		FileInputStream input = null;
	    try {
	        input = new FileInputStream(f);
	        boolean uploadResult = false;
	        System.out.println("文件名：" + f.getName());
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        uploadResult = ftpClient.storeFile(f.getName(), input);
	        if(uploadResult) {
	        	return true;
	        }
	        
        } catch (FileNotFoundException e) {
        	System.out.println("上传失败");
	        //e.printStackTrace();
        } catch (IOException e) {
        	System.out.println("上传失败");
        	//e.printStackTrace();
        } catch (Throwable e) {
        	System.out.println("上传失败");
        	//e.printStackTrace();
		} 
	    finally {
        	try {
	            input.close();
            } catch (IOException e) {
	            e.printStackTrace();
            }
        }
	    
	    return false;
	}

	/**
	 * 从服务器获取文件数据。
	 * @param filename 要获取数据的文件名
	 */
	public String receiveData(String filename){
		try {
	      ByteArrayOutputStream bout = new ByteArrayOutputStream();
	      ftpClient.retrieveFile(filename, bout);
	      return new String(bout.toByteArray(), "UTF-8");
	    } catch (Throwable e) {
	    	e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * 删除文件
	 * @param filename
	 * @return
	 */
	public boolean deleteFile(String filename) {
		try {
	        ftpClient.deleteFile(filename);
	        return true;
        } catch (Throwable e) {
	        e.printStackTrace();
        }
		return false;
	}
	
	/**
	 * 列出文件清单
	 * @return
	 */
	public String[] listFileNames() {
		try {
			ftpClient.enterLocalPassiveMode();
	        return ftpClient.listNames();
        } catch (Throwable e) {
	        e.printStackTrace();
        }
		return null;
	}
}
