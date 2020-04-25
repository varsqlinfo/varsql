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
	,'varsql.0007' : '보낼 사람을 선택하세요.'
	,'varsql.0008' : '내보내기 하시겠습니까?'
	,'varsql.0009' : '에디터 창이 없습니다.\n새파일을 클릭후에 추가해주세요.'
	,'varsql.0010' : 'sql명을 입력해주세요.'
	,'varsql.0011' : '일치하는 내용이 {count}회 변경되었습니다.'
	,'varsql.0012' : '다음 문자열을 찾을수 없습니다.\n{findText}'
	,'varsql.0013' : '초기화 하시면 기본 레이아웃으로 구성되고 새로고침 됩니다.\n초기화 하시겠습니까?'
	,'varsql.0014' : '메시지를 보내시겠습니까?'
	,'varsql.0015' : '[{itemText}] 삭제하시겠습니까?'
	,'varsql.0016' : '삭제 하시겠습니까?'
	,'varsql.0017' : '삭제 되었습니다'
	,'varsql.0018' : '이미 추가된 항목 입니다'

	,'varsql.m.0001' : '변경되었습니다.\n변경된 패스워드는 5초후에 사라집니다.'
	,'varsql.m.0002' : ' 매니저 권한을 삭제 하시겠습니까?'
	,'varsql.m.0003' : ' 차단 하시겠습니까?'
	,'varsql.m.0004' : ' 해제 하시겠습니까?'
	,'varsql.m.0005' : '사용자의 {itemName} 그룹을 제거 하시겠습니까?\n그룹을 제거 하시면 그룹에 속한 모든 db를 접근할수 없습니다.'
	,'varsql.m.0006' : '{groupName} 그룹을 제거 하시면 그룹에 속한 모든 db맵핑 정보와 사용자 정보가 삭제 됩니다.\n그룹을 삭제 하시겠습니까?'
	,'varsql.m.0007' : '현재 비밀번호가 맞지 않습니다'
	,'varsql.m.0008' : '비밀번호가 변경되었습니다'

	,'varsql.form.0001' : '필수 입력사항입니다'
	,'varsql.form.0002' : '최소 {len}글자 이상 이여야 합니다'
	,'varsql.form.0003' : '비밀번호가 같아야합니다'
	,'varsql.form.0004' : '크기는  {range} 사이여야 합니다'


	//error message code
	,'error.0001' : '로그아웃 되었습니다.\n로그인창 으로 이동하시겠습니까?'
	,'error.0002' : '유효하지않은 요청입니다. 새로고침하시겠습니까?'
	,'error.0003' : '유효하지않은 database 입니다.\n메인 페이지로 이동하시겠습니까?'
	,'error.0004' : '연결이거부되었습니다.관리자에게 문의하세요.'
	,'error.403' : '권한이 없습니다.'
	,'btn.ok' : 'Ok'
	,'btn.cancel' : 'Cancel'
	,'btn.close' : 'Close'
	,'btn.copy' : 'Copy'
	,'btn.save' : 'Save'

	,'menu.file.import_export' : '가져오기 & 내보내기...'

	,'msg.add.manager.confirm' : '"{name}" 님에게  매니저 권한을 추가 하시겠습니까?'
	,'msg.del.manager.confirm' : '"{name}" 님의 메니저 권한을 제거 하면 맵핑된 DB의 메니저 권한도 사라집니다\n권한을 제거 하시겠습니까?'

	,'msg.close.window' : '창을 닫으시겠습니까?'
	,'msg.loading' : '로딩중 입니다.'
	,'msg.refresh' : '새로고침 하시겠습니까?'
}