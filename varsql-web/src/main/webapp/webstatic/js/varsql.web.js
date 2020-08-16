/*
**
*ytkim
*varsql common js
 */
if (typeof window != "undefined") {
    if (typeof window.VARSQL == "undefined") {
        window.VARSQL = {};
    }
}else{
	if(!VARSQL){
		VARSQL = {};
	}
}

(function( window, undefined ) {
'use strict';

var _$base = {
	version:'0.1'
	,author:'ytkim'
	,contextPath: (typeof global_page_context_path === 'undefined' ? '/vsql' : global_page_context_path)
	,uri:{
		'admin':'/admin'
		,'manager':'/manager'
		,'join':'/join'
		,'user':'/user'
		,'guest':'/guest'
		,'database':'/database'
		,'sql':'/sql'
		,'plugin':'/plugin'
	}
}
,_defaultAjaxOption ={
	method :'post'
	,cache: false
	,dataType: "json"
};

_$base.staticResource  ={
	get:function (type){
		return this[type];
	}
	,'juiChart' :
		{
			'js' : [
			        '/webstatic/js/plugins/jui/core.min.js',
					'/webstatic/js/plugins/jui/chart.min.js',
					]
		}
	,
	'datepicker':{
		'js' : [
		        '/webstatic/js/plugins/datepicker/bootstrap-datepicker.js'
		        ]
		,'css' : [
	          '/webstatic/css/datepicker/datepicker.css'
	          ]
	}
	,'fileupload':{
		'js' : [
			'/webstatic/js/plugins/fileupload/filepond.js'
			]
	,'css' : [
		'/webstatic/js/plugins/fileupload/filepond.css'
		]
	}
};

var _defaultOption = {
	logLevel :1
	,httpMethod :{
		get:'get'
		,post:'post'
	}
	,openType:{
		'iframe':'iframe'
		,'popup':'popup'
		,'location':'location'
	}
};

_$base.getContextPathUrl =function (url){
	return url?_$base.contextPath+url:_$base.contextPath;
}

_$base.url = function (type , url){
	if(url !== undefined){
		return _$base.getContextPathUrl(type+ url);
	}else{
		return _$base.getContextPathUrl(type);
	}
};

/**
 * function check
 */
_$base.isFunction = function(obj){
	return typeof obj==='function';
};

/**
 * object check
 */
_$base.isObject = function(obj){
	return typeof obj==='object';
};

/**
 * undefined check
 */
_$base.isUndefined = function(obj){
	return typeof obj==='undefined';
};

/**
 * array contain
 */
_$base.inArray =function(array,val){
    for(var i = 0,l = array.length; i<l; i++){
        if(array[i] == val){return true;}
    }
    return false;
}

/**
 * message
 */
_$base.messageFormat =function (fmt,msgParam){

	if(_$base.isUndefined(msgParam)){
		var reval = VARSQL_LANG[fmt];

		if(!_$base.isUndefined(reval)){
			return reval;
		}
	}else{
		var tmpFmt = VARSQL_LANG[fmt];
		fmt = tmpFmt ? tmpFmt :fmt;
	}

	msgParam = msgParam||{};

	var strFlag = false
		,objFlag = false
		,arrFlag = false;

	var arrLen = -1;

	if(typeof msgParam ==='string'){
		strFlag = true;
	}else{
		objFlag = typeof msgParam ==='object';

		if (objFlag) {
			if(Array.isArray){
				arrFlag =Array.isArray(msgParam);
				arrLen = msgParam.length;
			}else {
				arrFlag = Object.prototype.toString.call(msgParam) === '[object Array]';
			}
			objFlag = arrFlag?false:true;
		}
	}

    this.$$index = 0;

    return fmt.replace(/\{{1,1}([A-Za-z0-9_.]*)\}{1,1}/g, function(match, key) {
		if(strFlag){
			return msgParam;
		}else if(arrFlag){
			if (key === '') { // {}
				key = this.$$index;
				this.$$index++
			}

			if(key < arrLen){
				return msgParam[key];
			}
			return match;
		}else{
			return typeof msgParam[key] !== 'undefined' ? msgParam[key] : match;
		}
    });
	return _langInfo(key,lang);
}
//웹 로그 쌓기
_$base.log={
	debug : function (msg){
		if( _defaultOption.logLevel > 1 ) return ;
		Array.prototype.unshift.call(arguments,"vsql debug : ");
		this._consoleApply('debug',arguments);
	}
	,info : function (msg){
		if( _defaultOption.logLevel > 2 ) return ;
		Array.prototype.unshift.call(arguments, "vsql info : ");
		this._consoleApply('info',arguments);
	}
	,warn : function (msg){
		if( _defaultOption.logLevel > 3 ) return ;
		Array.prototype.unshift.call(arguments, "vsql warn : ");
		this._consoleApply('warn',arguments);
	}
	,error : function (msg){
		if( _defaultOption.logLevel > 4 ) return ;
		Array.prototype.unshift.call(arguments, "vsql error : ");
		this._consoleApply('error',arguments);
	}
	,_consoleApply : function (method, args){
		var i, s,
			fn = window.console ? window.console[method] : null;
		if(fn){
			if(fn.apply){
				console.log(args);
				fn.apply(window.console, args);
			}else{
				// IE?
				s = "";
				for( i=0; i<args.length; i++){
					s += args[i];
				}
				fn(s);
			}
		}
	}
};

var $$csrf_token = $("meta[name='_csrf']").attr("content") ||'';
var $$csrf_header = $("meta[name='_csrf_header']").attr("content") ||'';
var $$csrf_param = $("meta[name='_csrf_parameter']").attr("content") ||'';


function fnReqCheck(data ,opts){
	var resultCode = data.resultCode;

	if(opts.disableResultCheck !== true){
		if(resultCode== 401){ // 로그아웃
			if(confirm(_$base.messageFormat('error.0001'))){
				(top || window).location.href=VARSQL.contextPath;
			}
			return false;
		}else if(resultCode == 403){	// error

			var msg = data.message || _$base.messageFormat('error.403');
			alert(msg);
			return false;
		}else if(resultCode == 412){ // 유효하지않은 요청입니다.
			if(confirm(_$base.messageFormat('error.0002'))){
				(top || window).location.href=VARSQL.contextPath;
			}
			return false;
		}else if(resultCode == 500){	// error
			alert(data.message);
			return false;
		}else if(resultCode == 2000){ // 유효하지않은 데이터 베이스
			if(confirm(_$base.messageFormat('error.0003'))){
				(top || window).location.href=VARSQL.contextPath;
			}
			return false;
		}else if(resultCode >= 80000 && resultCode < 90000){ // connection error
			alert(_$base.messageFormat('error.'+resultCode));
			return false;
		}
	}

	return true;
}
/**
 * ajax 요청
 */
_$base.req ={
	isConnectError : false
	,ajax:function (option){
		var _this =this;
		var urlObj = option.url;
		option.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.type, urlObj.url);

		var loadSelector = option.loadSelector ?option.loadSelector :false;

		if(option.dataType == 'jsonp'){
			option.timeout = option.timeout || 10000;
		}
		var ajaxOpt =_$base.util.objectMerge({}, _defaultAjaxOption ,option);

		ajaxOpt.beforeSend = function (xhr){
			xhr.setRequestHeader($$csrf_header, $$csrf_token);

			if($(loadSelector).length > 0){
				$(loadSelector).centerLoading({
					contentClear:false
				});
			}

			if($.isFunction(option.beforeSend)){
				option.beforeSend(xhr);
			}
		}

		ajaxOpt.error = function (xhr){
			if (xhr.readyState == 4) {
				// xhr.status , xhr.statusText check
			}else if (xhr.readyState == 0) { // connection refused , access denied

				if(_this.isConnectError===true){
					return ;
				}
				$(loadSelector).centerLoadingClose();
				alert(_$base.messageFormat('error.0004'));
				_this.isConnectError = true;

				setTimeout(function() {
					_this.isConnectError =false;
				},2000 );

				return ;
			}else {
				//Other errors
			}
		}

		ajaxOpt.success =  function (data, status, jqXHR) {
			_this.isConnectError = false;
			var resultCode = data.resultCode;

			if(!fnReqCheck(data,option)) return ;

			try{
				option.success.call(this, data, status, jqXHR);
			}catch(e){
				$(loadSelector).centerLoadingClose();
				console.log(e);
			}
		}

		$.ajax(ajaxOpt).done(function (xhr){
			if(loadSelector){
				$(loadSelector).centerLoadingClose();
			}
		}).fail(function (xhr){
			if(loadSelector) {
				$(loadSelector).centerLoadingClose();
			}
		});
	}
	,validationCheck : function (resData){
		if(resData.messageCode=='valid'){
			var items = resData.items;
			var objLen = items.length;
			if(objLen >0){
				var item;
				for(var i=0; i <objLen; i++){
					item = items[i];
					alert(item.field + "\n"+ item.defaultMessage)
					return false;
				}
			}
		}
		return true;
	}
	,uploadFile : function (formSelector , opts){
		var _this =this;

		var formData = new FormData($(formSelector)[0]);

		var urlObj = opts.url;

		if(_$base.isUndefined(urlObj)){
			if(opts.multiple===true){
				urlObj = '/upload/multiFile';
			}else{
				urlObj = '/upload/file';
			}
		}

		if(!_$base.isUndefined(opts.param)){
			formData.set('param',JSON.stringify(opts.param));
		}

		opts.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.type, urlObj.url);

		$.ajax({
			type : 'post',
			url : opts.url,
			data : formData,
			processData : false,
			contentType : false,
			beforeSend : function (xhr){
				xhr.setRequestHeader($$csrf_header, $$csrf_token);
			}
			,success : function (data, status, jqXHR) {
				_this.isConnectError = false;

				if(!fnReqCheck(data,opts)) return ;

				try{
					opts.success.call(this, data, status, jqXHR);
				}catch(e){
					console.log(e);
				}
			}
			,error : function (xhr){
				if (xhr.readyState == 4) {
					// xhr.status , xhr.statusText check
				}else if (xhr.readyState == 0) { // connection refused , access denied

					if(_this.isConnectError===true){
						return ;
					}
					$(loadSelector).centerLoadingClose();
					alert(_$base.messageFormat('error.0004'));
					_this.isConnectError = true;

					setTimeout(function() {
						_this.isConnectError =false;
					},2000 );

					return ;
				}else {
					//Other errors
				}
			}
		});
	}
	,ajaxSubmit:function (formid , opts){

		var formObj = $(formid)
			,tmpParam = opts.params?opts.params:{}
			,urlObj = opts.url
			,inputStr = [];

		opts.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.type, urlObj.url);

		var tmpVal;
		for(var key in tmpParam){
			tmpVal = tmpParam[key];
			inputStr.push('<input type="hidden" name="'+key+'" value="'+((typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal))+'"/>');
		}

		formObj.empty();
		formObj.append(inputStr.join(''));

		opts.beforeSubmit=function(arr, formObj, opts) {
			//_cursorWaitStart();
			return true;
		}

		formObj.ajaxSubmit(opts);
	}
	,download :function (opts){
		var tmpMethod = opts.method?opts.method:'post'
			,tmpParam = opts.params?opts.params:{}
			,urlObj = opts.url;

		if($('#varsql_hidden_down_area').length < 1){
			var strHtm = '<div id="varsql_hidden_down_area"style="display:none;">'
				+'<iframe name="varsql_hidden_down_iframe"  style="width:0px;height:0px;"></iframe><div id="varsql_hidden_down_form_area"></div><div>';
			$('body').append(strHtm);
		}

		opts.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.type, urlObj.url);

		var contHtm = [];
		contHtm.push('<form action="'+opts.url+'" method="post" name="varsql_hidden_down_form" target="varsql_hidden_down_iframe">');

		var tmpVal;

		var token = {};
		contHtm.push(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
			'key' : $$csrf_param ,val :$$csrf_token
		}));

		for(var key in tmpParam){
			tmpVal = tmpParam[key];

			contHtm.push(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
				'key' : key ,val : (typeof tmpVal==='string' ?tmpVal:JSON.stringify(tmpVal))
			}));
		}
		contHtm.push('</form>');

		$('#varsql_hidden_down_form_area').empty().html(contHtm.join(''));

		document.varsql_hidden_down_form.submit();

	}
};

