package com.cat.task.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.file.controller.MinioFileController;
import com.cat.system.model.Organ;
import com.cat.system.model.User;
import com.cat.task.enumable.TaskType;
import com.cat.task.jsonbean.CommentBean;
import com.cat.task.jsonbean.ProcessBean;
import com.cat.task.jsonbean.ProcessViewBean;
import com.cat.task.model.Comment;
import com.cat.task.model.ProcessStep;
import com.cat.task.model.StepCondition;
import com.cat.task.model.TaskExt;

public abstract class BaseProcess {

	@Autowired
	public BaseService baseService;
	
	@Autowired
	public MinioFileController minioFileController;
	
	/***
	 * 初次提交
	 * @param p_parms_bean 传递的参数bean
	 * @param next_step 流程配置
	 * @param userIds 下一步审核人
	 * @param frist 初次申请时，需要更新的entity参数
	 * @param last 重新提交时，需要更新的entity参数
	 * @return
	 */
	public String submit_process(ProcessBean p_parms_bean,ProcessStep next_step,List<String> userIds,
			Map<String,Object> frist,Map<String,Object> last) {
		if(StringUtil.isListEmpty(userIds)) {
			return ResultBean.getResultBean("400", "未找到下一步办理人", "未找到下一步办理人");
		}
		// 初次提交
		if("0".equals(p_parms_bean.getBiaozhi())) {
			// 保存任务
			saveTask(userIds, NameQueryUtil.setParams_s("step_id",next_step.getId(),
					"key_value",p_parms_bean.getEntity_id(),"sqr_id",p_parms_bean.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));
			// 保存意见
			saveComment(NameQueryUtil.setParams_s("step_id",next_step.getScId()+"0000","key_value",p_parms_bean.getEntity_id()
					,"shyj",StringUtil.isEmpty(p_parms_bean.getShyj()) ? "" : p_parms_bean.getShyj(),"shjl","受理","sqr_id",p_parms_bean.getSqr_id()
					,"organ_id",p_parms_bean.getOrgan_id()));
			// 更新实体
			updataEntity(next_step.getSc_TableName(),
				StringUtil.isMapEmpty(frist) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
					new Object[] {next_step.getScId(),next_step.getStep(),next_step.getName(),"受理",p_parms_bean.getEntity_id()}) : frist);	
		} else {
			// 重新提交
			TaskExt current_task = new TaskExt();
			if(StringUtil.isEmpty(p_parms_bean.getTask_id())) {
				// 当task_id为空时 根据传递的step_id来查找当前task
				current_task = (TaskExt)baseService.getSimple("TaskExt", null, true
						,NameQueryUtil.setParams("key_value",p_parms_bean.getEntity_id(),
								"step_id",p_parms_bean.getStep_id()+"0000","taskType",TaskType.待办));
			} else {
				// 否则 根据task_id 来查找
				current_task = (TaskExt)baseService.findById(TaskExt.class, p_parms_bean.getTask_id());
			}
			saveTask(userIds, NameQueryUtil.setParams_s("step_id",next_step.getId(),
					"key_value",p_parms_bean.getEntity_id(),"sqr_id",current_task.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));
			current_task.setTaskType(TaskType.已办);
			baseService.save(current_task);
			saveComment(NameQueryUtil.setParams_s("step_id",next_step.getScId()+"0000","key_value",p_parms_bean.getEntity_id()
					,"shyj",StringUtil.isEmpty(p_parms_bean.getShyj()) ? "" : p_parms_bean.getShyj(),"shjl","重新提交","sqr_id",p_parms_bean.getSqr_id()
					,"organ_id",p_parms_bean.getOrgan_id()));
			updataEntity(next_step.getSc_TableName(),StringUtil.isMapEmpty(last) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
					new Object[] {next_step.getScId(),next_step.getStep(),next_step.getName(),"重新提交",p_parms_bean.getEntity_id()}) : last);			
		}
		
		return ResultBean.getSucess("sucess");
	}
	
	/**退回申请人
	 * 
	 * @param current_task 当前任务
	 * @param p_parms_bean 传递的参数bean
	 * @param current_step 当前步骤
	 * @param stepCondition 当前处理条件
	 * @param userIds 下一步处理人
	 * @param maps 更新entity的map
	 * @return
	 */
	public String to_0(TaskExt current_task,ProcessBean p_parms_bean,ProcessStep current_step,
			StepCondition stepCondition,List<String> userIds,
			Map<String,Object> maps) {
		
		// 保存任务
		saveTask(Arrays.asList(current_task.getSqr_id()), NameQueryUtil.setParams_s("step_id",current_step.getScId()+"0000",
				"key_value",current_task.getKey_value(),"sqr_id",current_task.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));
		
		// 保存意见
		saveComment(NameQueryUtil.setParams_s("step_id",current_step.getId(),"key_value",current_task.getKey_value()
				,"shyj",p_parms_bean.getShyj(),"shjl",stepCondition.getConclusion(),"sqr_id",p_parms_bean.getSqr_id()
				,"organ_id",p_parms_bean.getOrgan_id()));
		// 当前任务处理为已办
		current_task.setTaskType(TaskType.已办);
		baseService.save(current_task);
		// 更新实体
		updataEntity(current_step.getSc_TableName(),
				StringUtil.isMapEmpty(maps) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
						new Object[] {current_step.getScId(),String.valueOf(stepCondition.getTo_next()),
								"申请人",stepCondition.getConclusion(),p_parms_bean.getEntity_id()}) : maps);
		return ResultBean.getSucess("sucess");
	}
	
	/**流程结束
	 * 
	 * @param current_task 当前任务
	 * @param p_parms_bean 传递的参数bean
	 * @param current_step 当前步骤
	 * @param stepCondition 当前处理条件
	 * @param userIds 下一步处理人
	 * @param maps 更新entity的map
	 * @return
	 */
	public String to_99(TaskExt current_task,ProcessBean p_parms_bean,ProcessStep current_step,StepCondition stepCondition,
			Map<String,Object> maps) {
		// 保存意见
		saveComment(NameQueryUtil.setParams_s("step_id",current_step.getId(),"key_value",current_task.getKey_value()
				,"shyj",p_parms_bean.getShyj(),"shjl",stepCondition.getConclusion(),"sqr_id",p_parms_bean.getSqr_id()
				,"organ_id",p_parms_bean.getOrgan_id()));
		// 处理原任务
		current_task.setTaskType(TaskType.已办);
		baseService.save(current_task);
		// 更新实体
		updataEntity(current_step.getSc_TableName(),
				StringUtil.isMapEmpty(maps) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
						new Object[] {current_step.getScId(),stepCondition.getTo_next(),"流程结束",stepCondition.getConclusion(),
								p_parms_bean.getEntity_id()}) : maps);
		return ResultBean.getSucess("sucess");
	}
	
	/** 下一步
	 * 
	 * @param current_task 当前任务
	 * @param p_parms_bean 传递的参数bean
	 * @param current_step 当前步骤
	 * @param stepCondition 当前处理条件
	 * @param userIds 下一步处理人
	 * @param maps 更新entity的map
	 * @return
	 */
	public String to_next(TaskExt current_task,ProcessBean p_parms_bean,ProcessStep current_step,StepCondition stepCondition,List<String> userIds,
			Map<String,Object> maps) {
		if(StringUtil.isListEmpty(userIds)) {
			return ResultBean.getResultBean("400", "未找到下一步办理人", "未找到下一步办理人");
		}
		ProcessStep next_step = (ProcessStep)baseService.getSimple("ProcessStep", null, true
				,NameQueryUtil.setParams("scId",current_step.getScId(),"step",stepCondition.getTo_next()));
		if(next_step == null) {
			return ResultBean.getResultBean("400", "流程未配置", "流程未配置");
		}
		saveTask(userIds, NameQueryUtil.setParams_s("step_id",next_step.getId(),
				"key_value",current_task.getKey_value(),"sqr_id",current_task.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));	
		// 创建意见
		saveComment(NameQueryUtil.setParams_s("step_id",current_step.getId(),"key_value",current_task.getKey_value()
				,"shyj",p_parms_bean.getShyj(),"shjl",stepCondition.getConclusion(),"sqr_id",p_parms_bean.getSqr_id()
				,"organ_id",p_parms_bean.getOrgan_id()));			
		// 处理原任务
		current_task.setTaskType(TaskType.已办);
		baseService.save(current_task);
		// 更新实体
		updataEntity(current_step.getSc_TableName(),
				StringUtil.isMapEmpty(maps) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
						new Object[] {current_step.getScId(),String.valueOf(next_step.getStep()),next_step.getName(),
								stepCondition.getConclusion(),p_parms_bean.getEntity_id()}) : maps);
		return ResultBean.getSucess("sucess");
	}
	
	/** to会签
	 * 
	 * @param current_task 当前任务
	 * @param p_parms_bean 传递的参数bean
	 * @param current_step 当前步骤
	 * @param next_step 下一步骤
	 * @param stepCondition 当前处理条件
	 * @param userIds 会签人
	 * @param maps 更新entity的map
	 * @return
	 */
	public String to_hq(TaskExt current_task,ProcessBean p_parms_bean,ProcessStep current_step,ProcessStep next_step,StepCondition stepCondition,List<String> userIds,
			Map<String,Object> maps) {
		if(StringUtil.isListEmpty(userIds)) {
			return ResultBean.getResultBean("400", "未找到下一步办理人", "未找到下一步办理人");
		}
		
		if(next_step == null) {
			return ResultBean.getResultBean("400", "流程未配置", "流程未配置");
		}
		for (String userId : userIds) {
			saveTask(Arrays.asList(new String[] {userId}), NameQueryUtil.setParams_s("step_id",next_step.getId(),
					"key_value",current_task.getKey_value(),"sqr_id",current_task.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));	
		}

		// 创建意见
		saveComment(NameQueryUtil.setParams_s("step_id",current_step.getId(),"key_value",current_task.getKey_value()
				,"shyj",p_parms_bean.getShyj(),"shjl",stepCondition.getConclusion(),"sqr_id",p_parms_bean.getSqr_id()
				,"organ_id",p_parms_bean.getOrgan_id()));			
		// 处理原任务
		current_task.setTaskType(TaskType.已办);
		baseService.save(current_task);
		// 更新实体
		updataEntity(current_step.getSc_TableName(),
				StringUtil.isMapEmpty(maps) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
						new Object[] {current_step.getScId(),String.valueOf(next_step.getStep()),next_step.getName(),
								stepCondition.getConclusion(),p_parms_bean.getEntity_id()}) : maps);
		return ResultBean.getSucess("sucess");
	}
	
	/** 会签
	 * 
	 * @param current_task 当前任务
	 * @param p_parms_bean 传递的参数bean
	 * @param current_step 当前步骤
	 * @param next_step 下一步骤
	 * @param stepCondition 当前处理条件
	 * @param userIds 会签人
	 * @param maps 更新entity的map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String hq(TaskExt current_task,ProcessBean p_parms_bean,ProcessStep current_step,ProcessStep next_step,StepCondition stepCondition,List<String> userIds,
			Map<String,Object> maps) {
		// 创建意见
		saveComment(NameQueryUtil.setParams_s("step_id",current_step.getId(),"key_value",current_task.getKey_value()
			,"shyj",p_parms_bean.getShyj(),"shjl",stepCondition.getConclusion(),"sqr_id",p_parms_bean.getSqr_id()
				,"organ_id",p_parms_bean.getOrgan_id()));			
		// 处理原任务
		current_task.setTaskType(TaskType.已办);
		baseService.save(current_task);
		List<TaskExt> tasks = (List<TaskExt>)baseService.getList("TaskExt", "", true,NameQueryUtil.setParams("key_value",current_task.getKey_value()
				,"step_id",current_task.getStep_id(),"taskType",Arrays.asList(new TaskType[] {TaskType.待办,TaskType.待领取,TaskType.已领取})));
		if(StringUtil.isListEmpty(tasks)) {
			if(StringUtil.isListEmpty(userIds)) {
				return ResultBean.getResultBean("400", "未找到下一步办理人", "未找到下一步办理人");
			}
			if(next_step == null) {
				return ResultBean.getResultBean("400", "流程未配置", "流程未配置");
			}
			saveTask(userIds, NameQueryUtil.setParams_s("step_id",next_step.getId(),
					"key_value",current_task.getKey_value(),"sqr_id",current_task.getSqr_id(),"previous_id",p_parms_bean.getSqr_id()));	
			// 更新实体
			updataEntity(current_step.getSc_TableName(),
					StringUtil.isMapEmpty(maps) ? NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","id"},
							new Object[] {current_step.getScId(),String.valueOf(next_step.getStep()),next_step.getName(),
									"会签完成",p_parms_bean.getEntity_id()}) : maps);
		} 
		
		return ResultBean.getSucess("sucess");
	}
	
	protected ProcessViewBean initProcessViewBean(ProcessBean bean, List<ProcessStep> ps) {
		ProcessViewBean vbean = new ProcessViewBean();
		ProcessStep p = (ProcessStep)baseService.findById(ProcessStep.class, ps.get(0).getScId());
		// 查询当前step
		int i = (int)baseService.getSimple(p.getTable_name(), "", true, "o.step",NameQueryUtil.setParams("id",
				bean.getEntity_id()));
		vbean.setTitle(p.getName());
		vbean.setCurrent(i);
		initSteps(bean,ps,vbean);
		return vbean;
	}
	
	@SuppressWarnings("unchecked")
	protected List<CommentBean> initSteps(ProcessBean bean, List<ProcessStep> ps,ProcessViewBean vbean) {
		Map<String,String> map = new HashMap<String,String>();
		List<CommentBean> cs = new ArrayList<CommentBean>();
		List<CommentBean> scs = new ArrayList<CommentBean>();
		CommentBean cb = new CommentBean();
		cb.setName("开始");
		scs.add(cb);
		map.put(ps.get(0).getScId() + "0000", "受理");
		for (ProcessStep processStep : ps) {
			String key = processStep.getId();
			String value = processStep.getName();
			map.put(key, value);
			
			CommentBean cbx = new CommentBean();
			cbx.setName(value);
			scs.add(cbx);
		}
		CommentBean cby = new CommentBean();
		cby.setName("结束");
		scs.add(cby);
		vbean.setPrcocess_steps(scs);
		
		Map<String,String> maps = new HashMap<String,String>();
		// 查询意见
		List<Comment> comments = (List<Comment>)baseService.
				getList("Comment", "task", "findComment", NameQueryUtil.setParams("key_value",bean.getEntity_id(),
						"task_id",ps.get(0).getScId()));
		if(!StringUtil.isListEmpty(comments)) {
			for (Comment comment : comments) {
				String key = comment.getStep_id();
				String value = comment.getId();
				if(maps.containsKey(key)) {
					maps.replace(key, value);
				} else {
					maps.put(key, value);
				}
			}
			for (Comment comment : comments) {
				String key = comment.getStep_id();
				String value = maps.get(key);
				CommentBean b = new CommentBean();
				b.setName(map.get(key));
				b.setJbr(comment.getShr());
				b.setJbsj(comment.getShtime());
				b.setShjl(comment.getShjl());
				b.setShyj(comment.getShyj());
				if(comment.getId().equals(value)) {
					b.setLevel("1");
				} else {
					b.setLevel("2");
				}
				cs.add(b);
			}
		}
		vbean.setComment_current(cs.size());
		// 查询当前流程
		List<TaskExt> tasks = (List<TaskExt>)baseService.
				getList("TaskExt", "task", "findCurrentTask", NameQueryUtil.setParams("key_value",bean.getEntity_id(),
						"task_id",ps.get(0).getScId()));
		if(!StringUtil.isListEmpty(tasks)) {
			for (TaskExt task : tasks) {
				CommentBean b = new CommentBean();
				b.setName(map.get(task.getStep_id()));
				if(!StringUtil.isEmpty(task.getTransactor())) {
					b.setJbr(task.getTransactor_name());
				} else {
					b.setJbr(task.getTransactorgroups_name());
				}
				b.setTime_out_type(task.getTime_out_type());
				cs.add(b);
			}
		}
		
		vbean.setComments(cs);
		return cs;
	}
	
	/**
	 * 查询第一步
	 * @param bean
	 * @return
	 */
	protected ProcessStep getFristStep(ProcessBean bean) {
		ProcessStep p;
		// 根据step_id 查询 step=1的任务  
		// 三种情况 当传递的step_id 为父节点时，length=4 通过父节点查找step=1的
		// 考虑到可能有多个step=1的（情况很少）length=8 直接查找step_id
		// 如果step_id未传递，可以通过app绑定的流程进行查找
		if(!StringUtil.isEmpty(bean.getStep_id())) {
			if(bean.getStep_id().length()==4) {
				p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("scId",bean.getStep_id(),"step",1));
			} else {
				p = (ProcessStep)baseService.findById(ProcessStep.class, bean.getStep_id());
			}
		} else {
			ProcessStep px = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("appId",bean.getApp_id()));
			p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("scId",px.getId(),"step",1));
		}
		return p;
	}
	
	/**
	 * 查询流程父节点
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<ProcessStep> getProcessStep(ProcessBean bean) {
		List<ProcessStep> ps = new ArrayList<ProcessStep>();
		String scId = "";
		ProcessStep p;
		// 根据step_id 查询
		// 三种情况 当传递的step_id 为父节点时，length=4  直接查找
		// length=8 直接截取前四位查找
		// 如果step_id未传递，可以通过app绑定的流程进行查找
		if(!StringUtil.isEmpty(bean.getStep_id())) {
			if(bean.getStep_id().length()==4) {
				scId = bean.getStep_id();
			} else {
				scId = bean.getStep_id().substring(0, 4);
			}
		} else {
			p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("appId",bean.getApp_id()));
			scId = p.getId();
		}
		ps = (List<ProcessStep>)baseService.getList("ProcessStep", "o.step asc", 
				true, NameQueryUtil.setParams("scId",scId));
		return ps;
	}
	
	/***
	 * 保存任务
	 * @param userIds
	 * @param map
	 * @return
	 */
	protected TaskExt saveTask(List<String> userIds ,Map<String,String> map) {
		TaskExt task = new TaskExt();
		// 步骤
		task.setStep_id(map.get("step_id"));
		// 实体id
		task.setKey_value(map.get("key_value"));
		// 申请人id
		task.setSqr_id(map.get("sqr_id"));
		// 上一步处理人 id
		task.setPrevious_id(map.get("previous_id"));
		
		Integer time_out;
		// 查询超时
		ProcessStep ps = (ProcessStep)baseService.findById(ProcessStep.class, map.get("step_id"));
		if(ps==null) {
			// 查询上级
			ProcessStep sup = (ProcessStep)baseService.findById(ProcessStep.class, map.get("step_id").substring(0,4));
			time_out = sup.getTime_out();
		} else if(ps.getTime_out() == null) {
			// 查询上级
			ProcessStep sup = (ProcessStep)baseService.findById(ProcessStep.class, ps.getScId());
			time_out = sup.getTime_out();
		} else {
			time_out = ps.getTime_out();
		}
		
		if(time_out !=null) {
			// 插入超时时间
			task.setTime_out(CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance(), Calendar.MINUTE, time_out));
		}
			
		if(userIds.size()>1) {
			task.setTransactorgroups(StringUtil.listToString(userIds));
			//task.setTaskType(TaskType.待领取);
		} else {
			task.setTransactorgroups(StringUtil.listToString(userIds));
			task.setTransactor(StringUtil.listToString(userIds));
			//task.setTaskType(TaskType.待办);
		}
		task.setTaskType(TaskType.待办);
		baseService.save(task);
			
		return task;
	}
	
	/***
	 * 保存意见
	 * @param map
	 * @return
	 */
	protected Comment saveComment(Map<String,String> map) {
		Comment comment = new Comment();
		// 步骤
		comment.setStep_id(map.get("step_id"));
		// 实体id
		comment.setKey_value(map.get("key_value"));
		// 审核意见
		comment.setShyj(map.get("shyj"));
		// 审核结论
		comment.setShjl(map.get("shjl"));
		// 审核时间
		comment.setShtime(CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()));
		// 审核人
		comment.setShr(((User)baseService.findById(User.class, map.get("sqr_id"))).getName());
		// 审核人id
		comment.setShr_id(map.get("sqr_id"));
		// 审核机构id
		comment.setShjg_id(map.get("organ_id"));
		// 审核机构
		comment.setShjg_name(StringUtil.isEmpty(map.get("organ_id")) ? "" : 
			((Organ)baseService.findById(Organ.class, map.get("organ_id"))).getName());
		baseService.save(comment);
		return comment;
	}
	
	@SuppressWarnings("rawtypes")
	protected void updataEntity(String table_name,Map<String,Object> map) {
		String update_sql = "update " + table_name + " t "
				+ "set " ;
		// 根据map拼接参数
		for (String x : map.keySet()) {
			if(!"id".equals(x)) {
				update_sql = update_sql + "t." + x + "=:"+ x + ",";
			}
		}
		update_sql = StringUtil.removeLast(update_sql) + " where t.id=:id";
		Query query = baseService.query(update_sql, true);
		for (String x : map.keySet()) {
			query.setParameter(x, map.get(x));
		}
		query.executeUpdate();
	}
	
	
	/**
	 * 根据 ProcessStep 获取处理人
	 * 1.当直接选定用户时，取用户。
	 * 2.当选择领导角色时，根据情况来进行判定。如果是董事长/总经理这种不分部门的角色，直接找对应的人即可。
	 * 如果是分管领导，如果该步骤选择了多个分管领导，并且指定了本级部门，说明是通用流程。则需要根据找当前环节处理人所在机构对应的分管领导；
	 * 如果没有选择本级部门，或者只选择了一个分管领导，则根据角色查询对应的分管领导即可。
	 * 3.当选择岗位角色时，则根据选定的审核角色来进行寻找。上级/本级部门，则找当前环节处理人所在机构/上级机构对应的岗位角色；
	 * 指定部门，则根据指定部门查找角色；不指定部门，则查询拥有该角色的所有人。
	 * 4.其他，做单独处理。
	 *
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getSprs(ProcessStep p,String org_id){
		List<String> userIds = new ArrayList<String>();
		if("1".equals(p.getShr_type())){
			userIds = p.getUser_s();
		} else if("3".equals(p.getShr_type())) {
			if(StringUtil.isEmpty(p.getRole_type())) {
				userIds = getLd_Users(p.getLd_role_s());
			} else {
				userIds = getLd_Bm_Users(p.getLd_role_s(),org_id);
			}
		} else if("2".equals(p.getShr_type())) {
			List<String> organs = new ArrayList<String>();
			// 审核人类型为角色
			if("1".equals(p.getRole_type())) {
				// 上级部门
				Organ o = (Organ)baseService.findById(Organ.class, org_id);
				if(StringUtil.isEmpty(o.getScId())) {
					// 当上级部门为空时，取本级部门
					organs.add(org_id);
				} else {
					organs.add(o.getScId());
				}
			} else if("2".equals(p.getRole_type())) {
				// 本级部门
				organs.add(org_id);
			} else if("3".equals(p.getRole_type())) {
				// 指定部门
				organs.addAll(p.getOrgan_s());
			} 
			// 根据部门和角色 查询审核人
			userIds = (List<String>)baseService.getList("User", "system", "User_by_OrganAndRole_ids", NameQueryUtil.setParams("orgs",
					organs,"roles",p.getRole_s()));
		}
		return userIds;
	}
	
	/**
	 * 查询领导角色
	 * @param roles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getLd_Users(List<String> roles) {
		List<String> userIds = new ArrayList<String>();
		userIds = (List<String>)baseService.getList
				("Sys_User_Role", null, false, "user_id",NameQueryUtil.setParams("role",roles));
		if(!StringUtil.isListEmpty(userIds)) {
			userIds =  (List<String>)baseService.getList
					("SYS_USERS", null, true, "id",NameQueryUtil.setParams("id",userIds));
		}
		return userIds;
	}
	
	/***
	 * 根据部门查询
	 * @param roles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getLd_Bm_Users(List<String> roles,String organ_id) {
		List<String> userIds = new ArrayList<String>();
		// 先查询organ_id 对应的角色 一个部门应该只有一个分管领导
		List<String> rolexs = (List<String>)baseService.getList
				("Sys_Role_Organ", null, true, "role",NameQueryUtil.setParams("org_id",organ_id,"role",roles));
		if(StringUtil.isListEmpty(rolexs)) {
			userIds = (List<String>)baseService.getList
					("Sys_User_Role", null, false, "user_id",NameQueryUtil.setParams("role",roles));
		}
		return userIds;
	}
}
