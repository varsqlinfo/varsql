package com.varsql.core.common.beans;

public class FileInfo {
	private String name;
	private String path;
	private String ext;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public String toString() {
		return new StringBuffer()
				.append("name : ").append(name)
				.append(", ext : ").append(ext)
				.append(", path : ").append(path)
				.toString();
	}
}
