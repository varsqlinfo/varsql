package com.varsql.web.configuration;

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

import com.vartech.common.constants.ViewResourceConstants;

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

		String url  = this.getUrl();

		if(url.endsWith(ViewResourceConstants.DIALOG_SUFFIX) || url.endsWith(ViewResourceConstants.POPUP_SUFFIX)) {

			BasicTilesContainer container = (BasicTilesContainer) TilesAccess.getContainer(tilesContext);

			if (container == null) {
	            throw new ServletException("Tiles container is not initialized");
	        }

			exposeModelAsRequestAttributes(model,request);

			Request tilesRequest = new ServletRequest(container.getApplicationContext(), request, response);

			Definition definition = null;
			if(url.endsWith(ViewResourceConstants.DIALOG_SUFFIX)) {
				definition = container.getDefinitionsFactory().getDefinition("common.dialog", tilesRequest);
				definition.putAttribute(ViewResourceConstants.BODY_KEY, Attribute.createTemplateAttribute(getViewUrl( ViewResourceConstants.DIALOG_SUFFIX)));
			}else {
				definition = container.getDefinitionsFactory().getDefinition("common.popup", tilesRequest);
				definition.putAttribute(ViewResourceConstants.BODY_KEY, Attribute.createTemplateAttribute(getViewUrl( ViewResourceConstants.POPUP_SUFFIX)));
			}

			container.render(definition, tilesRequest);
		}else {
			super.renderMergedOutputModel(model, request, response);
		}
	}

	private String getViewUrl(String replaceStr) {
		return new StringBuffer().append(ViewResourceConstants.VIEW_PREFIX)
				.append(this.getUrl().replace(replaceStr, ""))
				.append(ViewResourceConstants.VIEW_SUFFIX)
				.toString();
	}
}