jQuery.fn.centerLoading = function(options) {
	this.config = {
		closeAutoYn: 'N' ,
		timeOut:1000,
		action: 'slide',
		height: 0,
		width: 0,
		padding:'0',
		margin :'0',
		top :'0',
		left :'0',
		centerYn:'Y',
		bgColor : '#ffffff',
		loadingImg : _$base.url('/webstatic/css/images/loading.gif'),
		cursor:	'wait',
		contentClear : false
	}

	var id,w,h;

	var config = $.extend({},this.config, options);
	id = this.attr('id');

	w = config.width==0?this.width():config.width;
	h = config.height==0?this.height():config.height;

	if($(this).parent().attr('prevspan') =='Y')	config.contentClear = false;

	var loadStr = '<div class="centerLoading" style="z-index:100;position:absolute;width:100%; height:100%;">'
	if(config.content){
		if(config.centerYn=='Y'){
			loadStr +='<div style="margin: 0;position: absolute;top: 50%;left: 50%;transform: translate(-50%, -50%);">'+ config.content+'</div>';
		}else{
			loadStr += config.content;
		}
	}else{
		loadStr +='<div style="height:100%;background-repeat:no-repeat;cursor:'+config.cursor+';background-image:url('+config.loadingImg+');background-position:'+(config.centerYn=='Y'?'center center':'')+'"></div>';
	}
	loadStr +='</div>';

	if(!config.contentClear){
		this.prepend(loadStr);
	}else{
		this.html(loadStr);
	}

	var cssPosition = this.css('position');

	if (cssPosition != 'relative' &&  cssPosition != 'absolute') {
		this.css('position','relative');
		var heightVal = this.css('height') ||'';
		heightVal = heightVal.replace('px','');
		var addCssKey = 'relative';
		this.attr('var-css-key',addCssKey);
	}
	config.action == 'slide'?jQuery(this).slideDown('slow') : config.action == 'fade'?jQuery(this).fadeIn('slow'):jQuery(this).show();

	return this;
};

