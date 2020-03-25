package com.varsql.core.sql.mapping;

import org.apache.ibatis.type.JdbcType;

import com.varsql.core.sql.util.StringRegularUtils;

public class ParameterMapping {
	private String property;
	private JdbcType jdbcType;
	private boolean isFunction;
	private String functionName;
	private String[] functionParam;

	private ParameterMapping() {
	}

	public String getProperty() {
		return property;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
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

	public static class Builder {
		private ParameterMapping parameterMapping;

		@SuppressWarnings("unused")
		private Builder() {
		};

		public Builder(String property) {
			this.parameterMapping = new ParameterMapping();

			parseProperty(property);
		}

		private void parseProperty(String property) {
			property = StringRegularUtils.allTrim(property);
			int fnStartIdx = property.indexOf("fn:");

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
					getFunctionVal(property, fnVal.replace("fn:", ""), fnVal.split(":")[1]);
					property = property.replace(fnVal, "");
				}
			}

			if (!"".equals(property)) {

				int startIdx = property.indexOf(',');
				if (startIdx > -1) {
					String[] propertyArr = property.split(",");
					int len = propertyArr.length;
					for (int i = 0; i < len; i++) {
						String propKeyVal = propertyArr[i];
						if (propKeyVal.indexOf(':') > -1) {
							String[] propKeyValArr = propKeyVal.split(":");
							String propVal = propKeyValArr[1];
							if (propKeyVal.startsWith("jdbcType:")) {
								this.parameterMapping.jdbcType = JdbcType.valueOf(propVal);
							}
						} else {
							this.parameterMapping.property = propKeyVal;
						}
					}
				} else {
					this.parameterMapping.property = property;
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
		sb.append(", jdbcType='").append(this.jdbcType).append('\'');
		sb.append(", isFunction='").append(isFunction).append('\'');
		sb.append(", functionName='").append(this.functionName).append('\'');
		sb.append(", functionParam='").append(this.functionParam).append('\'');
		return sb.toString();
	}

}
