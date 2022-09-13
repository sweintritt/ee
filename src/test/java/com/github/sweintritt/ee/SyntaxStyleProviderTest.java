package com.github.sweintritt.ee;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SyntaxStyleProviderTest {

    @Test
    public void test() {
        final Map<String, String> values = new HashMap<>();

        values.put("file.c", SyntaxConstants.SYNTAX_STYLE_C);
        // TODO Could be c or c++
        // values.put("file.h", SyntaxConstants.SYNTAX_STYLE_C);
        values.put("file.cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        values.put("file.cxx", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        values.put("file.hpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        values.put("file.hxx", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        values.put("file.css", SyntaxConstants.SYNTAX_STYLE_CSS);
        values.put("file.csv", SyntaxConstants.SYNTAX_STYLE_CSV);
        values.put("Dockerfile", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
        values.put("file.go", SyntaxConstants.SYNTAX_STYLE_GO);
        values.put("file.groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        values.put("file.html", SyntaxConstants.SYNTAX_STYLE_HTML);
        values.put("file.ini", SyntaxConstants.SYNTAX_STYLE_INI);
        values.put("file.java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        values.put("file.js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        values.put("file.json", SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
        values.put("file.tex", SyntaxConstants.SYNTAX_STYLE_LATEX);
        values.put("Makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        values.put("file.md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        values.put("file.properties", SyntaxConstants.SYNTAX_STYLE_INI);
        values.put("file.py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        values.put("file.sql", SyntaxConstants.SYNTAX_STYLE_SQL);
        values.put("file.bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        values.put("file.xml", SyntaxConstants.SYNTAX_STYLE_XML);
        values.put("file.yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
        values.put("file.xhtml", SyntaxConstants.SYNTAX_STYLE_HTML);
        values.put("file.jsf", SyntaxConstants.SYNTAX_STYLE_HTML);

        for (final Map.Entry<String, String> entry : values.entrySet()) {
            assertThat(SyntaxStyleProvider.get(new File(entry.getKey()))).isEqualTo(entry.getValue());
        }
    }
}
