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
    private XmlReaderContext readerContext;
    private ClassPathMapperScanner scanner;

    @BeforeEach
    public void setUp() {
        parser = new MapperScannerBeanDefinitionParser();
        element = mock(Element.class);
        parserContext = mock(ParserContext.class);
        readerContext = mock(XmlReaderContext.class);
        scanner = mock(ClassPathMapperScanner.class);

        when(parserContext.getReaderContext()).thenReturn(readerContext);
        when(readerContext.getRegistry()).thenReturn(mock(org.springframework.beans.factory.support.BeanDefinitionRegistry.class));
        when(readerContext.getResourceLoader()).thenReturn(mock(org.springframework.core.io.ResourceLoader.class));
    }

    @Test
    public void testParseWithTemplateRefs() {
        when(element.getAttribute("template-ref")).thenReturn("sqlSessionTemplate");
        when(element.getAttribute("factory-ref")).thenReturn("sqlSessionFactory");
        when(element.getAttribute("base-package")).thenReturn("org.mybatis.test");

        parser.parse(element, parserContext);

        verify(scanner).setSqlSessionTemplateBeanName("sqlSessionTemplate");
        verify(scanner).setSqlSessionFactoryBeanName("sqlSessionFactory");
        verify(scanner).scan(new String[]{"org.mybatis.test"});
    }
}
```