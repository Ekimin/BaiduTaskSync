package com.he.IOHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.he.Utils.DBConfiguration;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TaskHandler {
	public static Logger logger = Logger.getLogger(TaskHandler.class);

	/**
	 * 获取表DT_OBJECT_TASK_INCR中最大ID
	 * 
	 * @return 最大ID
	 */
	public int getMaxID() {
		int maxID = 0;
		String sql = "SELECT MAX(ID) FROM DT_OBJECT_TASK_INCR";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DBConfiguration dbConn = new DBConfiguration();
		conn = dbConn.getConnection();

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				maxID = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("获取表DT_OBJECT_TASK_INCR中最大ID出错");
			e.printStackTrace();
		} catch (NumberFormatException nfe) {
			logger.error("结果为非数值类型");
			nfe.printStackTrace();
		}

		return maxID;
	}

	/**
	 * 从fulePath指定Excel中读取企业名单
	 * <li>
	 * 
	 * @param filePath
	 * @return 企业名单列表
	 */
	public List<String> getObjectName(String filePath) {
		List<String> objects = new ArrayList<String>();
		File file = new File(filePath);
		try {
			FileInputStream fis = new FileInputStream(file);
			StringBuilder sb = new StringBuilder();
			Workbook wb = Workbook.getWorkbook(fis);
			// Sheet[] sheet = wb.getSheets();

			Sheet rs = wb.getSheet(0);// 第一个sheet
			for (int j = 1; j < rs.getRows(); j++) {// 从第二行开始读
				Cell[] cells = rs.getRow(j);
				sb.append(cells[0].getContents());
				objects.add(cells[0].getContents());
			}

			fis.close();

		} catch (FileNotFoundException fnfe) {
			logger.error("从Excel中获取企业名单出错：找不到文件");
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			logger.error("从Excel中获取企业名单出错");
			ioe.printStackTrace();
		} catch (BiffException be) {
			logger.error("从Excel中获取企业名单出错");
			be.printStackTrace();
		}

		return objects;
	}

	/**
	 * 添加任务到表DT_OBJECT_TASK_INCR
	 * 
	 * @param objects
	 *            企业名字列表
	 * @param maxID
	 *            当前表中最大ID
	 * @param beginTime
	 *            指定爬取内容的开始时间
	 * @param endTime
	 *            指定爬取内容结束时间
	 */
	public void addTaskToDB(List<String> objects, int maxID, String beginTime, String endTime) {
		DBConfiguration dbConn = new DBConfiguration();
		Connection conn = dbConn.getConnection();
		PreparedStatement ps = null;

		String sql = "insert into dt_object_task_incr (id,taskid,objectname,status,begintime,endtime,inputtime,page)"
				+ " values(?,?,?,?,?,?,?,?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		String status = "waiting1";
		String inputTime = sdf.format(new Date());
		int page = 50;
		int cID = maxID;

		try {
			ps = conn.prepareStatement(sql);
			int batchCount = 0;
			for (String objectName : objects) {
				cID++;
				String taskID = "task000" + cID;

				ps.setString(1, String.valueOf(cID));
				ps.setString(2, taskID);
				ps.setString(3, objectName);
				ps.setString(4, status);
				ps.setString(5, beginTime);
				ps.setString(6, endTime);
				ps.setString(7, inputTime);
				ps.setString(8, String.valueOf(page));
				ps.addBatch();
				batchCount++;
				if (batchCount > 500) {
					ps.executeBatch();
					batchCount = 0;
				}
			}
			ps.executeBatch();
			batchCount = 0;
		} catch (SQLException e) {
			logger.error("添加任务到表出错");
			e.printStackTrace();
		}
	}
}
