/*
**
*ytkim
*varsql common js
 */
if (typeof window != "undefined") {
	if (typeof window.VARSQL == "undefined") {
		window.VARSQL = {};
	}
} else {
	if (!VARSQL) {
		VARSQL = {};
	}
}

(function (window) {
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
		version: '0.1'
		, logLevel: 1
		, author: 'ytkim'
		, contextPath: (typeof global_page_context_path === 'undefined' ? '/vsql' : global_page_context_path)
		, uri: {
			'admin': '/admin'
			, 'manager': '/manager'
			, 'join': '/join'
			, 'user': '/user'
			, 'guest': '/guest'
			, 'database': '/database'
			, 'sql': '/sql'
			, 'plugin': '/plugin'
			, 'ignore': ''
			, 'progress': '/progress'
		}
	}
		, _defaultAjaxOption = {
			method: 'post'
			, cache: false
			, dataType: "json"
		}
		, speicalChar = {
			'|': '[|]'
			, '+': '[+]'
			, '$': '[$]'
			, '*': '[*]'
			, '(': '\\('
			, ')': '\\)'
			, '{': '\\{'
			, '}': '\\}'
			, '[': '\\['
			, ']': '\\]'
			, '\\': '\\\\\\\\'
		};

	_$base.staticResource = {
		get: function (type) {
			return this[type];
		}
		, 'juiChart':
		{
			'js': [
				'/webstatic/js/plugins/jui/core.min.js',
				'/webstatic/js/plugins/jui/chart.min.js',
			]
		}
		,
		'datepicker': {
			'js': [
				'/webstatic/js/plugins/datepicker/bootstrap-datepicker.js'
			]
			, 'css': [
				'/webstatic/css/datepicker/datepicker.css'
			]
		}
		, 'fileupload': {
			'js': [
				'/webstatic/js/plugins/file/dropzone.js',
			]
			, 'css': [
				'/webstatic/js/plugins/file/dropzone.css'
			]
		}
	};

	_$base.getContextPathUrl = function (url) {
		return url ? _$base.contextPath + url : _$base.contextPath;
	}

	_$base.url = function (type, url) {
		if (url !== undefined) {
			return _$base.getContextPathUrl(type + url);
		} else {
			return _$base.getContextPathUrl(type);
		}
	};

	/**
	 * function check
	 */
	function isFunction(obj) {
		return typeof obj === 'function';
	};

	_$base.isFunction = isFunction;

	/**
	 * object check
	 */
	function isObject(obj) {
		if (isArray(obj)) {
			return false;
		} else if (isFunction(obj)) {
			return false;
		}

		return typeof obj === 'object';
	};
	_$base.isObject = isObject;

	/**
	 * String check
	 */
	function isString(val) {
		return typeof val === 'string' || ((!!val && typeof val === 'object') && Object.prototype.toString.call(val) === '[object String]');
	}
	_$base.isString = isString;


	/**
	 * property check
	 */
	var hasOwnProperty = Object.prototype.hasOwnProperty;
	function hasProperty(obj, key) {
		return hasOwnProperty.call(obj, key);
	}

	_$base.hasProperty = hasProperty;

	/**
	 * array check
	 */
	function isArray(obj) {
		if (Array.isArray) {
			return Array.isArray(obj)
		} else {
			return Object.prototype.toString.call(obj) === '[object Array]';
		}
	}

	_$base.isArray = isArray;

	/**
	 * undefined check
	 */
	function isUndefined(obj) {
		return typeof obj === 'undefined';
	};
	_$base.isUndefined = isUndefined;

	/**
	 * empty  check
	 */
	function isEmpty(obj) {
		return (isUndefined(obj) || obj == null);
	}
	_$base.isEmpty = isEmpty;

	/**
	 * blank  check undefined  ||  null || ''
	 */
	function isBlank(obj) {
		return isEmpty(obj) || (isString(obj) && _$base.str.trim(obj) == '');
	};
	_$base.isBlank = isBlank;

	function isDate(obj) {
		if (obj instanceof Date) return true;

		if (isObject(obj)) {
			return typeof obj.toDateString === 'function'
				&& typeof obj.getDate === 'function'
				&& typeof obj.setDate === 'function';
		}

		return false;
	}
	_$base.isDate = isDate;

	function parseJSON(resText) {
		return JSON.parse(resText);
	};
	_$base.parseJSON = parseJSON;

	/**
	 * array contain
	 */
	_$base.inArray = function (array, val) {
		for (var i = 0, l = array.length; i < l; i++) {
			if (array[i] == val) { return i; }
		}
		return -1;
	}

	/**
	 * object , array , string length
	 */
	_$base.getLength = function (val) {
		if (isObject(val)) {
			return Object.keys(val).length;
		} else if (isArray(val)) {
			return val.length;
		} else if (isString(val)) {
			return val.length;
		} else {
			return val;
		}
	}


	/**
	 * @method VARSQL.localStorage
	 * @param opt {string,object} object :{key, value, remove, clear}
	 * @description 글자 형식 처리.
	 */
	_$base.localStorage = function (opt) {

		if (isString(opt)) {
			return localStorage.getItem(opt);
		} else {
			var key = opt.key;
			if (opt.clear === true) {
				return localStorage.clear();
			} else if (opt.remove === true) {
				return localStorage.removeItem(key)
			} else {
				return localStorage.setItem(key, opt.value || '');
			}
		}
	}


	/**
	 * message
	 */
	_$base.confirmMessage = function (fmt, msgParam) {
		return confirm(_$base.message(fmt, msgParam));
	}
	_$base.alertMessage = function (fmt, msgParam) {
		return VARSQLUI.alert.open(_$base.message(fmt, msgParam));
	}
	_$base.toastMessage = function (fmt, msgParam) {
		return VARSQLUI.toast.open(_$base.message(fmt, msgParam));
	}
	_$base.message = function (fmt, msgParam) {

		var msgFormat = VARSQL_LANG[fmt];

		if (_$base.isUndefined(msgFormat)) {
			msgFormat = fmt;
		}

		if (_$base.isUndefined(msgParam)) {
			return msgFormat;
		}

		msgParam = msgParam || {};

		var strFlag = false
			, arrFlag = false;

		var arrLen = -1;

		if (typeof msgParam === 'string') {
			strFlag = true;
		} else {
			arrFlag = isArray(msgParam);
			if (arrFlag) {
				arrLen = msgParam.length;
			}
		}

		this.$$index = 0;

		return msgFormat.replace(/\{{1,1}([A-Za-z0-9_.]*)\}{1,1}/g, function (match, key) {
			if (strFlag) {
				return msgParam;
			} else if (arrFlag) {
				if (key === '') { // {}
					key = this.$$index;
					this.$$index++
				}

				if (key < arrLen) {
					return msgParam[key];
				}
				return match;
			} else {
				return typeof msgParam[key] !== 'undefined' ? msgParam[key] : match;
			}
		});
	}
	//웹 로그 쌓기
	_$base.log = {
		debug: function (msg) {
			if (_$base.logLevel > 1) return;
			Array.prototype.unshift.call(arguments, "vsql debug : ");
			this._consoleApply('debug', arguments);
		}
		, info: function (msg) {
			if (_$base.logLevel > 2) return;
			Array.prototype.unshift.call(arguments, "vsql info : ");
			this._consoleApply('info', arguments);
		}
		, warn: function (msg) {
			if (_$base.logLevel > 3) return;
			Array.prototype.unshift.call(arguments, "vsql warn : ");
			this._consoleApply('warn', arguments);
		}
		, error: function (msg) {
			if (_$base.logLevel > 4) return;
			Array.prototype.unshift.call(arguments, "vsql error : ");
			this._consoleApply('error', arguments);
		}
		, _consoleApply: function (method, args) {
			var i, s,
				fn = window.console ? window.console[method] : null;
			if (fn) {
				if (fn.apply) {
					console.log(args);
					fn.apply(window.console, args);
				} else {
					// IE?
					s = "";
					for (i = 0; i < args.length; i++) {
						s += args[i];
					}
					fn(s);
				}
			}
		}
	};
	var _currentAjaxUid;
	var pageReloadCheckFlag = false;
	function fnReqCheck(data, opts) {

		if (pageReloadCheckFlag) return;

		opts = opts || {};

		var resultCode = data.resultCode;

		if (resultCode == 401) { // 로그아웃
			if (confirm(_$base.message('error.0001'))) {
				pageReloadCheckFlag = true;
				(top || window).location.href = VARSQL.contextPath;
			}
			return false;
		} else if (resultCode == 403) {	// error

			var msg = data.message || _$base.message('error.403');
			alert(msg);
			return false;
		} else if (resultCode == 412) { // 유효하지않은 요청입니다.
			if (confirm(_$base.message('error.0002'))) {
				pageReloadCheckFlag = true;
				(top || window).location.href = (top || window).location.href;
			}
			return false;
		} else if (resultCode == 1000) {	// error
			if (data.message) {
				alert(data.message);
			} else {
				alert('app error');
			}

			return false;
		} else if (resultCode == 2000) { // 유효하지않은 데이터 베이스
			if (confirm(_$base.message('error.0003'))) {
				pageReloadCheckFlag = true;
				(top || window).location.href = VARSQL.contextPath;
			}
			return false;
		} else if (resultCode >= 80000 && resultCode < 90000) { // connection error
			var msgCode = 'error.' + resultCode;
			var msg = _$base.message(msgCode, { errorCode: resultCode });

			if (msgCode == msg) {
				msg = _$base.message('error.default', { errorCode: resultCode });
			}

			alert(msg);

			return false;
		}

		var statusCode = data.status;
		if (opts.disableResultCheck !== true) {
			if (statusCode != 200) {
				if (resultCode == 500) {	// error
					PROGRESS_BAR_STATUS=false;
					alert(data.message);
					return false;
				} else if (resultCode != 200) {
					PROGRESS_BAR_STATUS=false;
					if (data.messageCode) {
						alert('request check : ' + data.messageCode);
					} else {
						alert('request check : ' + data.message);
					}
					return false;
				}
			}
		}

		return true;
	}
	_$base.reqCheck = fnReqCheck;

	var $$csrf_token = $("meta[name='_csrf']").attr("content") || '';
	var $$csrf_header = $("meta[name='_csrf_header']").attr("content") || '';
	var $$csrf_param = $("meta[name='_csrf_parameter']").attr("content") || '';

	/**
	 * ajax 요청
	 */
	var ALL_REQ_STATUS = {};
	var PROGRESS_BAR_STATUS = false;
	_$base.req = {
		isConnectError: false

		, ajax: function (option) {
			var _this = this;
			var urlObj = option.url;

			if (typeof urlObj === 'string') {
				option.url = _$base.url(urlObj)
			} else {
				if (urlObj.type == 'ignore') {
					option.url = urlObj.url;
				} else {
					option.url = _$base.url(urlObj.type, urlObj.url);
				}
			}

			var loadSelector = option.loadSelector ? option.loadSelector : false;

			if (option.dataType == 'jsonp') {
				option.timeout = option.timeout || 10000;
			}

			var ajaxUid = _$base.generateUUID();

			_currentAjaxUid = ajaxUid;

			var ajaxOpt = _$base.util.objectMerge({}, _defaultAjaxOption, option, (option.ignoreUid !== true ? { data: { _requid_: ajaxUid } } : {}));

			var cancelFlag = false;

			ajaxOpt.beforeSend = function (xhr) {
				xhr.setRequestHeader($$csrf_header, $$csrf_token);
				xhr.setRequestHeader('WWW-Authenticate', 'Basic realm=varsql');

				if ($(loadSelector).length > 0) {

					if (option.enableLoadSelectorBtn) {
						ALL_REQ_STATUS[ajaxUid] = true;
					}

					$(loadSelector).centerLoading({
						contentClear: false
						, enableLoadSelectorBtn: option.enableLoadSelectorBtn
						, callback: function () {
							cancelFlag = true;
							//xhr.abort();
							databaseReqCancel(ajaxUid);
						}
					});
				}

				if (_$base.isFunction(option.beforeSend)) {
					option.beforeSend(xhr);
				}
			}

			ajaxOpt.error = function (xhr) {
				if (cancelFlag) {
					return;
				}

				if (xhr.readyState == 4) {
					// xhr.status , xhr.statusText check
				} else if (xhr.readyState == 0) { // connection refused , access denied

					if (_this.isConnectError === true) {
						return;
					}

					$(loadSelector).centerLoadingClose();
					alert(_$base.message('error.0004'));
					_this.isConnectError = true;

					_$base.log.error(xhr);

					setTimeout(function () {
						_this.isConnectError = false;
					}, 2000);

					return;
				} else {
					//Other errors
				}
			}

			ajaxOpt.success = function (data, status, jqXHR) {
				_this.isConnectError = false;

				if (ajaxOpt.dataType == 'json') {
					if (!fnReqCheck(data, option)) return;
				}

				try {
					option.success.call(this, data, status, jqXHR);
				} catch (e) {
					$(loadSelector).centerLoadingClose();
					console.log(e);
				}
			}

			$.ajax(ajaxOpt).done(function (xhr) {
				delete ALL_REQ_STATUS[ajaxUid];
				if (loadSelector) {
					$(loadSelector).centerLoadingClose();
				}
			}).fail(function (xhr) {
				PROGRESS_BAR_STATUS=false;
				delete ALL_REQ_STATUS[ajaxUid];
				if (loadSelector) {
					$(loadSelector).centerLoadingClose();
				}
			});
		}
		, validationCheck: function (resData) {
			if (resData.messageCode == 'valid') {
				var items = resData.list;

				if (isArray(items)) {
					var objLen = items.length;
					if (objLen > 0) {
						var item = items[0];
						alert(item.field + "\n" + item.defaultMessage)
						return false;
					}
				} else {
					alert(resData.message);
					return false;
				}

			}
			return true;
		}
		, getCsrf: function () {
			var csrfVal = {};
			csrfVal[$$csrf_header] = $$csrf_token;
			csrfVal[$$csrf_param] = $$csrf_token;

			return csrfVal;
		}
		, uploadFile: function (formSelector, opts) {
			var _this = this;

			var formData = new FormData($(formSelector)[0]);

			var urlObj = opts.url;

			if (_$base.isUndefined(urlObj)) {
				urlObj = '/file/upload';
			}
			var param = opts.param;
			if (!_$base.isUndefined(param)) {
				for (var key in param) {
					formData.set(key, param[key]);
				}
			}

			opts.url = (typeof urlObj) === 'string' ? _$base.url(urlObj) : _$base.url(urlObj.type, urlObj.url);

			$.ajax({
				type: 'post',
				url: opts.url,
				data: formData,
				processData: false,
				contentType: false,
				beforeSend: function (xhr) {
					xhr.setRequestHeader($$csrf_header, $$csrf_token);
					$('body').centerLoading();
				}
				, success: function (data, status, jqXHR) {
					_this.isConnectError = false;

					if (!fnReqCheck(data, opts)) return;

					try {
						opts.success.call(this, data, status, jqXHR);
					} catch (e) {
						console.log(e);
					}
				}
				, error: function (xhr) {
					if (xhr.readyState == 4) {
						// xhr.status , xhr.statusText check
					} else if (xhr.readyState == 0) { // connection refused , access denied

						if (_this.isConnectError === true) {
							return;
						}
						$('body').centerLoadingClose();
						alert(_$base.message('error.0004'));
						_this.isConnectError = true;

						setTimeout(function () {
							_this.isConnectError = false;
						}, 2000);

						return;
					} else {
						//Other errors
					}
				}
			}).done(function (xhr) {
				$('body').centerLoadingClose();
			}).fail(function (xhr) {
				$('body').centerLoadingClose();
			});
		}
		, ajaxSubmit: function (formid, opts) {

			var formObj = $(formid)
				, tmpParam = opts.params ? opts.params : {}
				, urlObj = opts.url
				, inputStr = [];

			opts.url = (typeof urlObj) === 'string' ? _$base.url(urlObj) : _$base.url(urlObj.type, urlObj.url);

			var tmpVal;
			for (var key in tmpParam) {
				tmpVal = tmpParam[key];
				inputStr.push('<input type="hidden" name="' + key + '" value="' + ((typeof tmpVal === 'string') ? tmpVal : JSON.stringify(tmpVal)) + '"/>');
			}

			formObj.empty();
			formObj.append(inputStr.join(''));

			opts.beforeSubmit = function (arr, formObj, opts) {
				//_cursorWaitStart();
				return true;
			}

			formObj.ajaxSubmit(opts);
		}
		, download: function (opts) {
			var _this = this;
			
			var DOWNLOAD_MODE = {1 :'direct', 2:'ajax-direct', 3:'ajax'};
			
			var _downloadMode  = VARSQL.isUndefined(DOWNLOAD_MODE[opts.mode]) ? 1 : opts.mode;

			var tmpParam = opts.params ? opts.params : {}
				, urlObj = opts.url;

			var loadSelector = (opts.loadSelector ? opts.loadSelector : 'body');

			tmpParam[$$csrf_param] = $$csrf_token;
			
			if(VARSQL.isUndefined(tmpParam.progressUid)){
				tmpParam.progressUid = VARSQL.generateUUID();	
			}

			if(_downloadMode==1){
				fileDownload(opts);
				return; 
			}
			
			try {
				PROGRESS_BAR_STATUS = true; 
				var ajaxOpt = {
					type: "POST",
					cache: false,
					url: (typeof urlObj) === 'string' ? _$base.url(urlObj) : _$base.url(urlObj.type, urlObj.url),
					data: tmpParam,
					beforeSend: function (xhr) {
						xhr.setRequestHeader($$csrf_header, $$csrf_token);

						if ($(loadSelector).length > 0) {

							$(loadSelector).centerLoading({
								contentClear: false
								,content:''
							});
						}
					}
					, success: function (response, status, xhr) {
						if(_downloadMode==2){
							opts.url = response.item;
							setTimeout(function (){
								fileDownload(opts);	
							},1)
							return; 
						}
						
						var filename = "";
						var disposition = xhr.getResponseHeader('Content-Disposition');

						var filenameIdx = disposition.indexOf('filename');
						filename = filenameIdx < 0 ? 'filename-empty' : disposition.substring(filenameIdx).replace(/filename[- ]?=[- ]?"/, '').replace(/[-"]?[- ]?;[- ]?$/, '');
						filename = decodeURIComponent(filename);

						var type = xhr.getResponseHeader('Content-Type');
						var blob = new Blob([response], { type: type });

						saveAs(blob, filename)
					}
					, error: function (xhr) {
						PROGRESS_BAR_STATUS = false; 
						if (xhr.readyState == 4) {
							// xhr.status , xhr.statusText check
						} else if (xhr.readyState == 0) { // connection refused , access denied

							if (_this.isConnectError === true) {
								return;
							}
							$('body').centerLoadingClose();
							alert(_$base.message('error.0004'));
							_this.isConnectError = true;

							setTimeout(function () {
								_this.isConnectError = false;
							}, 2000);

							return;
						} else {
							//Other errors
						}
					}
				};
				
				if(_downloadMode==2){
					ajaxOpt.dataType="json";	
				}else{
					ajaxOpt.xhr= function () {
						var xhr = new XMLHttpRequest();
						xhr.onreadystatechange = function () {
							if (xhr.readyState == 2) {
								if (xhr.status == 200) {
									xhr.responseType = "blob";
								} else {
									xhr.responseType = "text";
								}
							}
						};
						return xhr;
					}
				}
				
				$.ajax(ajaxOpt).done(function (xhr) {
					if (loadSelector) {
						$(loadSelector).centerLoadingClose();
					}
				}).fail(function (xhr) {
					PROGRESS_BAR_STATUS = false; 
					if (xhr.status == 404) {
						alert('File not found');
					} else if (xhr.status == 401) {
						alert('Unauthorized');
					} else {
						if (xhr.responseText) {
							try{
								var responseData = JSON.parse(xhr.responseText);
								if (responseData.message) {
									alert(responseData.message);
								} else {
									alert('File download error');
								}
							}catch(e){
								alert('File download error\n'+e.message);
							}
						}
					}

					if (loadSelector) {
						$(loadSelector).centerLoadingClose();
					}
				})
				
				if(opts.progressBar===true){
					_$base.req.progressInfo({
						progressUid : tmpParam.progressUid
						,callback : function (resData){
							var item = resData.item; 
							
							if(item == 'fail'){
								$(loadSelector +' .center-loading-centent').text('fail');
							}else if(item == 'complete'){
								$(loadSelector +' .center-loading-centent').text('complete');
							}else{
								if(item != null){
									var progressText = VARSQL.util.numberFormat(item.progressContentLength)+'';
							
									if(item.totalContentLength && item.totalContentLength > 0){
										progressText += ' / ' + VARSQL.util.numberFormat(item.totalContentLength);
									}
									
									if(item.name){
										progressText = item.name + '('+progressText + ')';
									}
									
									$(loadSelector +' .center-loading-centent').text(progressText);
								}
							}
						} 
					});
				}
			} catch (e) {
				fileDownload(opts);
			}
		}
		
		, progressInfo : function(progressBarInfo){
			progressBar(progressBarInfo);
		}
		, stopProgress : function(){
			PROGRESS_BAR_STATUS=false;
		}
	};
	
	function fileDownload(opts){
		var tmpParam = opts.params ? opts.params : {}
			, urlObj = opts.url;

		if ($('#varsql_hidden_down_area').length < 1) {
			var strHtm = '<div id="varsql_hidden_down_area"style="display:none;">'
				+ '<iframe name="varsql_hidden_down_iframe"  style="width:0px;height:0px;"></iframe><div id="varsql_hidden_down_form_area"></div><div>';
			$('body').append(strHtm);
		}

		opts.url = (typeof urlObj) === 'string' ? _$base.url(urlObj) : _$base.url(urlObj.type, urlObj.url);

		var contHtm = [];
		contHtm.push('<form action="' + opts.url + '" method="post" name="varsql_hidden_down_form" target="varsql_hidden_down_iframe">');

		var tmpVal;

		contHtm.push(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
			'key': $$csrf_param, val: $$csrf_token
		}));

		for (var key in tmpParam) {
			tmpVal = tmpParam[key];

			contHtm.push(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
				'key': key, val: (typeof tmpVal === 'string' ? tmpVal : JSON.stringify(tmpVal))
			}));
		}
		contHtm.push('</form>');

		$('#varsql_hidden_down_form_area').empty().html(contHtm.join(''));

		document.varsql_hidden_down_form.submit();
	}
	
	function progressBar(progressBarInfo){
		if(PROGRESS_BAR_STATUS===false)return; 
		
		$.ajax({
			type: "POST",
			cache: false,
			dataType: "json",
			url: _$base.url(VARSQL.uri.progress, '/info'),
			data:  {
				progressUid : progressBarInfo.progressUid
				,type : progressBarInfo.type || 'download'
				,keep : (progressBarInfo.keep ==true ?"Y":"N")
			}
			, beforeSend: function (xhr) {
				xhr.setRequestHeader($$csrf_header, $$csrf_token);
			}
			,success: function (resData, status, xhr) {
				
				if(progressBarInfo.type == 'remove'){
					return ; 
				}
				
				var item = resData.item; 
				
				if(progressBarInfo.callback(resData)===false){
					return ; 
				}
					
				if(item != 'fail' && item != 'complete'){
					setTimeout(function() {
						progressBar(progressBarInfo);
					}, progressBarInfo.timeout || 700);
				}
			}
		}).fail(function (xhr) {
			if(progressBarInfo.type !='remove'){
				progressBarInfo.type = 'remove';
				progressBar(progressBarInfo);	
			}
		})
	}

	// database request cancel
	function databaseReqCancel(reqUid) {
		reqUid = reqUid || _currentAjaxUid;

		if (!_$base.isBlank(reqUid) && ($varsqlConfig || {}).conuid) {

			_$base.req.ajax({
				url: { type: VARSQL.uri.database, url: '/reqCancel' }
				, ignoreUid: true
				, data: {
					_requid_: reqUid
					, conuid: ($varsqlConfig || {}).conuid
				}
				, success: function (resData) {
				}
			})
		}
	}

	$(window).on("beforeunload", function () {
		_$base.socket.close();

		var reqKeys = [];
		for (var key in ALL_REQ_STATUS) {
			reqKeys.push(key);
		};

		if (reqKeys.length > 0) {
			databaseReqCancel(reqKeys.join(','));
		}
	})


	_$base.logout = function (callback) {
		if (_$base.isFunction(callback)) {
			_$base.req.ajax({
				url: { type: 'ignore', url: $varsqlConfig.logoutUrl }
				, success: callback
			})
			return;
		}

		locatiion.href = $varsqlConfig.logoutUrl;
	}

	// file upload size
	_$base.getFileMaxUploadSize = function () {
		return $varsqlConfig.file.maxUploadSize || 1000;
	}

	// file unit max size
	_$base.getFileSizePerFile = function () {
		return $varsqlConfig.file.sizePerFile || 1000;
	}

	_$base.socket = {
		stompClient: null
		, isCreate: false
		, connRetryCount: 0
		, maxRetry: 10
		, subscripeActiveMap: {}
		, subscripeObj: {}
		//알림 수신
		, addSubscribe: function (endpoint, headers, callback) {

			var subscribeId = this.getSubscribeId(endpoint, headers.uid);

			if (this.subscripeActiveMap[subscribeId] === true && this.stompClient.connected === true) {
				return;
			}

			this.subscripeActiveMap[subscribeId] = true;

			this.subscripeObj[subscribeId] = this.stompClient.subscribe(subscribeId, function (data) {
				if (_$base.isFunction(callback)) {
					callback.call(null, parseJSON(data.body));
				}
			});
		}
		, getSubscribeId: function (endpoint, id) {
			return '/user/' + endpoint + '.' + id;
		}
		, unSubscribe: function (endpoint, id) {
			var subscribeId = this.getSubscribeId(endpoint, id);
			try {
				delete this.subscripeActiveMap[subscribeId];
				this.subscripeObj[subscribeId].unsubscribe();
			} catch (e) {
				console.log(e);
			}
		}
		, connect: function (endpoint, headers, callback) {
			var _this = this;
			headers = headers || {};

			if (_$base.isUndefined(endpoint)) return;

			if (_this.stompClient == null) {
				_this._createConnection(endpoint, headers, callback);
			} else {
				if (_this.isCreate) {
					_this.close();
					_this._createConnection(endpoint, headers, callback);
					return;
				}

				if (_this.stompClient.connected === true) {
					_this.addSubscribe(endpoint, headers, callback);
				} else {

					var connectTimer = setInterval(function () {
						if (_this.stompClient.connected === true) {
							clearInterval(connectTimer);
							_this.addSubscribe(endpoint, headers, callback);
						}
					}, 1000);
				}
			}
		}
		, _createConnection: function (endpoint, headers, callback) {
			var _this = this;

			this.subscripeActiveMap = {};
			
			//var url = location.protocol + '//' + location.host + _$base.getContextPathUrl("/ws/" + endpoint);
			var url = _$base.getContextPathUrl("/ws/" + endpoint);

			var sockJSConn = new SockJS(url, null, { transports: ['websocket'], timeout: 60000 });
			//sockJSConn._transportTimeout = function() { console.log('gotcha!!!'); };

			var stompClient = Stomp.over(sockJSConn);
			stompClient.heartbeat.outgoing = 20000;
			stompClient.heartbeat.incoming = 20000;
			stompClient.reconnect_delay = 5000;
			stompClient.debug = function (str) {
				//console.log('STOMP: ' + str);
			}

			stompClient.connect({}, function (frame) {
				_this.isCreate = true;
				_this.addSubscribe(endpoint, headers, callback);
			}, function (err) {
				if(_this.stompClient){
					console.log(location.href, err);
				}
			});

			_this.stompClient = stompClient;
			return stompClient;
		}
		,
		// 알림 연결 끊기
		close: function () {
			if (this.stompClient != null) {
				this.stompClient.reconnect_delay = -1;
				this.stompClient.disconnect();
				this.stompClient = null; 
			}
		}
		, isConnect: function () {
			if (this.stompClient != null) {
				return this.stompClient.connected;
			}

			return false;
		}
	}

	jQuery.fn.centerLoading = function (options) {
		this.config = {
			closeAutoYn: 'N',
			timeOut: 1000,
			action: 'slide',
			height: 0,
			width: 0,
			padding: '0',
			margin: '0',
			top: '0',
			left: '0',
			centerYn: 'Y',
			bgColor: '#e8e8e8',
			loadingImg: _$base.url('/webstatic/css/images/loading.gif'),
			cursor: 'wait',
			contentClear: false,
			enableLoadSelectorBtn: false,
			callback: false,
		}

		var id, w, h;

		var config = $.extend({}, this.config, options);
		id = this.attr('id');

		w = config.width == 0 ? this.width() : config.width;
		h = config.height == 0 ? this.height() : config.height;

		if ($(this).parent().attr('prevspan') == 'Y') config.contentClear = false;

		var loadStr = '<div class="centerLoading" style="cursor:' + config.cursor + ';top:0px;left:0px;z-index:100;position:absolute;width:100%; height:100%;">';

		loadStr += '<div style="position:absolute;background: ' + config.bgColor + ';opacity: 0.5; width:100%; height:100%;z-index:1;"></div>';
		
		loadStr += ' <div style="z-index:10; text-align: center; position: absolute; top: 40%;left: 50%; transform: translate(-50%, -50%);"><img src="' + config.loadingImg + '"/> ';
		
		loadStr += ' <div class="center-loading-centent" style="padding: 5px;">' + (config.content ||'') + '</div>';

		if (config.enableLoadSelectorBtn === true) {
			loadStr += '<div style="height: 35px;line-height: 35px;"><a href="javascript:;" class="center-loading-btn _loadSelectorCancelBtn">Cancel</a></div>';
		}

		loadStr += '</div>';
		
		loadStr += '</div>';

		if (!config.contentClear) {
			this.prepend(loadStr);
		} else {
			this.empty().html(loadStr);
		}

		if (config.enableLoadSelectorBtn === true) {
			var centerLoadingEle = $(this);
			this.find('._loadSelectorCancelBtn').on('click', function (e) {
				if (_$base.isFunction(config.callback)) {
					config.callback();
				} else {
					centerLoadingEle.find('.centerLoading').remove();
				}
			})
		}

		var cssPosition = this.css('position');

		if (cssPosition != 'fixed' && cssPosition != 'relative' && cssPosition != 'absolute') {
			this.css('position', 'relative');
			var heightVal = this.css('height') || '';
			heightVal = heightVal.replace('px', '');
			var addCssKey = 'relative';
			this.attr('var-css-key', addCssKey);
		}
		config.action == 'slide' ? jQuery(this).slideDown('slow') : config.action == 'fade' ? jQuery(this).fadeIn('slow') : jQuery(this).show();

		return this;
	};

	jQuery.fn.centerLoadingClose = function (options) {

		this.find('.centerLoading').remove();
		var posVal = (this.attr('var-css-key') || '');
		if (posVal.indexOf('relative') > -1) {
			this.css('position', '');
			this.removeAttr('var-css-key');
		}
	};

	_$base.progress = {
		start: function (divObj) {
			try {
				var obj = $(divObj);

				var modalcls = divObj.replace(/^[.#]/, '');

				$(divObj).prepend('<div class="' + modalcls + 'dialog-modal transbg" style="position:absolute;z-index:100000;text-align:center;border:1px solid;background: #CCC; filter:alpha(opacity=50); -moz-opacity:0.5; opacity: 0.5;display:table-cell;vertical-align:middle"><span><span style="font-weight:bold;background: #fff;">기다리시오....인내심을 가지고..</span></span></div>');

				$("." + modalcls + 'dialog-modal > span').css('line-height', obj.outerHeight() + 'px');
				$("." + modalcls + 'dialog-modal').css('width', obj.outerWidth());
				$("." + modalcls + 'dialog-modal').css('height', obj.outerHeight());
				$("." + modalcls + 'dialog-modal').show();
			} catch (e) {
				_$base.log(e);
			}
		},
		end: function (divObj) {
			try {
				$('.' + divObj.replace(/^[.#]/, '') + 'dialog-modal').hide();
			} catch (e) {
				_$base.log(e);
			}
		}
	};

	_$base.isDataEmpty = function (opt) {
		return $.isEmptyObject(opt);
	};

	/**
	 add csrf html
	 */
	_$base.addCsrfElement = function (eleSelector) {
		$(eleSelector).append(_$base.util.renderHtml('<input type="hidden" name="{{key}}" value="{{val}}" />', {
			'key': $$csrf_param, val: $$csrf_token
		}));
		return $.isEmptyObject(opt);
	};

	/**
	 *check box util
	 */
	_$base.check = {
		getCheckVal: function (opt) {
			var option = { delim: ',' }
				, rv = [];

			if (typeof opt === "string") {
				option.selector = opt;
			}

			$(option.selector + ':checked').each(function () {
				rv.push($(this).val());
			});

			return rv;
		}
		, allCheck: function (allSelector, itemSelector) {

			if ($(allSelector).is(":checked")) {
				$(itemSelector).prop("checked", true);
			} else {
				$(itemSelector).prop("checked", false);
			}
		}
		, radio: function (itemSelector) {
			return $(itemSelector + ':checked').val()
		}
	};
	/**
	 * @method _$base.unload
	 * @description 페이지 빠져나가기
	 */
	var $$initFlag = false;
	_$base.unload = function (mode) {
		if ($$initFlag) {
			return;
		}
		$$initFlag = true;


		// F5, ctrl + F5, ctrl + r 새로고침 막기
		$(document).keydown(function (e) {
			var keychk = false;

			var keyCode = e.which;

			if (e.ctrlKey) {
				if (keyCode == 84) { // ctrl+t
					return false;
				}
				if (keyCode == 72) { // ctrl+h
					return false;
				}
				if (keyCode == 82) { // ctrl+r
					return false;
				}
			}

			if (e.altKey) {
				if (keyCode == 37) { // alt+left
					return false;
				}
				if (keyCode == 39) { // alt+right
					return false;
				}
			}

			if (keyCode === 122) {
				return false;
			} else if (keyCode === 116) {
				if (typeof e == "object") {
					e.keyCode = 0;
				}
				keychk = true;
			} else if (keyCode === 82 && e.ctrlKey) {
				keychk = true;
			}

			if (mode == 'security') {
				if (keyCode == 123) {
					keychk = true;
				}
			}

			if (keychk) {

				if (mode == 'security') {
					e.keyCode = 0;
					e.returnValue = false;
					e.preventDefault()
					e.stopPropagation()
					return false;
				}

				if (mode == 'refresh') {
					databaseReqCancel();
					location.reload();
					return false;
				}

				if (!confirm(_$base.message('varsql.0001'))) {
					return false;
				} else {
					e.preventDefault()
					e.stopPropagation()

					if (mode == 'top') {
						if (window.userMain) {
							window.userMain.pageRefresh();
							return false;
						}
					}

					if (top != window) {
						if (top.userMain) {
							top.userMain.pageRefresh();
							return false;
						}
					}
					databaseReqCancel();
					location.reload();
				}
			}
		});
	}


	/**
	 * drop 무효화
	 */
	_$base.undrop = function () {
		$(document).on("dragover", function (e) {
			return false;
		}).on("drop", function (e) {
			return false;
		})
	}
	/**
	 * js, css 동적 로드
	 */

	function _load(_url, type, resourceName) {
		_url = _$base.getContextPathUrl(_url);
		try {
			if (type == 'js') {
				$('head').append($('<script resource-name="' + resourceName + '" src="' + _url + '"><\/script>'));
			} else {
				$('head').append('<link resource-name="' + resourceName + '" rel="stylesheet" href="' + _url + '">');
			}
		} catch (e) {
			_$base.log.info(e)
			_$base.log.error(e);
		};
	}

	function _loadProxy(resourceName) {
		var resourceObj = _$base.staticResource.get(resourceName);

		if (isUndefined(resourceObj)) {
			throw 'empty resource name : [' + resourceName + ']';
		}

		if ($('[resource-name="' + resourceName + '"]').length > 0) {
			throw 'duplicate resource load : [' + resourceName + ']';
		}

		for (var key in resourceObj) {
			var items = resourceObj[key];

			for (var j = 0; j < items.length; j++) {
				var resourcePath = items[j];
				_load(resourcePath, key, resourceName);
			}
		}
	}
	_$base.loadResource = function (resources) {
		var _self = _$base;

		if (isArray(resources)) {
			var len = resources.length;
			for (var i = 0; i < len; i++) {
				_loadProxy(resources[i]);
			}
		} else {
			_loadProxy(resources);
		}
	}

	/**
	 * unique 한값 찾기
	 */
	_$base.generateUUID = function () {
		var d = new Date().getTime();
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
			var r = (d + Math.random() * 16) % 16 | 0;
			d = Math.floor(d / 16);
			return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
		});
		return uuid.replace(/-/g, '');
	};

	/**
	 * 매칭되는 글자수
	 */
	_$base.matchCount = function (str, ms) {
		var re = new RegExp(ms, "ig");
		var resultArray = str.match(re);
		return resultArray.length;
	};

	/**
	 * 글자 끝부분 맞는지 체크
	 */
	_$base.endsWith = function (str, searchString, position) {
		var subjectString = str.toString();
		if (position === undefined || position > subjectString.length) {
			position = subjectString.length;
		}
		position -= searchString.length;
		var lastIndex = subjectString.indexOf(searchString, position);
		return lastIndex !== -1 && lastIndex === position;
	};

	// 글자 시작 부분 체크.
	_$base.startsWith = function (str, search, pos) {
		return str.substr(!pos || pos < 0 ? 0 : +pos, search.length) === search;
	};

	//array으로 변환
	function paramToArray(param) {
		var tmpArr = new Array();
		var tmpVal;
		for (var key in param) {
			if (key) {
				tmpVal = param[key];
				tmpArr.push(key + '=' + ((typeof tmpVal === 'string') ? tmpVal : JSON.stringify(tmpVal)));
			}
		}
		return tmpArr;
	}

	function getParameter(url, param) {

		var paramSplit = url.split('?');
		var paramLen = paramSplit.length;

		if (paramLen < 2) return param;

		var rtnval = param ? param : new Object();
		var parameters = paramSplit[1].split('&');

		var tmpKey = '';
		for (var i = 0; i < parameters.length; i++) {
			var tmpPara = parameters[i].split('=');
			tmpKey = tmpPara[0];
			if (!rtnval[tmpKey]) {
				rtnval[tmpKey] = tmpPara[1];
			}
		}

		if (paramLen > 2) {
			var lastParam = '';
			for (var i = 2; i < paramLen; i++) {
				lastParam = lastParam + '?' + paramSplit[i];
			}
			rtnval[tmpKey] = rtnval[tmpKey] + lastParam
		}

		return rtnval;
	}
	// location.href parameter;
	function getLocationParameter(key) {

		var paramSplit = location.href.split('?');
		var paramLen = paramSplit.length;

		if (paramLen < 2) return null;

		var parameters = paramSplit[1].split('&');

		for (var i = 0; i < parameters.length; i++) {
			var tmpPara = parameters[i].split('=');
			if (tmpPara[0] == key) {
				return tmpPara[1];
			}
		}

		return null;
	}

	// object deep copy
	function cloneDeep(dst, src) {
		if (VARSQL.isObject(src)) {
			return cloneObjectDeep(dst, src);
		} else if (VARSQL.isArray(src)) {
			return cloneArrayDeep(dst, src);
		} else {
			if (VARSQL.isDate(src)) {
				return new src.constructor(src);
			} else {
				return src;
			}
		}
	}

	function cloneObjectDeep(dst, src) {
		if (typeof src === 'function') {
			return src;
		}

		for (let key in src) {

			if (!src.hasOwnProperty(key)) { continue; }

			var val = src[key];

			if (val === undefined) { continue; }

			if (typeof val !== 'object' || val === null) {
				dst[key] = val;
			} else if (typeof dst[key] !== 'object' || dst[key] === null) {
				dst[key] = cloneDeep(VARSQL.isArray(val) ? [] : {}, val);
			} else {
				cloneDeep(dst[key], val);
			}
		}
		return dst;
	}

	function cloneArrayDeep(dst, src) {
		var isObj = VARSQL.isObject(dst);

		for (var i = 0; i < src.length; i++) {
			var val = src[i];
			var newVal;

			if (val == null) {
				newVal = val;
			} else {
				newVal = cloneDeep(VARSQL.isArray(val) ? [] : {}, val);
			}

			if (isObj) {
				dst[i] = newVal;
			} else {
				dst.push(newVal);
			}
		}
		return dst;
	}

	/**
	 * @method VARSQL.str
	 * @param str
	 * @description 글자 형식 처리.
	 */
	_$base.str = {
		trim: function (str) {
			return str.replace(/(^\s*)|(\s*$)/gi, '');
		}
		, allTrim: function (str) {
			return str.replace(/\s/g, '');
		}
		, allLineTrim: function (str) {
			return str.replace(/^\s+|\s+$/gm, '');
		}
		// 왼쪽 공백 제거
		, ltrim: function (str) {
			return str.replace(/^\s+/, "");
		}
		// 오른쪽 공백 제거
		, rtrim: function (str) {
			return str.replace(/\s+$/, "");
		}
	}


	_$base.util = {
		/**
		 * @method VARSQL.util.calcDate
		 * @param date
		 * @param masks
		 * @description 날짜 계산
		 */
		calcDate: function (date, num, type) {
			var a = new Date(date);
			var format = "yyyy-mm-dd";
			if (type == 'm') {
				a.setMonth(a.getMonth() + num);
				format = "yyyy-mm";
			} else {
				a.setDate(a.getDate() + num);
			}
			return this.dateFormat(a, format);
		}
		, renderHtml: function (template, renderItem) {
			return VARSQLTemplate.render.html(template, renderItem);
		}
		/**
		 * @method VARSQL.util.dateFormat
		 * @param date
		 * @param masks
		 * @description 날짜 포켓 변환
		 */
		, dateFormat: function (date, masks) {
			return dateFormat(date, masks);
		}
		/**
		 * @method VARSQL.util.removeSpecial
		 * @param str
		 * @description 특수문자 제거
		 */
		, removeSpecial: function (str) {
			return str.replace(/[-&\/\\#,+()$~%.'":*?<>{}]/g, '');
		}
		/**
		 * @method VARSQL.util.setRangeDate
		 * @param sdtObj
		 * @param edtObj
		 * @param cdt
		 * @param rangeNum
		 * @description 날자 범위 지정하기
		 */
		, setRangeDate: function (sdtObj, edtObj, cdt, rangeNum, type) {

			if (isNaN(rangeNum)) return false;

			var _self = this;
			var flag = rangeNum > 0 ? true : false;

			sdtObj = $(sdtObj);
			edtObj = $(edtObj);

			if (flag) {
				var sdt = sdtObj.val()
					, tmped = _self.calcDate(sdt, rangeNum, type);

				if (parseInt(_self.removeSpecial(sdt), 10) > parseInt(_self.removeSpecial(cdt), 10)) {
					sdtObj.val(cdt);
					tmped = cdt;
				} else if (parseInt(_self.removeSpecial(tmped), 10) > parseInt(_self.removeSpecial(cdt), 10)) {
					tmped = cdt;
				}

				edtObj.val(tmped);
			} else {
				var sdt = edtObj.val()
					, tmped = _self.calcDate(sdt, rangeNum, type);

				if (parseInt(_self.removeSpecial(sdt), 10) > parseInt(_self.removeSpecial(cdt), 10)) {
					edtObj.val(cdt);
					tmped = _self.calcDate(cdt, rangeNum, type);
				}
				sdtObj.val(tmped);
			}
		}
		, copyObject: function (obj) {
			return this.objectMerge({}, obj);
		}
		/**
		 * @method objectMerge
		 * @param target
		 * @param source
		 * @description object merge
		 */
		, objectMerge: function () {
			var reval = arguments[0];
			if (typeof reval !== 'object' || reval === null) { return reval; }
			var i = 1;
			if (Object.keys(reval).length > 0) {
				i = 0;
				reval = VARSQL.isArray(reval) ? [] : {};
			}
			var argLen = arguments.length;
			for (; i < argLen; i++) {
				cloneDeep(reval, arguments[i]);
			}
			return reval;
		}
		, escapeHTML: function (html) {
			var fn = function (tag) {
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
		, getCharLength: function (s, b, i, c) {
			for (b = i = 0; c = s.charCodeAt(i++); b += c >> 11 ? 2 : c >> 7 ? 2 : 1);
			return b;
		}
		, getConvertCamelObject: function (obj) {
			var param = {};
			for (var key in obj) {
				param[_fnConvertCamel(key)] = obj[key];
			}
			return param;
		}
		, removeUnderscore: function (str, lowerCaseFlag) {
			if (str == '') {
				return str;
			}

			if (lowerCaseFlag) {
				str = str.toLowerCase();
			}
			// conversion
			var returnStr = str.replace(/_(\w)/g, function (word) {
				return word;
			});
			returnStr = returnStr.replace(/_/g, "");

			return returnStr;
		}
		, appendUrlParam: function (url, params) {
			if (!isObject(params)) {
				return url;
			}
			var paramArr = paramToArray(params);

			if (paramArr.length > 0) {
				return openUrl + (url.indexOf('?') > -1 ? '&' : '?') + paramArr.join('&');
			}
			return url;
		}
		// post method check
		, isMethodPost: function (method) {
			return (method + '').toLowerCase() == 'post';
		}
		// get method check
		, isMethodGet: function (method) {
			return (method + '').toLowerCase() == 'get';
		}

		// camel 변환
		, convertCamel: _fnConvertCamel
		, convertUnderscoreCase: _fnConvertUnderscoreCase
		, toLowerCase: _fnToLowerCase
		, toUpperCase: _fnToUpperCase
		, capitalize: function (str) {
			return str.charAt(0).toUpperCase() + str.slice(1);
		}
		, paramToArray: paramToArray
		, getParameter: getParameter
		, getLocationParameter: getLocationParameter
		, browserSize: function () {
			return {
				width: (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth)
				, height: (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight)
			}
		}
		, replaceParamUrl: function (url, param) {
			var _this = this;
			if (!url) return '';

			var queryStr = [];

			var urlArr = url.split('?');
			if (urlArr.length > 1) {
				queryStr.push(urlArr[0] + '?');
				url = urlArr.splice(1).join('?');
			}
			var parameters = url.split('&');

			var sParam, sParamArr;
			for (var i = 0; i < parameters.length; i++) {
				sParam = parameters[i];

				if (i != 0) queryStr.push('&');

				queryStr.push(_this.replaceParam(sParam, param));
			}
			return queryStr.join('');
		}
		/**
		 * @method VARSQL.util.replageParam
		 * @param str replace string
		 * @param replaceParam 변경함 파라미터
		 * @description get all attirbute
		 */
		, replaceParam: function (str, replaceParam) {
			var matchObj = str.match(/#.*?#/g);

			if (matchObj != null) {
				var _paramVal = str, tmpKey = {}, matchKey, orginKey, paramObjFlag = (typeof replaceParam === 'object');

				for (var j = 0, matchLen = matchObj.length; j < matchLen; j++) {
					orginKey = matchObj[j];
					var matchKey = orginKey;

					var keyMatch = matchKey.match(/[*+$|^(){}\[\]]/gi);

					if (keyMatch != null) {
						var tmpReplaceKey = {}
						for (var z = 0, matchKeyLen = keyMatch.length; z < matchKeyLen; z++) {
							var specCh = keyMatch[z];

							if (!tmpReplaceKey[specCh]) {
								matchKey = matchKey.replace(new RegExp(speicalChar[specCh], 'g'), speicalChar[specCh]);
								tmpReplaceKey[specCh] = specCh;
							}
						}
					}

					if (paramObjFlag) {
						if (!tmpKey[orginKey]) {
							_paramVal = _paramVal.replace(new RegExp(matchKey, 'g'), (replaceParam[orginKey.replace(/#/g, '')] || ''));
							tmpKey[orginKey] = orginKey;
						}
					} else {
						_paramVal = _paramVal.replace(new RegExp(matchKey, 'g'), replaceParam);
					}
				}
				return _paramVal;
			}

			return str;
		}
		/**
		 * number format
		 */
		,numberFormat:function(num){
			return num.toLocaleString();
		}
		/**
		 * number format
		 */
		,fileDisplaySize :function(fileSize) {
		    if (fileSize <= 1) {
		        return fileSize + " bytes";
		    }
		
		    var units = ["bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
		    var digitGroups = Math.floor(Math.log(fileSize) / Math.log(1024));
		    if(digitGroups ==0){
				return fileSize +' '+ units[digitGroups];
			}
		    return (fileSize / Math.pow(1024, digitGroups)).toFixed(2) + " " + units[digitGroups];
		}
	}

	/**
	 * 숫자 계산
	 */
	_$base.math = {
		cal: function (a, b, calType, fixNum) {

			a = isNaN(a) ? 0 : a;
			b = isNaN(b) ? 0 : b;

			var reval = 0.0;
			if (calType == '+') {
				reval = a + b;
			} else if (calType == '/') {
				reval = a / b;
			} else if (calType == '%') {
				reval = a % b;
			} else if (calType == '*') {
				reval = a * b;
			}

			if (fixNum) {
				return reval.toFixed(fixNum);
			} else {
				var dotIdxA = (a + '').split('.')[1];
				dotIdxA = dotIdxA ? dotIdxA.length : 0;

				var dotIdxB = (b + '').split('.')[1];
				dotIdxB = dotIdxB ? dotIdxB.length : 0;

				fixNum = dotIdxA > dotIdxB ? dotIdxA : dotIdxB;

				if (fixNum < 1) {
					var revalDot = (reval + '').split('.')[1];
					revalDot = revalDot ? revalDot.length : 0;
					fixNum = fixNum > revalDot ? fixNum : revalDot;
				}
			}

			return fixNum > 0 ? reval.toFixed(fixNum) : reval;
		}
		, sum: function (a, b) {
			return a + b;
		}
		//배열 sum
		, arraySum: function (array, key) {
			if (array.length < 1) return 0;

			var _self = this;

			var result = 0.0, tmpVal = 0.0, maxFixed = 0, dotIdx
				, flag = key ? true : false;

			$.each(array, function () {
				tmpVal = flag ? this[key] : this;

				if (isNaN(tmpVal)) {
					tmpVal = (tmpVal + '').replace(/[$,]+/g, '');
					tmpVal = isNaN(tmpVal) ? 0 : tmpVal;
				}

				dotIdx = (tmpVal + '').split('.')[1];
				dotIdx = dotIdx ? dotIdx.length : maxFixed;
				maxFixed = maxFixed > dotIdx ? maxFixed : dotIdx;

				result = (_self.sum(parseFloat(result), parseFloat(tmpVal))).toFixed(maxFixed);
			});

			return result;
		}
		// 배열 평균 구하기 함수
		, average: function (array, key) {
			if (array.length < 1) return 0;
			var tmpSum = this.arraySum(array, key);

			var dotIdx = (tmpSum + '').split('.')[1];
			dotIdx = dotIdx ? dotIdx.length : 0;

			return (tmpSum / array.length).toFixed(dotIdx);
		}
	}


	/** -------------------------plugin-------------------------------------- **/
	var dateFormat = function () {
		var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
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

			var _ = utc ? "getUTC" : "get",
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
					d: d,
					dd: pad(d),
					ddd: dF.i18n.dayNames[D],
					dddd: dF.i18n.dayNames[D + 7],
					m: m + 1,
					mm: pad(m + 1),
					mmm: dF.i18n.monthNames[m],
					mmmm: dF.i18n.monthNames[m + 12],
					yy: String(y).slice(2),
					yyyy: y,
					h: H % 12 || 12,
					hh: pad(H % 12 || 12),
					H: H,
					HH: pad(H),
					M: M,
					MM: pad(M),
					s: s,
					ss: pad(s),
					l: pad(L, 3),
					L: pad(L > 99 ? Math.round(L / 10) : L),
					t: H < 12 ? "a" : "p",
					tt: H < 12 ? "am" : "pm",
					T: H < 12 ? "A" : "P",
					TT: H < 12 ? "AM" : "PM",
					Z: utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
					o: (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
					S: ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
				};

			return mask.replace(token, function ($0) {
				return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
			});
		};
	}();

	// Some common format strings
	dateFormat.masks = {
		"default": "ddd mmm dd yyyy HH:MM:ss",
		shortDate: "m/d/yy",
		mediumDate: "mmm d, yyyy",
		longDate: "mmmm d, yyyy",
		fullDate: "dddd, mmmm d, yyyy",
		shortTime: "h:MM TT",
		mediumTime: "h:MM:ss TT",
		longTime: "h:MM:ss TT Z",
		isoDate: "yyyy-mm-dd",
		isoTime: "HH:MM:ss",
		isoShortTime: "HH:MM",
		isoDateTime: "yyyy-mm-dd'T'HH:MM:ss",
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

	function contains(arr, element) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == element) {
				return true;
			}
		}
		return false;
	}

	//camel 변환
	function _fnConvertCamel(camelStr) {

		if (camelStr == '') {
			return camelStr;
		}
		camelStr = _fnToLowerCase(camelStr);
		// conversion
		var returnStr = camelStr.replace(/_(\w)/g, function (word) {
			return _fnToUpperCase(word);
		});
		returnStr = returnStr.replace(/_/g, "");

		return returnStr;
	}
	//camel case -> underscorecase 변환
	function _fnConvertUnderscoreCase(str) {
		if (str == '') {
			return str;
		}
		return str.split(/(?=[A-Z])/).join('_').toUpperCase();
	}

	function _fnToLowerCase(str) {
		return (str || '').toLowerCase();
	}

	function _fnToUpperCase(str) {
		return (str || '').toUpperCase();
	}

	window.VARSQL = _$base;

})(window);