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

/**
 * @namespace SampleData
  @example
{
  "table": {
    "name": "test_table",
    "remarks": "test table"
  },
  "columns": [
    {
      "no": 0,
      "name": "test_id",
      "dataType": "STRING",
      "typeName": "VARCHAR",
      "typeAndLength": "VARCHAR(32)",
      "length": 32,
      "nullable": "N",
      "comment": "",
      "constraints": "PK",
      "autoincrement": null,
      "defaultVal": ""
    },
    {
      "no": 0,
      "name": "link_id",
      "dataType": "STRING",
      "typeName": "VARCHAR",
      "typeAndLength": "VARCHAR(32)",
      "length": 32,
      "nullable": "N",
      "comment": "",
      "constraints": "PK",
      "autoincrement": null,
      "defaultVal": ""
    },
    {
      "no": 0,
      "name": "open_type",
      "dataType": "CHAR",
      "typeName": "CHAR",
      "typeAndLength": "CHAR(1)",
      "length": 1,
      "nullable": "Y",
      "comment": "",
      "constraints": "",
      "autoincrement": null,
      "defaultVal": ""
    },
    {
      "no": 0,
      "name": "open_option",
      "dataType": "STRING",
      "typeName": "VARCHAR",
      "typeAndLength": "VARCHAR(1000)",
      "length": 1000,
      "nullable": "Y",
      "comment": "",
      "constraints": "",
      "autoincrement": null,
      "defaultVal": ""
    }
  ]
}
 */


/**
 * 문자를 casemel case 변환 TEXT_WORD -> textWord
 * @namespace MyNamespace
 * @method camelCase
 * @param {String} text
 * @param {options} options
 * @returns {String}
 * @example
 */
Handlebars.registerHelper("camelCase", function(text, options) {
    return VARSQL.util.convertCamel(text);
});

/**
 * 문자를 upper case 변환 text_word -> TEXT_WORD
 * @method upperCase
 * @param {String} text
 * @param {options} options
 * @returns {String}
 */
Handlebars.registerHelper("upperCase", function(text, options) {
    return VARSQL.util.toUpperCase(text);
});

/**
 * 문자를 lower case 변환 TEXT_WORD -> text_word
 * @method lowerCase
 * @param {String} text
 * @param {options} options
 * @returns {String}
 */
Handlebars.registerHelper("lowerCase", function(text, options) {
    return VARSQL.util.capitalize(text);
});

/**
* 첫번째 문자를 대문자로 변환 TEXT_WORD -> Text_word
 * @method capitalize
 * @param {String} text
 * @param {options} options
 * @returns {String}
 */
Handlebars.registerHelper("capitalize", function(text, options) {
    return  VARSQL.util.capitalize(text);;
});

/**
 * Database 컬럼 타입을 Java Type으로 변환
 * @method javaType
 * @param {String} dbType
 * @param {options} options
 * @returns {String}
 */
Handlebars.registerHelper("javaType", function(dbDataType, options) {
	var tmpDbType = VARSQLCont.getDataTypeInfo(dbDataType)
	return tmpDbType.javaType;
});

/**
 * 문자를 추가
 * @method addChar
 * @param {Boolean, Number} booleanVal 	// condition
 * @param {String} firstStr	// true string
 * @param {String} otherStr	// false string
 * @returns {String}
 */
Handlebars.registerHelper("addChar", function(condVal, firstStr, otherStr, options) {
	if(condVal === true){
		return firstStr;
	}else if(condVal === false){
		return otherStr;
	}else{
		return condVal < 1 ? firstStr : otherStr;
	}
});

/**
 * 문자를 앞뒤에 추가
 * @method addPreSuffix
 * @param {String} dbType
 * @param {options} options
 * @returns {String}
 */
Handlebars.registerHelper("addPreSuffix", function(prefix, suffix , text) {
	return prefix + text + suffix;
});

/**
 * 컬럼 pk 여부
 * @method isPk
 * @param {Object} item
 * @returns {Boolean}
 */
Handlebars.registerHelper("isPk", function(item) {
	var constraintVal = item[VARSQLCont.tableColKey.CONSTRAINTS];
	return isPk(constraintVal) ;
});

/**
 * pk 컬럼 구하기
 * @method pkColumns
 * @param {Object} items
 * @returns {Array}
 */
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

/**
 * pk 컬럼을 제외한 컬럼 구하기
 * @method pkExcludeColumns
 * @param {Object} items
 * @returns {Array}
 */
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

/**
 * 로직 처리
 * @method xif
 * @param {Object} v1  첫번째 인수
 * @param {Object} o1  operation
 * @param {Object} v2  두번째 인수
 * @param {Object} options
 * @returns {Boolean}
 */
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

/**
 * ddl 문 생성시 키워드
 * @method ddlIndexKeyword
 * @param {String} mode 첫번째 인수
 * @param {Object} item operation
 * @returns {String}
 */
Handlebars.registerHelper('ddlIndexKeyword', function (mode , item) {
	if('unique' == mode){
		return "UQ" == item["TYPE"] ? "unique" : "";
	}

	if('ascDesc' == mode){
		return "ASC" == item["ASC_OR_DESC"] ? "" : "desc";
	}

	return '';
});

/**
 * ddl 생성시 체크 문자
 * @function ddlTableValue
 * @param {String} mode 첫번째 인수
 * @param {Object} item operation
 * @param {String} dbType operation
 * @returns {String}
 */
Handlebars.registerHelper('ddlTableValue', function (mode, item, dbType) {
	var dataType = item.DATA_TYPE || item.TYPE_AND_LENGTH;

	dataType = dataType.replace(/\((.*?)\)/g,'');
	var dataTypeInfo = VARSQLCont.getDataTypeInfo(dataType);

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

/**
 * ddl 테이블 키
 * @method ddlTableKey
 * @param {Array} list 첫번째 인수
 * @param {String} objectName operation
 * @param {String} dbType operation
 * @param {Object} opts operation
 * @returns {String}
 */
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
	html : function (template, item, errorHandler){
		try{
			var template = Handlebars.compile(template);
	        var _ns = item['$namespace$'] || ''; 

			delete item['$namespace$'];
			return template(item , {data : {'namespace' : _ns} });
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
	,text : function (template, item, errorHandler){
		try{
			var template = Handlebars.compile(template, {noEscape:true});
			var _ns = item['$namespace$'] || ''; 

			delete item['$namespace$'];
			return template(item , {data : {'namespace' : _ns} });
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
		main: "insert into {{table.name}} ({{columnName}} )?values( {{columnValue}} );"
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