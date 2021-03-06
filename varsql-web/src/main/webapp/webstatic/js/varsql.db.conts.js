/*
**
*ytkim
*varsql base object js
 */

if (typeof window != "undefined") {
    if (typeof window.VARSQLCont == "undefined") {
        window.VARSQLCont = {};
    }
}else{
	if(!VARSQLCont){
		VARSQLCont = {};
	}
}


;(function(VARSQLCont) {
"use strict";

function setHints(str) {
	return str.split(",");
}

var _constants = {
	newline :'\n'
	,tab : '\t'
	,querySuffix : ';  '
	,queryParameterPrefix:'#{'
	,queryParameterSuffix:'}'
};

var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '', javaType:'char'}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0, javaType:'int'}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0, javaType:'int'}
	,'4':{name : 'INTEGER',isNum : true ,val : 0, javaType:'int'}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0, javaType:'long'}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0, javaType:'float'}
	,'7':{name : 'REAL',isNum : true ,val : 0, javaType:'Integer'}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0, javaType:'double'}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0, javaType:'int'}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0, javaType:'int'}
	,'1':{name : 'CHAR',isNum : false ,val : '', javaType:'char'}
	,'12':{name : 'VARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'91':{name : 'DATE',isNum : false, isDate : true, val : 'current_date', javaType:'String'}
	,'92':{name : 'TIME',isNum : false, isDate : true, val : 'current_time', javaType:'String'}
	,'93':{name : 'TIMESTAMP',isNum : false, isDate : true, val : 'current_timestamp', javaType:'String'}
	,'-2':{name : 'BINARY',isNum : false ,val : '', javaType:'String'}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '', javaType:'String'}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '', javaType:'String'}
	,'0':{name : 'NULL',isNum : false ,val : '', javaType:'String'}
	,'1111':{name : 'NVARCHAR2',isNum : false ,val : '', javaType:'String' , getLen : function (len){
		return parseInt(len /2,10);
	}}
	//,'1111':{name : 'OTHER',isNum : false ,val : '', javaType:'Object'}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '', javaType:'Object'}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '', javaType:'Object'}
	,'2002':{name : 'STRUCT',isNum : false ,val : '', javaType:'Object'}
	,'2003':{name : 'ARRAY',isNum : false ,val : '', javaType:'Object'}
	,'2004':{name : 'BLOB',isNum : false ,val : '', javaType:'Object'}
	,'2005':{name : 'CLOB',isNum : false ,val : '', javaType:'String'}
	,'2006':{name : 'REF',isNum : false ,val : '', javaType:'Object'}
	,'70':{name : 'DATALINK',isNum : false ,val : '', javaType:'Object'}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '', javaType:'boolean'}
	,'-8':{name : 'ROWID',isNum : false ,val : '', javaType:'Object'}
	,'-15':{name : 'NCHAR',isNum : false ,val : '', javaType:'String'}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'2011':{name : 'NCLOB',isNum : false ,val : '', javaType:'String'}
	,'2009':{name : 'SQLXML',isNum : false ,val : '', javaType:'String'}
	,'9999':{name : 'OTHER',isNum : false ,val : '', javaType:'Object'}
};

var DEFAULT_HINTS = [
	'BIT'
	,'TINYINT'
	,'SMALLINT'
	,'INTEGER'
	,'BIGINT'
	,'FLOAT'
	,'REAL'
	,'DOUBLE'
	,'NUMERIC'
	,'DECIMAL'
	,'CHAR'
	,'VARCHAR'
	,'LONGVARCHAR'
	,'DATE'
	,'TIME'
	,'TIMESTAMP'
	,'BINARY'
	,'VARBINARY'
	,'LONGVARBINARY'
	,'BLOB'
	,'CLOB'
	,'CURRENT_TIMESTAMP'
];

var TABLE_COL_KEYS ={
	NAME :'name'
	,TYPE_NAME :'typeName'
	,DATA_TYPE :'dataType'
	,NULLABLE :'nullable'
	,PRIMAY_KEY :'primayKey'
	,CONSTRAINTS :'constraints'
	,COMMENT :'comment'
	,SIZE :'length'
}

