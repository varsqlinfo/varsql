package com.varsql.core.db.meta;

import com.vartech.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class DBVersionInfo {
	private String version;
	private boolean defultFlag;
	
	private int major;
	private int minor;
	private int patch;
	
	public static DBVersionInfoBuilder builder(int major, int minor, int patch) {
        return new CustomDBVersionInfoBuilder().major(major).minor(minor).patch(patch);
    }
		
	private static class CustomDBVersionInfoBuilder extends DBVersionInfoBuilder {

		@Override
        public DBVersionInfo build() {
        	
        	if(StringUtils.isBlank(super.version)) {
        		version(String.format("%d.%d.%d", super.major, super.minor, super.patch));
        	}
           
            return super.build();
        }
    }
	
}