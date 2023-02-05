package com.varsql.core.sql.builder;

import java.util.List;
import java.util.Map;

import com.varsql.core.sql.beans.GridColumnInfo;


public class SqlSourceResultVO {
	
	private String viewType;
	private String resultType;
	private long starttime;
	private long endtime;
	private long delay;
	private List<?> data;
	// return 컬럼 값
	private List<GridColumnInfo> column;
	// return result 
	private long resultCnt;
	// 쿼리 total count 
	private long totalCnt;
	// 페이징 정보. 
	private Map pagingInfo;
	
	public long getResultCnt() {
		return resultCnt;
	}

	public void setResultCnt(long resultCnt) {
		this.resultCnt = resultCnt;
	}

	private List<Boolean> numberTypeFlag;
	private String resultMessage;
	
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public List<GridColumnInfo> getColumn() {
		return column;
	}

	public void setColumn(List<GridColumnInfo> column) {
		this.column = column;
	}
	
	public List<Boolean> getNumberTypeFlag() {
		return numberTypeFlag;
	}

	public void setNumberTypeFlag(List<Boolean> numberTypeFlag) {
		this.numberTypeFlag = numberTypeFlag;
	}
	
	public long getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	public Map getPagingInfo() {
		return pagingInfo;
	}

	public void setPagingInfo(Map pagingInfo) {
		this.pagingInfo = pagingInfo;
	}

	@Override
	public String toString() {
		return new StringBuffer()
			.append("viewType : [").append(viewType)
			.append("] resultType : [").append(resultType)
			.append("] starttime : [").append( starttime)
			.append("] totalCnt : [").append( totalCnt)
			.append("] endtime : [").append(endtime)
			.append("] delay : [").append(delay)
			.append("] gridData : [").append(data)
			.append("] gridColumn : [").append(column)
			.append("] pagingInfo : [").append(pagingInfo)
			.append("] numberTypeFlag : [").append(numberTypeFlag)
			.append("]")
			.toString();
	}

	

	
}

