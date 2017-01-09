/*
 * pubTree v0.0.1
 * ========================================================================
 * Copyright 2016 ytkim
 * Licensed under MIT
 * http://www.opensource.org/licenses/mit-license.php
*/
;(function($, window, document) {
	"use strict";

	var _datastore = {}
	,defaultsConfig={
		icon : {
			path : '../theme/default/images/tree/'
			,root				: 'base.gif'
			,folder			: 'folder.gif'
			,folderOpen	: 'folderopen.gif'
			,node				: 'page.gif'
			,empty				: 'empty.gif'
			,line				: 'line.gif'
			,join				: 'join.gif'
			,joinBottom	: 'joinbottom.gif'
			,plus				: 'plus.gif'
			,plusBottom	: 'plusbottom.gif'
			,minus				: 'minus.gif'
			,minusBottom	: 'minusbottom.gif'
			,nlPlus			: 'nolines_plus.gif'
			,nlMinus			: 'nolines_minus.gif'
		}
		,itemKey :{
			id : 'id'
			,pid : 'pid'
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

		
		for(var key in _this.options.icon){
			if('path' !== key){ 
				_this.options.icon[key] = (_this.options.icon['path']+''+_this.options.icon[key]);	
			}	
		}
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
		,toggle : function (selectEle){
			var _this = this
				,_opt = _this.options; 
			var treeId = selectEle.closest('[data-tree-id]').attr('data-tree-id');
				
			var tNode=_opt.treeItem[treeId];

			if(_opt.selectedNode){
				$('#'+_opt.selectedNode.id+'_a').removeClass('pub-tree-atag-focus');
			}
			$('#'+tNode.id+'_a').addClass('pub-tree-atag-focus');

			_opt.selectedNode = tNode;
			var icon = _opt.icon;
			var imgSel = document.getElementById('c_'+treeId);

			if(imgSel){
				if(imgSel.childNodes.length >0 || tNode.childCnt > 0){
					var viewYn= imgSel.style.display=='none'?'inline':'none';
					if(tNode.depth !=0 ){
						document.getElementById(treeId+'_icon').src = tNode.icon =='' || tNode.icon ==undefined?(viewYn !='none'?icon.folderOpen:icon.folder):(viewYn =='none'?(tNode.iconOpen==''||tNode.iconOpen==undefined?tNode.icon:tNode.iconOpen):tNode.icon);
						document.getElementById(treeId+'_join').src = _opt.treeItem[tNode.pid].childDataCnt != tNode.sortOrder?((viewYn !='none')?icon.minus:icon.plus):((viewYn !='none')?icon.minusBottom:icon.plusBottom);
					}
					imgSel.style.display =viewYn;
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
		,setImg : function(tNode, display){	
			var treeItem =  this.options.treeItem;
			var depth = treeItem[tNode.id].depth;
			var str= [];	
			var icon = this.options.icon;
			
			var imgYnArr= tNode.imgYn.split(',');

			var i = this.options.topMenuView===true?0:1;
			
			for(; i <depth ; i ++){
				str.push(((imgYnArr[i]=='false' || imgYnArr[i]=='' )?'<span class="tree-empty-area"></span>':'<img src="'+icon.line+'">'));
			}
			str.push( '<img id="'+tNode.id+'_join" src="');

			var flag = display=='block'?true:false;
			
			str.push( (treeItem[tNode.pid].childDataCnt != tNode.sortOrder?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.join :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.plus:(flag?icon.minus:icon.plus))):(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.joinBottom :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.plusBottom:(flag?icon.minusBottom:icon.plusBottom)))));
			str.push('" class="pub-tree-join-icon">');

			return str.join('');
		}

		,setFolder : function (tNode){
			var treeItem = this.options.treeItem;

			var icon = this.options.icon;
			
			var sNode = treeItem[tNode.id];

			this.options.idArr.length==0?(this.options.treeItem[sNode.id].childDataCnt=sNode.childDataCnt=0):sNode.childDataCnt;
			
			if(sNode.depth > 0 && sNode != null){
				if(sNode.depth >0){
					if(sNode.childDataCnt>0){
						document.getElementById(sNode.id +'_join').src = (treeItem[sNode.pid].childDataCnt != sNode.sortOrder?(sNode.childDataCnt ==0?icon.join:icon.minus):(sNode.childDataCnt ==0?icon.joinBottom:icon.minusBottom));	
						document.getElementById(sNode.id +'_icon').src = icon.folderOpen;	
					}else{
						document.getElementById(sNode.id +'_join').src = (treeItem[sNode.pid].childDataCnt != sNode.sortOrder?(sNode.childDataCnt ==0?icon.join:icon.minus):(sNode.childDataCnt ==0?icon.joinBottom:icon.minusBottom));	
						document.getElementById(sNode.id +'_icon').src = icon.node;	
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
				,imgYn:''
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
			
			if(firstNodeFlag && obj===undefined)	treeHtml.push('<div id="'+this.contextId+'"class="pub-tree" ondrag="return false">')
			
			for(var i = 0 ; i < idArrlLen ; i++){
				
				var tNode = tree_item[id_arr[i]];

				childNodeArr = tNode.childNodes;

				this.options.treeItem[id_arr[i]].imgYn = tree_item[tNode.pid]?(tree_item[tNode.pid].imgYn+','+(tree_item[tNode.pid].childDataCnt==tNode.sortOrder?false:true)):'';
				
				tNode=tree_item[id_arr[i]];

				var display = this.options.openDepth != 'all' ? (this.options.openDepth > tNode.depth?'block':'none') :'block';
				var flag = display=='block'?true:false;
				var iconImg = (tNode.icon =='' || tNode.icon ==undefined) ?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.node :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.folder:(flag?icon.folderOpen:icon.folder))):tNode.icon;

				if(tNode.depth ==0){
					treeHtml.push('<div data-tree-id="'+tNode.id+'" style="display:'+(this.options.topMenuView?'inline':'none')+'"><img id="'+tNode.id+'_icon" src="'+icon.root+'" class="pubtree-join-icon"> <a href ="javascript:" id = "'+tNode.id+'_a" class="pubtree-item">'+tNode.name+'</a> </div><span id="c_'+tNode.id+'" style="display:inline">'+this.toString(childNodeArr) +'</span>');
				}else{
					if(tNode.childDataCnt ==0){
						treeHtml.push('<div data-tree-id="'+tNode.id+'" style="display:block">'+this.setImg(tNode, display)+'<img id="'+tNode.id+'_icon" src="'+iconImg+'"> <a href ="javascript:" id="'+tNode.id+'_a" class="pubtree-item pubtree-item-folder">'+tNode.name+'</a> </div><span id="c_'+tNode.id+'" style="display:none"></span>');
						
					}else{
						treeHtml.push('<div data-tree-id="'+tNode.id+'" style="display:block">'+this.setImg(tNode, display)+'<img id="'+tNode.id+'_icon" src="'+iconImg+'"> <a href="javascript:" id="'+tNode.id+'_a" class="pubtree-item">'+tNode.name+'</a> </div><span id ="c_'+tNode.id+'" style="display:'+display+'">' +this.toString(childNodeArr) +'</span>');
					}
				}
			}
			
			if(firstNodeFlag && obj===undefined)	treeHtml.push('</div>');
			
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
			var treeItem = this.options.treeItem;
			var rootArrLen =rootArr.length;
			
			for(var i =0 ; i <rootArrLen ;  i ++){
				var tNode =treeItem[rootArr[i]];

				this.setFolderOpenImg(tNode);
				this.folderOpen(tNode);

			}
		}
		,folderOpen : function (tNode){
			var cNode = tNode.childNodes;
			var cNodeLen = cNode.length;
			var treeItem = this.options.treeItem;

			if( cNodeLen > 0 ){
				for(var i=0; i < cNodeLen; i ++){
					var tmpNode = treeItem[cNode[i]];
					this.setFolderOpenImg(tmpNode)
					
					if(tmpNode.childNodes.length >0) this.folderOpen(tmpNode);
					
				}
			}
		}
		,setFolderOpenImg : function (tNode){
			var icon = this.options.icon;
			var treeItem = this.options.treeItem;

			var c_obj = document.getElementById('c_'+tNode.id);
			if(c_obj != '' && c_obj != undefined){
				
				if(tNode.depth > 0  && tNode.childNodes.length > 0){
					c_obj.style.display='inline';
					document.getElementById(tNode.id+'_icon').src = tNode.icon =='' || tNode.icon ==undefined ? (icon.folderOpen) : (tNode.iconOpen==''||tNode.iconOpen==undefined?tNode.icon:tNode.iconOpen);
					document.getElementById(tNode.id+'_join').src = treeItem[tNode.pid].childDataCnt != tNode.sortOrder?icon.minus:icon.minusBottom;
				}
			}
		}
		,allClose : function(){
			var rootArr =  this.options.rootArr;
			var treeItem = this.options.treeItem;
			var rootArrLen =rootArr.length;
			
			for(var i =0 ; i <rootArrLen ;  i ++){
				var tNode =treeItem[rootArr[i]];

				this.setFolderCloseImg(tNode);
				this.folderClose(tNode);

			}
		}
		,folderClose : function (tNode){
			var cNode = tNode.childNodes;
			var cNodeLen = cNode.length;
			var treeItem = this.options.treeItem;

			if( cNodeLen > 0 ){
				for(var i=0; i < cNodeLen; i ++){
					var tmpNode = treeItem[cNode[i]];
					this.setFolderCloseImg(tmpNode)
					
					if(tmpNode.childNodes.length >0) this.folderClose(tmpNode);
					
				}
			}
		}
		,setFolderCloseImg : function (tNode){
			var icon = this.options.icon;
			var treeItem = this.options.treeItem;

			var c_obj = document.getElementById('c_'+tNode.id);
			if(c_obj != '' && c_obj != undefined){
				if(tNode.depth > 0  && tNode.childNodes.length > 0){
					c_obj.style.display='none';		
					document.getElementById(tNode.id+'_icon').src = tNode.icon =='' || tNode.icon ==undefined?icon.folder:tNode.icon;
					document.getElementById(tNode.id+'_join').src = treeItem[tNode.pid].childDataCnt != tNode.sortOrder?icon.plus:icon.plusBottom;
				}
			}
		}
	}


$.pubTree = function (selector,options) {

	if(!selector){
		return ; 
	}

	var _cacheObject = _datastore[selector];

	if(typeof options === undefined){
		return _cacheObject; 
	}

	console.log('asdfasfd',options,  _cacheObject);
	
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