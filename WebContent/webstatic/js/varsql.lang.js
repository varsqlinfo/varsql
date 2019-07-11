/*
**
*ytkim
*varsql base lang js
 */

;(function(VARSQL) {
"use strict";
function _langInfo (key, lang){
	var _lang = {
		'varsql.0001' : '페이지를 나가시겠습니까?'
		,'varsql.0002' : '저장되었습니다.'
		,'varsql.0003' : '추가할 그룹정보를 선택해주세요.'
		,'varsql.0004' : '삭제할 그룹정보를 선택해주세요.'
		,'varsql.0005' : '저장하지않은 파일이 존재 합니다.\n페이지를 나가시겠습니까?'
		,'btn.ok' : 'Ok'
		,'btn.cancel' : 'Cancel'
		,'btn.close' : 'Close'
		,'btn.copy' : 'Copy'
		,'btn.save' : 'Save'
	} 
	
	return _lang[key]||lang||''; 
}

VARSQL.messageLang =function (key,lang){
	return _langInfo(key,lang);
}

}(VARSQL));