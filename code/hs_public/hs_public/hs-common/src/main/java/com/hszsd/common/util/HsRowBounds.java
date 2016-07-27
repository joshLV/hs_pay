package com.hszsd.common.util;

import java.io.Serializable;

/**
 * 分页公共类
 * @author 艾伍
 * @version 1.0.0
 *
 */
public class HsRowBounds implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3372057355141826717L;
	public final static int NO_ROW_OFFSET = 0;
	public final static int NO_ROW_LIMIT = Integer.MAX_VALUE;
	public final static HsRowBounds DEFAULT = new HsRowBounds();
	
	/**
	 * 开始条数
	 */
	private int offset;
	/**
	 * 结束条数
	 */
	private int limit;

	public HsRowBounds() {
		this.offset = NO_ROW_OFFSET;
		this.limit = NO_ROW_LIMIT;
	}

	public HsRowBounds(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
