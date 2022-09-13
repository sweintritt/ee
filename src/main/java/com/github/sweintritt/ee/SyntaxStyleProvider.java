package com.github.sweintritt.ee;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SyntaxStyleProvider {

    private static final Map<String, String> MIME_TYPE_TO_SYNTAX_STYLE;
    private static final Map<String, String> FILE_EXTENTSION_TO_SYNTAX_STYLE;
    static {
        final List<Triple<String, List<String>, List<String>>> mappings = new LinkedList<>();
        // TODO Better put this in a config file
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_C, Collections.singletonList("text/x-c"), Arrays.asList(".c", ".h")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, Collections.emptyList(), Arrays.asList(".cc", ".cpp", ".cxx", ".h", ".hh", ".hxx", ".hpp")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_CSS, Collections.singletonList("text/css"), Collections.singletonList(".css")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_CSV, Arrays.asList("text/tab-separated-values", "text/csv"), Arrays.asList(".tsv", ".csv")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, Collections.emptyList(), Collections.singletonList("Dockerfile")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_GO, Collections.emptyList(), Collections.singletonList(".go")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_GROOVY, Collections.emptyList(), Collections.singletonList(".groovy")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_HTML, Arrays.asList("text/html", "application/xhtml+xml"), Arrays.asList(".html", ".xhtml", ".jsf")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_INI, Collections.emptyList(), Arrays.asList(".ini", ".properties")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_JAVA, Collections.singletonList("text/x-java-source"), Collections.singletonList(".java")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, Arrays.asList("text/javascript", "application/ecmascript", "application/javascript", "application/x-ecmascript",
                "application/x-javascript", "text/ecmascript", "text/javascript1.0", "text/javascript1.1", "text/javascript1.2",
                "text/javascript1.3", "text/javascript1.4", "text/javascript1.5", "text/jscript", "text/livescript", "text/x-ecmascript",
                "text/x-javascript"), Collections.singletonList(".js")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS, Arrays.asList("application/json", "text/json"), Collections.singletonList(".json")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_LATEX, Arrays.asList("application/x-latex", "application/x-tex"), Arrays.asList(".latex", ".tex", ".dtd")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, Collections.emptyList(), Collections.singletonList("Makefile")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, Collections.singletonList("text/markdown"), Arrays.asList(".md", ".markdown", ".mdown", ".markdn")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_PYTHON, Collections.singletonList("text/x-python"), Collections.singletonList(".py")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_SQL, Collections.singletonList("application/sql"), Collections.singletonList(".sql")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, Arrays.asList("application/x-sh", "application/x-shellscript"), Collections.singletonList(".sh")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC, Collections.emptyList(), Arrays.asList(".vb", ".bas")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH, Collections.emptyList(), Collections.singletonList(".bat")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_XML, Arrays.asList("text/xml", "application/xml"), Collections.singletonList(".xml")));
        mappings.add(Triple.of(SyntaxConstants.SYNTAX_STYLE_YAML, Collections.emptyList(), Arrays.asList(".yaml", ".yml")));

        final Map<String, String> mimeTypeToSyntaxStyle = new HashMap<>();
        mappings.forEach(e -> e.getMiddle().forEach(m -> mimeTypeToSyntaxStyle.put(m, e.getLeft())));
        MIME_TYPE_TO_SYNTAX_STYLE = Collections.unmodifiableMap(mimeTypeToSyntaxStyle);

        final Map<String, String> fileExtenxionToSyntaxStyle = new HashMap<>();
        mappings.forEach(e -> e.getRight().forEach(m -> fileExtenxionToSyntaxStyle.put(m, e.getLeft())));
        FILE_EXTENTSION_TO_SYNTAX_STYLE = Collections.unmodifiableMap(fileExtenxionToSyntaxStyle);
    }

    private SyntaxStyleProvider() {
        // Static use only
    }

    public static String get(final File file) {
        try {
            log.debug("Determining file type for {}", file.getAbsolutePath());
            String type = Files.probeContentType(file.toPath());
            log.debug("Files.probeContentType(): {}", type);

            if (StringUtils.isEmpty(type) || type.equals(SyntaxConstants.SYNTAX_STYLE_NONE)) {
                type = URLConnection.guessContentTypeFromName(file.getName());
                log.debug("URLConnection.guessContentTypeFromName(): {}", type);
            }

            if (StringUtils.isEmpty(type) || type.equals(SyntaxConstants.SYNTAX_STYLE_NONE)) {
                type = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
                log.debug("URLConnection.getFileNameMap().getContentTypeFor(): {}", type);
            }

            if (StringUtils.isEmpty(type) || type.equals("text/plain")) {
                return FILE_EXTENTSION_TO_SYNTAX_STYLE.entrySet().stream()
                        .filter(e -> file.getName().endsWith(e.getKey()))
                        .map(Map.Entry::getValue)
                        .findAny()
                        .orElse(SyntaxConstants.SYNTAX_STYLE_NONE);
            }

            return MIME_TYPE_TO_SYNTAX_STYLE.getOrDefault(type, SyntaxConstants.SYNTAX_STYLE_NONE);
        } catch (final Exception e) {
            log.error("Unable to get file type: {}", e.getMessage(), e);
            return  SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }
}
