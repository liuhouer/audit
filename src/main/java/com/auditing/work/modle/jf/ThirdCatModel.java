package com.auditing.work.modle.jf;

import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Model;

public class ThirdCatModel extends Model<ThirdCatModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6712726970814555270L;
	
	public static final ThirdCatModel dao = new ThirdCatModel();
	
	public static ThirdCatModel getCacheFirstCatByLevel(String level) {
		String key = "jf:thirdcat:level" + level;
		Object obj = CacheHelper.cache.get(key);
		if(obj==null) {
			ThirdCatModel cat = dao.findFirst("select * from ThirdCategory where level=?", level);
			if(cat!=null) {
				CacheHelper.cache.put(key, cat);
			}
			return cat;
		} else {
			return (ThirdCatModel)obj;
		}
	}

}
