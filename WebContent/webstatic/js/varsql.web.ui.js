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

if(parent && parent.VARSQL && parent.VARSQL.ui){
	$('html').addClass(parent.VARSQL.ui.getTheme());
}

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
	,open : function (opt){
		
		var msg = opt;
		if(VARSQL.isObject(opt) && !VARSQL.isUndefined(opt.key)){
			msg = VARSQL.messageFormat(opt.key);
		}
		
		return alert(msgOpt);
	}
}

_$base.toast = {
	options : {
		info :  {
			icon : 'info'
			, bgColor: '#7399e6'
		}
		,error : {
			icon : 'error'
			, bgColor: '#ee8777'
		}
		,warning :{
			icon : 'warning'
			, bgColor: '#d9b36c'
		}
		,success:{
			icon : 'success'
			, bgColor: '#7399e6'
		}
		,alert : {
			icon: 'warning'
			, bgColor: '#7399e6'
		}
		,text :{
			bgColor: '#7399e6'
		}
	}
	/**
	 * @method _$base.dialog.view
	 * @param opt {Object} toast option
	 * @description toast view
	 */	
	,open :function (option){
		
		if(typeof option !=='object'){
			option = {text: option};
		}
		
		var opt = this.options[option.icon]; 
		opt = opt?opt : this.options['info'];
		
		// 기본 옵션 셋팅
		var setOpt = $.extend({}, {
			 hideAfter: 2000
			, loader :false
			, position: {left:"50%",top:"50%"}
			, textColor: '#fff'
			, stack:false
			, showHideTransition: 'fade'
			//, beforeShow : function(){$('.jq-toast-wrap').css("margin" , "0 0 0 -155px") }
		},opt);
		
		var tmpP = window ; 
		
		if(parent){
			if(typeof tmpP.$ === 'undefined' && typeof tmpP.$.toast === 'undefined' ){
				tmpP = parent;
			}
		}
		
		tmpP.$.toast($.extend(true,setOpt, option));
	}
}

window.VARSQLUI = _$base; 
})(VARSQL, jQuery);


(function($) {
    $.fn.pagingNav = function(options, callback) {
        if(!options){
        	$(this).html('');
			return false;
		}
		
		var currP = options.currPage ;
		if(currP == "0") currP = 1;
		var preP_is = options.prePage_is;
		var nextP_is = options.nextPage_is;
		var currS = options.currStartPage;
		var currE = options.currEndPage;

		if(currE == "0") currE = 1;
		var nextO = 1*currP+1;
		var preO = currP - 1;
		
		var strHTML=new Array();

		strHTML.push('<div class="text-center">');
		strHTML.push('	<ul class="pagination">');
			
		if (preP_is=="true"){
			strHTML.push('	<li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
		}else{
			if (currP<=1)
			{
				strHTML.push('	<li class="disabled"><a href="javascript:">&laquo;</a></li>');
			}else{
				strHTML.push('	<li><a href="javascript:" class="page-click" pageno="'+preO+'">&laquo;</a></li>');
			}
		}
		var no=0;

		for (no = currS*1; no <= currE*1; no++){

			if ( no == currP ){
				strHTML.push('	<li class="active"><a href="javascript:">'+no+'</a></li>');
			}
			else{
				strHTML.push('	<li><a href="javascript:" class="page-click" pageno="'+no+'">'+no+'</a></li>');
			}
		}

		if(nextP_is=="true"){
			strHTML.push('	<li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');		
		}else{
			if (currP==currE)
			{
				strHTML.push('	<li class="disabled"><a href="javascript:">&raquo;</a></li>');
			}else{
				strHTML.push('	<li><a href="javascript:" class="page-click" pageno="'+nextO+'">&raquo;</a></li>');	
			}
		}

		strHTML.push('	</ul>');
		strHTML.push('</div>');
		
		this.empty('');
		this.html(strHTML.join(''));
		
		var initFlag = this.data('initFlag');
		
		if(initFlag != 'true'){
			this.data('initFlag', 'true')
			this.on('click',' .page-click', function() {
				var sNo = $(this).attr('pageno');
				
				
				if (typeof callback == 'function') {
					callback(sNo);
				} else {
					try {
						nextPage(sNo);
					} catch (e) {
					}
				}
			});
		}
		
		return this; 
	};
	
})(jQuery);
