package com.varsql.core.sql.format;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @FileName : SqlFormatter.java
 * @작성자 	 : ytkim
 * @Date	 : 2013. 12. 20.
 * @프로그램설명: sql formatter을 하기 위한 것 
 * @변경이력	:
 */
public abstract class VarsqlFormatter implements VarsqlFormatterInterface{
	
	public static final char LF = '\n';
	public static final char CR = '\r';
	    
	public static final String OPT_BEFORE="before";
	public static final String OPT_LAST="last";
	public static final String OPT_BEFOREINDENT="beforeIndent";
	public static final String OPT_LASTINDENT="lastIndent";
	public static final String OPT_INITINDENT="initIndent";
	
	public static final String CLAUSE_CREATE = "create";
	public static final String CLAUSE_SELECT = "select";
	public static final String CLAUSE_UPDATE = "update";
	public static final String CLAUSE_DELETE = "delete";
	public static final String CLAUSE_INSERT = "insert";
	public static final String CLAUSE_FROM = "from";
	
	private Map<String ,String> FORMAT_CLAUSE = new LinkedHashMap<String ,String>(){
		private static final long serialVersionUID = 1L;
	{
		put(CLAUSE_CREATE,"create");
		put(CLAUSE_SELECT,"select");
		put(CLAUSE_UPDATE,"update");
		put(CLAUSE_DELETE,"delete");
		put(CLAUSE_INSERT,"insert");
		
	}};

	private Map<String,HashMap<String ,String>> SQL_CLAUSE = new LinkedHashMap<String,HashMap<String ,String>>();
	
	protected int line_width = 60;
	
	private String sql;
	protected String indent_string = "\t";
	protected String new_line_string = System.lineSeparator();
	protected String tab_string = "\t";
	protected String white_string = " ";
	//protected String reg = "(/\\*)|(\\*/)|-{2}|[\\s]|[\\n]|[\\f]|[\\t]|[;]|[(]|[)]|[,]|[<]|[>]|[']|[\"]|[+]";
	protected String reg = "(/\\*)|(\\*/)|-{2}|[\\s]|[\\n]|[\\f]|[\\t]|[;]|[(]|[)]|[,]|[<]|[>]|[']|[\"]|[+]";
	
	private int indent = 0;

	private StringBuffer result = new StringBuffer();
	private String token;
	private String lcToken;
	
	String startClause="", processClause="";
	
	public VarsqlFormatter(){
		init("", getSqlClause());
	}
	
	public VarsqlFormatter(String sql){
		init(sql, getSqlClause());
	}
	
	public VarsqlFormatter(String sql , Map<String,HashMap<String ,String>> SQL_CLAUSE){
		init(sql, SQL_CLAUSE);
	}
	
	private void init(String sql , Map<String,HashMap<String ,String>> SQL_CLAUSE){
		this.sql = sql ;
		this.SQL_CLAUSE = SQL_CLAUSE ; 
		this.reg = getRegularExpression();
		this.FORMAT_CLAUSE = getFormatCluse();
	}
	
	public String execute(){
		return execute(this.sql);
	}
	
	public String execute(String sql){
		this.sql = sql; 
		return formatProcessRun(this.sql);
	}
	
	private String formatProcessRun(String sql){
		return formatProcessRun(sql,0).replaceFirst(new_line_string, "");
	}
	
