package com.varsql.app.common.view;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.varsql.app.common.constants.ViewPageConstants;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: VarsqlTilesView.java
* @DESC		: tiles render 처리. 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 11. 19. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class VarsqlTilesView extends TilesView{
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ApplicationContext tilesContext = ServletUtil.getApplicationContext(getServletContext());

		BasicTilesContainer container = (BasicTilesContainer) TilesAccess.getContainer(tilesContext);
		
		
		if (container == null) {
            throw new ServletException("Tiles container is not initialized");
        }
		 
		String url  = this.getUrl();
		
		if(url.endsWith(ViewPageConstants.DIALOG_SUFFIX)) {
			
			Request tilesRequest = new ServletRequest(container.getApplicationContext(), request, response);
			Definition definition = container.getDefinitionsFactory().getDefinition("common.dialog", tilesRequest);
			definition.putAttribute(ViewPageConstants.BODY_KEY, Attribute.createTemplateAttribute(getViewUrl( ViewPageConstants.DIALOG_SUFFIX)));
			container.render(definition, tilesRequest);
			
		}else if(url.endsWith(ViewPageConstants.POPUP_SUFFIX)) {
			Request tilesRequest = new ServletRequest(container.getApplicationContext(), request, response);
			Definition definition = container.getDefinitionsFactory().getDefinition("common.popup", tilesRequest);
			definition.putAttribute(ViewPageConstants.BODY_KEY, Attribute.createTemplateAttribute(getViewUrl( ViewPageConstants.POPUP_SUFFIX)));
			
			container.render(definition, tilesRequest); 
		}else {
			super.renderMergedOutputModel(model, request, response);
		}
	}

	private String getViewUrl(String replaceStr) {
		return new StringBuffer().append(ViewPageConstants.VIEW_PREFIX)
				.append(this.getUrl().replace(replaceStr, ""))
				.append(ViewPageConstants.VIEW_SUFFIX)
				.toString();
	}
}
