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
/**
 * :{name : 'TIMESTAMP'	-- dataType 여부.
 * ,isNum : false	-- 숫자 여부
 * , isDate : true	-- date
 * , val : 'current_timestamp'	-- generate insert, update default value
 * , javaType:'String'	-- java type
 * , isSize: false -> ddl create size check
 * }
 */
var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '', javaType:'Boolean', isSize: true}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0, javaType:'int'}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0, javaType:'int'}
	,'4':{name : 'INTEGER',isNum : true ,val : 0, javaType:'int'}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0, javaType:'long'}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0, javaType:'float'}
	,'7':{name : 'REAL',isNum : true ,val : 0, javaType:'float'}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0, javaType:'double'}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0, javaType:'BigDecimal'}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0, javaType:'BigDecimal'}
	,'1':{name : 'CHAR',isNum : false ,val : '', javaType:'String'}
	,'12':{name : 'VARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'91':{name : 'DATE',isNum : false, isDate : true, val : 'current_date', javaType:'Date', isSize: false}
	,'92':{name : 'TIME',isNum : false, isDate : true, val : 'current_time', javaType:'Time', isSize: false}
	,'93':{name : 'TIMESTAMP',isNum : false, isDate : true, val : 'current_timestamp', javaType:'Timestamp', isSize: false}
	,'-2':{name : 'BINARY',isNum : false ,val : '', javaType:'String'}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '', javaType:'byte[]'}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '', javaType:'byte[]'}
	,'0':{name : 'NULL',isNum : false ,val : '', javaType:'Object'}
	,'1111':{name : 'NVARCHAR2',isNum : false ,val : '', javaType:'String' , getLen : function (len){
		return parseInt(len /2,10);
	}}
	//,'1111':{name : 'OTHER',isNum : false ,val : '', javaType:'Object'}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'2002':{name : 'STRUCT',isNum : false ,val : '', javaType:'Object'}
	,'2003':{name : 'ARRAY',isNum : false ,val : '', javaType:'Object'}
	,'2004':{name : 'BLOB',isNum : false ,val : '', javaType:'Object'}
	,'2005':{name : 'CLOB',isNum : false ,val : '', javaType:'String'}
	,'2006':{name : 'REF',isNum : false ,val : '', javaType:'Object'}
	,'70':{name : 'DATALINK',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '', javaType:'boolean', isSize: false}
	,'-8':{name : 'ROWID',isNum : false ,val : '', javaType:'Object', isSize: false}
	,'-15':{name : 'NCHAR',isNum : false ,val : '', javaType:'String'}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '', javaType:'String'}
	,'2011':{name : 'NCLOB',isNum : false ,val : '', javaType:'String'}
	,'2009':{name : 'SQLXML',isNum : false ,val : '', javaType:'String', isSize: false}
	,'9999':{name : 'OTHER',isNum : false ,val : '', javaType:'Object', isSize: false}

};

_dto['INT'] = VARSQL.util.objectMerge({},_dto['4'],{name:'INT'});
_dto['DATETIME'] =VARSQL.util.objectMerge({},_dto['91'],{name:'DATETIME'});

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
var COMPARE_COL_KEY = [
	{ label: '컬럼명', key: 'name'},
	{ label: '데이터타입', key: 'typeAndLength'},
	{ label: 'Key', key: 'constraints'},
	{ label: '기본값', key: 'defaultVal'},
	{ label: '널여부', key: 'nullable'},
	{ label: '설명', key: 'comment'}
];

var DEFINE_INFO = {
	MARIADB : {
		type :'text/x-mariadb'
		,formatType : 'mariadb '
	}
	,MSSQL : {
		type :'text/x-mssql'
		,formatType : 'tsql'
	}
	,MYSQL : {
		type :'text/x-mssql'
		,formatType : 'mysql'
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
		,formatType : 'plsql'
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
		,formatType : 'plsql'
	}
	,H2 : {
		type : 'text/x-mariadb'
		,formatType : 'sql'
	}
	,POSTGRESQL :{
		type :'text/x-mssql'
		,formatType : 'postgresql'
	}
	,DB2  :{
		type :'text/x-mssql'
		,formatType : 'DB2'
	}
	,'DEFAULT' : {
		type :  'text/x-sql',hint : [] ,dataType:{}
		,formatType : 'sql'
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
	dataType= (dataType+'').toUpperCase();

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

		if(!dupChk[item.name] && item.name != 'NULL'){
			result.push({
				type : item.name
				,name : item.name
			})
			dupChk[item.name] = true;
		}
	}

	return result;
}

// js formatter format type
VARSQLCont.formatType = function (dbType){
	return (DEFINE_INFO[dbType] || defaultInfo).formatType;
}

VARSQLCont.isDateType = function (type){

	if(type=='date'){
		return true;
	}else if(type=='time'){
		return true;
	}else if(type=='timestamp'){
		return true;
	}
	return false;
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