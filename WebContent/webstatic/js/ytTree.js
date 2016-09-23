/**
* javascript tree 
* Copyright 2011 , ytkim
* 
*/
var ytTreeConfig={
	icon : {
		root				: '/TaiAssembl/common/img/base.gif'
		,folder			: '/TaiAssembl/common/img/folder.gif'
		,folderOpen	: '/TaiAssembl/common/img/folderopen.gif'
		,node				: '/TaiAssembl/common/img/page.gif'
		,empty				: '/TaiAssembl/common/img/empty.gif'
		,line				: '/TaiAssembl/common/img/line.gif'
		,join				: '/TaiAssembl/common/img/join.gif'
		,joinBottom	: '/TaiAssembl/common/img/joinbottom.gif'
		,plus				: '/TaiAssembl/common/img/plus.gif'
		,plusBottom	: '/TaiAssembl/common/img/plusbottom.gif'
		,minus				: '/TaiAssembl/common/img/minus.gif'
		,minusBottom	: '/TaiAssembl/common/img/minusbottom.gif'
		,nlPlus			: '/TaiAssembl/common/img/nolines_plus.gif'
		,nlMinus			: '/TaiAssembl/common/img/nolines_minus.gif'
	}
	,obj				: ''
	,divName			: ''
	,waitObj : 'tree_wait_content_cover'
	,treeItem		: new Object()
	,selected		: null
	,selectedNode	: null
	,selectedFound	: false
	,completed		: false
	,idArr			: new Array()
	,rootArr		: new Array()
	,topMenuView	:'inline'
	,openDepth		:'1'
	,toggle : function(obj_id){
		var tNode=this.treeItem[obj_id];

		this.selectedNode = tNode;
		var icon = this.icon;
		var imgSel = document.getElementById('c_'+obj_id);

		if(imgSel){
			if(imgSel.childNodes.length >0 || tNode.childCnt > 0){
				var viewYn= imgSel.style.display=='none'?'inline':'none';
				if(tNode.depth !=0 ){
					document.getElementById(obj_id+'_icon').src = tNode.icon =='' || tNode.icon ==undefined?(viewYn !='none'?icon.folderOpen:icon.folder):(viewYn =='none'?(tNode.iconOpen==''||tNode.iconOpen==undefined?tNode.icon:tNode.iconOpen):tNode.icon);
					document.getElementById(obj_id+'_join').src = this.treeItem[tNode.pid].childDataCnt != tNode.sortOrder?((viewYn !='none')?icon.minus:icon.plus):((viewYn !='none')?icon.minusBottom:icon.plusBottom);
				}
				imgSel.style.display =viewYn;
				
				if(viewYn=='none') return ; 
			}

			if(tNode.childCnt > 0 && imgSel.childNodes.length < 1 ) {
				this.waitStart();
				subMenuTreeCall(obj_id);
			}
		}
	}
	,doClick : function(obj_id){
	
		var tNode =	this.treeItem[obj_id];

		this.selectedNode = tNode;

		if(tNode.url != ''){
			location.href ='javascript:'+tNode.url;
		}
	}
	,waitStart:function (){
		var treeObj = document.getElementById(ytTreeConfig.divName);
		
		var firstDiv = '<div id="'+ytTreeConfig.waitObj+'" style="width:292px; height:540px;filter:alpha(opacity=20);opacity:0.2;display:inline-block;z-index:1000px;top:0px;left:0px;background-color:white;position:absolute;"></div>';

		if (treeObj.insertAdjacentHTML != null) {
			treeObj.insertAdjacentHTML('BeforeBegin', firstDiv);
		}else{
			var df;	
			var r = treeObj.ownerDocument.createRange();
			r.selectNodeContents(oElement);
			r.collapse(false);
			df = r.createContextualFragment(firstDiv);
			treeObj.appendChild(df);
		}
	}
	,waitStop:function (){
		var rc = document.getElementById(ytTreeConfig.waitObj);
		if(rc) rc.parentNode.removeChild(rc);		
	}
}

function ytTree(divName) {
	ytTreeConfig.divName = divName;
}

ytTree.prototype.setOpenDepth = function(depth){	
	ytTreeConfig.openDepth=depth;
}