	private String formatProcessRun(String sql ,int indent2){
		
		//System.out.println("indent : "+indent + " : sql : "+ sql);
		
		if("".equals(sql) || null==sql) return sql; 
		
		indent = indent2;
		
		HashMap<String,String> select_clause=null;
		
		boolean empty1=false , empty2=false;
		
		LinkedList<String> sqlTokenVal = sqlSplit(sql ,getRegularExpression());
		
		Iterator<String> iterToken = sqlTokenVal.iterator();
		
		String beforeStartClause="";
		
		while(iterToken.hasNext()){
			token = iterToken.next();
			
			//System.out.println("33333333asdfasdf  [[ " +token+" ]]");
			
			empty1 = isWhitespace(token);
			if(empty1){
				if(!empty2){
					white();
					empty2  =empty1;
				}
				continue;
			}else{
				empty2 =empty1 = false;
			}
			
			lcToken = token.toLowerCase();
			
			//System.out.println("token: ["+token+"]");
			
			if ("'".equals(lcToken)) { // 쿼리에 일반일경우.  
				String t;
				do {
					t = iterToken.next();;
					token += t;
				} while (!"'".equals(t) && iterToken.hasNext()); 
				
			} else if ("\"".equals(lcToken)) { // 일반 문자일 경우. 
				String t;
				do {
					t = iterToken.next();
					token += t;
				} while (!"\"".equals(t) && iterToken.hasNext());
			}
			
			if(FORMAT_CLAUSE.containsKey(lcToken)){
				startClause = lcToken;
			}
			
			// query 안에   ()안에 내용 처리.
			if("(".equals(lcToken)){  
				beforeStartClause = startClause;
				String t , lct;
				StringBuilder subStr = new StringBuilder();
				int  totLeftParen =1 , totRightParen = 0 ;
				
				do {
					t = iterToken.next();
					//System.out.println("t : ["+t+"]");
					lct = t.toLowerCase();
					
					empty1 = isWhitespace(t);
					
					//System.out.println("lct : " +lct);
					
					if(empty1){
						if(!empty2){
							subStr.append(white_string);
							empty2  =empty1;
						}
						continue;
					}else{
						empty2 =empty1 = false;
					}
					
					if("(".equals(lct)) ++totLeftParen;
					
					if(")".equals(lct)) ++totRightParen;
					
					if(totLeftParen==totRightParen){
						break; 
					}
					
					subStr.append(t);
				} while ( iterToken.hasNext()); // cannot
				
				String tmpSubStr = subStr.toString(); 
				
				if(tmpSubStr.length() < line_width){
					result.append("(");
					result.append(tmpSubStr);
					result.append(")");
					
				}else{
					selectTokenOut(lcToken ,SQL_CLAUSE.get(lcToken));
					formatProcessRun(tmpSubStr, indent);
					token = ")";
					lcToken = token.toLowerCase();
					selectTokenOut(lcToken ,SQL_CLAUSE.get(lcToken));
				}
				
				startClause = beforeStartClause;
				continue; 
			}
			
			if("--".equals(lcToken)){
				String t;
				do {
					t = iterToken.next();
					if(t.indexOf(LF) > -1 || t.indexOf(CR) > -1){
						break; 
					}else{
						token += t;
					}
				} while ( iterToken.hasNext());
				out(true);
				
				//System.out.println("11111111");
				continue; 
			}
			//System.out.println("2222");
			if(SQL_CLAUSE.containsKey(lcToken)){
				select_clause = SQL_CLAUSE.get(lcToken);
				
				selectTokenOut(lcToken ,select_clause);	
			}else{
				//System.out.println("lcToken : " +lcToken + " ] token : "+token + "]" );
				out(false);
			}
		}
		
		return result.toString();
	}
	
