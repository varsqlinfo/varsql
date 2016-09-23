package com.varsql.web.util;

import java.util.HashMap;
import java.util.Map;

import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;

/**
 * 리스트 형태의 데이터를 여러페이지에 나타낼 경우 페이지 이동에 관련된 클래스
 * 
 */
public class PagingUtil {
	final static private int countPerPage = 10;	// 한 페이지에 출력할 갯수 지정 
	final static private int unitPage = 100;

	/**
	 * ColunmList 생성자 주석.
	 */
	public PagingUtil() {
	}

	/**
	 * PageControl을 설정한다.
	 */
	public static Map<String,String> getPageObject(int count, int cPage) {
		return getPageObject(count,cPage,countPerPage);
	}
	
	/**
	 * PageControl을 설정한다.
	 */
	public static Map<String,String> getPageObject(int count, DataCommonVO paramMap) {
		return getPageObject(count,paramMap.getInt(VarsqlParamConstants.SEARCH_FIRST),paramMap.getInt(VarsqlParamConstants.SEARCH_ROW));
	}
	public static Map<String,String>  getPageObject(int count, int cPage, int vPage) {
		return getPageObject(count,cPage,vPage,unitPage);
	}
	
	/**
	 * 전체 레코드의 갯수를 입력 받아 PageControl을 설정한다.
	 */
	public static Map<String,String>  getPageObject(int count, int cPage, int vPage, int uPage) {
		int totalCount=count; // 전체 데이터 row 개수 저장
		int currPage = cPage; // 현재 페이지 지정 ( 기본 : 1)
		int countPerPage = vPage; // 한 페이지에 출력할 갯수 지정
		int unitPage = uPage; // 페이지 단위
		int unitCount = 100; // 자료 표시 단위
		int totalPage; // 전체 페이지 수 저장
		int currStartCount; // 현재 화면에 표시되는 첫라인
		int currEndCount; // 현재 화면에 표시되는 마지막라인
		int prePage; // 이전 페이지
		boolean prePage_is; // 이전 페이지 존재 여부
		int currStartPage; // 페이지 번호 시작값
		int currEndPage; // 페이지 번호 마지막값
		int nextPage; // 다음 페이지
		boolean nextPage_is; // 이후 페이지 존재 여부
		int listBackPage; // 돌아갈 페이지
		boolean isFirst = false; // 처음인 경우는 totalCount를 구함.
		
		if (totalCount == 0) {
			countPerPage = unitCount;
		} else if (totalCount < countPerPage) {
			countPerPage = (totalCount / unitCount) * unitCount;
			if ((totalCount % unitCount) > 0) {
				countPerPage += unitCount;
			}
		}

		totalPage = getMaxNum(totalCount, countPerPage);

		if (totalPage < currPage) {
			currPage = totalPage;
		}

		if (currPage != 1) {
			currEndCount = currPage * countPerPage;
			currStartCount = currEndCount - countPerPage;
		} else {
			currEndCount = countPerPage;
			currStartCount = 0;
		}

		if (currEndCount > totalCount) {
			currEndCount = totalCount;
		}

		if (totalPage <= unitPage) {
			currEndPage = totalPage;
			currStartPage = 1;
		} else {
			currEndPage = ((currPage - 1) / unitPage) * unitPage + unitPage;
			currStartPage = currEndPage - unitPage + 1;
		}

		if (currEndPage > totalPage) {
			currEndPage = totalPage;
		}

		if (currStartPage != 1) {
			prePage_is = true;
			prePage = currStartPage - 1;
		} else {
			prePage_is = false;
			prePage = 0;
		}

		if (currEndPage != totalPage) {
			nextPage_is = true;
			nextPage = currEndPage + 1;
		} else {
			nextPage_is = false;
			nextPage = 0;
		}

		Map<String,String> pageInfo = new HashMap<String,String>();
		try {

			pageInfo.put("currPage", currPage + "");
			pageInfo.put("unitPage", unitPage + "");
			pageInfo.put("prePage", prePage + "");
			pageInfo.put("prePage_is", prePage_is + "");
			pageInfo.put("nextPage", nextPage + "");
			pageInfo.put("nextPage_is", nextPage_is + "");
			pageInfo.put("currStartPage", currStartPage + "");
			pageInfo.put("currEndPage", currEndPage + "");

			pageInfo.put("totalCount", totalCount + "");// 총문서수
			pageInfo.put("totalPage", totalPage + "");// 토탈페이지
		} catch (Exception e) {
		}

		return pageInfo;
	}

	private static int getMaxNum(int allPage, int list_num) {
		if ((allPage % list_num) == 0) {
			return allPage / list_num;
		} else {
			return allPage / list_num + 1;
		}
	}
}
