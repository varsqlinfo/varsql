package com.varsql.core.changeset.data;

import java.util.Map;

import com.varsql.core.changeset.beans.ChangeLogDTO;

public interface ChangeSetData {
	/**
	 * 변경 이력 조회
	 * @return
	 * @throws Exception
	 */
	public Map history() throws Exception;
	
	/**
	 * 변경 이력 추가. 
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean addLog(ChangeLogDTO dto) throws Exception;
}
