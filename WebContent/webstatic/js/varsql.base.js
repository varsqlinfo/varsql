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

var _constants = {
	newline :'\n'
	,tab : '\t'
	,querySuffix : ';  '
	,queryParameterPrefix:'#{'
	,queryParameterSuffix:'}'
};

var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '\'\'', javaType:'char'}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0, javaType:'int'}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0, javaType:'int'}
	,'4':{name : 'INTEGER',isNum : true ,val : 0, javaType:'int'}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0, javaType:'long'}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0, javaType:'float'}
	,'7':{name : 'REAL',isNum : true ,val : 0, javaType:'Integer'}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0, javaType:'double'}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0, javaType:'int'}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0, javaType:'int'}
	,'1':{name : 'CHAR',isNum : false ,val : '\'\'', javaType:'char'}
	,'12':{name : 'VARCHAR',isNum : false ,val : '\'\'', javaType:'String'}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '\'\'', javaType:'String'}
	,'91':{name : 'DATE',isNum : false ,val : 'current date', javaType:'String'}
	,'92':{name : 'TIME',isNum : false ,val : 'current time', javaType:'String'}
	,'93':{name : 'TIMESTAMP',isNum : false ,val : 'current timestamp', javaType:'String'}
	,'-2':{name : 'BINARY',isNum : false ,val : '\'\'', javaType:'String'}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '\'\'', javaType:'String'}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '\'\'', javaType:'String'}
	,'0':{name : 'NULL',isNum : false ,val : '\'\'', javaType:'String'}
	,'1111':{name : 'NVARCHAR2',isNum : false ,val : '\'\'', javaType:'String' , getLen : function (len){
		return parseInt(len /2,10);
	}}
	//,'1111':{name : 'OTHER',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2002':{name : 'STRUCT',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2003':{name : 'ARRAY',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2004':{name : 'BLOB',isNum : false ,val : '\'\'', javaType:'Object'}
	,'2005':{name : 'CLOB',isNum : false ,val : '\'\'', javaType:'String'}
	,'2006':{name : 'REF',isNum : false ,val : '\'\'', javaType:'Object'}
	,'70':{name : 'DATALINK',isNum : false ,val : '\'\'', javaType:'Object'}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '\'\'', javaType:'boolean'}
	,'-8':{name : 'ROWID',isNum : false ,val : '\'\'', javaType:'Object'}
	,'-15':{name : 'NCHAR',isNum : false ,val : '\'\'', javaType:'String'}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '\'\'', javaType:'String'}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '\'\'', javaType:'String'}
	,'2011':{name : 'NCLOB',isNum : false ,val : '\'\'', javaType:'String'}
	,'2009':{name : 'SQLXML',isNum : false ,val : '\'\'', javaType:'String'}
	,'9999':{name : 'OTHER',isNum : false ,val : '\'\'', javaType:'Object'}
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
	,'CURRNET TIMESTAMP'
];

var TABLE_COL_KEYS ={
	NAME :'name'
	,TYPE_NAME :'typeName'
	,DATA_TYPE :'dataType'
	,NULLABLE :'nullable'
	,PRIMAY_KEY :'primayKey'
	,COMMENT :'comment'
	,SIZE :'length'
}

var DBTYPE_HINTS = {
	oracle : ['VARCHAR2','NVARCHAR2','NCHAR2']
	,mssql :[]
}

var MIME_TYPE ={
	mssql : 'text/x-mssql'
	,mysql : 'text/x-mysql'
	,mariadb : 'text/x-mariadb'
	//,oracle : 'text/x-oracle'
}

var dataType = {};

// sql datatype bean;
dataType.dto = _dto;

dataType.sqlHints = function (dbType){
	if(DBTYPE_HINTS[dbType]){
		return DEFAULT_HINTS.concat(DBTYPE_HINTS[dbType]);
	}else{
		return DEFAULT_HINTS
	}
}

dataType.getMimeType = function (dbType){
	return MIME_TYPE[dbType] || 'text/x-sql';
}

dataType.getDbType = function (dbType){
	var tmpDataType= _dto[dbType]; 
	if(typeof  tmpDataType !=='undefined'){
		return  tmpDataType;
	}else{
		return _dto['9999'] ;
	}
}

VARSQLCont.dataType = dataType;
VARSQLCont.constants = _constants;
VARSQLCont.tableColKey = TABLE_COL_KEYS;

}(VARSQLCont));