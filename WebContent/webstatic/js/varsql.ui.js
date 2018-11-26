/*
**
*ytkim
*varsql common js
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

_$base.dialog = {
	open : function (selector ,opt){
		return $(selector).dialog(opt);
	}
}

window.VARSQLUI = _$base; 
})(VARSQL, jQuery);

