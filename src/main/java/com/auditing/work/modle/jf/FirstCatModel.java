package com.auditing.work.modle.jf;

import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Model;

public class FirstCatModel extends Model<FirstCatModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8904594749268972314L;
	
	public static final FirstCatModel dao = new FirstCatModel();
	
	public static FirstCatModel getCacheFirstCatByLevel(String level) {
		String key = "jf:firstcat:level" + level;
		Object obj = CacheHelper.cache.get(key);
		if(obj==null) {
			FirstCatModel cat = dao.findFirst("select * from FirstCategory where level=?", level);
			if(cat!=null) {
				CacheHelper.cache.put(key, cat);
			}
			return cat;
		} else {
			return (FirstCatModel)obj;
		}
	}

}
