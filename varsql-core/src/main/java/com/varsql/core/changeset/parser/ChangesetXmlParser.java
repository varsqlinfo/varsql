package com.varsql.core.changeset.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.varsql.core.changeset.beans.ChangeSql;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.ResourceUtils;
import com.vartech.common.utils.VartechUtils;

public class ChangesetXmlParser {
	private Resource resource;
	
	public ChangesetXmlParser(Resource resource) {
		this.resource =resource; 
	}
	
	public List<ChangeSql> parser(){
		LinkedList<ChangeSql> result = new LinkedList<>();
		
		try {
			JsonNode jsonInfo =VartechUtils.xmlToJsonNode(ResourceUtils.getResourceString(this.resource, VarsqlConstants.CHAR_SET));
			
			JsonNode apply = jsonInfo.get("apply");
			
			if(apply == null) {
				return result;
			}
			
			JsonNode sql = apply.get("sql");
			
			if(sql instanceof ObjectNode) {
				
				//CDATA 처리 할것. 
				
				System.out.println(sql.textValue()+" :: "+ sql.get(""));
				result.add(ChangeSql.builder()
						.description(sql.get("description").asText(""))
						.delimiter(sql.get("delimiter").asText(""))
						.multiple(sql.get("multiple").asBoolean())
						.sql(sql.textValue())
						.build());
			}else if(sql instanceof ArrayNode) {
				for (JsonNode jsonNode : sql) {
					result.add(ChangeSql.builder()
							.description(getJsonNodeValue(jsonNode.get("description"),""))
							.delimiter(getJsonNodeValue(jsonNode.get("delimiter"),""))
							.multiple(getJsonNodeBooleanValue(jsonNode.get("multiple"), false))
							.sql(jsonNode.textValue())
							.build());
				}
			}
			
			
		} catch (IOException io) {
			throw new Error(io);
		}
	
		return result; 
	}

	private boolean getJsonNodeBooleanValue(JsonNode jsonNode, boolean defaultValue) {
		if(jsonNode==null) {
			return defaultValue; 
		}
		return jsonNode.asBoolean(defaultValue);
	}

	private String getJsonNodeValue(JsonNode jsonNode, String defaultValue) {
		if(jsonNode==null) {
			return defaultValue; 
		}
		return jsonNode.asText(defaultValue);
	}
}
