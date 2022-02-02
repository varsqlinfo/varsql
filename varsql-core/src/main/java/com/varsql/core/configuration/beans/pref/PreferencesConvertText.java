package com.varsql.core.configuration.beans.pref;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "preferences-item")
public class PreferencesConvertText extends AbstractPreferences {
	@JacksonXmlProperty(localName = "propItems")
	@JacksonXmlElementWrapper(useWrapping = false)
	private PropItem[] propItems;
	
	public PropItem[] getPropItems() {
		return propItems;
	}

	public void setPropItems(PropItem[] propItems) {
		this.propItems = propItems;
	}

	public Object getRootItem() {
		return getPropItems();
	}
}
