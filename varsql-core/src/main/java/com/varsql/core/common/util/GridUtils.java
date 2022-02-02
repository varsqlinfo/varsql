package com.varsql.core.common.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.varsql.core.sql.beans.GridColumnInfo;

/**
 * -----------------------------------------------------------------------------
* @fileName		: GridUtils.java
* @desc		: grid key util
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2021. 1. 3. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class GridUtils {
	private final static String [] KEYS= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private final static int KEYLEN= KEYS.length;

	/**
	 * @method  : getAliasKeyArr
	 * @desc : get column alias
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param len
	 * @return
	 */
	public static String[] getAliasKeyArr(int len){
		String [] reval = new String[len];
		for (int i = 0; i < len; i++) {
			reval[i] = getAliasKey(i);
		}

		return reval;
	}

	public static String getAliasKey(int idx){
		 if(idx < KEYLEN) {
			 return KEYS[idx];
		 }else {
			 StringBuffer sb = new StringBuffer();
			 getKeyArr(sb , idx);
			 return sb.toString();
		 }
	}

	private static void getKeyArr(StringBuffer sb, int idx) {
		if(idx+1 > KEYLEN) {
			int prefix = (idx / KEYLEN);
			int b = (idx % KEYLEN);

			if(prefix > KEYLEN) {
				getKeyArr(sb, prefix-1);
			}else {
				prefix = prefix > 0 ? prefix -1 : 0;
				sb.append(KEYS[prefix]);
			}

			sb.append(KEYS[b]);

			return ;
		}

		sb.append(KEYS[idx]);
	}

	/**
	 * @method  : getKeyMap
	 * @desc : grid column key map
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param columnInfos
	 * @return
	 */
	public static Map<String, GridColumnInfo> getKeyMap(List<GridColumnInfo> columnInfos) {
		Map<String,GridColumnInfo> columnKeyLabel = new LinkedHashMap<>();
		columnInfos.stream().forEach(item ->{
			columnKeyLabel.put(item.getKey(), item);
		});
		return columnKeyLabel;
	}

	/**
	 * @method  : getColumnKeyLabelInfo
	 * @desc : get grid column key -> label
	 * @author   : ytkim
	 * @date   : 2021. 1. 3.
	 * @param fieldName
	 * @param columnKeyLabel
	 * @return
	 */
	public static GridColumnInfo getGridInfoForKey(String fieldName, Map<String, GridColumnInfo> columnKeyLabel) {
		if(columnKeyLabel.containsKey(fieldName)) {
			return columnKeyLabel.get(fieldName);
		}else {
			return null;
		}
	}
}
