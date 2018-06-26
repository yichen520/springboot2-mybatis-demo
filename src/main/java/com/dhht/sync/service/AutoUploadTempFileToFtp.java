package com.dhht.sync.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


@Service
public class AutoUploadTempFileToFtp implements InitializingBean{
	
	@Resource
	private FtpManager ftpManager;
	//缓存目录
	private File tempDir;
	//自动上传线程
	private Thread autoUploadThread;

	@Value("${jiaohu.TEMP_DATA_DIR}")
	private String temp;
		
	@Override
    public void afterPropertiesSet() throws Exception {
		tempDir = new File(temp);
		
		Thread.sleep(10000);
		
		autoUploadThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {//注意，sleep放置于此以免continue导致sleep失效
	                    Thread.sleep(1000);
                    } catch (InterruptedException e) {
	                    e.printStackTrace();
                    }

					try {
		                File[] files = tempDir.listFiles();
		                if(files == null || files.length == 0) {
		                	continue;
		                }
		                
		                boolean uploadResult = false;
		                
		                File uploadFile;
		                FileOutputStream fileOutputStream = null;
		                FileChannel fileChannel = null;
		                
		                for(int i = 0; i < files.length; i++) {
		                	uploadFile = files[i];
		                	
							if(uploadFile.length() == 0) {//等待文件写完整
								continue;
							}
		                	
							//判断文件是否写入完毕，写入时会加锁，还有更好的办法？
		                	try {
		                        fileOutputStream = new FileOutputStream(uploadFile, true);
		                        fileChannel = fileOutputStream.getChannel();
		                        fileChannel.tryLock();
		                    } catch (Throwable e) {
		                    	System.out.println(uploadFile.getName() + "正在写入，稍后重试");
		                        continue;
		                    } finally {
		                    	try {
		                            fileChannel.close();
		                            fileOutputStream.close();
		                        } catch (Throwable e) {
		                            continue;
		                        }
		                    }
		                	
		                	uploadResult = ftpManager.upload(uploadFile);
		                	if(uploadResult) {
		                		uploadFile.delete();
		                	}
		                }
		            } catch (Throwable e) {
		                e.printStackTrace();
		            }
				}
			}
		});
		autoUploadThread.start();
    }

	
}
