package com.cat.quartz.listener;

import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.cat.boot.util.StringUtil;
import com.cat.boot.util.XmlUtil;
import com.cat.quartz.util.QuartzManager;

@Service
public class QuartzListener implements ApplicationListener<ApplicationReadyEvent> {

	@SuppressWarnings("unchecked")
	private void addJobs() {
		Map<Object, Object> maps = XmlUtil.parserXml("/com/cat/quartz/controller/quartz.config.xml");
		if (!StringUtil.isMapEmpty(maps)) {
			Map<Object, Object> map2s = (Map<Object, Object>) maps.get("quartz");
			if (!StringUtil.isMapEmpty(map2s)) {
				for (Object obj : map2s.keySet()) {
					Map<Object, Object> map3s = (Map<Object, Object>) map2s.get(obj);
					try {
						System.out.println("加入定时任务-------"+map3s.get("taskname"));
						QuartzManager.addJob((String) map3s.get("taskname"), Class.forName((String) map3s.get("class")),
								(String) map3s.get("cron"));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}

	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		//addJobs();
	}

}
