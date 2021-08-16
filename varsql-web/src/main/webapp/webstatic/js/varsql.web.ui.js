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

var _$base = {};

var defaultPopupPosition = {
	align : 'top'
	,topMargin : 10
	,top:2
	,left:2
	,ieDualCenter : false
	,browser : {
		msie : 40
		,mozilla : 70
		,chrome :70
		,'default' : 70
		,safari : 70
	}
}

//theme class

/**
 * theme get set
 */
_$base.theme = function (uid, theme){
	if(VARSQL.isUndefined(theme)){
		return VARSQL.localStorage(uid +'theme');
	}else{
		VARSQL.localStorage({key : uid +'theme', value : theme});
		_$base.refreshTheme();
	}
}

_$base.refreshTheme = function (){
	var theme = _$base.theme($varsqlConfig.conuid);
	$('html').attr({
		'varsql-theme':theme
		,'pub-theme' : theme
	});
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
			msg = VARSQL.messageFormat(opt.key, opt);
		}

		return alert(msg);
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

_$base.frame = {
	open : function (selector, url, opts){
		var iframeEle =$(selector);

		if(selector && iframeEle.length <1) throw TypeError('iframe selector empty : '+ selector);

		if(url==''){
			try{
				iframeEle.attr('src','').on('load.varsql.evt', function() {
					var tmpFrameObj = iframeEle.get(0).contentWindow;
					try{tmpFrameObj.document.open();}catch(e){console.log(e)}
					tmpFrameObj.document.write('<div><h2>Check iframe url : ['+url+']</h2></div>');
					try{tmpFrameObj.document.close();}catch(e){console.log(e)}

					iframeEle.off('load.varsql.evt');
				});
			}catch(e){console.log(e)}

			return false;
		}

		opts = opts || {};

		if(!VARSQL.isUndefined(opts.style)){
			var styleArr = opts.style.split(';');

			var styles = {};
			for(var i = 0 ; i <styleArr.length; i ++){
				var cssArr = styleArr[i].split(':');
				if(cssArr.length > 1){
					styles[cssArr[0]]=cssArr[1];
				}
			}

			iframeEle.css(styles);
		}

		var tmpParam = opts.param ? opts.param : {};

		if(VARSQL.util.isMethodPost(opts.method)){
			iframeEle.attr('_view_url', url);

			var frameName = iframeEle.attr('name');

			if(VARSQL.isBlank(frameName)){
				frameName = VARSQL.generateUUID();
				iframeEle.attr('name', frameName);
			}

			var postForm = $('<form method="post"></form>');
			postForm.attr("target", frameName);
			postForm.attr("action", url);

			for(var key in tmpParam){
				postForm.append('<input type="hidden" name="'+key+'" >');
				postForm.find('[name="'+key+'"]').val(((typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal)));
			}

			if($("#iframehiddenFormArea").length < 1){
				$("body").append('<div id="iframehiddenFormArea" style="display:none;"></div>');
			}

			$("#iframehiddenFormArea").empty.html(postForm);
			postForm.submit();
		}else{
			url = VARSQL.util.appendUrlParam(url, tmpParam);

			if(iframeEle.attr('data-current-url') == url){
				try{
					iframeEle.get(0).contentWindow.VARSQLUI.refreshTheme();
				}catch(e){
					console.log(e);
				}
			}else{
				iframeEle.attr({
					'data-current-url': url
					,'src': url
				});
			}
		}
		return iframeEle;
	}
}
_$base.popup = {
	open : function (url, opt){
		opt = opt ||{};
		var targetId = VARSQL.generateUUID()
			, tmpParam = opt.param?opt.param:{}
			, tmpMethod = opt.method ? opt.method : 'get'
			, tmpIsNewYn = opt.isNewYn=='Y' ? 'Y' : 'N'
			, tmpPopOption = opt.viewOption?opt.viewOption:''
			, tmpPosition =  $.extend({},defaultPopupPosition,( $.isPlainObject(opt.position)?opt.position:{align:opt.position} ))
			, tmpName = (opt.name?( escape(opt.name).replace(/[ \{\}\[\]\/?.,;:|\)*~`!^\-+┼<>@\#$%&\'\"\\(\=]/gi,'') ):targetId.replace(/-/g,''));

		var urlIdx = url.indexOf('?');
		var openUrl = urlIdx > -1 ?url.substring(0,urlIdx):url;

		tmpPopOption = tmpPopOption.replace(/\s/gi,'');

		if(tmpPopOption != ''){

			var popupOptArr = tmpPopOption.split(',');
			var tmpItem, _t=-1, _l=-1, _w=1050, _h=0, addFlag, tmpOpt='', addScrollbarOpt=true, addStatusOpt=false, addResizeableOpt=true;

			for(var i = 0 ;i < popupOptArr.length; i++){
				tmpItem = popupOptArr[i];
				addFlag = true;
				if(tmpItem.toLowerCase().indexOf('width=')==0){
					_w= tmpItem.replace(/[^0-9]/g,'');
					addFlag = false;
				}
				if(tmpItem.toLowerCase().indexOf('height=')==0){
					_h= tmpItem.replace(/[^0-9]/g,'');
					addFlag = false;
				}
				if(tmpItem.toLowerCase().indexOf('top=')==0){
					_t= tmpItem.replace(/[^0-9]/g,'');
					addFlag = false;
				}
				if(tmpItem.toLowerCase().indexOf('left=')==0){
					_l= tmpItem.replace(/[^0-9]/g,'');
					addFlag = false;
				}
				if(tmpItem.toLowerCase().indexOf('scrollbars=')==0){
					addScrollbarOpt= false;
				}

				if(tmpItem.toLowerCase().indexOf('resizable=')==0){
					addResizeableOpt= false;
				}

				tmpOpt += (addFlag ? ( (tmpOpt==''?'':',') + tmpItem ) : '');
			}

			tmpOpt += addScrollbarOpt?',scrollbars=yes':'';
			tmpOpt += addResizeableOpt?',resizable=yes':'';

			if(typeof opt.position=='undefined' && _h !=0 ){
				tmpPosition.align='center';
			}
			var tmpTopMargin = tmpPosition.topMargin
				,tmpLeftMargin = tmpPosition.leftMargin;

			var heightMargin = tmpPosition.browser['default'];
			_h= ( _h==0 ? (screen.availHeight-heightMargin- tmpTopMargin) :_h);

			_h =addStatusOpt?_h-23:_h;
			var _viewPosition = {};

			_viewPosition = popupPosition(_w, _h, tmpPosition.top, tmpPosition.left, tmpPosition.ieDualCenter);

			if(tmpPosition.align=='top'){
				_viewPosition.top = typeof screen['availTop']!=='undefined' ?screen['availTop'] : (window.screenTop || screen.top);
				_viewPosition.top = (_viewPosition.top > 0? _viewPosition.top- tmpTopMargin : _viewPosition.top+tmpTopMargin);
			}else if(tmpPosition.align=='left'){
				_viewPosition.left =0;
			}else if(tmpPosition.align=='right'){
				_viewPosition.left = window.screen.availWidth-_w;
			}else if(tmpPosition.align=='bottom'){
				_viewPosition.top = window.screen.availHeight-_h;
			}

			_viewPosition.top = _t != -1 ? _t : (isNaN(tmpTopMargin)?_viewPosition.top:_viewPosition.top+tmpTopMargin);
			_viewPosition.left = _l != -1 ? _l : (isNaN(tmpLeftMargin)?_viewPosition.left:_viewPosition.left+tmpLeftMargin);

			tmpPopOption = tmpOpt+', width=' + _w + 'px, height=' + _h + 'px, top=' + _viewPosition.top + 'px, left=' + _viewPosition.left+'px';
		}
		tmpParam=VARSQL.util.getParameter(url , tmpParam);

		var winPopupObj;
		if(tmpIsNewYn=='N'){
			winPopupObj = window.open('', tmpName, tmpPopOption);

			if(winPopupObj && winPopupObj.VARSQL){
				winPopupObj.focus();

				try{winPopupObj.VARSQLUI.refreshTheme();}catch(e){} // theme refresh

				return winPopupObj;
			}

			if(openUrl ==''){
				return winPopupObj;
			}
		}

		// get method
		if('get' ==tmpMethod){

			tmpParam=VARSQL.util.paramToArray(tmpParam);

			if(tmpParam.length > 0)	openUrl =openUrl+'?'+tmpParam.join('&');

			return window.open(openUrl, tmpName, tmpPopOption);
		}else{  // post method
			var inputStr = [];
			inputStr.push('<!doctype html><head>');
			inputStr.push('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta charset="UTF-8" /></head>');
			inputStr.push('<form action="'+openUrl+'" method="post" id="'+targetId+'" name="'+targetId+'">');

			var tmpVal;
			for(var key in tmpParam){
				tmpVal = tmpParam[key];

				inputStr.push('<input type="hidden" name="'+key+'" value=\''+((typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal))+'\'/>');
			}
			inputStr.push('</form>');
			inputStr.push('<script async type="text/javascript">try{document.charset="utf-8";}catch(e){}document.'+targetId+'.submit();</'+'script>');

			winPopupObj=window.open('about:blank', tmpName, tmpPopOption);

			try{
				try{winPopupObj.document.open();}catch(e){console.log(e)}
				winPopupObj.document.write(inputStr.join(''));
				winPopupObj.focus();
				try{winPopupObj.document.close();}catch(e){console.log(e)}
			}catch(e){
				winPopupObj=window.open('about:blank', tmpName+targetId, tmpPopOption);
				try{
					try{winPopupObj.document.open();}catch(e){console.log(e)}
					winPopupObj.document.write(inputStr.join(''));
					winPopupObj.focus();

					try{winPopupObj.document.close();}catch(e){console.log(e)}
				}catch(e1){
					console.log(e1);
				}
			}

			try{winPopupObj.VARSQLUI.refreshTheme();}catch(e){} // theme refresh

			return winPopupObj;
		}
	}
}

function popupPosition(_w, _h, tr, lr, ieDualCenter){
	_h=  parseInt(_h,10);
	_w = parseInt(_w,10);
	tr = parseInt(tr,10);
	lr = parseInt(lr,10);
	tr =tr? tr :2;
	lr =lr? lr :2;

	var dualScreenLeft = window.screenX || window.screenLeft || screen.left
		,dualScreenTop = window.screenY || window.screenTop || screen.top
		, width = window.innerWidth || document.documentElement.clientWidth || screen.width
		, height = window.innerHeight || document.documentElement.clientHeight || screen.height;

	var ua = window.navigator.userAgent;
	var old_ie= ua.indexOf('MSIE');
	var new_ie= ua.indexOf('Trident/');
	var _top = 0 , _left = 0 ;

	if(old_ie >-1 || new_ie >-1){
		if(ieDualCenter){

			var dualScreenLeft = window.screenLeft || screen.left
				,dualScreenTop = window.screenTop || screen.top
				,width = window.innerWidth || document.documentElement.clientWidth || screen.width
				,height = window.innerHeight || document.documentElement.clientHeight || screen.height;

			_left = ((width / 2) - (_w / 2)) + dualScreenLeft;
			_top = ((height / 2) - (_h / 2)) + dualScreenTop;
		}else{
			width=window.screen.availWidth;
			height=window.screen.availHeight;
			dualScreenTop= 0 ;
			dualScreenLeft =0 ;
			_top = ((height / tr) - (_h / tr))+ dualScreenTop;
			_top = _top < 0 ? 0 :_top;
			_left = ((width / lr) - (_w / lr)) + dualScreenLeft;
		}
	}else{
		width=window.screen.availWidth;
		height=window.screen.availHeight;
		_top = ((height / tr) - (_h / tr))+ window.screen.availTop;
		_left = ((width / lr) - (_w / lr))+window.screen.availLeft;
	}

	return {
		top : _top
		,left :  _left
	}
}

if(!VARSQL.isUndefined($varsqlConfig.conuid)){
	_$base.refreshTheme();
}

window.VARSQLUI = _$base;
})(VARSQL, jQuery);