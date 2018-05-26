package com.auditing.work.modle.jf;

import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Model;

public class SecondCatModel extends Model<SecondCatModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7176419911676768543L;
	
	public static final SecondCatModel dao = new SecondCatModel();
	
	public static SecondCatModel getCacheFirstCatByLevel(String level) {
		String key = "jf:secondcat:level" + level;
		Object obj = CacheHelper.cache.get(key);
		if(obj==null) {
			SecondCatModel cat = dao.findFirst("select * from SecondCategory where level=?", level);
			if(cat!=null) {
				CacheHelper.cache.put(key, cat);
			}
			return cat;
		} else {
			return (SecondCatModel)obj;
		}
	}

}
