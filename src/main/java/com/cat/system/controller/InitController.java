package com.cat.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.config.JedisUtil;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.PassWordUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.jsonbean.OrganConfigBean;
import com.cat.system.jsonbean.SysConfigBean;
import com.cat.system.model.AppTree;
import com.cat.system.model.Role;
import com.cat.system.model.RoleApp;
import com.cat.system.model.User;

@RestController
@RequestMapping("/init")
public class InitController {

	@Autowired(required = true)
	public BaseService baseService;
	
	@Autowired
	private JedisUtil jedisUtil;
	
	/**
	 * {"system_name":"xxx平台","db_type":"Mysql","company_name":"湖北协同信安科技有限公司",
	 *   "manager_name":"xxx","phone":"139xxx","service_time":"工作日 08:00-18:00"}
	 * */
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public String first(@RequestBody SysConfigBean bean) {
		jedisUtil.set("sys_config", JSONObject.toJSONString(bean), RedisConst.config_db);
		return ResultBean.getSucess("");
	}
	
	/**
	 * {"hasOrgan":"1","hasPost":"1","mutiOrgan":"1",
	 *   "mutiPost":"1"}
	 * */
	@RequestMapping(value = "/sencond", method = RequestMethod.POST)
	public String sencond(@RequestBody OrganConfigBean bean) {
		jedisUtil.set("organ_config", JSONObject.toJSONString(bean), RedisConst.config_db);
		return ResultBean.getSucess("");
	}
	
