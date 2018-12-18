/*
**
*ytkim
*varsql ui js
 */
if (typeof window != "undefined") {
    if (typeof window.VARSQLUI == "undefined") {
        window.VARSQLUI = {};
    }
}else{
	if(!VARSQLUI){
		VARSQLUI = {};
	}
}

(function(VARSQL, $) {
'use strict';

var _$base = {
	_version:'0.1'
	,author:'ytkim'
};

/**
 * dialog
 */
_$base.dialog = {
	open : function (selector ,opt){
		return $(selector).dialog(opt);
	}
}

/**
 * confirm 창
 */
_$base.confirm = {
	template : function (opt){
		var strHtm = [];
		strHtm.push('<div class="confirm-area">');
		strHtm.push('	<div class="message-area">');
		strHtm.push('		<div>#message#</div>');
		strHtm.push('	</div>');
		strHtm.push('	<div class="button-area">');
		strHtm.push('		<div>#button#</div>');
		strHtm.push('	</div>');
		strHtm.push('</div>');
		return strHtm.join('');
	}
	,open : function (selector ,opt){
		return $(selector).dialog(opt);
	}
}

/**
 * alert 창
 */
_$base.alert = {
	template : function (opt){
		
	}
	,open : function (msgOpt){
		return alert(msgOpt);
	}
}

_$base.toast = {
	template : function (opt){
		
	}
	,open : function (msgOpt){
		return alert(msgOpt);
	}
}

window.VARSQLUI = _$base; 
})(VARSQL, jQuery);