// db 비교 키 값.
var COMPARE_COL_KEY = ['name','typeAndLength','constraints','defaultVal','nullable','comment'];

var DEFINE_INFO = {
	MARIADB : {
		type :'text/x-mariadb'
	}
	,MSSQL : {
		type :'text/x-mssql'
	}
	,MYSQL : {
		type :'text/x-mssql'
	}
	,ORACLE : {
		type : 'text/x-plsql'
		,hint : setHints('VARCHAR2,NVARCHAR2,NCHAR2')
		,setDataType :  function (pdto){
			pdto['VARCHAR2'] = VARSQL.util.objectMerge({},pdto['12'],{name:'VARCHAR2'});
			pdto['NVARCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NUMBER'] = VARSQL.util.objectMerge({},pdto['4'],{name:'NUMBER'});
			pdto['91'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdatte'});
			pdto['92'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdatte'});

			return pdto;
		}
	}
	,TIBERO :{
		type : 'text/x-plsql'
		,hint : setHints('VARCHAR2,NVARCHAR2,NCHAR2,NUMBER')
		,setDataType :  function (pdto){
			pdto['VARCHAR2'] = VARSQL.util.objectMerge({},pdto['12'],{name:'VARCHAR2'});
			pdto['NVARCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NCHAR2'] = VARSQL.util.objectMerge({},pdto['1111'],{name:'NVARCHAR2'});
			pdto['NUMBER'] = VARSQL.util.objectMerge({},pdto['4'],{name:'NUMBER'});
			pdto['91'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdatte'});
			pdto['92'] = VARSQL.util.objectMerge(pdto['91'],{val:'sysdatte'});

			return pdto;
		}
	}
	,H2 : {
		type : 'text/x-mariadb'
	}
	,'DEFAULT' : {
		type :  'text/x-sql',hint : [] ,dataType:{}
	}
}
var defaultInfo = DEFINE_INFO['DEFAULT'];

// vendor 정보 얻기.
function getDBTypeObj (dbType , field){
	var dbTypeInfo = DEFINE_INFO[dbType] || defaultInfo;

	if(typeof field ==='undefined') {
		return dbTypeInfo;
	}else{
		return dbTypeInfo[field] || defaultInfo.field;
	}
}

// set db type
function setDtoInfo (dbType , pdto){
	var dbTypeInfo = getDBTypeObj (dbType);
	try{
		return dbTypeInfo.setDataType(pdto);
	}catch(e){}
}

// db data typename 을 키로 셋팅.
function setNameKeyMapping (pdto){
	for(var key  in pdto){
		var item = pdto[key];
		pdto[item.name] =item;
	}
	return pdto;
}

var dataType = {};

dataType.getDataTypeInfo = function (dataType){
	var tmpDataType= _dto[dataType];

	if(typeof  tmpDataType !=='undefined'){
		return  tmpDataType;
	}else{
		return _dto['9999'] ;
	}
}

VARSQLCont.allDataType = function (){
	var result =[];
	var dupChk ={};
	for(var key in _dto){
		var item = _dto[key];

		if(!dupChk[item.name]){
			result.push({
				type : item.name
				,name : item.name
			})
			dupChk[item.name] = true;
		}
	}

	return result;
}

VARSQLCont.init  = function (dbType, uiBase){
	var _this =this;

	uiBase.sqlHints = DEFAULT_HINTS.concat(getDBTypeObj(dbType , 'hint'));
	uiBase.mimetype =getDBTypeObj(dbType , 'type');

	setDtoInfo(dbType, _dto);
	_dto = setNameKeyMapping (_dto); // name 을 키로 등록.

	_this._dto = _dto;
	_this.dataType = dataType;
	_this.constants = _constants;
	_this.tableColKey = TABLE_COL_KEYS;

	_this.compareColKey = COMPARE_COL_KEY;
}

}(VARSQLCont));