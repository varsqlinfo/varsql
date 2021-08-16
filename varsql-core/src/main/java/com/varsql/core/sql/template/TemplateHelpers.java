package com.varsql.core.sql.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.checkerframework.checker.units.qual.K;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.varsql.core.db.MetaControlFactory;
import com.varsql.core.db.meta.column.MetaColumnConstants;
import com.varsql.core.db.util.DbMetaUtils;
import com.varsql.core.db.valueobject.DataTypeInfo;
import com.vartech.common.app.beans.ParamMap;
import com.vartech.common.utils.StringUtils;

public enum TemplateHelpers implements Helper<Object> {

	/**
	 * Capitalizes the first character of the value. For example:
	 *
	 * <pre>
	 * {{capitalizeFirst value}}
	 * </pre>
	 *
	 * If value is "handlebars.java", the output will be "Handlebars.java".
	 */
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
			int idx = (int)context;
			String firstStr = options.param(0,"");
			String otherStr = options.param(1,"");

			return idx < 1 ? firstStr: otherStr;
		}
	}

	,ddlTableValue{

		@Override
		public Object apply(Object context, Options options) throws IOException {
			String mode = (String)context;
			Map item =options.param(0, new ParamMap());

			String dbType = options.param(2, "OTHER");

			String dataType = MapUtils.getString(item, MetaColumnConstants.DATA_TYPE);

			dataType = !StringUtils.isBlank(dataType)? dataType : (item.containsKey(MetaColumnConstants.TYPE_AND_LENGTH) ? MapUtils.getString(item, MetaColumnConstants.TYPE_AND_LENGTH) :"" );

			dataType = dataType.replaceAll("\\((.*?)\\)", "");

			DataTypeInfo dataTypeInfo = MetaControlFactory.getDbInstanceFactory(dbType).getDataTypeImpl().getDataType(dataType);

			if("typeAndLength".equals(mode)){

				if(item.containsKey(MetaColumnConstants.TYPE_AND_LENGTH)){
					Object val = item.get(MetaColumnConstants.TYPE_AND_LENGTH);

					if(val != null  && !"".equals(val)) {
						return String.valueOf(String.valueOf(item.get(MetaColumnConstants.TYPE_AND_LENGTH)));
					}
				}

				return DbMetaUtils.getTypeName(dataTypeInfo ,null ,dataTypeInfo.getDataTypeName(),MapUtils.getString(item,MetaColumnConstants.COLUMN_SIZE)
						, MapUtils.getString(item, MetaColumnConstants.DECIMAL_DIGITS));
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

			Map<String, Map<String,List>> reval = new HashMap<String, Map<String,List>>();

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
					reval.put(keyType,addItem);
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