jQuery.fn.centerLoadingClose= function(options) {

	this.find('.centerLoading').remove();
	var posVal = (this.attr('var-css-key')||'');
	if(posVal.indexOf('relative') > -1){
		this.css('position','');
		this.removeAttr('var-css-key');
	}
};

_$base.progress = {
	start:function (divObj){
		try{
			var obj = $(divObj);

			var modalcls = divObj.replace(/^[.#]/, '');

			$(divObj).prepend('<div class="'+modalcls+'dialog-modal transbg" style="position:absolute;z-index:100000;text-align:center;border:1px solid;background: #CCC; filter:alpha(opacity=50); -moz-opacity:0.5; opacity: 0.5;display:table-cell;vertical-align:middle"><span><span style="font-weight:bold;background: #fff;">기다리시오....인내심을 가지고..</span></span></div>');

			$("."+modalcls +'dialog-modal > span').css('line-height',obj.outerHeight() +'px');
			$("."+modalcls +'dialog-modal').css('width',obj.outerWidth());
			$("."+modalcls +'dialog-modal').css('height',obj.outerHeight());
			$("."+modalcls +'dialog-modal').show();
		}catch(e){
			_$base.log(e);
		}
	},
	end :function (divObj){
		try{
			$('.'+divObj.replace(/^[.#]/, '') +'dialog-modal').hide();
		}catch(e){
			_$base.log(e);
		}
	}
};

_$base.isDataEmpty =function (opt){
	return $.isEmptyObject(opt);
};

/**
 add csrf html
 */
_$base.addCsrfElement =function (eleSelector){
	$(eleSelector).append(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
		'key' : $$csrf_param ,val :$$csrf_token
	}));
	return $.isEmptyObject(opt);
};

/**
 *check box util
 */
_$base.check = {
	getCheckVal:function (opt){
		var option ={delim:','}
			,rv = [];

		if ( typeof opt === "string" ) {
			option.selector = opt;
		}

		$(option.selector+':checked').each(function (){
			rv.push($(this).val());
		});

		return rv;
	}
	,allCheck : function (allSelector, itemSelector){

		if($(allSelector).is(":checked")){
			$(itemSelector).prop("checked", true);
		}else{
			$(itemSelector).prop("checked", false);
		}
	}
	,radio:function (itemSelector){
		return $(itemSelector+':checked').val()
	}
};
/**
 * @method _$base.unload
 * @description 페이지 빠져나가기
 */
_$base.unload =function (){
	// F5, ctrl + F5, ctrl + r 새로고침 막기
	$(document).keydown(function (e) {
		var keychk = false;
	    if (e.which === 116) {
            if (typeof e == "object") {
                e.keyCode = 0;
            }
            keychk = true;
        } else if (e.which === 82 && e.ctrlKey) {
        	keychk = true;
        }

	    if(keychk){
			if(!confirm(_$base.messageFormat('varsql.0001'))){
				return false;
			}else{
				$('#varsql_page_load_msg_wrapper').show();
				location.reload();
	        }
	    }
	});
}

/**
 * js, css 동적 로드
 */
_$base.loadResource = function (resource, type){
	var _self = _$base;
	function _load(_url , type){
		_url = _self.getContextPathUrl(_url);
		try{
			if(type == 'js'){
				$('head').append($('<script src="'+_url+'"><\/script>'));
			}else{
				$('head').append('<link rel="stylesheet" href="'+_url+'">');
			}
		}catch(e){
			_$base.log.info(e)
			_$base.log.error(e);
		};
	}

	if(Object.prototype.toString.call(resource) == '[object Array]'){
		var len = resource.length;
		for(var i =0; i <len ; i++){
			if(typeof resource === 'object'){
				_self.loadResource(resource[i]);
			}else{
				if(type){
					_load(resource[i],type);
				}else{
					_load(resource[i],_$base.endsWith(resource,'.js')?'js':'css');
				}
			}
		}
	}else if(typeof resource === 'object'){
		for (var key in resource) {
			_self.loadResource(resource[key], key);
		}
	}else if(typeof resource === 'string'){
		_load(resource,_self.endsWith(resource,'.js')?'js':'css');
	}
}

/**
 * unique 한값 찾기
 */
_$base.generateUUID = function() {
	var d = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = (d + Math.random()*16)%16 | 0;
		d = Math.floor(d/16);
		return (c=='x' ? r : (r&0x7|0x8)).toString(16);
	});
	return uuid;
};

/**
 * 매칭되는 글자수
 */
_$base.matchCount = function(str,ms) {
	var re = new RegExp(ms, "ig");
    var resultArray = str.match(re);
    return resultArray.length;
};

/**
 * 글자 끝부분 맞는지 체크
 */
_$base.endsWith = function(str ,searchString, position) {
    var subjectString = str.toString();
    if (position === undefined || position > subjectString.length) {
      position = subjectString.length;
    }
    position -= searchString.length;
    var lastIndex = subjectString.indexOf(searchString, position);
    return lastIndex !== -1 && lastIndex === position;
};

//array으로 변환
function paramToArray(param){
	var tmpArr = new Array();
	var tmpVal;
	for(var key in param) {
		if(key) {
			tmpVal = param[key];
			tmpArr.push( key+'='+ ( (typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal) ) );
		}
	}
	return tmpArr;
}

function getParameter(url, param){

	var paramSplit  = url.split('?');
	var paramLen = paramSplit.length;

	if(paramLen < 2) return param;

	var rtnval = param ? param : new Object();
	var parameters = paramSplit[1].split('&');

	var tmpKey='';
	for(var i = 0 ; i < parameters.length ; i++){
		var tmpPara = parameters[i].split('=');
		tmpKey=tmpPara[0];
		if(!rtnval[tmpKey]){
			rtnval[tmpKey]=tmpPara[1];
		}
	}

	if(paramLen > 2){
		var lastParam = '';
		for(var i=2; i<paramLen; i ++){
			lastParam = lastParam+'?'+paramSplit[i];
		}
		rtnval[tmpKey] = rtnval[tmpKey]+lastParam
	}

	return rtnval;
}

/**
 * @method VARSQL.str
 * @param str
 * @description 글자 형식 처리.
 */
_$base.str = {
	trim : function(str) {
		return str.replace(/(^\s*)|(\s*$)/gi,'');
	}
	,allLineTrim : function(str) {
		return str.replace(/^\s+|\s+$/gm,'');
	}
}


_$base.util = {
	/**
	 * @method VARSQL.util.calcDate
	 * @param date
	 * @param masks
	 * @description 날짜 계산
	 */
	calcDate:function (date,num,type) {
		var a = new Date(date);
		var format = "yyyy-mm-dd";
		if(type=='m'){
			a.setMonth(a.getMonth() + num);
			format="yyyy-mm";
		}else{
			a.setDate(a.getDate()+num);
		}
		return this.dateFormat(a,format);
	}
	,renderHtml : function (template, renderItem){
		return Mustache.render(template, renderItem);
	}
	/**
	 * @method VARSQL.util.dateFormat
	 * @param date
	 * @param masks
	 * @description 날짜 포켓 변환
	 */
	,dateFormat : function(date, masks){
		return dateFormat(date, masks);
	}
	/**
     * @method VARSQL.util.removeSpecial
     * @param str
     * @description 특수문자 제거
     */
	,removeSpecial :function (str){
		return str.replace(/[-&\/\\#,+()$~%.'":*?<>{}]/g,'');
	}
	/**
     * @method VARSQL.util.setRangeDate
     * @param sdtObj
     * @param edtObj
     * @param cdt
     * @param rangeNum
     * @description 날자 범위 지정하기
     */
	,setRangeDate :function(sdtObj, edtObj, cdt, rangeNum, type){

		if(isNaN(rangeNum)) return false;

		var _self = this;
		var flag = rangeNum >0 ?true:false;

		sdtObj = $(sdtObj);
		edtObj = $(edtObj);

		if(flag){
			var sdt = sdtObj.val()
				,tmped = _self.calcDate(sdt, rangeNum,type);

			if(parseInt(_self.removeSpecial(sdt), 10) > parseInt(_self.removeSpecial(cdt),10)){
				sdtObj.val(cdt);
				tmped = cdt;
			}else if(parseInt(_self.removeSpecial(tmped), 10) > parseInt(_self.removeSpecial(cdt),10)){
				tmped = cdt;
			}

			edtObj.val(tmped);
		}else{
			var sdt = edtObj.val()
				,tmped = _self.calcDate(sdt,rangeNum, type);

			if(parseInt(_self.removeSpecial(sdt), 10) > parseInt(_self.removeSpecial(cdt),10)){
				edtObj.val(cdt);
				tmped = _self.calcDate(cdt, rangeNum, type);
			}
			sdtObj.val(tmped);
		}
	}
	,copyObject : function (obj){
		return this.objectMerge({},obj);
	}
	/**
	 * @method objectMerge
	 * @param target
	 * @param source
	 * @description object merge
	 */
	,objectMerge : function () {
		var objMergeRecursive = function (dst, src) {

			for (var p in src) {
				if (!src.hasOwnProperty(p)) {continue;}

				var srcItem = src[p] ;
				if (srcItem=== undefined) {continue;}

				if ( typeof srcItem!== 'object' || srcItem=== null) {
					dst[p] = srcItem;
				} else if (typeof dst[p]!=='object' || dst[p] === null) {
					dst[p] = objMergeRecursive(srcItem.constructor===Array ? [] : {}, srcItem);
				} else {
					objMergeRecursive(dst[p], srcItem);
				}
			}
			return dst;
		}

		var reval = arguments[0];
		if (typeof reval !== 'object' || reval === null) {	return reval;}
		for (var i = 1, il = arguments.length; i < il; i++) {
			objMergeRecursive(reval, arguments[i]);
		}
		return reval;
	}
	,escapeHTML : function(html) {
	    var fn=function(tag) {
	        var charsToReplace = {
	            '&': '&amp;',
	            '<': '&lt;',
	            '>': '&gt;',
	            '"': '&#34;'
	        };
	        return charsToReplace[tag] || tag;
	    }
	    return html.replace(/[&<>"]/g, fn);
	}
	,getCharLength : function (s ,b ,i , c){
		for(b=i=0; c=s.charCodeAt(i++); b+=c >>11?2:c>>7?2:1);
		return b;
	}
	,getConvertCamelObject : function (obj){
		var param = {};
		for(var key in obj){
			param[_fnConvertCamel(key)] = obj[key];
		}
		return param;
	}
	,removeUnderscore : function (str, lowerCaseFlag){
	    if(str == '') {
	        return str;
	    }

	    if(lowerCaseFlag){
	    	str = str.toLowerCase();
	    }
	    // conversion
	    var returnStr = str.replace(/_(\w)/g, function(word) {
	        return word;
	    });
	    returnStr = returnStr.replace(/_/g, "");

	    return returnStr;
	}

	// camel 변환
	,convertCamel : _fnConvertCamel
	,convertUnderscoreCase : _fnConvertUnderscoreCase
	,toLowerCase: _fnToLowerCase
	,toUpperCase : _fnToUpperCase
	,capitalize : function (str){
		return str.charAt(0).toUpperCase() + str.slice(1);
	}
	,paramToArray : paramToArray
	,getParameter : getParameter
	,browserSize : function(){
		return {
			width : (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth)
			,height : (window.innerHeight || document.documentElement.clientHeight ||document.body.clientHeight)
		}

	}
}

/**
 * 숫자 계산
 */
_$base.math = {
	cal :function(a, b , calType, fixNum) {

		a = isNaN(a) ? 0 : a;
		b = isNaN(b) ? 0 : b;

		var reval =0.0;
		if(calType=='+'){
			reval= a+b;
		}else if(calType=='/'){
			reval= a/b;
		}else if(calType=='%'){
			reval= a%b;
		}else if(calType=='*'){
			reval= a*b;
		}

		if(fixNum){
			return reval.toFixed(fixNum);
		}else{
			var dotIdxA =(a+'').split('.')[1];
			dotIdxA = dotIdxA?dotIdxA.length : 0;

			var dotIdxB =(b+'').split('.')[1];
			dotIdxB = dotIdxB?dotIdxB.length : 0;

			fixNum= dotIdxA >dotIdxB?dotIdxA : dotIdxB;

			if(fixNum < 1){
				var revalDot =(reval+'').split('.')[1];
				revalDot = revalDot?revalDot.length : 0;
				fixNum= fixNum >revalDot?fixNum : revalDot;
			}
		}

		return fixNum > 0 ? reval.toFixed(fixNum) : reval;
	}
	,sum :function(a, b) {
		return a+b;
	}
	//배열 sum
	,arraySum :function(array, key) {
		if(array.length < 1) return 0;

		var _self = this;

		var result =0.0, tmpVal= 0.0, maxFixed=0,dotIdx
			,flag = key ? true :false;

		$.each(array, function(){
			tmpVal = flag?this[key]:this;

			if(isNaN(tmpVal)){
				tmpVal = (tmpVal+'').replace(/[$,]+/g,'');
				tmpVal = isNaN(tmpVal)?0:tmpVal;
			}

			dotIdx =(tmpVal+'').split('.')[1];
			dotIdx = dotIdx?dotIdx.length : maxFixed;
			maxFixed = maxFixed > dotIdx?maxFixed :dotIdx;

			result = (_self.sum(parseFloat(result) , parseFloat(tmpVal))).toFixed(maxFixed);
		});

		return result;
	}
	// 배열 평균 구하기 함수
	,average : function (array, key) {
		if(array.length < 1) return 0;
		var tmpSum = this.arraySum(array,key);

		var dotIdx =(tmpSum+'').split('.')[1];
		dotIdx = dotIdx?dotIdx.length : 0;

		return (tmpSum / array.length).toFixed(dotIdx);
	}
}


/** -------------------------plugin-------------------------------------- **/
var dateFormat = function () {
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = dateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

// Some common format strings
dateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoShortTime:   "HH:MM",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
	dayNames: [
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	],
	monthNames: [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
};

function contains(arr , element) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] == element) {
			return true;
		}
	}
	return false;
}

//camel 변환
function _fnConvertCamel(camelStr){

    if(camelStr == '') {
        return camelStr;
    }
    camelStr = _fnToLowerCase(camelStr);
    // conversion
    var returnStr = camelStr.replace(/_(\w)/g, function(word) {
        return _fnToUpperCase(word);
    });
    returnStr = returnStr.replace(/_/g, "");

    return returnStr;
}
//camel case -> underscorecase 변환
function _fnConvertUnderscoreCase(str){
	if(str == '') {
		return str;
	}
	return str.split(/(?=[A-Z])/).join('_').toUpperCase();
}

function _fnToLowerCase(str){
	return (str || '').toLowerCase()
}

function _fnToUpperCase(str){
	return (str || '').toUpperCase()
}

window.VARSQL = _$base;

})( window );