ytTree.prototype.setImg = function(tNode, display){	
	var treeItem =  ytTreeConfig.treeItem;
	var depth = treeItem[tNode.id].depth;
	var str= new Array();	
	var icon = ytTreeConfig.icon;
	
	var imgYnArr= tNode.imgYn.split(',');
	
	for(var i = 0 ; i <depth ; i ++){
		str.push(((imgYnArr[i]=='false' || imgYnArr[i]=='' )?'<img src="'+icon.empty+'">':'<img src="'+icon.line+'">'));
	}
	str.push( '<img id="'+tNode.id+'_join" src="');

	var flag = display=='block'?true:false;
	var ti = (treeItem[tNode.pid].childDataCnt != tNode.sortOrder?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.join :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.plus:(flag?icon.minus:icon.plus))):(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.joinBottom :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.plusBottom:(flag?icon.minusBottom:icon.plusBottom))));
	str.push(ti );

	if(ti == icon.joinBottom || ti==icon.join) str.push('">');
	else str.push('" onclick = "ytTreeConfig.toggle(\''+tNode.id+'\')" style = "cursor:pointer;">');

	return str.join('');
}

ytTree.prototype.setFolder = function (tNode){
	var treeItem = ytTreeConfig.treeItem;

	var icon = ytTreeConfig.icon;
	
	sNode = treeItem[tNode.id];

	ytTreeConfig.idArr.length==0?(ytTreeConfig.treeItem[sNode.id].childDataCnt=sNode.childDataCnt=0):sNode.childDataCnt;
	
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

// 다중 트리 추가 . 
ytTree.prototype.add = function(o){
	var pid=o.pid;
	var id=o.id;
	
	var tNode = this.createNode(pid);

	for(var key in o){
		if(o[key]!==undefined) tNode[key]= o[key];
	}

	if(ytTreeConfig.treeItem[pid]){
		ytTreeConfig.treeItem[pid].childNodes.push(id);
	}else{
		ytTreeConfig.idArr.push(id);
		ytTreeConfig.rootArr.push(id);
	}
	
	ytTreeConfig.treeItem[pid]?(tNode.sortOrder=ytTreeConfig.treeItem[pid].childDataCnt =(ytTreeConfig.treeItem[pid].childDataCnt+1)):0;
	
	ytTreeConfig.treeItem[id] = tNode;
}

ytTree.prototype.createNode= function (pid){
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
		,depth:ytTreeConfig.treeItem[pid]?((ytTreeConfig.treeItem[pid].depth)+1):0
		,childNodes: new Array()
	};

	return tNode;
}

// 단일 트리 추가 . 
ytTree.prototype.addNode = function(o){
	if(document.getElementById(pid)){
		
		var pid=o.pid;
		var id=o.id;

		var tNode = this.createNode(pid);

		for(var key in o){
			if(o[key]!==undefined) tNode[key]= o[key];
		}
			
		if(ytTreeConfig.treeItem[pid]){
			ytTreeConfig.treeItem[pid].childNodes.push(id);
		}else{
			ytTreeConfig.idArr.push(id);
		}
		
		ytTreeConfig.treeItem[pid]?(tNode.sortOrder=ytTreeConfig.treeItem[pid].childDataCnt =(ytTreeConfig.treeItem[pid].childDataCnt+1)):0;
		
		ytTreeConfig.treeItem[id] = tNode;

		this.addNodeBeforeEnd(tNode);
	}
}

ytTree.prototype.addNodeBeforeEnd = function (tNode) {
	var oElement = document.getElementById('c_'+tNode.pid);
	var sHTML  = this.toString();
	
	this.setFolder(ytTreeConfig.treeItem[tNode.pid]); // 폴더 셋팅. 
	
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
	ytTreeConfig.idArr =new Array();
}

// 트리 하나 삭제 . 
ytTree.prototype.delNode = function(id){
	var tNode = ytTreeConfig.treeItem[id];
	if(tNode){
		ytTreeConfig.treeItem[tNode.pid]?(ytTreeConfig.treeItem[tNode.pid].childDataCnt =(ytTreeConfig.treeItem[tNode.pid].childDataCnt-1)):0;
		ytTreeConfig.treeItem[id] = '';
		this.remove(tNode);
	}
}


ytTree.prototype.remove = function(tNode){
	var el = document.getElementById(tNode.id);
	var el1 = document.getElementById('c_'+tNode.id);

	el.parentNode.removeChild(el);
	el1.parentNode.removeChild(el1);
	if(ytTreeConfig.treeItem[tNode.pid]){
		this.setFolder(ytTreeConfig.treeItem[tNode.pid]);
	}
	ytTreeConfig.selectedNode='';
}

