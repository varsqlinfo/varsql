package com.varsql.core.sql.mapping;


import com.varsql.core.db.DBType;
import com.varsql.core.pattern.StringRegularUtils;
import com.varsql.core.sql.type.SQLDataType;

public class ParameterMapping {
	private String property;
	private SQLDataType dataType;
	private boolean isFunction;
	private String functionName;
	private String[] functionParam;
	private ParameterMode mode;

	private ParameterMapping() {
	}

	public String getProperty() {
		return property;
	}

	public SQLDataType getDataType() {
		return dataType;
	}

	public boolean isFunction() {
		return isFunction;
	}

	public String getFunctionName() {
		return functionName;
	}

	public String[] getFunctionParam() {
		return functionParam;
	}

	public ParameterMode getMode() {
		return mode;
	}

	public static class Builder {
		private ParameterMapping parameterMapping;
		private DBType dbType;

		@SuppressWarnings("unused")
		private Builder() {
		};

		public Builder(DBType dbType, String property) {
			this.parameterMapping = new ParameterMapping();
			this.dbType = dbType;

			parseProperty(property);
		}

		private void parseProperty(String property) {
			property = StringRegularUtils.allTrim(property);
			int fnStartIdx = property.indexOf("fn=");

			if (fnStartIdx > -1) {
				int closeIdx = property.indexOf(')', fnStartIdx);

				String fnVal;
				if (closeIdx > -1) {
					fnVal = property.substring(fnStartIdx, closeIdx + 1);
				} else {
					closeIdx = property.indexOf(',', fnStartIdx);
					fnVal = property.substring(fnStartIdx, closeIdx > -1 ? closeIdx + 1 : property.length());
				}

				if (!"".equals(fnVal)) {
					getFunctionVal(property, fnVal.replace("fn=", ""), fnVal.split(":")[1]);
					property = property.replace(fnVal, "");
				}
			}

			if (!"".equals(property)) {

				String[] propertyArr = property.split(",");


				int len = propertyArr.length;
				for (int i = 0; i < len; i++) {
					String propertyVal = propertyArr[i];
					String[] propSplitArr = propertyVal.split("=");
					String key = propSplitArr[0];
					if (propSplitArr.length > 1) {
						String val = propSplitArr[1];
						if ("dataType".equalsIgnoreCase(key)) {
							this.parameterMapping.dataType = SQLDataType.getSQLDataType(this.dbType, val);
						}else if ("mode".equalsIgnoreCase(key)) {
							this.parameterMapping.mode = ParameterMode.getParameterMode(val);
						}
					}else {
						this.parameterMapping.property = propSplitArr[0];
					}
				}

			}
		}

		private void getFunctionVal(String ogrinStr, String propVal, String propKeyVal) {
			this.parameterMapping.isFunction = true;
			int openIdx = propVal.indexOf('(');
			int closeIdx = propVal.indexOf(')');

			if (openIdx > closeIdx) {
				throw new ParameterParseException(String.format("function not valid : %s ", ogrinStr));
			}

			if (openIdx > -1) {
				this.parameterMapping.functionName = propVal.substring(0, openIdx);
				this.parameterMapping.functionParam = propVal.substring(openIdx).replaceAll("[()]", "").split(",");
				;
			} else {
				this.parameterMapping.functionName = propVal.substring(0, propVal.length());
				this.parameterMapping.functionParam = null;
			}

		}

		public ParameterMapping build() {
			return this.parameterMapping;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());

		sb.append(" property='").append(this.property).append('\'');
		sb.append(", mode='").append(this.mode).append('\'');
		sb.append(", dataType='").append(this.dataType).append('\'');
		sb.append(", isFunction='").append(isFunction).append('\'');
		sb.append(", functionName='").append(this.functionName).append('\'');
		sb.append(", functionParam='").append(this.functionParam).append('\'');
		return sb.toString();
	}

}