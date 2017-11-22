/*
 * pubTree v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
 * url : https://github.com/ytechinfo/pub
 * demo : http://pub.moaview.com/
*/
;(function($, window, document) {
	"use strict";

	var _datastore = {}
	,defaultsConfig={
		icon : {
			root				: 'pub-tree-root'
			,folder			: 'pub-tree-folder'
			,node				: 'pub-tree-node'
			,empty				: 'pub-tree-empty'
			,line				: 'pub-tree-line'
			,join				: 'pub-tree-join'
			,joinBottom	: 'pub-tree-joinbottom'
			,oc				: 'pub-tree-nloc'
			,ocBottom	: 'pub-tree-nloc'
			,nloc			: 'pub-tree-nloc'
		}
		,itemKey :{
			id : 'id'
			,pid : 'pid'
		}
		,useIcon :{
			line : false
			,icon : true
		}
		,items			: []
		,treeItem		: new Object()
		,selected		: null
		,selectedNode	: null
		,selectedFound	: false
		,completed		: false
		,idArr			: []
		,rootArr		: []
		,topMenuView	: false
		,openDepth		: 'all'
		,subMenuCall	:''
		,toggle : function(obj_id){
			
		}
		,click : function(clickItem){
			//alert(clickItem);
		}
		,dblclick : function (){
			
		}
		,source : function (request, response){
			response(this.items);
		}
	}

	function objectMerge() {
		var dst = {},src ,p ,args = [].splice.call(arguments, 0);
		
		while (args.length > 0) {
			src = args.splice(0, 1)[0];
			if (Object.prototype.toString.call(src) == '[object Object]') {
				for (p in src) {
					if (src.hasOwnProperty(p)) {
						if (Object.prototype.toString.call(src[p]) == '[object Object]') {
							dst[p] = objectMerge(dst[p] || {}, src[p]);
						} else {
							dst[p] = src[p];
						}
					}
				}
			}
		}
		return dst;
	}

	function pubTree(element, options) {
		var _this = this; 
		_this.selector = (typeof element=='object') ? element.selector : element;
		_this.contextId = 'pubTree_'+new Date().getTime();
        _this.options = objectMerge(defaultsConfig, options);
		
		_this.init();
		_this.initEvt();
	
	}

	pubTree.prototype={
		init : function (){
			var _this = this; 
			_this._requestItems();
		
		}
		,_requestItems : function (type){
			var _this = this
				,_opts = _this.options; 
			function callbackResponse (item){
				_this.treeGrid.call(_this , item);
			}
			if(type=='sub' || $.isFunction(_opts.source)){
				_opts.source.call(_opts, _opts.selectedNode ,callbackResponse); 
			}else{
				if($.isArray(_opts.source)){
					callbackResponse(_opts.source);
				}
			}
		}
		,setItems : function (items){
			this.options.items = items;
			return this; 
		}
		,initEvt : function (){
			var _this  = this
				,_opt = _this.options; 
			$(this.selector).on('click','.pubtree-item', function (e){
				var sObj = $(this); 
				var treeId = sObj.closest('[data-tree-id]').attr('data-tree-id');

				var tNode =	_opt.treeItem[treeId];

				if(_opt.selectedNode){
					$('#'+_opt.selectedNode.id+'_a').removeClass('pub-tree-atag-focus');
				}
				$('#'+tNode.id+'_a').addClass('pub-tree-atag-focus');

				_opt.selectedNode = tNode;

				if($.isFunction(_opt.click)){
					_opt.click({element:sObj, item:tNode});
				}
			})

			$(this.selector).on('dblclick','.pubtree-item', function (e){
				_this.toggle($(this));
			})

			$(this.selector).on('click','.pub-tree-join-icon', function (e){
				_this.toggle($(this));
			})
		}
		,nodeClick : function (id){
			var _this  = this;
			
			var clickEle = $(this.selector).find('#'+id+'_a');
			
			clickEle.trigger('click');
			
		}
		,toggle : function (selectEle){
			var _this = this
				,_opt = _this.options
				,treeItemEle = selectEle.closest('[data-tree-id]'); 
			var treeId = treeItemEle.attr('data-tree-id');
				
			var tNode=_opt.treeItem[treeId];
			
			_opt.selectedNode = tNode;
			var icon = _opt.icon;
			var imgSel = document.getElementById('c_'+treeId);

			if(imgSel){
				if(treeItemEle.hasClass('open')){
					treeItemEle.removeClass('open');
				}else{
					treeItemEle.addClass('open');
				}
				
				if(tNode.childCnt > 0 && imgSel.childNodes.length < 1 ) {
					_this._requestItems('child');
				}
			}
		}
		,treeGrid : function (items){
			var _this = this; 

			for(var i=0 ; i < items.length; i++){
				_this.add(items[i]);
			}
			_this.open();
		}
		,setOpenDepth : function(depth){	
			this.options.openDepth=depth;
		}
		,_getEmptyImg : function (){
			return '<span class="tree-empty-area"></span>'
		}
		,setImg : function(tNode, iconImg){	
			var treeItem =  this.options.treeItem
				,depth = treeItem[tNode.id].depth
				,str= []
				,icon = this.options.icon
				,useLineFlag  = (this.options.useIcon.line === true);
		
			var i = this.options.topMenuView===true?0:1;
					
			str.push( '<span id="'+tNode.id+'_join" class="pub-tree-icon ');
			str.push( (treeItem[tNode.pid].childDataCnt != tNode.sortOrder ?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.join :'pub-tree-join-icon '+icon.oc):(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.joinBottom :'pub-tree-join-icon '+icon.nloc)));
			str.push('"></span>');

			if(this.options.useIcon.icon){
				str.push( '<span id="'+tNode.id+'_icon" class="pub-tree-icon '+iconImg+'"></span>');
			}

			return str.join('');
		}

		,setFolder : function (tNode){
			if(this.options.useIcon.icon !== true){
				return '';
			}
			var treeItem = this.options.treeItem;

			var icon = this.options.icon;
			
			var sNode = treeItem[tNode.id];

			this.options.idArr.length==0?(this.options.treeItem[sNode.id].childDataCnt=sNode.childDataCnt=0):sNode.childDataCnt;
			
			if(sNode.depth > 0 && sNode != null){
				if(sNode.depth >0){
					if(sNode.childDataCnt>0){
						$(sNode.id +'_icon').removeClass(icon.node);
						$(sNode.id +'_icon').addClass(icon.folder);	
					}else{
						$(sNode.id +'_icon').removeClass(icon.folder);
						$(sNode.id +'_icon').addClass(icon.node);
					}
				}
			}
		}
		,add : function(o){
			var pid=o[this.options.itemKey.pid];
			var id=o[this.options.itemKey.id];
			
			var tNode = this.createNode(pid);

			for(var key in o){
				if(o[key]!==undefined) tNode[key]= o[key];
			}

			if(this.options.treeItem[pid]){
				this.options.treeItem[pid].childNodes.push(id);
			}else{
				this.options.idArr.push(id);
				this.options.rootArr.push(id);
			}
			
			this.options.treeItem[pid]?(tNode.sortOrder=this.options.treeItem[pid].childDataCnt =(this.options.treeItem[pid].childDataCnt+1)):0;
			
			this.options.treeItem[id] = tNode;
		}
		,createNode : function (pid){
			var tNode = {
				id :''
				,pid :''
				,name :''
				,url :''
				,title :''
				,target :''
				,icon :''
				,iconOpen :''
				,open :''
				,childDataCnt:0
				,childCnt:0
				,sortOrder:0
				,depth:this.options.treeItem[pid]?((this.options.treeItem[pid].depth)+1):0
				,childNodes:[]
			};

			return tNode;
		}
		,addNode : function(o){
			if($('[data-tree-id="'+o.pid+'"]').length >0){
				
				var pid=o.pid;
				var id=o.id;

				var tNode = this.createNode(pid);

				for(var key in o){
					if(o[key]!==undefined) tNode[key]= o[key];
				}
					
				if(this.options.treeItem[pid]){
					this.options.treeItem[pid].childNodes.push(id);
				}else{
					this.options.idArr.push(id);
				}
				
				this.options.treeItem[pid]?(tNode.sortOrder=this.options.treeItem[pid].childDataCnt =(this.options.treeItem[pid].childDataCnt+1)):0;
				
				this.options.treeItem[id] = tNode;

				this.addNodeBeforeEnd(tNode);
			}
		}
		,addNodeBeforeEnd : function (tNode) {
			var oElement = document.getElementById('c_'+tNode.pid);
			var sHTML  = this.toString();
			
			this.setFolder(this.options.treeItem[tNode.pid]); // ��� ����. 
			
			if (oElement.insertAdjacentHTML != null) {
				oElement.insertAdjacentHTML('BeforeEnd', sHTML);
			}else{
				var df;	
				var r = oElement.ownerDocument.createRange();
				r.selectNodeContents(oElement);
				r.collapse(false);
				df = r.createContextualFragment(sHTML);
				oElement.appendChild(df);
			}
			this.options.idArr =[];
		}
		,delNode : function(id){
			var tNode = this.options.treeItem[id];
			if(tNode){
				this.options.treeItem[tNode.pid]?(this.options.treeItem[tNode.pid].childDataCnt =(this.options.treeItem[tNode.pid].childDataCnt-1)):0;
				this.options.treeItem[id] = '';
				this.remove(tNode);
			}
		}
		,remove : function(tNode){
			var el = document.getElementById(tNode.id);
			var el1 = document.getElementById('c_'+tNode.id);

			el.parentNode.removeChild(el);
			el1.parentNode.removeChild(el1);
			if(this.options.treeItem[tNode.pid]){
				this.setFolder(this.options.treeItem[tNode.pid]);
			}
			this.options.selectedNode='';
		}
		,isChild : function (obj){
			return obj.length > 0?true:false;
		}
		,toString : function(obj){
			var treeHtml = [];
			var id_arr = 	obj?obj:this.options.idArr;
			var idArrlLen = id_arr.length;
			var tree_item = this.options.treeItem;
			var icon =this.options.icon;
			var tmpchildDataCnt ='';
			var tmpDepth = '';
			var childNodeArr;
			var childDataCnt=0;
			var firstNodeFlag = this.options.selectedNode==null && this.selector?true:false;
			
			if(firstNodeFlag && obj===undefined)	treeHtml.push('<ul id="'+this.contextId+'"class="pub-tree" ondrag="return false">')
			
			for(var i = 0 ; i < idArrlLen ; i++){
				
				var tNode = tree_item[id_arr[i]];

				childNodeArr = tNode.childNodes;

				tNode=tree_item[id_arr[i]];

				var openClass = this.options.openDepth != 'all' ? (this.options.openDepth > tNode.depth?'open ':'') :'open ';
				var flag = openClass=='open'?true:false;
				var iconImg = (tNode.icon =='' || tNode.icon ==undefined) ?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.node :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.folder:(flag?icon.folder:icon.folder))):tNode.icon;

				if(this.options)
				
				if(tNode.depth ==0){
					treeHtml.push('<li data-tree-id="'+tNode.id+'"><span style="display:'+(this.options.topMenuView?'inline':'none')+'"><a href ="javascript:" id = "'+tNode.id+'_a" class="pubtree-item">'+tNode.name+'</a></span><ul id="c_'+tNode.id+'" class="sub-node-wrapper first-child">'+this.toString(childNodeArr) +'</ul></li>');
				}else{
					var lastNodeClass =(tree_item[tNode.pid].childDataCnt==tNode.sortOrder?'tree-last-node':'');

					if(tNode.childDataCnt ==0){
						treeHtml.push('<li data-tree-id="'+tNode.id+'" class="'+openClass+lastNodeClass+'">'+this.setImg(tNode, iconImg)+'<a href ="javascript:" id="'+tNode.id+'_a" class="pubtree-item pubtree-item-folder">'+tNode.name+'</a> <ul id="c_'+tNode.id+'" class="sub-node-wrapper"></ul></li>');
						
					}else{
						treeHtml.push('<li data-tree-id="'+tNode.id+'" class="'+openClass+lastNodeClass+'">'+this.setImg(tNode, iconImg)+'<a href="javascript:" id="'+tNode.id+'_a" class="pubtree-item">'+tNode.name+'</a> <ul id ="c_'+tNode.id+'" class="sub-node-wrapper">' +this.toString(childNodeArr) +'</ul></li>');
					}
				}
			}
			
			if(firstNodeFlag && obj===undefined)	treeHtml.push('</ul>');
			
			return treeHtml.join('');
		}
		,getSelect : function(){
			var sNode = this.options.selectedNode;
			if(sNode !=null && sNode != ''){
				document.getElementById(sNode.id+'_a').className = 'style2';
				return sNode;
			}
		}
		,open : function(){
			var icon = this.options.icon;
			var sNode = this.options.selectedNode;
			var treeItem = this.options.treeItem;
			
			if(sNode==null && this.selector){ 
				$(this.selector).html(this.toString());
			}else{
				if(sNode.id){
					this.options.idArr = treeItem[sNode.id].childNodes;
					this.setFolder(sNode);
					$('#c_'+sNode.id).html(this.toString());
				}
			}
			this.options.idArr =[];
			this.getSelect();
		}
		,allOpen : function(){
			var rootArr =  this.options.rootArr;
			var rootArrLen =rootArr.length;
			
			for(var i =0 ; i <rootArrLen ;  i ++){
				var tNode =this.options.treeItem[rootArr[i]];
				this.folderOpen(tNode);
			}
		}
		,folderOpen : function (tNode){
			var cNode = tNode.childNodes;
			var cNodeLen = cNode.length;

			if( cNodeLen > 0 ){
				for(var i=0; i < cNodeLen; i ++){
					var tmpNode = this.options.treeItem[cNode[i]];
					$('[data-tree-id="'+tmpNode.id+'"]').addClass('open');
					
					if(tmpNode.childNodes.length >0) this.folderOpen(tmpNode);
				}
			}
		}
		
		,allClose : function(){
			var rootArr =  this.options.rootArr;
			var rootArrLen =rootArr.length;

			for(var i =0 ; i <rootArrLen ;  i ++){
				var tNode =this.options.treeItem[rootArr[i]];
				this.folderClose(tNode);
			}
		}
		,folderClose : function (tNode){
			var cNode = tNode.childNodes;
			var cNodeLen = cNode.length;

			if( cNodeLen > 0 ){
				for(var i=0; i < cNodeLen; i ++){
					var tmpNode = this.options.treeItem[cNode[i]];
					$('[data-tree-id="'+tmpNode.id+'"]').removeClass('open');
					if(tmpNode.childNodes.length >0) this.folderClose(tmpNode);
				}
			}else{
				$('[data-tree-id="'+tNode.id+'"]').removeClass('open');	
			}
		}
	}


$.pubTree = function (selector,options) {

	if(!selector){
		return ; 
	}

	var _cacheObject = _datastore[selector];

	if(typeof options === 'undefined'){
		return _cacheObject||{}; 
	}

	
	if(!_cacheObject){
		_cacheObject = new pubTree(selector, options);
		_datastore[selector] = _cacheObject;
		return _cacheObject; 
	}else if(typeof options==='object'){
		_cacheObject = new pubTree(selector, options);
		_datastore[selector] = _cacheObject;
		return _cacheObject; 
	}

	if(typeof options === 'string'){
		var callObj =_cacheObject[options]; 
		if(typeof callObj ==='undefined'){
			return options+' not found';
		}else if(typeof callObj==='function'){
			return _cacheObject[options].apply(_cacheObject,args);
		}else {
			return typeof callObj==='function'; 
		}
	}

	return _cacheObject;	
};
}(jQuery, window, document));