ytTree.prototype.isChild = function (obj){
	return obj.length > 0?true:false;
}

ytTree.prototype.toString = function(obj){
	var treeHtml = new Array();
	var id_arr = 	obj?obj:ytTreeConfig.idArr;
	var idArrlLen = id_arr.length;
	var tree_item = ytTreeConfig.treeItem;
	var icon =ytTreeConfig.icon;
	var childNodeArr;
	var firstNodeFlag = ytTreeConfig.selectedNode==null && ytTreeConfig.divName?true:false;
	
	if(firstNodeFlag && obj===undefined)	treeHtml.push('<div id="ytTreeDiv" class="ytTree" ondrag="return false">')
	
	for(var i = 0 ; i < idArrlLen ; i++){
		
		var tNode = tree_item[id_arr[i]];

		childNodeArr = tNode.childNodes;

		ytTreeConfig.treeItem[id_arr[i]].imgYn = tree_item[tNode.pid]?(tree_item[tNode.pid].imgYn+','+(tree_item[tNode.pid].childDataCnt==tNode.sortOrder?false:true)):'';
		
		tNode=tree_item[id_arr[i]];

		var display = ytTreeConfig.openDepth != 'all' ? (ytTreeConfig.openDepth > tNode.depth?'block':'none') :'block';
		var flag = display=='block'?true:false;
		var iconImg = (tNode.icon =='' || tNode.icon ==undefined) ?(tNode.childDataCnt==0 && tNode.childCnt==0 ? icon.node :(tNode.childDataCnt == 0 && tNode.childCnt > 0 ? icon.folder:(flag?icon.folderOpen:icon.folder))):tNode.icon;

		if(tNode.depth ==0){
			treeHtml.push('<div id="'+tNode.id+'" style="display:'+ytTreeConfig.topMenuView+'"><img id="'+tNode.id+'_icon" src="'+icon.root+'" onclick ="ytTreeConfig.toggle(\''+tNode.id+'\')"> <a href ="javascript:" id = "'+tNode.id+'_a" ondblclick="ytTreeConfig.toggle(\''+tNode.id+'\')" onclick="ytTreeConfig.doClick(\''+tNode.id+'\')" target="'+(tNode.target?tNode.target:'')+'">'+tNode.name+'</a> </div><span id="c_'+tNode.id+'" style="display:inline">'+this.toString(childNodeArr) +'</span>');
		}else{
			if(tNode.childDataCnt ==0){
				treeHtml.push('<div id="'+tNode.id+'" style="display:block">'+this.setImg(tNode, display)+'<img id="'+tNode.id+'_icon" src="'+iconImg+'"> <a href ="javascript:" id="'+tNode.id+'_a" ondblclick="ytTreeConfig.toggle(\''+tNode.id+'\')" onclick="ytTreeConfig.doClick(\''+tNode.id+'\')" target="'+(tNode.target?tNode.target:'')+'">'+tNode.name+'</a> </div><span id="c_'+tNode.id+'" style="display:none"></span>');
				
			}else{
				treeHtml.push('<div id="'+tNode.id+'" style="display:block">'+this.setImg(tNode, display)+'<img id="'+tNode.id+'_icon" src="'+iconImg+'"> <a href="javascript:" id="'+tNode.id+'_a" ondblclick="ytTreeConfig.toggle(\''+tNode.id+'\')" onclick= "ytTreeConfig.doClick(\''+tNode.id+'\')" target="'+(tNode.target?tNode.target:'')+'">'+tNode.name+'</a> </div><span id ="c_'+tNode.id+'" style="display:'+display+'">' +this.toString(childNodeArr) +'</span>');
			}
		}
	}
	
	if(firstNodeFlag && obj===undefined)	treeHtml.push('</div>');
	
	return treeHtml.join('');
}
ytTree.prototype.getSelect= function(){
	var sNode = ytTreeConfig.selectedNode;
	if(sNode !=null && sNode != ''){
		document.getElementById(sNode.id+'_a').className = 'style2';
		return sNode;
	}
}

