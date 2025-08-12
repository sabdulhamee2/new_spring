```java
package org.mybatis.spring.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

public class MapperScannerBeanDefinitionParserTest {

    private MapperScannerBeanDefinitionParser parser;
    private Element element;
    private ParserContext parserContext;
    private ClassPathMapperScanner scanner;

    @BeforeEach
    public void setUp() {
        parser = new MapperScannerBeanDefinitionParser();
        element = mock(Element.class);
        parserContext = mock(ParserContext.class);
        XmlReaderContext readerContext = mock(XmlReaderContext.class);
        when(parserContext.getReaderContext()).thenReturn(readerContext);
        scanner = mock(ClassPathMapperScanner.class);
    }

    @Test
    public void testParseWithTemplateRefAttributes() {
        when(element.getAttribute("template-ref")).thenReturn("sqlSessionTemplate");
        when(element.getAttribute("factory-ref")).thenReturn("sqlSessionFactory");

        parser.parse(element, parserContext);

        verify(scanner).setSqlSessionTemplateBeanName("sqlSessionTemplate");
        verify(scanner).setSqlSessionFactoryBeanName("sqlSessionFactory");
    }

    @Test
    public void testParseWithBasePackage() {
        when(element.getAttribute("base-package")).thenReturn("org.mybatis.example");

        parser.parse(element, parserContext);

        verify(scanner).scan(new String[]{"org.mybatis.example"});
    }

    @Test
    public void testParseWithAnnotation() throws Exception {
        when(element.getAttribute("annotation")).thenReturn("org.mybatis.example.MyAnnotation");

        parser.parse(element, parserContext);

        verify(scanner).setAnnotationClass(any(Class.class));
    }

    @Test
    public void testParseWithMarkerInterface() throws Exception {
        when(element.getAttribute("marker-interface")).thenReturn("org.mybatis.example.MyInterface");

        parser.parse(element, parserContext);

        verify(scanner).setMarkerInterface(any(Class.class));
    }

    @Test
    public void testParseWithNameGenerator() throws Exception {
        when(element.getAttribute("name-generator")).thenReturn("org.mybatis.example.MyNameGenerator");

        parser.parse(element, parserContext);

        verify(scanner).setBeanNameGenerator(any());
    }
}
```