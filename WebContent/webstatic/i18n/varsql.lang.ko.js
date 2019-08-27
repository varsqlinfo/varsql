/*
**
*ytkim
*varsql base lang js
 */
if (typeof window != "undefined") {
    if (typeof window.VARSQLLang == "undefined") {
        window.VARSQL_LANG = {};
    }
}else{
	if(!VARSQL_LANG){
		VARSQL_LANG = {};
	}
}

VARSQL_LANG = {
	'varsql.0001' : '페이지를 나가시겠습니까?'
	,'varsql.0002' : '저장되었습니다.'
	,'varsql.0003' : '추가할 항목을 선택해주세요.'
	,'varsql.0004' : '삭제할 항목을 선택해주세요.'
	,'varsql.0005' : '저장하지않은 파일이 존재 합니다.\n페이지를 나가시겠습니까?'
	,'varsql.0006' : '항목을 선택해주세요.'
	
	,'varsql.m.0001' : '변경되었습니다.\n변경된 패스워드는 5초후에 사라집니다.'
	,'varsql.m.0002' : ' 매니저 권한을 삭제 하시겠습니까?'
	,'varsql.m.0003' : ' 차단 하시겠습니까?'
	,'varsql.m.0004' : ' 해제 하시겠습니까?'
	,'varsql.m.0005' : '{itemName} 을 삭제 하시겠습니까?'

	//error message code
	,'error.0001' : '로그아웃 되었습니다.\n로그인창 으로 이동하시겠습니까?'
	,'error.0002' : '유효하지않은 요청입니다. 새로고칭하시겠습니까?'
	,'error.0003' : '유효하지않은 database 입니다.\n메인 페이지로 이동하시겠습니까?'
	,'error.0004' : '연결이거부되었습니다.관리자에게 문의하세요.'
	,'error.403' : '권한이 없습니다.'
	,'btn.ok' : 'Ok'
	,'btn.cancel' : 'Cancel'
	,'btn.close' : 'Close'
	,'btn.copy' : 'Copy'
	,'btn.save' : 'Save'
}