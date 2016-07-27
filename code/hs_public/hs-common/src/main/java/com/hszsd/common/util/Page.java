package com.hszsd.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hszsd.common.util.string.StringUtil;




/**
 * 支持easyui 分页的公共类
 * @author 艾伍
 * @param <T>
 *
 */
public class Page<T>  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 返回总条数
	 */
	private int total = 0;
	/**
	 * 设置开始页数
	 */
	private int page=1;
	/**
	 * 设置每页条数，获取分页结果集
	 */
	private Object rows=0;
	/**
	 * 排序字段，使用驼峰命名，不得包含数据库关键字
	 */
	private String sort;
	/**
	 * 排序类型，只能是desc 或asc
	 */
	private String order="";
	/**
	 * 扩展的获取分页参数的范围
	 */
	private HsRowBounds rowBounds;
	/**
	 * 设置结果集
	 */
	private List<T> list=new ArrayList<T>();
	
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 获取数据总条数
	 * @param total
	 */
	public int getTotal() {
		return total;
	}
	
	/**
	 * 设置数据总条数
	 * @param total
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	
	public void setRows(Object rows) {
		this.rows = rows;
	}
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public Object getRows() {
		if(list!=null||list.size()>0){
			return list;
		}else
		return rows;
	}
/*	
	private int getRow() {
		int row=10;
		try {
			 row=Integer.parseInt(rows+"");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return row;
	}*/
	
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
	public HsRowBounds getRowBounds() {
		int i=Integer.parseInt(rows.toString()) ;
		rowBounds=i>0?new HsRowBounds(i* (page - 1),i):new HsRowBounds();
		return rowBounds;
	}
	
	public String getOrderBy(){
		if(null!=this.order){
			this.order=this.order.toLowerCase();
		}
		if("asc".equals(this.order)||"desc".equals(this.order)||null==this.order||"".equals(this.order)){
			if(null!=sort&&!"".equals(sort)){
				//驼峰转换
				String order=StringUtil.camelToUnderline(this.sort)+" "+this.order;
				if(this.sort.trim().lastIndexOf(" ")>-1){
					if(checkKeyWord(this.sort.trim().toLowerCase())){
						return "";
					}else{
						return order;
					}
				}else{
					return order;
				}
			}
		}
		return "";
	}

	// sql过滤关键字
	private boolean checkKeyWord(String sWord) {
		// 过滤关键字
		if (null != sWord && !"".equals(sWord)) {
			String s[]=new String[]{"'","and","exec","execute","insert","select","delete","update","count","drop","*","%","chr","mid","master","truncate","char","declare","sitename","net user","xp_cmdshell",";","or","-","+",",","like'","and","exec","execute","insert","create","drop","table","from","grant","use","group_concat","column_name","information_schema.columns","table_schema","union","where","select","delete","update","order","by","count","*","chr","mid","master","truncate","char","declare","or",";","-","--","+",",","like","//","/","%","#"};
			for (int i = 0; i < s.length; i++) {
				if (sWord.lastIndexOf(s[i]) > -1) {
					return true;
				}
			}
		}
		return false;
	}
}
