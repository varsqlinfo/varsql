package com.varsql.core.common;

import static org.apache.commons.lang3.Validate.isTrue;

import java.io.IOException;
import java.util.Objects;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.varsql.core.sql.template.TemplateHelpers;
import com.vartech.common.utils.StringUtils;

public class TemplateFactory {
	
	private Handlebars handlebars;
	
	private static class FactoryHolder{
        private static final TemplateFactory instance = new TemplateFactory();
    }

	public static TemplateFactory getInstance() {
		return FactoryHolder.instance;
    }
	
	private TemplateFactory(){

		handlebars = new Handlebars();

		handlebars.setPrettyPrint(true);
		handlebars.registerHelper("equals", euqalsHelper());
		handlebars.registerHelper("xif", xifHelper());
//		handlebars.registerHelper("and", andHelper());
		
//		handlebars.registerHelper("eq", ConditionalHelpers.eq);
//		handlebars.registerHelper("neq", ConditionalHelpers.neq);
		handlebars.registerHelper("and", ConditionalHelpers.and);
		handlebars.registerHelper("or", ConditionalHelpers.or);
        
        
		handlebars.registerHelper("camelCase", camelCaseHelper());

		for(TemplateHelpers helper : TemplateHelpers.values()) {
			handlebars.registerHelper(helper.name(), helper);
		}
	}
	
	/**
	 * handlebars template create
	 * 
	 * @param template string template
	 * @return Template 
	 * @throws IOException
	 */
	public Template compileInline(String template) throws IOException {
		return handlebars.compileInline(template);
	}
	
	/**
	 * template render 
	 * 
	 * @param template 템플릿
	 * @param param 템플릿 파람
	 * @return
	 * @throws IOException
	 <pre>
	 HashMap param = new HashMap();
	 param.put("testkey","asdf");
	 String renderTempalte = render("<test>{{aaa}}</test>",param);
	 
	 
	 </pre>
	 */
	public String render(String template, Object param) throws IOException {
		return handlebars.compileInline(template).apply(param);
	}
	
	private static Helper<Object> euqalsHelper() {
	    return new Helper<Object>() {
	    	@Override
			public Object apply(Object arg0, Options options) throws IOException {
				Object obj1 = options.param(0);
		        return Objects.equals(obj1, arg0) ? options.fn() : options.inverse();
			}
	    };
	}
	
	private static Helper<String> camelCaseHelper() {
		return new Helper<String>() {
			@Override
			public Object apply(String text, Options options) throws IOException {
				return StringUtils.camelCase(text);
			}
		};
	}
	
	private static Helper<Object> xifHelper() {
		return new Helper<Object>() {
			@Override
			public Object apply(Object arg0, Options options) throws IOException {
				Object obj1 = options.param(0);
				Object obj2 = options.param(1);
				
				boolean result = false; 
				
				if("==".equals(obj1)) {
					result = Objects.equals(arg0,obj2) ; 
				}

				if("!=".equals(obj1)) {
					result = !Objects.equals(arg0,obj2) ; 
				}
				
				if("<".equals(obj1)) {
					result = compare (arg0, obj2) < 0 ;
				}

				if("<=".equals(obj1)) {
					result = compare (arg0, obj2) <= 0;
				}

				if(">".equals(obj1)) {
					result = compare (arg0, obj2) > 0;
				}

				if(">=".equals(obj1)) {
					result = compare (arg0, obj2) >= 0;	
				}
				
				if(options.tagType == TagType.SUB_EXPRESSION) {
					return result; 
				}
				
				return result ? options.fn() : options.inverse();
			}
		};
	}
	
	private static int compare(final Object a, final Object b) {
		try {
			isTrue(a instanceof Comparable, "Not a comparable: " + a);
			isTrue(b instanceof Comparable, "Not a comparable: " + b);
			return ((Comparable) a).compareTo(b);
		} catch (ClassCastException x) {
			return Double.compare(parseDouble(a, x), parseDouble(b, x));
		}
	}

	private static double parseDouble(final Object value, final RuntimeException x) {
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		throw x;
	}
}