	private void selectTokenOut(String lcToken2, HashMap<String, String> select_clause) {
		
		String beforeVal = "", lastval = "";
		int scindent = 0;
		boolean indentBeforeFlag= false, indentLastFlag = false;
		
		// 구분절앞에 값넣기 start. 
		if(select_clause.containsKey(startClause+OPT_BEFORE)){
			beforeVal =select_clause.get(startClause+OPT_BEFORE);
		}else{
			beforeVal =select_clause.containsKey(OPT_BEFORE) ? select_clause.get(OPT_BEFORE):"";
		}
		
		if(!"order".equals(processClause) || ("order".equals(processClause) && !",".equals(lcToken2))){
			result.append(beforeVal);
		}
		// 구분절앞에 값넣기 end.
		
		if(select_clause.containsKey(startClause+OPT_BEFOREINDENT)){
			indentBeforeFlag = true; 
			scindent  = Integer.parseInt(select_clause.get(startClause+OPT_BEFOREINDENT));
		}else{
			if(select_clause.containsKey(OPT_BEFOREINDENT)){
				indentBeforeFlag = true; 
				scindent  = Integer.parseInt(select_clause.get(OPT_BEFOREINDENT));
			}
		}
		
		indent = indent +scindent;
		
		//System.out.println("lcToken2 : [" +lcToken2+"]startClause: ["+startClause+"] : indent: ["+indent+"]indentBeforeFlag:["+indentBeforeFlag+"] select_clause : "+select_clause);
		
		if(indentBeforeFlag){
			indentTabIns();
		}else{
		
			if(new_line_string.equals(beforeVal)){
				 // 선택된값 넣기.
				if("order".equals(processClause) && ",".equals(lcToken2)) {
					
				}else{
	//				/System.out.println("indent : [ " + indent + " ] " +select_clause);
					indentTabIns();
					processClause=lcToken2;
				}
			}
		}
		
		out(true);
		
		scindent =0; 
		
		if(select_clause.containsKey(startClause+OPT_LASTINDENT)){
			indentLastFlag =true; 
			scindent  = Integer.parseInt(select_clause.get(startClause+OPT_LASTINDENT));
		}else{
			if(select_clause.containsKey(OPT_LASTINDENT)){
				indentLastFlag =true; 
				scindent  = Integer.parseInt(select_clause.get(OPT_LASTINDENT));
			}
		}
		indent = indent +scindent;
		
		if(select_clause.containsKey(startClause+OPT_LAST)){
			lastval = select_clause.get(startClause+OPT_LAST);
		}else{
			if(select_clause.containsKey(OPT_LAST)){
				lastval = select_clause.get(OPT_LAST);
			}
		}
		
		//System.out.println("indent ["+indent+"]beforeVal : [" +beforeVal+ "] lastval : ["+lastval+"] startClause : [" + startClause+"]");
		if(!"".equals(lastval)){
			result.append(lastval);
		}
		
		if(new_line_string.equals(lastval)){
			if(indentLastFlag){
				indentTabIns();
			}
		}
		
		scindent = 0; 
		// 해당 절에 대한 indent 0으로 초기화
		if(select_clause.containsKey(startClause+OPT_INITINDENT)){
			scindent  = Integer.parseInt(select_clause.get(startClause+OPT_INITINDENT));
			indent = scindent; 
		}else{
			if(select_clause.containsKey(OPT_INITINDENT)){
				scindent  = Integer.parseInt(select_clause.get(OPT_INITINDENT));
				indent = scindent; 
			}
		}
		
		indent = indent < 0 ? 0 : indent;
	}
	
	private boolean isWhitespace(String token) {
		boolean returnFlag = false; 
		if(Pattern.compile("\\p{Space}").matcher(token).matches()){
			if(token.indexOf(LF) > -1 || token.indexOf(CR) > -1){
				returnFlag= false; 
			}else{
				returnFlag= true; 
			}
		}else{
			returnFlag= false; 
		}
		
		//System.out.println(returnFlag+" ||"+token+";;");
		
		return returnFlag;
		//return Pattern.compile("\\p{Space}").matcher(token).matches();
	}

	private void out(boolean b) {
		if(!isWhitespace(token)) result.append(token);
		else result.append(white_string);
	}
	
	private void newline() {
		result.append(new_line_string);
	}
	
	private void tabIns(){
		result.append(indent_string);
	}
	
	private void indentTabIns(){
		for (int i = 0; i < indent; i++) {
			tabIns();
		}
	}
	private void white() {
		if(result.length() < 1){
			return ;
		}
		
		if(!Character.isWhitespace(result.charAt(result.length()-1))){
			result.append(white_string);
		}
	}
	
	public LinkedList<String> sqlSplit(String sql, String regex) {
		LinkedList<String> parts = new LinkedList<String>();
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		
		int start=0 ,lastEnd = 0;
		
		while (m.find()) {
			start = m.start();
			
			if (lastEnd != start) {
				parts.add(sql.substring(lastEnd, start)); // non delim
			}
			parts.add(m.group()); // delim
			
			lastEnd = m.end();
		}
		
		if (lastEnd != sql.length()) {
			parts.add(sql.substring(lastEnd));
		}
		
		return parts;
	}
	
	public void setIndextString(String indentStr){
		this.indent_string = indentStr; 
	}
	