	@RequestMapping(value = "/getConfig", method = RequestMethod.GET)
	public String getConfig(@RequestParam String key) {
		if(jedisUtil.exists(key, RedisConst.config_db)) {
			return ResultBean.getSucess(jedisUtil.get(key, RedisConst.config_db));
		} else {
			return ResultBean.getSucess("");
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String init() {
		List obj = (List) baseService.getList("User", null, false);
		if (StringUtil.isListEmpty(obj)) {
			return "-1";
		} else {
			return "0";
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/initdata", method = RequestMethod.POST)
	public String initdata() {
		Map<Object, Object> map = NameQueryUtil.setParams("loginName", "admin");
		boolean flag = baseService.isUnion("User", null, false, map);
		if (flag) {
			init工作台();
			init数据字典();
			init系统管理();
			init日志();
			init用户();
		}
		return "ok";
	}
	
	private void init工作台() {
		AppTree apptree = new AppTree();
		apptree.setId("0001");
		apptree.setClc("0001");
		apptree.setWn("仪表盘");
		apptree.setName("仪表盘");
		apptree.setLeaf(false);
		apptree.setDes("仪表盘");
		apptree.setXh(1);
		apptree.setLxh(1L);
		apptree.setComponent("RouteView");
		baseService.save(apptree, false);
		
		AppTree apptree2 = new AppTree();
		apptree2.setId("00010001");
		apptree2.setClc("0001");
		apptree2.setWn("仪表盘_工作台");
		apptree2.setName("工作台");
		apptree2.setLeaf(true);
		apptree2.setDes("工作台");
		apptree2.setXh(1);
		apptree2.setPath("/index");
		apptree2.setComponent("/dashboard/Workplace");
		apptree2.setScId("0001");
		apptree2.setLxh(11L);
		baseService.save(apptree2, false);
	}
	
	private void init数据字典() {
		AppTree apptree21 = new AppTree();
		apptree21.setId("0003");
		apptree21.setClc("0003");
		apptree21.setWn("数据字典");
		apptree21.setName("数据字典");
		apptree21.setLeaf(false);
		apptree21.setDes("数据字典");
		apptree21.setXh(3);
		apptree21.setLxh(3L);
		apptree21.setComponent("PageView");
		baseService.save(apptree21, false);
		
		AppTree apptree22 = new AppTree();
		apptree22.setId("00030001");
		apptree22.setClc("0001");
		apptree22.setWn("数据字典_数据字典");
		apptree22.setName("数据字典");
		apptree22.setLeaf(true);
		apptree22.setDes("数据字典");
		apptree22.setXh(1);
		apptree22.setPath("/dictionary");
		apptree22.setComponent("/dictionary/dictionary/Main");
		apptree22.setScId("0003");
		apptree22.setLxh(apptree21.getLxh()*10+apptree22.getXh());
		baseService.save(apptree22, false);
		
		AppTree apptree23 = new AppTree();
		apptree23.setId("00030002");
		apptree23.setClc("0002");
		apptree23.setWn("数据字典_参考示例");
		apptree23.setName("参考示例");
		apptree23.setLeaf(true);
		apptree23.setDes("参考示例");
		apptree23.setXh(2);
		apptree23.setPath("/demo");
		apptree23.setComponent("/demo/demo/Main");
		apptree23.setScId("0003");
		apptree23.setLxh(apptree21.getLxh()*10+apptree23.getXh());
		baseService.save(apptree23, false);
		
		AppTree apptree24 = new AppTree();
		apptree24.setId("00030003");
		apptree24.setClc("0003");
		apptree24.setWn("数据字典_参考示例（Tree）");
		apptree24.setName("参考示例（Tree）");
		apptree24.setLeaf(true);
		apptree24.setDes("参考示例（Tree）");
		apptree24.setXh(3);
		apptree24.setPath("/demotree");
		apptree24.setComponent("/demo/demotree/Main");
		apptree24.setScId("0003");
		apptree24.setLxh(apptree21.getLxh()*10+apptree24.getXh());
		baseService.save(apptree24, false);
	}
	
	private void init系统管理() {
		AppTree apptree11 = new AppTree();
		apptree11.setId("0002");
		apptree11.setClc("0002");
		apptree11.setWn("系统管理");
		apptree11.setName("系统管理");
		apptree11.setLeaf(false);
		apptree11.setDes("系统管理");
		apptree11.setXh(2);
		apptree11.setLxh(2L);
		apptree11.setComponent("PageView");
		baseService.save(apptree11, false);
		
		AppTree apptree12 = new AppTree();
		apptree12.setId("00020001");
		apptree12.setClc("0001");
		apptree12.setWn("系统管理_菜单管理");
		apptree12.setName("菜单管理");
		apptree12.setLeaf(true);
		apptree12.setDes("菜单管理");
		apptree12.setXh(1);
		apptree12.setPath("/apptree");
		apptree12.setComponent("/system/apptree/Main");
		apptree12.setScId("0002");
		apptree12.setRelational_table("Sys_Role_App.app|Sys_App_Permission.app|SYS_App_Tree.sc_id");
		apptree12.setLxh(apptree11.getLxh()*10+apptree12.getXh());
		baseService.save(apptree12, false);

		AppTree apptree13 = new AppTree();
		apptree13.setId("00020002");
		apptree13.setClc("0002");
		apptree13.setWn("系统管理_角色管理");
		apptree13.setName("角色管理");
		apptree13.setLeaf(true);
		apptree13.setDes("角色管理");
		apptree13.setXh(2);
		apptree13.setPath("/role");
		apptree13.setComponent("/system/role/Main");
		apptree13.setScId("0002");
		apptree13.setRelational_table("sys_role_app.role|Sys_User_Role.role|org_post_role.role");
		apptree13.setLxh(apptree11.getLxh()*10+apptree13.getXh());
		baseService.save(apptree13, false);
		
		AppTree apptree14 = new AppTree();
		apptree14.setId("00020003");
		apptree14.setClc("0003");
		apptree14.setWn("系统管理_用户管理");
		apptree14.setName("用户管理");
		apptree14.setLeaf(true);
		apptree14.setDes("用户管理");
		apptree14.setXh(3);
		apptree14.setPath("/user");
		apptree14.setComponent("/system/user/Main");
		apptree14.setScId("0002");
		apptree14.setRelational_table("Sys_User_Role.user_ID|Org_Post_Information.user_ID");
		apptree14.setLxh(apptree11.getLxh()*10+apptree14.getXh());
		baseService.save(apptree14, false);
		
		//String hasOrgan = "2";
		//String hasPost = "2";
		//if(jedisUtil.exists("organ_config", RedisConst.config_db)) {
			//JSONObject j = JSONObject.parseObject(jedisUtil.get("organ_config", RedisConst.config_db));
			//hasOrgan = j.getString("hasOrgan");
			//hasPost = j.getString("hasPost");
		//}
		
		//if("1".equals(hasOrgan)) {
			AppTree apptree15 = new AppTree();
			apptree15.setId("00020004");
			apptree15.setClc("0004");
			apptree15.setWn("系统管理_组织机构");
			apptree15.setName("组织机构");
			apptree15.setLeaf(true);
			apptree15.setDes("组织机构");
			apptree15.setXh(3);
			apptree15.setPath("/organ");
			apptree15.setComponent("/system/organ/Main");
			apptree15.setScId("0002");
			apptree15.setRelational_table("Org_Post_Information.organ_Id|Org_Organ.sc_id");
			apptree15.setLxh(apptree11.getLxh()*10+apptree15.getXh());
			baseService.save(apptree15, false);
		//}
		
		//if("1".equals(hasPost)) {
			AppTree apptree16 = new AppTree();
			apptree16.setId("00020005");
			apptree16.setClc("0005");
			apptree16.setWn("系统管理_岗位管理");
			apptree16.setName("岗位管理");
			apptree16.setLeaf(true);
			apptree16.setDes("岗位管理");
			apptree16.setXh(3);
			apptree16.setPath("/post");
			apptree16.setComponent("/system/post/Main");
			apptree16.setScId("0002");
			apptree16.setRelational_table("org_Post_Role.Post|Org_Post_Orgtype.Post|Org_Information_Post.post");
			apptree16.setLxh(apptree11.getLxh()*10+apptree16.getXh());
			baseService.save(apptree16, false);
		//}
			
			AppTree apptree17 = new AppTree();
			apptree17.setId("00020006");
			apptree17.setClc("0006");
			apptree17.setWn("系统管理_流程配置");
			apptree17.setName("流程配置");
			apptree17.setLeaf(true);
			apptree17.setDes("流程配置");
			apptree17.setXh(3);
			apptree17.setPath("/processstep");
			apptree17.setComponent("/system/processstep/Main");
			apptree17.setScId("0002");
			apptree17.setRelational_table("Task_Step_Condition.step_id|Task_Process_Step.sc_id");
			apptree17.setLxh(apptree11.getLxh()*10+apptree17.getXh());
			baseService.save(apptree17, false);
	}
	
	private void init日志() {
		AppTree apptree21 = new AppTree();
		apptree21.setId("0004");
		apptree21.setClc("0004");
		apptree21.setWn("日志监控");
		apptree21.setName("日志监控");
		apptree21.setLeaf(false);
		apptree21.setDes("日志监控");
		apptree21.setXh(4);
		apptree21.setLxh(4L);
		apptree21.setComponent("PageView");
		baseService.save(apptree21, false);
		
		AppTree apptree22 = new AppTree();
		apptree22.setId("00040001");
		apptree22.setClc("0001");
		apptree22.setWn("日志监控_临时日志");
		apptree22.setName("临时日志");
		apptree22.setLeaf(true);
		apptree22.setDes("临时日志");
		apptree22.setXh(1);
		apptree22.setPath("/logtemp");
		apptree22.setComponent("/system/logtemp/Main");
		apptree22.setScId("0004");
		apptree22.setLxh(apptree21.getLxh()*10+apptree22.getXh());
		baseService.save(apptree22, false);
		
		AppTree apptree23 = new AppTree();
		apptree23.setId("00040002");
		apptree23.setClc("0002");
		apptree23.setWn("日志监控_访问日志");
		apptree23.setName("访问日志");
		apptree23.setLeaf(true);
		apptree23.setDes("访问日志");
		apptree23.setXh(2);
		apptree23.setPath("/logview");
		apptree23.setComponent("/system/logview/Main");
		apptree23.setScId("0004");
		apptree23.setLxh(apptree21.getLxh()*10+apptree23.getXh());
		baseService.save(apptree23, false);
		
		AppTree apptree24 = new AppTree();
		apptree24.setId("00040003");
		apptree24.setClc("0003");
		apptree24.setWn("日志监控_异常日志");
		apptree24.setName("异常日志");
		apptree24.setLeaf(true);
		apptree24.setDes("异常日志");
		apptree24.setXh(3);
		apptree24.setPath("/logexception");
		apptree24.setComponent("/system/logexception/Main");
		apptree24.setScId("0004");
		apptree24.setLxh(apptree21.getLxh()*10+apptree24.getXh());
		baseService.save(apptree24, false);
	}
	
	@SuppressWarnings("unchecked")
	private void init用户() {
		Role role = new Role();
		role.setId("SYS_ADMINISTRATOR");
		role.setDes("超级管理员");
		baseService.save(role, false);

		List<AppTree> apps = (List<AppTree>)baseService.getList("AppTree", "", true, 
				NameQueryUtil.setParams("isLeaf",true));
		
		if(!StringUtil.isListEmpty(apps)) {
			for (AppTree appTree : apps) {
				RoleApp ra = new RoleApp();
				ra.setApp(appTree.getId());
				ra.setRole(role.getId());
				baseService.save(ra, false);
			}
		}
		// 创建一个admin账号
		User user = new User();
		user.setLoginName("admin");
		user.setName("系统管理员");
		Map<String, String> pwds = PassWordUtil.entryptPassword("1", true);
		user.setPwd(pwds.get("pwd"));
		user.setSalt(pwds.get("salt"));
		baseService.save(user, false);
	}
}
