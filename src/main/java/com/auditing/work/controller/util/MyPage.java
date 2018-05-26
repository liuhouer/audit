package com.auditing.work.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Page;

import com.google.common.collect.Maps;

/**
 * 通用分页工具类，该工具类分页指定泛型的List对集合
 * 
 * @author 冯志强
 * @time 2010-01-12
 * @param <E>
 *            指定的泛型
 */
public class MyPage<E>
{
	/**
	 * 每页显示的记录数
	 */
	private int pageSize;
	
	/**
	 * 当前页数
	 */
	private int pageNumber;
	

	/**
	 * 总记录集合
	 */
	private List<E> totalList;
	
	/**
	 * 总记录数
	 */
	private int totalRecord;

	/**
	 * 分页切割的启始点
	 */
	private int startIndex;

	/**
	 * 分页切割的结束点
	 */
	private int endIndex;

	/**
	 * 总页数
	 */
	private int totalPage;


	public static Map<String,Object> toMap(@SuppressWarnings("rawtypes") Page data){
		Map<String, Object> page_data =Maps.newHashMap();
		page_data.put("list", data.getContent());
		page_data.put("totalPage", data.getTotalPages());
		page_data.put("totalRow", data.getTotalElements());
		page_data.put("pageSize", data.getSize());
		page_data.put("pageNumber", data.getNumber()+1);
		return page_data;
	}
	

	
	public static Map<String,Object> toPageMap(
			@SuppressWarnings("rawtypes") List list,
			Integer pageSize,Integer pageNumber){
		Map<String, Object> page_data =Maps.newHashMap();
		
		Integer totalRow = pageSize*pageNumber+pageSize; 
		page_data.put("list",list);
		page_data.put("totalRow", totalRow);
		page_data.put("pageSize", pageSize);
		page_data.put("pageNumber", pageNumber);
	
		return page_data;
	}
	
	
	public static Integer getMybatisStartRow(Integer page, Integer size) {
		Integer startRow = (page-1)*size;

		return startRow;
	}

	public MyPage(List<E> totalList,int pageSize ,int pageNumber )
	{
		super();

		this.totalList = totalList;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		innit();
	}

	/**
	 * 初始化该分页对象
	 */
	private void innit()
	{
		if (null != totalList)
		{
			totalRecord = totalList.size();

			if (totalRecord % this.pageSize == 0)
			{
				this.totalPage = totalRecord / this.pageSize;
			}
			else
			{
				this.totalPage = totalRecord / this.pageSize + 1;
			}
		}
	}

	/**
	 * 得到分页后的数据
	 * 
	 * @return 分页数据
	 */
	public List<E> getList()
	{
		
		if (this.pageNumber <= 0)
		{
			this.pageNumber = 1;
		}
		if (this.pageNumber >= this.totalPage)
		{
			this.pageNumber = this.totalPage;
		}

		List<E> subList = new ArrayList<E>();

		if (null != this.totalList)
		{
			subList.addAll(this.totalList.subList(getStartIndex(), getEndIndex()));
		}

		return subList;
	}

	/**
	 * 设置每页显示的记录条数,如果不设置则默认为每页显示30条记录
	 * 
	 * @param pageRecords
	 *            每页显示的记录条数(值必需介于10~100之间)
	 */
	public void setPageRecords(int pageRecords)
	{
		if (pageRecords >= 10 && pageRecords <= 100)
		{
			this.pageSize = pageRecords;

			innit();
		}
	}

	public int getStartIndex()
	{
		if (null == this.totalList)
		{
			return 0;
		}

		this.startIndex = (getPageNumber() - 1) * this.pageSize;

		if (startIndex > totalRecord)
		{
			startIndex = totalRecord;
		}

		if (startIndex < 0)
		{
			startIndex = 0;
		}

		return startIndex;
	}

	public int getEndIndex()
	{
		if (null == this.totalList)
		{
			return 0;
		}

		endIndex = getStartIndex() + this.pageSize;

		if (endIndex < 0)
		{
			endIndex = 0;
		}

		if (endIndex < getStartIndex())
		{
			endIndex = getStartIndex();
		}

		if (endIndex > this.totalRecord)
		{
			endIndex = this.totalRecord;
		}

		return endIndex;
	}

	public int getTotalPage()
	{
		return totalPage;
	}

	public int getTotalRow()
	{
		return totalRecord;
	}

	public boolean isEndPage()
	{
		return this.pageNumber == this.totalPage;
	}

	/**
	 * 获取下一页的页数
	 * 
	 * @return 下一页的页数
	 */
	public int getNextPage()
	{
		int nextPage = this.pageNumber + 1;

		if (nextPage > this.totalPage)
		{
			nextPage = this.totalPage;
		}
		if (nextPage <= 0)
		{
			nextPage = 1;
		}

		return nextPage;
	}

	/**
	 * 获取上一页的页数
	 * 
	 * @return 上一页的页数
	 */
	public int getPrivyPage()
	{
		int privyPage = this.pageNumber - 1;

		if (privyPage > this.totalPage)
		{
			privyPage = this.totalPage;
		}

		if (privyPage <= 0)
		{
			privyPage = 1;
		}

		return privyPage;
	}

	public int getPageNumber()
	{
		return pageNumber;
	}
	
	/**
	 * Return page size.
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}



	
	
	
	

	
}
