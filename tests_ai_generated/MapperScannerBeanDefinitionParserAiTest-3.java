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
    private ParserContext parserContext;
    private Element element;
    private ClassPathMapperScanner scanner;

    @BeforeEach
    public void setUp() {
        parser = new MapperScannerBeanDefinitionParser();
        parserContext = mock(ParserContext.class);
        element = mock(Element.class);
        scanner = mock(ClassPathMapperScanner.class);

        XmlReaderContext readerContext = mock(XmlReaderContext.class);
        when(parserContext.getReaderContext()).thenReturn(readerContext);
        when(readerContext.getResourceLoader()).thenReturn(mock(org.springframework.core.io.ResourceLoader.class));
        when(parserContext.getRegistry()).thenReturn(mock(org.springframework.beans.factory.support.BeanDefinitionRegistry.class));
    }

    @Test
    public void testParseWithTemplateRefs() {
        when(element.getAttribute("template-ref")).thenReturn("sqlSessionTemplate");
        when(element.getAttribute("factory-ref")).thenReturn("sqlSessionFactory");
        when(element.getAttribute("base-package")).thenReturn("org.mybatis.spring.mapper");

        parser.parse(element, parserContext);

        verify(scanner).setSqlSessionTemplateBeanName("sqlSessionTemplate");
        verify(scanner).setSqlSessionFactoryBeanName("sqlSessionFactory");
        verify(scanner).scan(any(String[].class));
    }

    @Test
    public void testParseWithInvalidAttributes() {
        when(element.getAttribute("annotation")).thenReturn("InvalidAnnotation");
        when(element.getAttribute("marker-interface")).thenReturn("InvalidInterface");
        when(element.getAttribute("name-generator")).thenReturn("InvalidNameGenerator");

        parser.parse(element, parserContext);

        verify(parserContext.getReaderContext(), times(3)).error(anyString(), any(), any());
    }
}
```