package com.sanq.product.config.utils.entity;

import java.io.Serializable;

public class Pagination implements Serializable{

	private static final long serialVersionUID = 1L;

	//当前显示的条数,默认显示10条
	private int pageSize = 20;
	
	//当前显示的页数
	private int currentIndex = 1;
	
	//查询结果总条数
	private int totalCount;
	
	//总页数
	private int totalPage;
		
	public int getStartPage() {
		if(this.currentIndex <= 0)
			this.currentIndex = 1;
		if(this.currentIndex >= getTotalPage())
			this.currentIndex = getTotalPage();
		int startPage = (this.currentIndex - 1) * this.pageSize;
		return startPage < 0 ? 0 : startPage;
	}

	public Pagination() {}
	
	public Pagination(int pageSize, int currentIndex) {
		this.pageSize = pageSize;
		this.currentIndex = currentIndex;
	}
	
	public int getTotalPage() {
//		this.totalPage = (int) Math.ceil((this.totalCount / this.pageSize));
		this.totalPage = this.totalCount % this.pageSize != 0 ? 	//
				this.totalCount / this.pageSize + 1 : 			//
				this.totalCount / this.pageSize; 
		return this.totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
