package com.cat.task.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Organ;
import com.cat.system.model.User;
import com.cat.task.enumable.TaskType;
import com.cat.task.jsonbean.CommentBean;
import com.cat.task.jsonbean.ProcessBean;
import com.cat.task.jsonbean.ProcessStepBean;
import com.cat.task.jsonbean.ProcessViewBean;
import com.cat.task.model.Comment;
import com.cat.task.model.ProcessStep;
import com.cat.task.model.StepCondition;
import com.cat.task.model.TaskExt;

@RestController
@RequestMapping("/processstep")
public class ProcessStepController {

	@Autowired
	public BaseService baseService;
	
	// 提交流程
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/submit_process", method = RequestMethod.POST)
	public String submit_process(ProcessBean bean) {
		ProcessStep p;
		// 根据step_id 查询 step=1的任务
		if(!StringUtil.isEmpty(bean.getStep_id())) {
			p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("scId",bean.getStep_id(),"step",1));
		} else {
			ProcessStep px = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("appId",bean.getApp_id()));
			p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("scId",px.getId(),"step",1));
		}
		
		if("重新提交".equals(bean.getShyj())) {
			TaskExt task = new TaskExt();
			if(StringUtil.isEmpty(bean.getTask_id())) {
				// 当task_id为空时 获取当前task
				task = (TaskExt)baseService.getSimple("TaskExt", null, true
						,NameQueryUtil.setParams("key_value",bean.getEntity_id(),
								"step_id",bean.getStep_id()+"0000","taskType",TaskType.待办));
			} else {
				task = (TaskExt)baseService.findById(TaskExt.class, bean.getTask_id());
			}
			List<String> userIds = getSprs(p, bean.getOrgan_id());
			String result = saveTask(userIds, NameQueryUtil.setParams_s("step_id",p.getId(),
					"key_value",bean.getEntity_id(),"sqr_id",bean.getSqr_id(),"previous_id",bean.getSqr_id()));
			if(result!=null) {
				return result;
			}
			task.setTaskType(TaskType.已办);
			baseService.save(task);
			saveComment(NameQueryUtil.setParams_s("step_id",p.getScId()+"0000","key_value",bean.getEntity_id()
					,"shyj","","shjl","重新提交","sqr_id",bean.getSqr_id()
					,"organ_id",bean.getOrgan_id()));
			updataEntity(NameQueryUtil.setParams_s("sc_TableName",p.getSc_TableName(),"step_id",p.getScId()
					,"step",String.valueOf(p.getStep()),
					"step_name",p.getName(),"conclusion","重新提交申请","id",bean.getEntity_id()));
		} else {
			List<String> userIds = getSprs(p, bean.getOrgan_id());
			String result = saveTask(userIds, NameQueryUtil.setParams_s("step_id",p.getId(),
					"key_value",bean.getEntity_id(),"sqr_id",bean.getSqr_id(),"previous_id",bean.getSqr_id()));
			if(result!=null) {
				return result;
			}
			saveComment(NameQueryUtil.setParams_s("step_id",p.getScId()+"0000","key_value",bean.getEntity_id()
					,"shyj","","shjl","提交","sqr_id",bean.getSqr_id()
					,"organ_id",bean.getOrgan_id()));
			updataEntity(NameQueryUtil.setParams_s("sc_TableName",p.getSc_TableName(),"step_id",p.getScId()
					,"step",String.valueOf(p.getStep()),
					"step_name",p.getName(),"conclusion","提交申请","id",bean.getEntity_id()));
		}
		
		return ResultBean.getSucess("sucess");
	}
	
	// 提交流程
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/submit_step", method = RequestMethod.POST)
	public String submit_step(StepCondition bean,ProcessBean pb) {
		ProcessStep px = (ProcessStep)baseService.findById(ProcessStep.class, bean.getStep_id());
		TaskExt t = (TaskExt)baseService.findById(TaskExt.class, pb.getTask_id());
		// 查询下一步
		if(0 == bean.getTo_next()) {
			saveTask(Arrays.asList(t.getSqr_id()), NameQueryUtil.setParams_s("step_id",px.getScId()+"0000",
					"key_value",t.getKey_value(),"sqr_id",t.getSqr_id(),"previous_id",pb.getSqr_id()));
			
			saveComment(NameQueryUtil.setParams_s("step_id",px.getId(),"key_value",t.getKey_value()
					,"shyj",pb.getShyj(),"shjl",bean.getConclusion(),"sqr_id",pb.getSqr_id()
					,"organ_id",pb.getOrgan_id()));
			t.setTaskType(TaskType.已办);
			baseService.save(t);
			updataEntity(NameQueryUtil.setParams_s("sc_TableName",px.getSc_TableName(),"step_id",px.getScId()
					,"step",String.valueOf(bean.getTo_next()),
					"step_name","退回申请人","conclusion",bean.getConclusion(),"id",t.getKey_value()));
		} else if(99 == bean.getTo_next()){
			saveComment(NameQueryUtil.setParams_s("step_id",px.getId(),"key_value",t.getKey_value()
					,"shyj",pb.getShyj(),"shjl",bean.getConclusion(),"sqr_id",pb.getSqr_id()
					,"organ_id",pb.getOrgan_id()));
			// 处理原任务
			t.setTaskType(TaskType.已办);
			baseService.save(t);
			updataEntity(NameQueryUtil.setParams_s("sc_TableName",px.getSc_TableName(),"step_id",px.getScId()
					,"step","99",
					"step_name","流程结束","conclusion",bean.getConclusion(),"id",t.getKey_value()));
		} else {
			ProcessStep p = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("scId",px.getScId(),"step",bean.getTo_next()));
			
			// 创建任务
			List<String> userIds = getSprs(p, pb.getOrgan_id());
			String result = saveTask(userIds, NameQueryUtil.setParams_s("step_id",p.getId(),
					"key_value",t.getKey_value(),"sqr_id",t.getSqr_id(),"previous_id",pb.getSqr_id()));	
			if(result!=null) {
				return result;
			}
			// 创建意见
			saveComment(NameQueryUtil.setParams_s("step_id",px.getId(),"key_value",t.getKey_value()
					,"shyj",pb.getShyj(),"shjl",bean.getConclusion(),"sqr_id",pb.getSqr_id()
					,"organ_id",pb.getOrgan_id()));			
			// 处理原任务
			t.setTaskType(TaskType.已办);
			baseService.save(t);
			// 更新实体
			updataEntity(NameQueryUtil.setParams_s("sc_TableName",p.getSc_TableName(),"step_id",p.getScId()
					,"step",String.valueOf(p.getStep()),
					"step_name",p.getName(),"conclusion",bean.getConclusion(),"id",t.getKey_value()));
		}
		return ResultBean.getSucess("sucess");	
	}
	
	@RequestMapping(value = "/init_step", method = RequestMethod.GET)
	public String init_step(ProcessBean bean) {
		ProcessStep s;
		if(StringUtil.isEmpty(bean.getTask_id())) {
			s = (ProcessStep)baseService.getSimple("ProcessStep", null, true
					,NameQueryUtil.setParams("appId",bean.getApp_id()));
		} else {
			s = (ProcessStep)baseService.findById(ProcessStep.class, bean.getTask_id());
		}
		
		ProcessViewBean vbean = new ProcessViewBean();
		vbean.setTitle(s.getName());
		vbean.setSteps(initSteps(s,bean.getEntity_id()));
		if(StringUtil.isEmpty(bean.getEntity_id())) {
			vbean.setCurrent(0);
		} else {
			// 查询表对应的节点
			Integer xx = (Integer)baseService.getSimple(
					s.getTable_name(), null, true, "step",NameQueryUtil.setParams("id",bean.getEntity_id()));
			vbean.setCurrent(xx);
		}
		return ResultBean.getSucess(vbean);
	}
	
	// 初始化button
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/init_button", method = RequestMethod.GET)
	public String initButton(@RequestParam String step_id) {
		List<StepCondition> scs = (List<StepCondition>)baseService.getList("StepCondition", "o.xh asc", true
				,NameQueryUtil.setParams("step_id",step_id));
		return ResultBean.getSucess(scs);
	}
	
	private String saveTask(List<String> userIds ,Map<String,String> map) {
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
	
	private void saveComment(Map<String,String> map) {
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
	
	private void updataEntity(Map<String,String> map) {
		String update_sql = "update " + map.get("sc_TableName") + " t "
				+ "set t.step_id=:step_id,t.step=:step,t.step_name=:step_name,t.conclusion=:conclusion "
				+ "where t.id=:id";
		baseService.query(update_sql, true)
				.setParameter("step_id", map.get("step_id"))
				.setParameter("step", Integer.valueOf(map.get("step")))
				.setParameter("step_name", map.get("step_name"))
				.setParameter("conclusion", map.get("conclusion"))
				.setParameter("id", map.get("id")).executeUpdate();
	}
	
	@SuppressWarnings({ "unchecked" })
	private List<ProcessStepBean> initSteps(ProcessStep s,String entity_id) {
		List<ProcessStepBean> psbs = new ArrayList<ProcessStepBean>();
		// 查询子流程
		List<ProcessStep> ps = (List<ProcessStep>)baseService.getList("ProcessStep", "o.step asc", true,
				NameQueryUtil.setParams("scId",s.getId()));
		// 查询当前处理人
		Map<String,List<CommentBean>> map = new HashMap<String,List<CommentBean>>();
		Map<String,List<CommentBean>> map2 = new HashMap<String,List<CommentBean>>();
		
		if(!StringUtil.isEmpty(entity_id)) {
			List<TaskExt> tasks = (List<TaskExt>)baseService.
					getList("TaskExt", "task", "findCurrentTask", NameQueryUtil.setParams("key_value",entity_id,
							"task_id",s.getId()));
			if(!StringUtil.isListEmpty(tasks)) {
				for (TaskExt taskExt : tasks) {
					String name = "";
					if(!StringUtil.isEmpty(taskExt.getTransactor())) {
						name = taskExt.getTransactor_name();
					} else {
						name = taskExt.getTransactorgroups_name();
					}
					CommentBean b = new CommentBean();
					b.setJbr(name);
					b.setLevel("0");
					String key = taskExt.getStep_id();
					if(map.containsKey(key)) {
						List<CommentBean> bs  = map.get(key);
						bs.add(b);
						map.replace(key, bs);
					} else {
						List<CommentBean> bs = new ArrayList<CommentBean>();
						bs.add(b);
						map.put(key, bs);
					}
				}
			}
			
			// 查询意见
			List<Comment> comments = (List<Comment>)baseService.
					getList("Comment", "task", "findComment", NameQueryUtil.setParams("key_value",entity_id,
							"task_id",s.getId()));
			if(!StringUtil.isListEmpty(comments)) {
				for (Comment comment : comments) {
					CommentBean b = new CommentBean();
					b.setJbr(comment.getShr());
					b.setJbsj(comment.getShtime());
					b.setShjl(comment.getShjl());
					b.setShyj(comment.getShyj());
					b.setLevel("2");
					String key = comment.getStep_id();
					if(map2.containsKey(key)) {
						List<CommentBean> bs  = map2.get(key);
						bs.add(b);
						map2.replace(key, bs);
					} else {
						List<CommentBean> bs = new ArrayList<CommentBean>();
						bs.add(b);
						map2.put(key, bs);
					}
				}
			}
		}
		
	
		if(!StringUtil.isListEmpty(ps)) {
			// 渲染流程 加入一个开始步骤
			psbs.add(iniProcessStepBean("开始",map,map2,s.getId() + "0000"));
			
			for (ProcessStep p : ps) {
				psbs.add(iniProcessStepBean(p.getName(),map,map2,p.getId()));
			}
			// 渲染流程 加入一个结束流程
			ProcessStepBean a = new ProcessStepBean();
			a.setName("结束");
			psbs.add(a);
		}
		
		return psbs;
	}
	
	private ProcessStepBean iniProcessStepBean(String name,Map<String,List<CommentBean>> map,
			Map<String,List<CommentBean>> map2,String key) {
		ProcessStepBean b = new ProcessStepBean();
		b.setName(name);
		if(map.containsKey(key) && map2.containsKey(key)) {
			b.setComments(map2.get(key));
			b.setCurrentTask(map.get(key));
		} else {
			if(map.containsKey(key)) {
				b.setCurrentTask(map.get(key));
			} else if(map2.containsKey(key)) {
				List<CommentBean> bs = map2.get(key);
				bs.get(bs.size()-1).setLevel("1");
				b.setComments(bs);
				
				b.setJl(bs.get(bs.size()-1).getShjl());
			}
		}
		return b;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getSprs(ProcessStep p,String organId) {
		if(("1").equals(p.getShr_type())) {
			// 审核人类型为用户
			return p.getUser_s();
		} else if("2".equals(p.getShr_type())) {
			List<String> organs = new ArrayList<String>();
			// 审核人类型为角色
			if("1".equals(p.getRole_type())) {
				// 上级部门
				Organ o = (Organ)baseService.findById(Organ.class, organId);
				if(StringUtil.isEmpty(o.getScId())) {
					// 当上级部门为空时，取本级部门
					organs.add(organId);
				} else {
					organs.add(o.getScId());
				}
			} else if("2".equals(p.getRole_type())) {
				// 本级部门
				organs.add(organId);
			} else if("3".equals(p.getRole_type())) {
				// 指定部门
				organs.addAll(p.getOrgan_s());
			} 
			// 根据部门和角色 查询审核人
			List<String> userIds = (List<String>)baseService.getList("User", "system", "User_by_OrganAndRole", NameQueryUtil.setParams("orgs",
					organs,"roles",p.getRole_s()));
			return userIds;
		}
		
		return null;
	}
}
