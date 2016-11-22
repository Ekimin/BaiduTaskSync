package com.he.BaiduTaskSync;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.he.IOHandler.TaskHandler;
import com.he.Utils.DBConfiguration;

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

		th.addTaskToDB(list, maxID, "2016-", "2017-");
	}
}
