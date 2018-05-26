package com.auditing.work.config;

import javax.annotation.PostConstruct;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auditing.work.controller.BaseController;
import com.auditing.work.modle.jf.DepDictModel;
import com.auditing.work.modle.jf.DepReviewPointStatusModel;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.modle.jf.FirstCatModel;
import com.auditing.work.modle.jf.FourthCatModel;
import com.auditing.work.modle.jf.FourthcatDepRelation;
import com.auditing.work.modle.jf.ReviewPointDocModel;
import com.auditing.work.modle.jf.ReviewPointModel;
import com.auditing.work.modle.jf.SecondCatModel;
import com.auditing.work.modle.jf.ThirdCatModel;
import com.auditing.work.modle.jf.UserModel;
import com.jfinal.kit.Prop;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

@Component
public class Init {
	
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void initActiveRecord() {
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource);
		System.out.println(dataSource.getUrl());
		System.out.println(dataSource.getUsername());
		System.out.println(dataSource.getPassword());
		arp.setDevMode(true);
		arp.setShowSql(true);
		arp.addMapping("fourthcat_dep_relation", "fourthid,depid", FourthcatDepRelation.class);
		arp.addMapping("users", UserModel.class);
		arp.addMapping("department", DepartmentModel.class);
		arp.addMapping("ReviewPoint", ReviewPointModel.class);
		arp.addMapping("dep_reviewpoint_status", DepReviewPointStatusModel.class);
		
		arp.addMapping("FirstCategory", FirstCatModel.class);
		arp.addMapping("SecondCategory", SecondCatModel.class);
		arp.addMapping("ThirdCategory", ThirdCatModel.class);
		arp.addMapping("FourthCategory", FourthCatModel.class);
		
		arp.addMapping("dep_dict", DepDictModel.class);
		arp.addMapping("review_point_doc", ReviewPointDocModel.class);
		
		arp.start();
	}
	
	@PostConstruct
	public void initProp() {
		BaseController.prop = new Prop("config.properties");
	}
	
}
