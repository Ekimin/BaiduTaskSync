package com.he.BaiduTaskSync;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.he.IOHandler.TaskHandler;

/**
 * 新增百度爬虫任务
 *
 */
public class SyncTask {
	public static Logger logger = Logger.getLogger(SyncTask.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("etc/log4j.properties");
		TaskHandler th = new TaskHandler();

		int maxID = th.getMaxID();
		List<String> list = th.getObjectName("doc/上报数据名单20161109.xls");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = sdf.format(date);
		System.out.println(today);
		long time = date.getTime();
		long btime = time/1000 -24*60*60;
		date.setTime(btime * 1000);
		String bday = sdf.format(date);
		System.out.println(bday);

		
		
		th.addTaskToDB(list, maxID, "2016-12-17", "2016-12-19");
		System.out.println("completed!");
	}
}
