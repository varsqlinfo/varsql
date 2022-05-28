/*
**
*ytkim
*varsql base lang js
desc
코드 추가 시  아래 규칙을 따라서 추가해주세요.
공통 -> varsql.
사용자 -> varsql.u.
매니저 -> varsql.m.
관리자 -> varsql.a.
error -> error.
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
	,'varsql.0019' : '저장 하시겠습니까?'
	,'varsql.0020' : '적용 하시겠습니까?'
	,'varsql.0021' : '초기 설정으로 복구 하시겠습니까?'
	,'varsql.0022' : '설정 정보가 잘못되었습니다\n초기 설정으로 복구 하시겠습니까?'
	,'varsql.0023' : '비밀번호를 입력해 주세요.'
	,'varsql.0024' : '저장 하시겠습니까?\n저장후 페이지 Reload 후에 반영됩니다.'
	,'varsql.0025' : '설정 정보가 잘못되었습니다\n 설정 정보를 확인해주세요.'
	,'varsql.0026' : '유효 하지 않은 설정입니다.'
	,'varsql.0027' : '복사 되었습니다'
	,'varsql.0028' : '내용을 입력해주세요.'
	,'varsql.0029' : '파일 업로드후 처리 실행해주세요.'
	,'varsql.0030' : 'find line:{line}, ch:{ch}'
	,'varsql.0031' : '허용하는 확장자 : {accept}'
	,'varsql.0032' : 'DB Name 또는 Schema을 확인해주세요.'
	,'varsql.0033' : '[{name}] 를 닫으시겠습니까?'

	,'varsql.m.0001' : '변경되었습니다.\n변경된 패스워드는 5초후에 사라집니다.'
	,'varsql.m.0002' : ' 매니저 권한을 삭제 하시겠습니까?'
	,'varsql.m.0003' : ' 차단 하시겠습니까?'
	,'varsql.m.0004' : ' 해제 하시겠습니까?'
	,'varsql.m.0005' : '사용자의 {itemName} 그룹을 제거 하시겠습니까?\n그룹을 제거 하시면 그룹에 속한 모든 db를 접근할수 없습니다.'
	,'varsql.m.0006' : '{groupName} 그룹을 제거 하시면 그룹에 속한 모든 db맵핑 정보와 사용자 정보가 삭제 됩니다.\n그룹을 삭제 하시겠습니까?'
	,'varsql.m.0007' : '비밀번호를 정확하게 입력해 주세요.'
	,'varsql.m.0008' : '비밀번호가 변경되었습니다'
	,'varsql.m.0009' : '{userName}님의 비밀번호를 변경하시겠습니까?\n변경된 패스워드는 5초후에 사라집니다.'
	,'varsql.m.0010' : '대상을 선택하세요.'
	,'varsql.m.0011' : '타켓을 선택하세요'
	,'varsql.m.0012' : 'objectType을 선택하세요.'
	,'varsql.m.0013' : '테이블의 설명이 같지 않습니다. 대상 : [{sourceRemark}] 타켓 : [{targetRemark}]'
	,'varsql.m.0014' : '컬럼 카운트  대상 : {sourceLen} 타켓 : {targetLen}'
	,'varsql.m.0015' : '테이블 정보가 동일하지 않습니다.'
	,'varsql.m.0016' : '{columnLabel} 대상 : [{sourceValue}] 타켓 : [{targetValue}]'
	,'varsql.m.0017' : '타켓 테이블에 [{sourceKey}] 컬럼이 존재 하지 않습니다.'
	,'varsql.m.0018' : '타켓에'
	,'varsql.m.0019' : '테이블이 존재 하지 않습니다'
	,'varsql.m.0020' : '대상에'

	,'varsql.m.0021' : '[{key}] 같지 않습니다.'
	,'varsql.m.0022' : '[{key}] 같지 않습니다. 대상 : {sourceValue} 타켓 : {targetValue}'
	,'varsql.m.0023' : '존재 하지 않습니다'
	,'varsql.m.0024' : '대상 테이블에 [{targetKey}] 컬럼이 존재 하지 않습니다.'
	,'varsql.m.0025' : '대상 테이블 비교 정보'
	,'varsql.m.0026' : '타켓 테이블 비교 정보'

	,'varsql.a.0001' : '모두 닫으시겠습니까?'
	,'varsql.a.0002' : '성공\n다시 연결을 하실경우 풀 초기화를 해주세요.'
	,'varsql.a.0003' : '모두 초기화 하시겠습니까?'
	,'varsql.a.0004' : '비밀번호를 변경하시겠습니까?'

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
	,'error.80000' : '연결[80000]이 거부되었습니다.\n관리자에게 문의하세요.'
	,'error.80001' : '연결[80001]이 닫혀 있습니다.\n관리자에게 문의하세요.'
	,'error.80002' : 'error code : {errorCode}\n연결이 유효하지 않습니다.\n관리자에게 문의하세요.'
	,'error.default' : 'error code : {errorCode}\n에러가 발생하였습니다 \n관리자에게 문의하세요.'
	,'btn.ok' : 'Ok'
	,'btn.cancel' : 'Cancel'
	,'btn.close' : 'Close'
	,'btn.copy' : 'Copy'
	,'btn.save' : 'Save'
	,'copy' : '복사'
	,'success' : '성공'
	,'fail' : '실패'
	,'remove' : '삭제'
	,'data' : '데이터'
	,'dataview' : '데이터 보기'
	,'count' : '카운트'
	,'export' : '내보내기'
	,'refresh' : '새로고침'
	,'data.export' : '데이터 내보내기'
	
	,'up' : '위'
	,'down' : '아래'
	,'add' : '추가'
	,'remove' : '삭제'
	,'all.add' : '전체 추가'
	,'all.remove' : '전체 삭제'
	,'search' : '검색'
	,'search.input' : '검색어를 입력해주세요'
	,'search.message' : '{searchType} 을(를) 검색해주세요'
	,'empty.data' : '데이터가 없습니다.'
	,'user' : '사용자'
	
	,'sender' : '보낸사람'
	,'recipient' : '받는사람'
	,'complete' : '완료'
	
	,'step.prev' : '이전'
	,'step.next' : '다음'
	,'step.complete' : '완료'
	,'step.close' : '닫기'

	,'file.add' : '파일 선택'
	,'file.upload' : '업로드'
	,'file.remove' : '모두삭제'

	,'menu.file.import' : '가져오기'
	,'menu.file.export' : '내보내기'
	,'menu.file.import_export' : '가져오기 & 내보내기...'

	,'msg.add.manager.confirm' : '"{name}" 님에게  매니저 권한을 추가 하시겠습니까?'
	,'msg.del.manager.confirm' : '"{name}" 님의 메니저 권한을 제거 하면 맵핑된 DB의 메니저 권한도 사라집니다\n권한을 제거 하시겠습니까?'

	,'msg.close.window' : '창을 닫으시겠습니까?'
	,'msg.loading' : '로딩중 입니다.'
	,'msg.refresh' : '새로고침 하시겠습니까?'
	
	,'grid.column.name' : '컬럼명'
	,'grid.data.type' : '데이터타입'
	,'grid.nullable' : '널여부'
	,'grid.key' : 'Key'
	,'grid.desc' : '설명'
	,'grid.default.value' : '기본값'
	
	,'grid.parameter.name' : '파라미터명'
	,'grid.inout.name' : 'IN/OUT'
		
                
}