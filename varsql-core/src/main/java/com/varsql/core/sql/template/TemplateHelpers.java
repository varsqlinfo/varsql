package com.varsql.core.sql.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.datatype.DataType;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.util.DbMetaUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.utils.StringUtils;

public enum TemplateHelpers implements Helper<Object> {

	ddlIndexKeyword {
		@Override
		public String apply(final Object context, final Options options) {
			String mode = (String)context;
			Map item =options.param(0, new HashMap());

			if("unique".equalsIgnoreCase(mode)) {
				return "UQ".equals(item.get("TYPE"))?"unique" :"";
			}

			if("ascDesc".equalsIgnoreCase(mode)) {
				return "ASC".equals(item.get("ASC_OR_DESC"))?"" :"desc";
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
			Map item =options.param(0, new DataMap());

			String dbType = options.param(2, "OTHER");

			String typeName = MapUtils.getString(item, MetaColumnConstants.TYPE_NAME);

			typeName = !StringUtils.isBlank(typeName)? typeName : (item.containsKey(MetaColumnConstants.TYPE_AND_LENGTH) ? MapUtils.getString(item, MetaColumnConstants.TYPE_AND_LENGTH) :"" );

			typeName = typeName.replaceAll("\\((.*?)\\)", "");

			DataType dataTypeInfo = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl().getDataType(typeName);

			if("typeAndLength".equals(mode)){

				if(item.containsKey(MetaColumnConstants.TYPE_AND_LENGTH)){
					Object val = item.get(MetaColumnConstants.TYPE_AND_LENGTH);

					if(val != null  && !"".equals(val)) {
						return String.valueOf(String.valueOf(item.get(MetaColumnConstants.TYPE_AND_LENGTH)));
					}
				}
				int columnSize = MapUtils.getIntValue(item, MetaColumnConstants.COLUMN_SIZE); 
				return dataTypeInfo.getJDBCDataTypeMetaInfo().getTypeAndLength(typeName, dataTypeInfo, null, columnSize,
						MapUtils.getIntValue(item, MetaColumnConstants.DATA_PRECISION, columnSize), MapUtils.getIntValue(item, MetaColumnConstants.DECIMAL_DIGITS));
			}

			if("default".equals(mode)){
				return DbMetaUtils.getDefaultValue(MapUtils.getString(item,MetaColumnConstants.COLUMN_DEF), dataTypeInfo, true);
			}

			if("nullable".equals(mode)){
				return DbMetaUtils.getNotNullValue(MapUtils.getString(item,MetaColumnConstants.NULLABLE));
			}

			return "";
		}

	}

	,ddlTableKey{
		
		
		
		
		
		
		
		                                                                

		@Override
		public Object apply(Object context, Options options) throws IOException {
			List list = (List)context;
			if(list.size() < 1){
				return options.inverse();
			}

			String objectName = options.param(0,"");

			Map<String, Map<String,List>> reval = new LinkedHashMap<String, Map<String,List>>();

			//[{"TABLE_NAME":"test_table","INDEX_TYPE":1,"COLUMN_NAME":"col1","CONSTRAINT_NAME":"test_table_20445312861",TYPE:"PK"}]

			for(int i =0; i< list.size(); i++){
				Map item = (Map)list.get(i);
				String keyType = MapUtils.getString(item, "TYPE");
				String constName = MapUtils.getString(item, "CONSTRAINT_NAME");
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
