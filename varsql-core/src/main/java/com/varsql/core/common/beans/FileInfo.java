package com.varsql.core.common.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {
	private String name;
	private String saveName;
	private String path;
	private String ext;
	private long size;

	public String toString() {
		return new StringBuffer()
				.append("name : ").append(name)
				.append(", ext : ").append(ext)
				.append(", path : ").append(path)
				.toString();
	}
}
