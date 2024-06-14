package com.varsql.core.changeset;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import com.varsql.core.changeset.beans.ChangeSetInfo;
import com.varsql.core.changeset.beans.ChangeSql;
import com.vartech.common.io.Resource;
import com.vartech.common.xml.AbstractXmlNode;
import com.vartech.common.xml.XMLParser;
import com.vartech.common.xml.XmlObjectNode;

/**
 * change log parser
 * 
 * @author ytkim
 *
 */
public class ChangeLogParser {
	
	/**
	 * change set 정보 얻기.
	 * 
	 * @param xml
	 * @param type
	 * @param version
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	public ChangeSetInfo getChangeSetInfo (Resource xml, String type, int version, String hash) throws Exception {
		
		try(InputStream is = xml.getInputStream()){
    		
    		XmlObjectNode node = XMLParser.parseXml(is, XMLParser.Options.TRIM_VALUE);
    		
    		ChangeSetInfo changeSetInfo= ChangeSetInfo.builder()
        		.type(type)
        		.hash(hash)
        		.fileName(xml.getFileName())
        		.version(version)
        		.resource(xml)
        		.description(node.get("description").nodeValue())
        		.applySqls(getApplyList(node))
        		.revertSqls(getRevertList(node))
        	.build();
    		
    		return changeSetInfo; 
    	}
	}
	
	/**
	 * 적용 xml 추출
	 * @param node
	 * @return
	 */
	private List<ChangeSql> getApplyList(XmlObjectNode node) {
		LinkedList<ChangeSql> result = new LinkedList<>();
		
		try {
			
			AbstractXmlNode apply = node.get("apply");
			
			if(apply == null) {
				return result;
			}
			
			AbstractXmlNode sql = apply.get("sql");
			
			if(sql instanceof XmlObjectNode) {
				
				result.add(ChangeSql.builder()
						.description(sql.getAttribute("description"))
						.delimiter(sql.getAttribute("delimiter"))
						.multiple(Boolean.parseBoolean(sql.getAttribute("multiple")))
						.sql(sql.nodeValue())
						.build());
			}else  {
				for (AbstractXmlNode jsonNode : apply.get("sql").toList()) {
					result.add(ChangeSql.builder()
							.description(jsonNode.getAttribute("description"))
							.delimiter(jsonNode.getAttribute("delimiter"))
							.multiple(Boolean.parseBoolean(jsonNode.getAttribute("multiple")))
							.sql(jsonNode.nodeValue())
							.build());
				}
			}
		} catch (Exception io) {
			throw new Error(io);
		}
		
		return result; 
	}
	
	/**
	 * 원복 sql 추출. 
	 * @param node
	 * @return
	 */
	private List<ChangeSql> getRevertList(XmlObjectNode node) {
		LinkedList<ChangeSql> result = new LinkedList<>();
		
		try {
			
			AbstractXmlNode revert = node.get("revert");
			
			if(revert == null) {
				return result;
			}
			
			AbstractXmlNode sql = revert.get("sql");
			
			if(sql instanceof XmlObjectNode) {
				
				result.add(ChangeSql.builder()
						.description(sql.getAttribute("description"))
						.delimiter(sql.getAttribute("delimiter"))
						.multiple(Boolean.parseBoolean(sql.getAttribute("multiple")))
						.sql(sql.nodeValue())
						.build());
			}else  {
				for (AbstractXmlNode jsonNode : revert.get("sql").toList()) {
					result.add(ChangeSql.builder()
							.description(jsonNode.getAttribute("description"))
							.delimiter(jsonNode.getAttribute("delimiter"))
							.multiple(Boolean.parseBoolean(jsonNode.getAttribute("multiple")))
							.sql(jsonNode.nodeValue())
							.build());
				}
			}
		} catch (Exception io) {
			throw new Error(io);
		}
		
		return result; 
	}
}
