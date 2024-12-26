package com.varsql.core.task.transfer.writer;

import java.sql.SQLException;
import java.util.Map;

public interface TargetWriter {
	public void update(Map data) throws SQLException;
}
