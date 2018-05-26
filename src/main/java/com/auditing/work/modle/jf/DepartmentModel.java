package com.auditing.work.modle.jf;

import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Model;

public class DepartmentModel extends Model<DepartmentModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2166784575759337800L;
	
	public static final DepartmentModel dao = new DepartmentModel();
	
	public static DepartmentModel getCacheDepartmentByName(String name) {
		String key = "jf:department:" + name;
		Object obj = CacheHelper.cache.get(key);
		if(obj==null) {
			DepartmentModel department = dao.findFirst("select * from department where name = ?", name);
			if(department!=null) {
				CacheHelper.cache.put(key, department);
			}
			return department;
		} else {
			return (DepartmentModel)obj;
		}
	}

}