	public void setNewLineString(String newLineString){
		this.new_line_string = newLineString; 
	}
	
	public void setTabString(String tabString){
		this.tab_string= tabString; 
	}
	
	public void setWhiteString(String whiteString){
		this.indent_string = whiteString; 
	}
	
	public String getRegularExpression() {
		return this.reg;
	}
	
	public Map<String, String> getFormatCluse() {
		return this.FORMAT_CLAUSE;
	}

	@SuppressWarnings("serial")
	public Map<String, HashMap<String, String>> getSqlClause() {
		Map<String,HashMap<String ,String>> SQL_CLAUSE = new LinkedHashMap<String,HashMap<String ,String>>();
		//########################enter############################
		
		SQL_CLAUSE.put("create", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
		}});
		
		SQL_CLAUSE.put("select", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,new_line_string);
			put(OPT_LASTINDENT,"1");
		}});
		
		SQL_CLAUSE.put("insert", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		
		SQL_CLAUSE.put("update", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		
		SQL_CLAUSE.put("delete", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		
		SQL_CLAUSE.put("/*", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		
		SQL_CLAUSE.put("*/", new HashMap<String,String>(){{
			put(OPT_LAST,new_line_string);
		}});
		
		SQL_CLAUSE.put("--", new HashMap<String,String>(){{
		}});
		
		SQL_CLAUSE.put("set", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			
			put(CLAUSE_UPDATE+OPT_BEFORE,new_line_string);
			put(CLAUSE_UPDATE+OPT_LAST,new_line_string);
			
			put(CLAUSE_UPDATE+OPT_LASTINDENT,"1");
		}});
		SQL_CLAUSE.put("where", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(CLAUSE_DELETE+OPT_BEFORE,new_line_string);
			put(OPT_LAST,tab_string);
			
			put(CLAUSE_UPDATE+OPT_BEFOREINDENT,"-1");
		}});
		SQL_CLAUSE.put("and", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,tab_string);
		}});
		SQL_CLAUSE.put("or", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,tab_string);
		}});
		SQL_CLAUSE.put("group", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		SQL_CLAUSE.put("order", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		SQL_CLAUSE.put("having", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,tab_string);
		}});
		SQL_CLAUSE.put("values", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		SQL_CLAUSE.put("from", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
			put(OPT_BEFOREINDENT,"-1");
//			put(OPT_LASTINDENT,"1");
//			
//			put(CLAUSE_DELETE+OPT_BEFORE,white_string);
//			put(CLAUSE_DELETE+OPT_BEFOREINDENT,"0");
//			put(CLAUSE_DELETE+OPT_LASTINDENT,"0");
		}});
		SQL_CLAUSE.put("union", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
		}});
		
		SQL_CLAUSE.put("on", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put(",", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_LAST,white_string);
		}});
		
		SQL_CLAUSE.put(";", new HashMap<String,String>(){{
			put(OPT_LAST,new_line_string);
			put(OPT_INITINDENT,"0");
		}});
		
		SQL_CLAUSE.put("(", new HashMap<String,String>(){{
			put(OPT_LAST,new_line_string);
			put(OPT_LASTINDENT,"1");
			
			put(CLAUSE_FROM+OPT_LAST,new_line_string);
			put(CLAUSE_FROM+OPT_LASTINDENT,"0");
		}});
		SQL_CLAUSE.put(")", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
			put(OPT_BEFOREINDENT,"-1");
		}});
		
		SQL_CLAUSE.put("call", new HashMap<String,String>(){{
			put(OPT_BEFORE,new_line_string);
		}});
		
		
		//########################tab############################
		SQL_CLAUSE.put("left", new HashMap<String,String>(){{
			put(OPT_BEFORE,tab_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("right", new HashMap<String,String>(){{
			put(OPT_BEFORE,tab_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("inner", new HashMap<String,String>(){{
			put(OPT_BEFORE,tab_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("outer", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("by", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("join", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("into", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,white_string);
		}});
		SQL_CLAUSE.put("as", new HashMap<String,String>(){{
			put(OPT_BEFORE,white_string);
			put(OPT_LAST,white_string);
		}});
		
		return SQL_CLAUSE;
	}
}
