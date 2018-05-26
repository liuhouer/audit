package com.auditing.work.modle.jf;

import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Model;

public class FourthCatModel extends Model<FourthCatModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6728141089251820064L;
	
	public static final FourthCatModel dao = new FourthCatModel();
	
	public static FourthCatModel getCacheFirstCatByLevel(String level) {
		String key = "jf:fourthcat:level" + level;
		Object obj = CacheHelper.cache.get(key);
		if(obj==null) {
			FourthCatModel cat = dao.findFirst("select * from FourthCategory where level=?", level);
			if(cat!=null) {
				CacheHelper.cache.put(key, cat);
			}
			return cat;
		} else {
			return (FourthCatModel)obj;
		}
	}

}
