package com.cat.task.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Organ;
import com.cat.system.model.User;
import com.cat.task.enumable.TaskType;
import com.cat.task.model.Comment;
import com.cat.task.model.TaskExt;

@Service
public class TaskRouterController {
	
	@Autowired
	public BaseService baseService;

	public String saveTask(List<String> userIds ,Map<String,String> map) {
		if(StringUtil.isListEmpty(userIds)) {
			return ResultBean.getResultBean("400", "未找到下一步办理人", "未找到下一步办理人");
		} else {
			TaskExt task = new TaskExt();
			task.setStep_id(map.get("step_id"));
			task.setKey_value(map.get("key_value"));
			task.setSqr_id(map.get("sqr_id"));
			task.setPrevious_id(map.get("previous_id"));
			
			if(userIds.size()>1) {
				task.setTransactorgroups(StringUtil.listToString(userIds));
				task.setTaskType(TaskType.待领取);
			} else {
				task.setTransactorgroups(StringUtil.listToString(userIds));
				task.setTransactor(StringUtil.listToString(userIds));
				task.setTaskType(TaskType.待办);
			}
			baseService.save(task);
			
			return null;
		}
	}
	
	public void saveComment(Map<String,String> map) {
		Comment comment = new Comment();
		comment.setStep_id(map.get("step_id"));
		comment.setKey_value(map.get("key_value"));
		comment.setShyj(map.get("shyj"));
		comment.setShjl(map.get("shjl"));
		comment.setShtime(CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()));
		comment.setShr(((User)baseService.findById(User.class, map.get("sqr_id"))).getName());
		comment.setShr_id(map.get("sqr_id"));
		comment.setShjg_id(map.get("organ_id"));
		comment.setShjg_name(StringUtil.isEmpty(map.get("organ_id")) ? "" : 
			((Organ)baseService.findById(Organ.class, map.get("organ_id"))).getName());
		baseService.save(comment);
	}
}
