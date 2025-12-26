package com.varsql.web.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * markdown xss util
 */
public class MarkdownXssUtils {

	 /** -------- 1) HTML 허용 규칙 ------- **/
    private static final Safelist SAFE_LIST;

    static {
        SAFE_LIST = Safelist.basic();

        SAFE_LIST
            .addTags("h1", "h2", "h3", "h4", "h5", "h6")
            .addTags("code", "pre", "hr")
            .addTags("table", "thead", "tbody", "tr", "th", "td")
            .addAttributes("a", "href", "title")
            .addAttributes("img", "src", "alt", "title") // HTML <img> 허용
            .addAttributes("code", "class")
            .addProtocols("a", "href", "http", "https", "mailto")
            .addProtocols("img", "src", "http", "https"); // img src는 http, https 만
    }

    /** -------- 2) 패턴 정의 ------- **/
    private static final Pattern CODE_BLOCK = Pattern.compile("```[\\s\\S]*?```");
    private static final Pattern HTML_COMMENT = Pattern.compile("<!--[\\s\\S]*?-->");
    private static final Pattern MD_LINK = Pattern.compile("\\[(.*?)\\]\\((.*?)\\)");
    private static final Pattern MD_IMAGE = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)");

    /** -------- URL 검사: javascript:, data:, vbscript: 등 차단 ------- **/
    private static boolean isSafeUrl(String url) {
        if (url == null) return false;

        String lower = url.trim().toLowerCase();

        // 불법 스킴 차단
        if (lower.startsWith("javascript:") ||
            lower.startsWith("data:") ||
            lower.startsWith("vbscript:") ||
            lower.startsWith("file:")) {
            return false;
        }

        // unicode whitespace 제거 후 재검사 (우회 방지)
        lower = lower.replaceAll("[\\x00-\\x20]+", "");

        if (lower.startsWith("javascript:") ||
            lower.startsWith("data:")) {
            return false;
        }

        // ----- 여기부터 허용 규칙 -----

        // 1) 절대 경로 ( /로 시작 )
        if (lower.startsWith("/")) return true;

        // 2) 상대 경로 (./ ../)
        if (lower.startsWith("./") || lower.startsWith("../")) return true;

        // 3) http, https, mailto
        return lower.startsWith("http://") ||
               lower.startsWith("https://") ||
               lower.startsWith("mailto:");
    }


    /** -------- Markdown 링크/이미지 URL Sanitizing ------- **/
    private static String sanitizeMarkdownUrls(String markdown) {

        // 1) 링크 처리
        Matcher m1 = MD_LINK.matcher(markdown);
        StringBuffer sb1 = new StringBuffer();
        while (m1.find()) {
            String text = m1.group(1);
            String url = m1.group(2);

            if (isSafeUrl(url)) {
                m1.appendReplacement(sb1, "[" + text + "](" + url + ")");
            } else {
                m1.appendReplacement(sb1, "[" + text + "]()"); // 위험 URL 제거
            }
        }
        m1.appendTail(sb1);

        String tmp = sb1.toString();

        // 2) 이미지 처리 — 안전한 URL만 허용
        Matcher m2 = MD_IMAGE.matcher(tmp);
        StringBuffer sb2 = new StringBuffer();
        while (m2.find()) {
            String alt = m2.group(1);
            String url = m2.group(2);

            if (isSafeUrl(url)) {
                m2.appendReplacement(sb2, "![" + alt + "](" + url + ")");
            } else {
                // 위험한 URL은 제거 → Markdown 이미지 태그 유지, src 공란
                m2.appendReplacement(sb2, "![" + alt + "]()");
            }
        }
        m2.appendTail(sb2);

        return sb2.toString();
    }

    /** -------- 메인 Sanitizer -------- **/
    public static String sanitizeAndSerializeHTML(String markdown) {

        if (markdown == null || markdown.isEmpty()) return markdown;

        // 1) 코드블록 보호
        Map<String, String> codeBlocks = new LinkedHashMap<>();
        Matcher m = CODE_BLOCK.matcher(markdown);
        int index = 0;
        while (m.find()) {
            String original = m.group();
            String token = "%%CODE_BLOCK_" + index++ + "%%";
            codeBlocks.put(token, original);
            markdown = markdown.replace(original, token);
        }

        // 2) HTML 주석 제거
        markdown = HTML_COMMENT.matcher(markdown).replaceAll("");

        // 3) Markdown 링크/이미지 URL Sanitizing
        markdown = sanitizeMarkdownUrls(markdown);

        // 4) HTML 부분 JSoup Safelist로 필터링
        markdown = Jsoup.clean(
                markdown,
                "",
                SAFE_LIST,
                new Document.OutputSettings().prettyPrint(false)
        );

        // 5) 코드블록 복원
        for (var entry : codeBlocks.entrySet()) {
            markdown = markdown.replace(entry.getKey(), entry.getValue());
        }

        return markdown;
    }
    
}