ytTree.prototype.open= function(){
	var icon = ytTreeConfig.icon;
	var sNode = ytTreeConfig.selectedNode;
	var treeItem = ytTreeConfig.treeItem;
	ytTreeConfig.waitStop();
	if(sNode==null && ytTreeConfig.divName){
		document.getElementById(ytTreeConfig.divName).innerHTML = this.toString();
	}else{
		if(sNode.id){
			ytTreeConfig.idArr = treeItem[sNode.id].childNodes;
			if(ytTreeConfig.idArr.length >0) this.setFolder(sNode);
			document.getElementById('c_'+sNode.id).innerHTML = this.toString();
		}
	}
	ytTreeConfig.idArr =new Array();
	this.getSelect();
}

ytTree.prototype.allOpen= function(){
	var rootArr =  ytTreeConfig.rootArr;
	var treeItem = ytTreeConfig.treeItem;
	var rootArrLen =rootArr.length;
	
	for(var i =0 ; i <rootArrLen ;  i ++){
		var tNode =treeItem[rootArr[i]];

		this.setFolderOpenImg(tNode);
		this.folderOpen(tNode);

	}
}

ytTree.prototype.folderOpen = function (tNode){
	var cNode = tNode.childNodes;
	var cNodeLen = cNode.length;
	var treeItem = ytTreeConfig.treeItem;

	if( cNodeLen > 0 ){
		for(var i=0; i < cNodeLen; i ++){
			var tmpNode = treeItem[cNode[i]];
			this.setFolderOpenImg(tmpNode)
			
			if(tmpNode.childNodes.length >0) this.folderOpen(tmpNode);
		}
	}
}

ytTree.prototype.setFolderOpenImg=function (tNode){
	var icon = ytTreeConfig.icon;
	var treeItem = ytTreeConfig.treeItem;

	var c_obj = document.getElementById('c_'+tNode.id);
	if(c_obj != '' && c_obj != undefined){
		
		if(tNode.depth > 0  && tNode.childNodes.length > 0){
			c_obj.style.display='inline';
			document.getElementById(tNode.id+'_icon').src = tNode.icon =='' || tNode.icon ==undefined ? (icon.folderOpen) : (tNode.iconOpen==''||tNode.iconOpen==undefined?tNode.icon:tNode.iconOpen);
			document.getElementById(tNode.id+'_join').src = treeItem[tNode.pid].childDataCnt != tNode.sortOrder?icon.minus:icon.minusBottom;
		}
	}
}

ytTree.prototype.allClose= function(){
	var rootArr =  ytTreeConfig.rootArr;
	var treeItem = ytTreeConfig.treeItem;
	var rootArrLen =rootArr.length;
	
	for(var i =0 ; i <rootArrLen ;  i ++){
		var tNode =treeItem[rootArr[i]];

		this.setFolderCloseImg(tNode);
		this.folderClose(tNode);

	}
}

ytTree.prototype.folderClose = function (tNode){
	var cNode = tNode.childNodes;
	var cNodeLen = cNode.length;
	var treeItem = ytTreeConfig.treeItem;

	if( cNodeLen > 0 ){
		for(var i=0; i < cNodeLen; i ++){
			var tmpNode = treeItem[cNode[i]];
			this.setFolderCloseImg(tmpNode)
			
			if(tmpNode.childNodes.length >0) this.folderClose(tmpNode);
			
		}
	}
}

ytTree.prototype.setFolderCloseImg=function (tNode){
	var icon = ytTreeConfig.icon;
	var treeItem = ytTreeConfig.treeItem;

	var c_obj = document.getElementById('c_'+tNode.id);
	if(c_obj != '' && c_obj != undefined){
		if(tNode.depth > 0  && tNode.childNodes.length > 0){
			c_obj.style.display='none';		
			document.getElementById(tNode.id+'_icon').src = tNode.icon =='' || tNode.icon ==undefined?icon.folder:tNode.icon;
			document.getElementById(tNode.id+'_join').src = treeItem[tNode.pid].childDataCnt != tNode.sortOrder?icon.plus:icon.plusBottom;
		}
	}
}

ytTree.prototype.clearTree=function (){
	ytTreeConfig.treeItem=new Object();
	ytTreeConfig.selectedNode = null;
	ytTreeConfig.rootArr=new Array();
	document.getElementById(ytTreeConfig.divName).innerHTML ='';
}

ytTree.prototype.getSelectItem=function (){
	return ytTreeConfig.selectedNode; 
}

