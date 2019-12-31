package com.varsql.core.configuration.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.varsql.core.configuration.AbstractConfiguration;
import com.vartech.common.app.beans.ParamMap;

public abstract class AbstractXmlLoad extends AbstractConfiguration implements XmlLoad {
	protected ParamMap<String,Object> getXmlElementsInfo(Element root) throws RuntimeException {
		return getNodeValue(root);
	}
	
	private ParamMap<String,Object> getNodeValue(Element el) {
		ParamMap<String,Object> dataMap = new ParamMap<String,Object>();
		List list = el.getChildren();

		Iterator<Element> it = list.iterator();

		Map childMap = null;
		List childList = null;
		Object tmpChildObj = null;
		int childNodeLen = -1;

		Element sEle = null;
		String nodeName = null;
		List sChildList = null;
		while (it.hasNext()) {
			childMap = new HashMap();

			sEle = (Element) it.next();
			nodeName = sEle.getName();
			sChildList = sEle.getChildren();
			childNodeLen = sChildList.size();
			if (childNodeLen != 0) {
				childMap = getNodeValue(sEle);
			}
			
			if (dataMap.containsKey(nodeName)) {
				tmpChildObj = dataMap.get(nodeName);
				if (!(tmpChildObj instanceof List)) {
					childList = new ArrayList();
					childList.add(tmpChildObj);
				} else {
					childList = (List) tmpChildObj;
				}
				childList.add(childNodeLen > 0 ? childMap : sEle.getValue());
				dataMap.put(nodeName, childList);
			} else {
				dataMap.put(nodeName,
						childNodeLen > 0 ? childMap : sEle.getValue());
			}
		}
		return dataMap;
	}
}
