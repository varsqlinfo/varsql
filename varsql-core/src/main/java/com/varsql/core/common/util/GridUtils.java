package com.varsql.core.common.util;

public class GridUtils {
	private final static String [] KEYS= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private final static int KEYLEN= KEYS.length;

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
}
