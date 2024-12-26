package com.varsql.core.sql.template;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.util.DataTypeUtils;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.ColumnInfo;
import com.varsql.core.db.valueobject.ConstraintInfo;
import com.varsql.core.sql.ConstraintType;
import com.vartech.common.utils.StringUtils;

public enum TemplateHelpers implements Helper<Object> {

	ddlIndexKeyword {
		@Override
		public String apply(final Object context, final Options options) {
			String mode = (String)context;
			Map item =options.param(0, new HashMap());

			if("unique".equalsIgnoreCase(mode)) {
				return ConstraintType.UNIQUE.typeEquals(String.valueOf(item.get("type")))?"unique" :"";
			}

			if("ascDesc".equalsIgnoreCase(mode)) {
				return "ASC".equalsIgnoreCase(String.valueOf(item.get("ascOrDesc")))?"" :"desc";
			}
			
			return "";
		}
	}
	,addChar {
		@Override
		public String apply(final Object context, final Options options) {


			String firstStr = options.param(0,"");
			String otherStr = options.param(1,"");

			if(context instanceof Boolean) {
				if(((Boolean) context).booleanValue()  == true) {
					return firstStr;
				}else {
					return otherStr;
				}
			}else {
				int idx = (int)context;
				return idx < 1 ? firstStr: otherStr;
			}
		}
	}

	,ddlTableValue{

		@Override
		public Object apply(Object context, Options options) throws IOException {
			String mode = (String)context;
			ColumnInfo item =options.param(0, new ColumnInfo());

			String dbType = options.param(1, "OTHER");

			String typeName = item.getTypeName();
			
			DataType dataTypeInfo = DataTypeUtils.getDataType(typeName, dbType, item);
			
			if(StringUtils.isBlank(typeName)) {
				typeName = dataTypeInfo.getTypeName();
			}
				
			if("typeAndLength".equals(mode)){

				if(!StringUtils.isBlank(item.getTypeAndLength())){
					return item.getTypeAndLength();
				}
				
				BigDecimal columnSize = item.getLength(); 
				return dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(typeName, dataTypeInfo, null, (columnSize == null? -1 : columnSize.longValue()),
						item.getDataPrecision(), item.getDecimalDigits());
			}

			if("default".equals(mode)){
				return DbMetaUtils.getDefaultValue(item.getDefaultVal(), dataTypeInfo, true);
			}

			if("nullable".equals(mode)){
				return DbMetaUtils.getNotNullValue(item.getNullable());
			}
			return "";
		}
	}

	,ddlTableKey{
		
		@Override
		public Object apply(Object context, Options options) throws IOException {
			List<ConstraintInfo> list = (List)context;
			if(list.size() < 1){
				return options.inverse();
			}

			String objectName = options.param(0,"");

			Map<String, Map<String,List>> reval = new LinkedHashMap<String, Map<String,List>>();

			for(int i =0; i< list.size(); i++){
				ConstraintInfo item = list.get(i);
				String keyType = item.getType();
				String constName = item.getConstraintName();
				if(reval.containsKey(keyType)){
					if(!reval.get(keyType).containsKey(constName)){
						reval.get(keyType).put(constName, new ArrayList());
					}

					reval.get(keyType).get(constName).add(item);
				}else{
					Map<String,List> addItem = new HashMap<String,List>();
					addItem.put(constName,new ArrayList());
					addItem.get(constName).add(item);
					reval.put(keyType, addItem);
				}
			}

			StringBuffer sb= new StringBuffer();
			for(String key : reval.keySet()){
				Map param = new HashMap();
				param.put("type", key);
				param.put("objectName", objectName);
				param.put("constList", reval.get(key));
				sb.append(options.fn(param));
			}

			return sb.toString();
		}

	}

}
