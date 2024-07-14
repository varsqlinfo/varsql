package com.varsql.core.configuration.beans.pref;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @FileName  : PreferencesContextMenu.java
 * @프로그램 설명 : contextmenu 설정 bean
 * @Date      : 2020. 09. 13.
 * @작성자      : ytkim
 * @변경이력 :
 */
@JacksonXmlRootElement(localName = "preferences-item")
public class PreferencesContextMenu extends AbstractPreferences {
	@JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
	private Item[] items;

	public Item[] getItems() {
		return items;
	}
	public void setItems(Item[] items) {
		this.items = items;
	}
	@Override
	public Object getRootItem() {
		return getItems();
	}
}

class Item {

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(localName = "templateInfo")
    @JacksonXmlElementWrapper(useWrapping = false)
	private TemplateInfo[] templateInfos;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TemplateInfo[] getTemplateInfos() {
		return templateInfos;
	}
	public void setTemplateInfos(TemplateInfo[] templateInfos) {
		this.templateInfos = templateInfos;
	}

}

class TemplateInfo {

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(isAttribute = true)
	private String viewMode;

	private String main;

	@JacksonXmlProperty(localName = "propItems")
    @JacksonXmlElementWrapper(useWrapping = false)
	private PropItem [] propItems = new PropItem[0];

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getViewMode() {
		return viewMode;
	}
	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public String getMain() {
		return main;
	}
	public void setMain(String main) {
		this.main = main;
	}
}