(function defineMustache(global,factory){if(typeof exports==="object"&&exports&&typeof exports.nodeName!=="string"){factory(exports)}else if(typeof define==="function"&&define.amd){define(["exports"],factory)}else{global.Mustache={};factory(global.Mustache)}})(this,function mustacheFactory(mustache){var objectToString=Object.prototype.toString;var isArray=Array.isArray||function isArrayPolyfill(object){return objectToString.call(object)==="[object Array]"};function isFunction(object){return typeof object==="function"}function typeStr(obj){return isArray(obj)?"array":typeof obj}function escapeRegExp(string){return string.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g,"\\$&")}function hasProperty(obj,propName){return obj!=null&&typeof obj==="object"&&propName in obj}var regExpTest=RegExp.prototype.test;function testRegExp(re,string){return regExpTest.call(re,string)}var nonSpaceRe=/\S/;function isWhitespace(string){return!testRegExp(nonSpaceRe,string)}var entityMap={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;","/":"&#x2F;","`":"&#x60;","=":"&#x3D;"};function escapeHtml(string){return String(string).replace(/[&<>"'`=\/]/g,function fromEntityMap(s){return entityMap[s]})}var whiteRe=/\s*/;var spaceRe=/\s+/;var equalsRe=/\s*=/;var curlyRe=/\s*\}/;var tagRe=/#|\^|\/|>|\{|&|=|!/;function parseTemplate(template,tags){if(!template)return[];var sections=[];var tokens=[];var spaces=[];var hasTag=false;var nonSpace=false;function stripSpace(){if(hasTag&&!nonSpace){while(spaces.length)delete tokens[spaces.pop()]}else{spaces=[]}hasTag=false;nonSpace=false}var openingTagRe,closingTagRe,closingCurlyRe;function compileTags(tagsToCompile){if(typeof tagsToCompile==="string")tagsToCompile=tagsToCompile.split(spaceRe,2);if(!isArray(tagsToCompile)||tagsToCompile.length!==2)throw new Error("Invalid tags: "+tagsToCompile);openingTagRe=new RegExp(escapeRegExp(tagsToCompile[0])+"\\s*");closingTagRe=new RegExp("\\s*"+escapeRegExp(tagsToCompile[1]));closingCurlyRe=new RegExp("\\s*"+escapeRegExp("}"+tagsToCompile[1]))}compileTags(tags||mustache.tags);var scanner=new Scanner(template);var start,type,value,chr,token,openSection;while(!scanner.eos()){start=scanner.pos;value=scanner.scanUntil(openingTagRe);if(value){for(var i=0,valueLength=value.length;i<valueLength;++i){chr=value.charAt(i);if(isWhitespace(chr)){spaces.push(tokens.length)}else{nonSpace=true}tokens.push(["text",chr,start,start+1]);start+=1;if(chr==="\n")stripSpace()}}if(!scanner.scan(openingTagRe))break;hasTag=true;type=scanner.scan(tagRe)||"name";scanner.scan(whiteRe);if(type==="="){value=scanner.scanUntil(equalsRe);scanner.scan(equalsRe);scanner.scanUntil(closingTagRe)}else if(type==="{"){value=scanner.scanUntil(closingCurlyRe);scanner.scan(curlyRe);scanner.scanUntil(closingTagRe);type="&"}else{value=scanner.scanUntil(closingTagRe)}if(!scanner.scan(closingTagRe))throw new Error("Unclosed tag at "+scanner.pos);token=[type,value,start,scanner.pos];tokens.push(token);if(type==="#"||type==="^"){sections.push(token)}else if(type==="/"){openSection=sections.pop();if(!openSection)throw new Error('Unopened section "'+value+'" at '+start);if(openSection[1]!==value)throw new Error('Unclosed section "'+openSection[1]+'" at '+start)}else if(type==="name"||type==="{"||type==="&"){nonSpace=true}else if(type==="="){compileTags(value)}}openSection=sections.pop();if(openSection)throw new Error('Unclosed section "'+openSection[1]+'" at '+scanner.pos);return nestTokens(squashTokens(tokens))}function squashTokens(tokens){var squashedTokens=[];var token,lastToken;for(var i=0,numTokens=tokens.length;i<numTokens;++i){token=tokens[i];if(token){if(token[0]==="text"&&lastToken&&lastToken[0]==="text"){lastToken[1]+=token[1];lastToken[3]=token[3]}else{squashedTokens.push(token);lastToken=token}}}return squashedTokens}function nestTokens(tokens){var nestedTokens=[];var collector=nestedTokens;var sections=[];var token,section;for(var i=0,numTokens=tokens.length;i<numTokens;++i){token=tokens[i];switch(token[0]){case"#":case"^":collector.push(token);sections.push(token);collector=token[4]=[];break;case"/":section=sections.pop();section[5]=token[2];collector=sections.length>0?sections[sections.length-1][4]:nestedTokens;break;default:collector.push(token)}}return nestedTokens}function Scanner(string){this.string=string;this.tail=string;this.pos=0}Scanner.prototype.eos=function eos(){return this.tail===""};Scanner.prototype.scan=function scan(re){var match=this.tail.match(re);if(!match||match.index!==0)return"";var string=match[0];this.tail=this.tail.substring(string.length);this.pos+=string.length;return string};Scanner.prototype.scanUntil=function scanUntil(re){var index=this.tail.search(re),match;switch(index){case-1:match=this.tail;this.tail="";break;case 0:match="";break;default:match=this.tail.substring(0,index);this.tail=this.tail.substring(index)}this.pos+=match.length;return match};function Context(view,parentContext){this.view=view;this.cache={".":this.view};this.parent=parentContext}Context.prototype.push=function push(view){return new Context(view,this)};Context.prototype.lookup=function lookup(name){var cache=this.cache;var value;if(cache.hasOwnProperty(name)){value=cache[name]}else{var context=this,names,index,lookupHit=false;while(context){if(name.indexOf(".")>0){value=context.view;names=name.split(".");index=0;while(value!=null&&index<names.length){if(index===names.length-1)lookupHit=hasProperty(value,names[index]);value=value[names[index++]]}}else{value=context.view[name];lookupHit=hasProperty(context.view,name)}if(lookupHit)break;context=context.parent}cache[name]=value}if(isFunction(value))value=value.call(this.view);return value};function Writer(){this.cache={}}Writer.prototype.clearCache=function clearCache(){this.cache={}};Writer.prototype.parse=function parse(template,tags){var cache=this.cache;var tokens=cache[template];if(tokens==null)tokens=cache[template]=parseTemplate(template,tags);return tokens};Writer.prototype.render=function render(template,view,partials){var tokens=this.parse(template);var context=view instanceof Context?view:new Context(view);return this.renderTokens(tokens,context,partials,template)};Writer.prototype.renderTokens=function renderTokens(tokens,context,partials,originalTemplate){var buffer="";var token,symbol,value;for(var i=0,numTokens=tokens.length;i<numTokens;++i){value=undefined;token=tokens[i];symbol=token[0];if(symbol==="#")value=this.renderSection(token,context,partials,originalTemplate);else if(symbol==="^")value=this.renderInverted(token,context,partials,originalTemplate);else if(symbol===">")value=this.renderPartial(token,context,partials,originalTemplate);else if(symbol==="&")value=this.unescapedValue(token,context);else if(symbol==="name")value=this.escapedValue(token,context);else if(symbol==="text")value=this.rawValue(token);if(value!==undefined)buffer+=value}return buffer};Writer.prototype.renderSection=function renderSection(token,context,partials,originalTemplate){var self=this;var buffer="";var value=context.lookup(token[1]);function subRender(template){return self.render(template,context,partials)}if(!value)return;if(isArray(value)){for(var j=0,valueLength=value.length;j<valueLength;++j){buffer+=this.renderTokens(token[4],context.push(value[j]),partials,originalTemplate)}}else if(typeof value==="object"||typeof value==="string"||typeof value==="number"){buffer+=this.renderTokens(token[4],context.push(value),partials,originalTemplate)}else if(isFunction(value)){if(typeof originalTemplate!=="string")throw new Error("Cannot use higher-order sections without the original template");value=value.call(context.view,originalTemplate.slice(token[3],token[5]),subRender);if(value!=null)buffer+=value}else{buffer+=this.renderTokens(token[4],context,partials,originalTemplate)}return buffer};Writer.prototype.renderInverted=function renderInverted(token,context,partials,originalTemplate){var value=context.lookup(token[1]);if(!value||isArray(value)&&value.length===0)return this.renderTokens(token[4],context,partials,originalTemplate)};Writer.prototype.renderPartial=function renderPartial(token,context,partials){if(!partials)return;var value=isFunction(partials)?partials(token[1]):partials[token[1]];if(value!=null)return this.renderTokens(this.parse(value),context,partials,value)};Writer.prototype.unescapedValue=function unescapedValue(token,context){var value=context.lookup(token[1]);if(value!=null)return value};Writer.prototype.escapedValue=function escapedValue(token,context){var value=context.lookup(token[1]);if(value!=null)return mustache.escape(value)};Writer.prototype.rawValue=function rawValue(token){return token[1]};mustache.name="mustache.js";mustache.version="2.3.0";mustache.tags=["{{","}}"];var defaultWriter=new Writer;mustache.clearCache=function clearCache(){return defaultWriter.clearCache()};mustache.parse=function parse(template,tags){return defaultWriter.parse(template,tags)};mustache.render=function render(template,view,partials){if(typeof template!=="string"){throw new TypeError('Invalid template! Template should be a "string" '+'but "'+typeStr(template)+'" was given as the first '+"argument for mustache#render(template, view, partials)")}return defaultWriter.render(template,view,partials)};mustache.to_html=function to_html(template,view,partials,send){var result=mustache.render(template,view,partials);if(isFunction(send)){send(result)}else{return result}};mustache.escape=escapeHtml;mustache.Scanner=Scanner;mustache.Context=Context;mustache.Writer=Writer;return mustache});
