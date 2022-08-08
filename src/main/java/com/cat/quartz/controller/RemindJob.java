package com.cat.quartz.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.quartz.util.JobHome;
import com.cat.task.model.Remind;
import com.cat.task.model.TaskExt;

public class RemindJob extends JobHome{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1561445979648782663L;

	@SuppressWarnings("unchecked")
	@Override
	protected void executeTask() {
		// 查询已经超时但是还未发送提醒的待办任务
		List<TaskExt> tasks =  (List<TaskExt>)baseJobService.
				getList("TaskExt", "quartz", "TaskExt_Chaoshi", 
						NameQueryUtil.setParams("dqsj",CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance())));
		if(!StringUtil.isListEmpty(tasks)) {
			for (TaskExt task : tasks) {
				List<String> xs = new ArrayList<String>();
				xs.addAll(Arrays.asList(task.getTransactorgroups().split(","))); 
				if(!StringUtil.isListEmpty(xs)) {
					for (String x : xs) {
						Remind r = new Remind();
						r.setTask_id(task.getId());
						r.setJsr_id(x);
						r.setR_type("任务超时");
						r.setTxsj(CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()));
						
						baseJobService.noAnnotationSave(r, false);
					}
					
				}
			}
		}
	}
}
