/**
handlebars custom function
 */

if (typeof window != "undefined") {
    if (typeof window.VARSQLTemplate == "undefined") {
        window.VARSQLTemplate = {};
    }
}else{
	if(!VARSQLTemplate){
		VARSQLTemplate = {};
	}
}


;(function(Handlebars, VARSQL) {
	"use strict";

function isPk(constraintVal){
	if(constraintVal =='PK' || constraintVal.indexOf('PRIMARY') > -1 ){
		return true;
	}
	return false;
}

Handlebars.registerHelper("camelCase", function(text, options) {
    return VARSQL.util.convertCamel(text);
});

Handlebars.registerHelper("upperCase", function(text, options) {
    return VARSQL.util.toUpperCase(text);
});

Handlebars.registerHelper("lowerCase", function(text, options) {
    return VARSQL.util.capitalize(text);
});

Handlebars.registerHelper("capitalize", function(text, options) {
    return  VARSQL.util.capitalize(text);;
});

Handlebars.registerHelper("javaType", function(dbType, options) {
	var tmpDbType = VARSQLCont.dataType.getDataTypeInfo(dbType)
	return tmpDbType.javaType;
});

Handlebars.registerHelper("addChar", function(idx, firstStr, otherStr, options) {
	return idx < 1 ? firstStr : otherStr;
});

Handlebars.registerHelper("addPreSuffix", function(prefix, suffix , text) {
	return prefix + text + suffix;
});

Handlebars.registerHelper("isPk", function(item) {
	var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS];
	return isPk(constraintVal) ;
});

Handlebars.registerHelper("pkColumns", function(items) {
	var reval = [];

	var constraintsKey = VARSQLCont.tableColKey.CONSTRAINTS;
	for(var i =0 ; i<items.length;i++){
		var item = items[i];
		var constraintVal = item[constraintsKey];
		if(isPk(constraintVal) ){
			reval.push(item);
		}
	}
	return reval;
});

Handlebars.registerHelper("pkExcludeColumns", function(items) {
	var reval = [];

	var constraintsKey = VARSQLCont.tableColKey.CONSTRAINTS;
	for(var i =0 ; i<items.length;i++){
		var item = items[i];
		var constraintVal = item[constraintsKey];
		if(!isPk(constraintVal)){
			reval.push(item);
		}
	}

	return reval;
});

Handlebars.registerHelper('xif', function (v1,o1,v2,options) {
    var operators = {
         '==': function(a, b){ return a==b},
         '===': function(a, b){ return a===b},
         '!=': function(a, b){ return a!=b},
         '!==': function(a, b){ return a!==b},
         '<': function(a, b){ return a<b},
         '<=': function(a, b){ return a<=b},
         '>': function(a, b){ return a>b},
         '>=': function(a, b){ return a>=b},
         '&&': function(a, b){ return a&&b},
         '||': function(a, b){ return a||b},
      }
    var isTrue = operators[o1](v1,v2);
    return isTrue ? options.fn(this) : options.inverse(this);
});

// index ddl gen keyword
Handlebars.registerHelper('ddlIndexKeyword', function (mode , item) {
	if('unique' == mode){
		return "UQ" == item["TYPE"] ? "unique" : "";
	}

	if('ascDesc' == mode){
		return "ASC" == item["ASC_OR_DESC"] ? "" : "desc";
	}

	return '';
});

Handlebars.registerHelper('ddlTableValue', function (mode, item, dbType) {
	var dataType = item.DATA_TYPE || item.TYPE_AND_LENGTH;

	dataType = dataType.replace(/\((.*?)\)/g,'');
	var dataTypeInfo = VARSQLCont.dataType.getDataTypeInfo(dataType);

	if('typeAndLength' == mode){
		if(!VARSQL.isBlank(item.TYPE_AND_LENGTH)){
			return item.TYPE_AND_LENGTH;
		}
		var columnSize = item.COLUMN_SIZE;

		if(dataTypeInfo.isSize !== false && !VARSQL.isBlank(columnSize)){

			var addStr =dataTypeInfo.name+"(" + columnSize;

			if(dataTypeInfo.isNum){
				var degitsLen = item.DECIMAL_DIGITS;
				if (!VARSQL.isBlank(degitsLen) && parseInt(degitsLen,10)  > 0) {
					addStr +="," + degitsLen;
				}
			}

			addStr+=")";
			return addStr;
		}

		return dataTypeInfo.name;
	}

	if('default' == mode){
		var columnDef = item.COLUMN_DEF;
		if (!VARSQL.isBlank(columnDef)) {

			if(columnDef.startsWith('DEFAULT')){
				return columnDef;
			}else{
				if (dataTypeInfo.isNum || dataTypeInfo.isDate || VARSQL.startsWith(columnDef,'\'')){
					return "DEFAULT " +columnDef;
				}

				return "DEFAULT '"+columnDef+"'";
			}
		}
		return "";
	}

	if('nullable' == mode){
		var nullable = (item.NULLABLE||'').toUpperCase();
		if ("NO" == nullable || "N" == nullable) {
			return " NOT NULL ";
		}
		return "";
	}

	return '';
});

Handlebars.registerHelper('ddlTableKey', function (list, objectName, dbType, opts) {

	if(list.length < 1){
		return opts.inverse(this);
	}
	var reval = {};

	//[{"TABLE_NAME":"test_table","INDEX_TYPE":1,"COLUMN_NAME":"col1","CONSTRAINT_NAME":"test_table_20445312861",TYPE:"PK"}]

	for(var i =0; i< list.length; i++){
		var item =list[i];
		var keyType = item.TYPE;
		if(reval[keyType]){
			if(reval[keyType][item.CONSTRAINT_NAME]){
				reval[keyType][item.CONSTRAINT_NAME].push(item);
			}else{
				reval[keyType][item.CONSTRAINT_NAME] = [item];
			}
		}else{
			var addItem = {};
			addItem[item.CONSTRAINT_NAME]=[item];
			reval[keyType]=addItem;
		}
	}

	var out= [];
	for(var key in reval){
		out.push(opts.fn({type :key, dbType: dbType, objectName : objectName, constList: reval[key]}));
	}

	return out.join('');
});


VARSQLTemplate.parse = function (template, errorHandler){
	try{
		return Handlebars.parse(template);
    }catch(e){
    	if(errorHandler){
    		errorHandler(e);
    	}
    	return false;
    }
}

VARSQLTemplate.compile = function (template ,errorHandler){
	try{
		return Handlebars.compile(template);
    }catch(e){
    	if(errorHandler){
    		errorHandler(e);
    	}
    	return false;
    }

}

VARSQLTemplate.render ={
	/**
	 * @method html
	 * @description html template render
	 */
	html : function (template , item , errorHandler){
		try{
			var template = Handlebars.compile(template);
	        return template(item);
        }catch(e){
        	if(errorHandler){
        		errorHandler(e);
        	}
        	return e.message;
        }
	}
	/**
	 * @method text
	 * @description text template render
	 */
	,text : function (template , item , errorHandler){
		try{
			var template = Handlebars.compile(template);
	        return template(item);
        }catch(e){
        	if(errorHandler){
        		errorHandler(e);
        	}
        	return e.message;
        }
	}
	/**
	 * @method generateSource
	 * @description template object render
		templateInfo =
		main: "insert into {{table.name}} ({{columnName}} )↵values( {{columnValue}} );"
		,name: "insert"
		,propItems: [
			code: "{{~#columns~}}	{{addChar @index ','}}{{name}} {{/columns}}"
			,compileValue: null
			,key: "columnName"
		]
	 */
	,generateSource : function (templateInfo, item, breakFlag){

		var reVal = {isError : false, value :'' , errorInfo :{}};
        try{
        	var template = VARSQLTemplate.compile(templateInfo.main);

	        var propItems = templateInfo.propItems;

	        var allParam = VARSQL.util.objectMerge({},item);

	        for(var i=0; i< propItems.length; i++){
	            var propItem = propItems[i];
	            try{
	            	var propTemplate = Handlebars.compile(propItem.code);
	            	allParam[propItem.key] =VARSQL.str.trim(propTemplate(allParam));
	            }catch(e){
	            	if(breakFlag === false){
	            		allParam[propItem.key] = e.message;
	            	}else{
		            	reVal.isError = true;
		            	reVal.errorInfo ={
		            		mode : 'prop'
		            		,msg : e.message
		            		,err : e
		            	};
		            	return reVal;
	            	}
	            }
	        }
	        reVal.value = VARSQL.str.trim(template(allParam));
	        return reVal;
        }catch(e){
        	reVal.isError = true;
        	reVal.errorInfo ={
        		mode : 'main'
    			,msg : e.message
        		,err : e
        	};
        	return reVal;
        }
	}
}


}(Handlebars, VARSQL));