/*
**
*ytkim
*varsql base object js
 */

;(function(VARSQL) {
"use strict";

var _constants = {
	newline :'\n'
	,tab : '\t'
	,querySuffix : ';  '
	,queryParameterPrefix:'#{'
	,queryParameterSuffix:'}'
};

// sql datatype bean;
var _dto = {
	'-7':{name : 'BIT',isNum : false ,val : '\'\''}
	,'-6':{name : 'TINYINT',isNum : true ,val : 0}
	,'5':{name : 'SMALLINT',isNum : true ,val : 0}
	,'4':{name : 'INTEGER',isNum : true ,val : 0}
	,'-5':{name : 'BIGINT',isNum : true ,val : 0}
	,'6':{name : 'FLOAT',isNum : true ,val : 0.0}
	,'7':{name : 'REAL',isNum : true ,val : 0}
	,'8':{name : 'DOUBLE',isNum : true ,val : 0}
	,'2':{name : 'NUMERIC',isNum : true ,val : 0}
	,'3':{name : 'DECIMAL',isNum : true ,val : 0}
	,'1':{name : 'CHAR',isNum : false ,val : '\'\''}
	,'12':{name : 'VARCHAR',isNum : false ,val : '\'\''}
	,'-1':{name : 'LONGVARCHAR',isNum : false ,val : '\'\''}
	,'91':{name : 'DATE',isNum : false ,val : 'current date'}
	,'92':{name : 'TIME',isNum : false ,val : 'current time'}
	,'93':{name : 'TIMESTAMP',isNum : false ,val : 'current timestamp'}
	,'-2':{name : 'BINARY',isNum : false ,val : '\'\''}
	,'-3':{name : 'VARBINARY',isNum : false ,val : '\'\''}
	,'-4':{name : 'LONGVARBINARY',isNum : false ,val : '\'\''}
	,'0':{name : 'NULL',isNum : false ,val : '\'\''}
	,'1111':{name : 'OTHER',isNum : false ,val : '\'\''}
	,'2000':{name : 'JAVA_OBJECT',isNum : false ,val : '\'\''}
	,'2001':{name : 'DISTINCT',isNum : false ,val : '\'\''}
	,'2002':{name : 'STRUCT',isNum : false ,val : '\'\''}
	,'2003':{name : 'ARRAY',isNum : false ,val : '\'\''}
	,'2004':{name : 'BLOB',isNum : false ,val : '\'\''}
	,'2005':{name : 'CLOB',isNum : false ,val : '\'\''}
	,'2006':{name : 'REF',isNum : false ,val : '\'\''}
	,'70':{name : 'DATALINK',isNum : false ,val : '\'\''}
	,'16':{name : 'BOOLEAN',isNum : false ,val : '\'\''}
	,'-8':{name : 'ROWID',isNum : false ,val : '\'\''}
	,'-15':{name : 'NCHAR',isNum : false ,val : '\'\''}
	,'-9':{name : 'NVARCHAR',isNum : false ,val : '\'\''}
	,'-16':{name : 'LONGNVARCHAR',isNum : false ,val : '\'\''}
	,'2011':{name : 'NCLOB',isNum : false ,val : '\'\''}
	,'2009':{name : 'SQLXML',isNum : false ,val : '\'\''}
};

VARSQL.datatype = _dto;
VARSQL.constants = _constants;

}(VARSQL));