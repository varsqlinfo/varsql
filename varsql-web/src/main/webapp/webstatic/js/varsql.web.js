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

(function( window ) {
'use strict';

if (typeof Object.assign != 'function') {
  // Must be writable: true, enumerable: false, configurable: true
  Object.defineProperty(Object, "assign", {
    value: function assign(target, varArgs) { // .length of function is 2
      'use strict';
      if (target == null) { // TypeError if undefined or null
        throw new TypeError('Cannot convert undefined or null to object');
      }

      var to = Object(target);

      for (var index = 1; index < arguments.length; index++) {
        var nextSource = arguments[index];

        if (nextSource != null) { // Skip over if undefined or null
          for (var nextKey in nextSource) {
            // Avoid bugs when hasOwnProperty is shadowed
            if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
              to[nextKey] = nextSource[nextKey];
            }
          }
        }
      }
      return to;
    },
    writable: true,
    configurable: true
  });
}

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
		,'ignore' :''
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
			'/webstatic/js/plugins/file/dropzone.js',
		]
		,'css' : [
			'/webstatic/js/plugins/file/dropzone.css',
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
function isFunction(obj){
	return typeof obj==='function';
};

_$base.isFunction =isFunction;

/**
 * object check
 */
function isObject(obj){
	return typeof obj==='object';
};
_$base.isObject =isObject;

/**
 * String check
 */
function isString(val) {
   return typeof val === 'string' || ((!!val && typeof val === 'object') && Object.prototype.toString.call(val) === '[object String]');
}

_$base.isString = isString;

/**
 * array check
 */
function isArray(obj){
	if(Array.isArray){
		return Array.isArray(obj)
	}else{
		return Object.prototype.toString.call(obj) === '[object Array]';
	}
}

_$base.isArray = isArray;

/**
 * undefined check
 */
function isUndefined(obj){
	return typeof obj==='undefined';
};
_$base.isUndefined = isUndefined;

/**
 * empty  check
 */
function isEmpty( obj ) {
    return (isUndefined(obj) || obj == null);
}
_$base.isEmpty = isEmpty;

/**
 * blank  check undefined  ||  null || ''
 */
function isBlank(obj){
	return  isEmpty( obj ) || (isString(obj)  && _$base.str.trim(obj) == '')  ;
};
_$base.isBlank = isBlank;

function parseJSON(resText){
	return JSON.parse(resText);
};
_$base.parseJSON = parseJSON;

/**
 * array contain
 */
_$base.inArray =function(array,val){
    for(var i = 0,l = array.length; i<l; i++){
        if(array[i] == val){return i;}
    }
    return -1;
}

/**
 * object , array , string length
 */
_$base.getLength = function (val){
	if(isObject(val)){
		return Object.keys(val).length; 
	}else if(isArray(val)){
		return val.length; 
	}else if(isString(val)){
		return val.length; 
	}else {
		return val; 
	}
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
		,arrFlag = false;

	var arrLen = -1;

	if(typeof msgParam ==='string'){
		strFlag = true;
	}else{
		arrFlag = isArray(msgParam);
		if(arrFlag){
			arrLen = msgParam.length;
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
	opts = opts ||{};
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
				(top || window).location.href=(top || window).location.href;
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
_$base.reqCheck = fnReqCheck; 

/**
 * ajax 요청
 */
_$base.req ={
	isConnectError : false
	,ajax:function (option){
		var _this =this;
		var urlObj = option.url;

		if(typeof urlObj ==='string'){
			option.url =  _$base.url(urlObj)
		}else{
			if(urlObj.type=='ignore'){
				option.url = urlObj.url;
			}else{
				option.url = _$base.url(urlObj.type, urlObj.url);
			}
		}

		var loadSelector = option.loadSelector ?option.loadSelector :false;

		if(option.dataType == 'jsonp'){
			option.timeout = option.timeout || 10000;
		}
		var ajaxOpt =_$base.util.objectMerge({}, _defaultAjaxOption ,option);

		ajaxOpt.beforeSend = function (xhr){
			xhr.setRequestHeader($$csrf_header, $$csrf_token);
			xhr.setRequestHeader('WWW-Authenticate', 'Basic realm=varsql');

			if($(loadSelector).length > 0){
				$(loadSelector).centerLoading({
					contentClear:false
				});
			}

			if(_$base.isFunction(option.beforeSend)){
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
			
			if(isArray(items)){
				var objLen = items.length;
				if(objLen >0){
					var item = items[0];
					alert(item.field + "\n"+ item.defaultMessage)
					return false;
				}
			}else{
				alert(resData.message);
				return false;
			}
			
		}
		return true;
	}
	,uploadFile : function (formSelector , opts){
		var _this =this;

		var formData = new FormData($(formSelector)[0]);

		var urlObj = opts.url;

		if(_$base.isUndefined(urlObj)){
			urlObj = '/file/upload';
		}
		var param = opts.param; 
		if(!_$base.isUndefined(param)){
			for(var key in param){
				formData.set(key,param[key]);
			}
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
				$('body').centerLoading();
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
					$('body').centerLoadingClose();
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
		}).done(function (xhr){
			$('body').centerLoadingClose();
		}).fail(function (xhr){
			$('body').centerLoadingClose();
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


$(window).on("beforeunload",function(){
	_$base.socket.close();
})

_$base.logout = function (callback){
	if(_$base.isFunction(callback)){
		_$base.req.ajax({
			url :{type : 'ignore', url : $varsqlConfig.logoutUrl } 
			,success: callback
		})
		return ; 
	}
	
	locatiion.href=$varsqlConfig.logoutUrl;
	
}

_$base.socket ={
	stompClient : null
	,connRetryCount : 0
	,maxRetry :10
	,subScripeActive :{}
	//알림 수신
	,addSubscribe : function (endpoint, opts){
		
		var subscribeId = '/sub/'+endpoint+'/'+opts.uid; 
		
		if(this.subScripeActive[subscribeId]===true){
			return ; 
		}
		
		this.subScripeActive[subscribeId] =true; 
		
		this.stompClient.subscribe(subscribeId, function (data) {
    		if(_$base.isFunction(opts.callback)){
    			opts.callback.call(null, parseJSON( data.body));
    		}
    	});
	}
	,connect : function(endpoint, opts){
		var _this = this;
		opts = opts ||{};
		
		if(_$base.isUndefined(endpoint))  return ;

		if(_this.stompClient==null){
			_this.createConnection(endpoint, opts);
		}
	}
	, createConnection : function (endpoint, opts){
		var _this = this;
		
		this.subScripeActive = {};
		
		var stompClient = Stomp.over(new SockJS(_$base.getContextPathUrl("/ws/"+ endpoint) , null, {transports : ['websocket'] }));
		stompClient.heartbeat.outgoing = 20000;
		stompClient.heartbeat.incoming = 20000;
		stompClient.reconnect_delay = 5000;
		stompClient.debug = function (str) {
	       //console.log('STOMP: ' + str);
		}
		
		stompClient.connect({}, function (frame) {
			_this.addSubscribe(endpoint, opts);
		}, function(err){
			console.log(err);
	    });
		
		_this.stompClient = stompClient;
		
		return stompClient; 
	}
	,
	// 알림 연결 끊기
	close : function(){
		if(this.stompClient != null){
			this.stompClient.disconnect();
		}
	}
}

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
_$base.unload =function (mode){
	// F5, ctrl + F5, ctrl + r 새로고침 막기
	$(document).keydown(function (e) {
		var keychk = false;

		var keyCode = e.which;

		if (keyCode === 122) {
			return false;
		}else if (keyCode === 116) {
            if (typeof e == "object") {
                e.keyCode = 0;
            }
            keychk = true;
        } else if (keyCode === 82 && e.ctrlKey) {
        	keychk = true;
        }

	    if(keychk){
			if(!confirm(_$base.messageFormat('varsql.0001'))){
				return false;
			}else{
				e.preventDefault()
                e.stopPropagation()
				
				
				
				if(mode=='top'){
					if(window.userMain) {
						window.userMain.pageRefresh();
						return false; 
					}
				}
				
				if(top != window){
					if(top.userMain){
						top.userMain.viewLoadMessage();
					}
				}
				location.reload();
	        }
	    }
	});
}



/**
 * js, css 동적 로드
 */

function _load(_url , type ,resourceName){
	_url = _$base.getContextPathUrl(_url);
	try{
		if(type == 'js'){
			$('head').append($('<script resource-name="'+resourceName+'" src="'+_url+'"><\/script>'));
		}else{
			$('head').append('<link resource-name="'+resourceName+'" rel="stylesheet" href="'+_url+'">');
		}
	}catch(e){
		_$base.log.info(e)
		_$base.log.error(e);
	};
}

function _loadProxy (resourceName){
	var resourceObj = _$base.staticResource.get(resourceName);
	
	if(isUndefined(resourceObj)){
		throw 'empty resource name : ['+ resourceName+']'; 
	}
	
	if($('[resource-name="'+resourceName+'"]').length > 0){
		throw 'duplicate resource load : ['+ resourceName+']';
	}
	
	for(var key in resourceObj){
		var items = resourceObj[key];
		
		for(var j =0; j< items.length;j++){
			var resourcePath = items[j]; 
			_load(resourcePath,key, resourceName);
		}
	}
}
_$base.loadResource = function (resources){
	var _self = _$base;
	
	if(isArray(resources)){
		var len = resources.length;
		for(var i =0; i <len ; i++){
			_loadProxy(resources[i]);
		}
	}else{
		_loadProxy(resources);
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
	,allTrim : function(str) {
		return str.replace(/\s/g, '');
	}
	,allLineTrim : function(str) {
		return str.replace(/^\s+|\s+$/gm,'');
	}
	// 왼쪽 공백 제거
	,ltrim : function(str) {
		 return str.replace(/^\s+/,"");
	}
	// 오른쪽 공백 제거
	,rtrim : function(str) {
		 return str.replace(/\s+$/,"");
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
		return VARSQLTemplate.render.html(template, renderItem);
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
	return (str || '').toLowerCase();
}

function _fnToUpperCase(str){
	return (str || '').toUpperCase();
}

window.VARSQL = _$base;

})( window );