package com.varsql.web.util;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class MarkdownXssUtils {

	private static final Safelist safeList = Safelist.basic()
    .addTags("h1", "h2", "h3", "h4", "h5", "h6")
    .addTags("code", "pre", "hr")
    .addTags("table", "thead", "tbody", "tr", "th", "td")
    .addAttributes("a", "href", "title")
    .addAttributes("img", "src", "alt", "title")
    .addAttributes("code", "class")
    .addProtocols("a", "href", "http", "https", "mailto")
    .addProtocols("img", "src", "http", "https");
	
    /** 메인 API — Markdown 내에서 XSS 위험 요소를 코드 블록/인라인 코드로 감싼다. */
    public static String sanitizeAndSerializeHTML(String markdown) {
        
        Node document = Parser.builder().build().parse(markdown);
        
        String html =  HtmlRenderer.builder().build().render(document);
        
        String cleanHtml = Jsoup.clean(html, safeList);

        return cleanHtml;
    }
    
    // 테스트 main
    public static void main(String[] args) {
    	String markdown = """
        		asdfasdf
        		
        		
        		<script>
        		
        		alert('xss')
        		
        		</script>

<b>허용 bold</b>
<i>허용 italic</i>
<iframe src="evil.com"></iframe>
<img src=x onerror=alert('XSS')>
<a href="javascript:alert('xss')">링크</a>

inline <object data="xss" onload="evil()"></object> text
        		""";
        

        System.out.println(sanitizeAndSerializeHTML(markdown));
    }
}
