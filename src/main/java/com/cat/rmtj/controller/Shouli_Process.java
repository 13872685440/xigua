package com.cat.rmtj.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseAppBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.task.controller.BaseProcess;
import com.cat.task.enumable.TaskType;
import com.cat.task.jsonbean.ProcessBean;
import com.cat.task.model.ProcessStep;
import com.cat.task.model.StepCondition;
import com.cat.task.model.TaskExt;

@RestController
@RequestMapping("/shouli_process")
public class Shouli_Process extends BaseProcess{
	
	/**
	 * 提交申请
	 * @param p_parms_bean 参数bean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/submit_process", method = RequestMethod.POST)
	public String submit_process(ProcessBean p_parms_bean,BaseAppBean bean) {
		
		ProcessStep first_step = getFristStep(p_parms_bean);
		if(first_step == null) {
			return ResultBean.getResultBean("400", "流程未配置", "流程未配置");
		} else {
			List<String> userIds = getSprs(p_parms_bean , first_step);
			
			return submit_process(p_parms_bean,first_step,userIds,
					NameQueryUtil.setParams_s(new String[] {"step_id","step","step_name","conclusion","slsj","id"},
					new Object[] {first_step.getScId(),first_step.getStep(),first_step.getName(),"受理成功",
							CalendarUtil.getYyyyMmDdHHmmss(Calendar.getInstance()),p_parms_bean.getEntity_id()}),null);
		}
	}
	
	/**
	 * 提交
	 * @param p_parms_bean
	 * @param stepCondition
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/submit_step", method = RequestMethod.POST)
	public String submit_step(ProcessBean p_parms_bean,StepCondition stepCondition) {
		ProcessStep current_step = (ProcessStep)baseService.findById(ProcessStep.class, stepCondition.getStep_id());
		TaskExt current_task = (TaskExt)baseService.findById(TaskExt.class, p_parms_bean.getTask_id());
		if(TaskType.已办.equals(current_task.getTaskType())) {
			return ResultBean.getResultBean("400", "流程已被处理", "流程已被处理");
		} else {
			current_task.setTransactor(p_parms_bean.getSqr_id());
		}
		// 查询下一步
		if(0 == stepCondition.getTo_next()) {
			// 退回申请人
			return to_0(current_task,p_parms_bean,current_step,stepCondition,Arrays.asList(current_task.getSqr_id()),null);
		} else if(99 == stepCondition.getTo_next() || 88 == stepCondition.getTo_next()){
			// 审核不通过
			return to_99(current_task,p_parms_bean,current_step,stepCondition,null);
		} else {
			ProcessStep p = (ProcessStep)baseService.getSimple
					("ProcessStep", null, true,NameQueryUtil.setParams("scId",current_step.getScId(),"step",stepCondition.getTo_next()));

			List<String> userIds = getSprs(p_parms_bean,p);
			return to_next(current_task,p_parms_bean,current_step,stepCondition,userIds,null);
		}
	}
	
	private List<String> getSprs(ProcessBean bean,ProcessStep p) {
		List<String> s = new ArrayList<String>();
		s.add(bean.getSqr_id());
		return s;
	}
	
	@RequestMapping(value = "/init_step", method = RequestMethod.GET)
	public String init_step(ProcessBean bean) {
		List<ProcessStep> ps = getProcessStep(bean);
		
		if(StringUtil.isListEmpty(ps)) {
			return ResultBean.getResultBean("400", "流程未配置", "流程未配置");
		} else {
			return ResultBean.getSucess(initProcessViewBean(bean,ps));
		}
	}
}
