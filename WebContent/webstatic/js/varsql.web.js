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
		,'user':'/user'
		,'guest':'/guest'
		,'database':'/database'
	}
};

_$base.staticResource  ={
	get:function (type){
		return this[type];
	}
	,
	'chart' : 
		{
			'js' : [
			        '/webstatic/js/plugins/flot/jquery.flot.min.js',
					'/webstatic/js/plugins/flot/jquery.flot.time.min.js',
					'/webstatic/js/plugins/flot/jquery.flot.canvas.min.js',
					'/webstatic/js/plugins/flot/jquery.flot.tooltip.js',
					'/webstatic/js/plugins/flot/jquery.flot.axislabels.js',
					'/webstatic/js/plugins/flot/jquery.flot.pie.min.js' 
					]
		}
	,'juiChart' : 
		{
			'js' : [
			        '/webstatic/js/plugins/jui/core.min.js',
					'/webstatic/js/plugins/jui/chart.min.js',
					]
		}
	,
	'jqgrid' : {
			'js' : [ 
			         '/webstatic/js/jquery.jqGrid.min.js' 
			       ]
			,'css' : [ 
			           '/webstatic/css/ui.jqgrid.css' 
			        ]
	}
	,
	'daterange':{
		'js' : [
		        '/webstatic/js/plugins/daterange/moment.js'
		        ,'/webstatic/js/plugins/daterange/daterangepicker.js'
		        ]
		,'css' : [
		          '/webstatic/css/plugins/daterange/daterangepicker-bs3.css'
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
	,
	'jqueryui':{
		'js' : [
		       
		        ]
		,'css' : [
	          '/webstatic/css/jquery-ui.min.css'
	          ,'/webstatic/css/jquery-ui-custom.css'
	          ]
	}
	,
	'contextMenu':{
		'js' : [
		       '/webstatic/js/pub.context.js' 
		        ]
		,'css' : [
	          '/webstatic/css/pub.grid.css'
	          ]
	}
	,
	'pubgrid':{
		'js' : [
		        '/webstatic/js/pub.grid.js' 
		        ]
	,'css' : [
	          '/webstatic/css/pub.grid.css'
	          ]
	}
	,
	'jquery.contextMenu':{
		'js' : [
		        '/webstatic/js/plugins/contextMenu/jquery.contextMenu.js' 
		        ]
	,'css' : [
	          '/webstatic/css/jquery.contextMenu.css'
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

_$base.url = function (gubun , url){
	if(url !== undefined){
		return _$base.getContextPathUrl(gubun+ url);
	}else{
		return _$base.getContextPathUrl('/' +gubun);
	}
};

/**
 * alert 띄우기
 */
_$base.alert =function (opt){
	var option ={
		delim:','
	};
	
	if ( typeof opt === "string" ) {
		alert(opt);
	}
};
/**
 * confirm 띄우기
 */
_$base.confirm =function (opt){
	return confirm(opt);
};

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

/**
 * ajax 요청
 */
_$base.req ={
	ajax:function (option){
		var loadSelector = option.loadSelector ?option.loadSelector :false;
		var urlObj = option.url;
		option.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.gubun, urlObj.url);  
		
		$.ajax($.extend({}, {
			type :'get'
			,cache: false
			,dataType: "json"
			,beforeSend : function( xhr ) {
				
				if(loadSelector){
					if(loadSelector=='#editorAreaTable'){
						$('#sqlEditerPreloaderArea').show();
					}
					$(loadSelector).centerLoading({
						contentClear:false 
					});
				}
			}
			,error : function (data, status, err){
				if(loadSelector) {
					if(loadSelector=='#editorAreaTable'){
						$('#sqlEditerPreloaderArea').hide(1000);
					}
					$(loadSelector).centerLoadingClose();
				}
			}
			,complete: function (data, status, err){
				if(loadSelector){
					if(loadSelector=='#editorAreaTable'){
						$('#sqlEditerPreloaderArea').hide(1000);
					}
					$(loadSelector).centerLoadingClose();
				} 
			}	
		},option));
	}
	,ajaxSubmit:function (formid , opts){
		
		var formObj = $(formid)
			,tmpParam = opts.params?opts.params:{}
			,urlObj = opts.url
			,inputStr = [];
		
		opts.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.gubun, urlObj.url);  
		
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
	,download :function (formid , opts){
		var formObj = $(formid)
			,tmpMethod = opts.method?opts.method:'post'
			,tmpParam = opts.params?opts.params:{}
			,urlObj = opts.url
			,inputStr = [];
		
		opts.url = (typeof urlObj) ==='string' ? _$base.url(urlObj) :_$base.url(urlObj.gubun, urlObj.url);  
		
		var tmpVal;
		for(var key in tmpParam){
			tmpVal = tmpParam[key];
			inputStr.push('<input type="hidden" name="'+key+'" value="'+((typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal))+'"/>');
		}
		
		formObj.empty();
		formObj.append(inputStr.join(''));
		formObj.prop('method',tmpMethod);
		formObj.prop('action',opts.url);

		formObj.submit();
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
		
	var firstDiv = $('<div style="'+(!config.contentClear?"position:absolute;":"")+'width:'+w+'px; height:'+h+';" class="centerLoading"></div>');
	var centerLoading = $('<div style="background-repeat:no-repeat;"></div>');
	centerLoading.css('background-image', 'url("'+config.loadingImg+'")')
				.css('background-position', config.centerYn=='Y'?'center center':'')
				.css('height', h)
				.css('width',w)
				.css('cursor', config.cursor);
	
	firstDiv.html(centerLoading);

	if(!config.contentClear){
		this.prepend(firstDiv);
	}else{
		this.html(firstDiv);
	}

	if(config.content != ""){
		centerLoading.appendTo(config.content);
	}
	
	config.action == 'slide'?jQuery(this).slideDown('slow') : config.action == 'fade'?jQuery(this).fadeIn('slow'):jQuery(this).show();
	
	return this;
};

jQuery.fn.centerLoadingClose= function(options) {
	this.find('.centerLoading').remove();
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
			if(!VARSQL.confirm(VARSQL.messageLang('varsql.0001','페이지를 나가시겠습니까?'))){
				return false;
			}else{
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
	function _load(_url , gubun){
		_url = _self.getContextPathUrl(_url);
		try{
			if(gubun == 'js'){
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

/**
* url open 메소드
* view 필수 항복  url, type , options{gubun:'menu , portlet, sso 등등'}
* ex : 
* domino ex : portalUICntl.page.view("comm.prototype",'domino',{gubun:'portlet', target:'contentArea'});
* popup ex : portalUICntl.page.view("http://devportal01.amorepacific.com/",'popup',{gubun:'menu', name:'popup name', mehtod:'get or post',viewOption:'toolbar=yes, scrollbars=yes, resizable=yes, top=500, left=500, width=400, height=400'});
* location ex : portalUICntl.page.view("http://devportal01.amorepacific.com/",'location',{gubun:'menu'});
* dominoPopup ex : portalUICntl.page.view("/bbs/app.nsf/0/?opendocument",'dominoPopup',{argv:'&type=1&aa=ss', feathers:{width:'800',height:'500'},langpack:{langpack:"bbs.comm", langprefix:"BBS.COMM"});
* 
*/
_$base.link ={
	view : function(url, type, options){
		var _self = this; 
		if(_defaultOption.openType.iframe== type){
			_self._iframe(url, options);
		}else if(_defaultOption.openType.popup== type){
			_self._popup(url, options);
		}else{
			_self._location(url);
		}
	}
	,_location:function (url){
		location.href=url;
	}
	,_popup:function (url, options){
		var targetId = '_$base_'+_$base.generateUUID().replace(/-/g,''); 
		var tmpParam = options.param?options.param:{};
		var tmpMethod = options.method?options.method:'get';
		var tmpPopOption = options.viewOption?options.viewOption:'';
		var tmpName ='AP_'+(options.name?( options.name.replace(/[ \{\}\[\]\/?.,;:|\)*~`!^\-+┼<>@\#$%&\'\"\\(\=]/gi,'') ):targetId.replace(/-/g,''));
		
		tmpParam=getParameter(url , tmpParam);
		var urlIdx = url.indexOf('?');
		var openUrl = urlIdx > 0 ?url.substring(0,urlIdx):url;
		
		// get method
		if(_defaultOption.httpMethod.get ==tmpMethod){
			tmpParam=paramToArray(tmpParam);
			
			openUrl= (tmpParam.length > 0) ?(openUrl+'?'+tmpParam.join('&')):url;
			
			try{
				var myWindow=window.open(openUrl,tmpName, tmpPopOption);
				myWindow.focus();
			}catch(e){}
		}else{  // post method
			var inputStr = [];
			
			inputStr.push('<form action="'+openUrl+'" method="post" id="'+targetId+'" name="'+targetId+'">');
			
			var tmpVal;
			for(var key in tmpParam){
				tmpVal = tmpParam[key];
				inputStr.push('<input type="hidden" name="'+key+'" value=\''+((typeof tmpVal==='string')?tmpVal:JSON.stringify(tmpVal))+'\'/>');
			}
			inputStr.push('</form>');
			inputStr.push('<script type="text/javascript">document.'+targetId+'.submit();</script>');
			
			var tmpPopupObj=window.open('about:blank', tmpName, tmpPopOption);

			try{
				tmpPopupObj.document.write(inputStr.join(''));
				tmpPopupObj.focus();
			}catch(e){
				_$base.log.info(inputStr.join(''))
				_$base.log.error(e);
			}
		}
	}
	,_iframe:function (url,options){
		var tmpiframe =$(options.target);
		var tmpParam = options.param?options.param:{};
		tmpParam=getParameter(url , tmpParam);
		
		var urlIdx = url.indexOf('?');
		var openUrl = urlIdx > 0 ?url.substring(0,urlIdx):url;
		
		tmpParam=paramToArray(tmpParam);
		openUrl= (tmpParam.length > 0) ?(openUrl+'?'+tmpParam.join('&')):url;
		
		tmpiframe.attr('src', openUrl);
		var cstyle=tmpiframe.attr('style');
		tmpiframe =tmpiframe.attr('style', cstyle+';'+ options.viewOption);
	}
}


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

_$base.util = {
	/**
	 * @method EWPFUEL.util.calcDate
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
	/**
	 * @method EWPFUEL.util.dateFormat
	 * @param date
	 * @param masks
	 * @description 날짜 포켓 변환
	 */
	,dateFormat : function(date, masks){
		return dateFormat(date, masks);
	}
	/**
     * @method EWPFUEL.util.removeSpecial
     * @param str
     * @description 특수문자 제거
     */
	,removeSpecial :function (str){
		return str.replace(/[-&\/\\#,+()$~%.'":*?<>{}]/g,'');
	}
	/**
     * @method EWPFUEL.util.setRangeDate
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
	/**
	 * @method objectMerge
	 * @param target
	 * @param source
	 * @description object merge
	 */	
	,objectMerge : function () {
		var dst = {},src ,p ,args = [].splice.call(arguments, 0);
		
		while (args.length > 0) {
			src = args.splice(0, 1)[0];
			if (Object.prototype.toString.call(src) == '[object Object]') {
				for (p in src) {
					if (src.hasOwnProperty(p)) {
						if (Object.prototype.toString.call(src[p]) == '[object Object]') {
							dst[p] = _$base.util.objectMerge(dst[p] || {}, src[p]);
						} else {
							dst[p] = src[p];
						}
					}
				}
			}
		}
		return dst;
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

window.VARSQL = _$base; 

})( window );



/**
 * javascript function 
 * @param first
 * @param second
 * @returns {___anonymous3771_6484}
 */
function selectBoxMove(first,second){
	var actionObj ={
		firstSelect:first
		,secondSelect:second
		,init:function (){
			this.initEvent();
		}
		,initEvent:function (){
			
			$(this.firstSelect).unbind('dblclick')
			$(this.firstSelect).bind("dblclick", function(){
				actionObj.firstMove();
			});
			
			$(this.secondSelect).unbind('dblclick')
			$(this.secondSelect).bind("dblclick", function(){
				actionObj.secondMove();
			});
		}
		,allFirstMove:function (){
			$(this.firstSelect).children().each(function (i, item){
				$(this.secondSelect).append('<option value="'+$(item).val()+'">'+$(item).html()+'</option>');
				$(item).remove();
			});
		}
		,firstMove:function (){
			var selectVal = $(this.firstSelect).val();

			if(selectVal){
				if(selectVal.length >0){
					
					var tmpVal = '';
					selectVal=selectVal.join('|')+'|';
					

					$(this.firstSelect).children().each(function (i, item){
						tmpVal=$(item).val()+'|';

						if(selectVal.indexOf(tmpVal) > -1){
							$(actionObj.secondSelect).append('<option value="'+$(item).val()+'">'+$(item).html()+'</option>');
							$(item).remove();
						}
					});
				}
			}else{
				VARSQL.alert(VARSQL.messageLang('varsql.0003','추가할 그룹정보를 선택해주세요.'));
				return ;
			}
		}
		,secondMove:function (){
			var selectVal = $(this.secondSelect).val();

			if(selectVal){
				if(selectVal.length >0){
					selectVal=selectVal.join('|')+'|';
					var tmpVal = '';
					$(this.secondSelect).children().each(function (i, item){

						tmpVal=$(item).val()+'|';

						if(selectVal.indexOf(tmpVal) > -1){
							$(actionObj.firstSelect).append('<option value="'+$(item).val()+'">'+$(item).html()+'</option>');
							$(item).remove();
						}
					});
				}
			}else{
				VARSQL.alert(VARSQL.messageLang('varsql.0004','삭제할 그룹정보를 선택해주세요.'));
				return ;
			}
		}
		,getAddItem:function (){
			var reInfo = new Array();
			$(this.secondSelect).children().each(function (i, item){
				reInfo.push($(item).val())
			});

			return reInfo;
		}
		,move:function (type){
			if(type=='up'){
				$($(this.secondSelect).val()).each(function (i, item){
					$(this.secondSelect).children().each(function (i , cItem){
						if($(cItem).val() ==item){
							if($(cItem).prev().length > 0){
								$(cItem).after($($(cItem).prev()).clone().wrapAll('<div/>').parent().html());
								$($(cItem).prev()).remove();
							}
						}
					});
				});
			}else{
				var rightVal = $(this.secondSelect).val();
				var len  = rightVal.length;
				var item;
				for(var i=len; i > 0 ; i--){
					item  = rightVal[i-1];

					$(this.secondSelect).children().each(function (j , cItem){
						if($(cItem).val() ==item){
							if($(cItem).next().length > 0){
								$(cItem).before($($(cItem).next()).clone().wrapAll('<div/>').parent().html());
								$($(cItem).next()).remove();
							}
						}
					});
				}
			}
		}
	}
	
	return actionObj; 
}


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
		
		$('.page-click').on('click',function (){
			var sNo = $(this).attr('pageno');
			
			if (typeof callback == 'function') {
		        callback(sNo);
		    }else{
		    	try{
		    		nextPage(sNo);
		    	}catch(e){}
		    }
		});
	};
	
})(jQuery);