```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.config.MapperScannerBeanDefinitionParser;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import static org.mockito.Mockito.*;

class MapperScannerBeanDefinitionParserTest {

    private MapperScannerBeanDefinitionParser parser;
    private ParserContext parserContext;
    private Element element;
    private ClassPathMapperScanner scanner;

    @BeforeEach
    void setUp() {
        parser = new MapperScannerBeanDefinitionParser();
        parserContext = mock(ParserContext.class);
        element = mock(Element.class);
        XmlReaderContext readerContext = mock(XmlReaderContext.class);
        when(parserContext.getReaderContext()).thenReturn(readerContext);
        scanner = mock(ClassPathMapperScanner.class);
    }

    @Test
    void testParseWithTemplateRefAttributes() {
        when(element.getAttribute("template-ref")).thenReturn("sqlSessionTemplate");
        when(element.getAttribute("factory-ref")).thenReturn("sqlSessionFactory");
        when(element.getAttribute("base-package")).thenReturn("org.mybatis.example");

        parser.parse(element, parserContext);

        verify(element).getAttribute("template-ref");
        verify(element).getAttribute("factory-ref");
        verify(element).getAttribute("base-package");
        verify(scanner).setSqlSessionTemplateBeanName("sqlSessionTemplate");
        verify(scanner).setSqlSessionFactoryBeanName("sqlSessionFactory");
        verify(scanner).scan(StringUtils.tokenizeToStringArray("org.mybatis.example", ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Test
    void testParseWithInvalidAnnotationClass() {
        when(element.getAttribute("annotation")).thenReturn("invalid.AnnotationClass");

        parser.parse(element, parserContext);

        verify(parserContext.getReaderContext()).error(anyString(), any(), any());
    }

    @Test
    void testParseWithInvalidMarkerInterface() {
        when(element.getAttribute("marker-interface")).thenReturn("invalid.MarkerInterface");

        parser.parse(element, parserContext);

        verify(parserContext.getReaderContext()).error(anyString(), any(), any());
    }

    @Test
    void testParseWithInvalidNameGenerator() {
        when(element.getAttribute("name-generator")).thenReturn("invalid.NameGenerator");

        parser.parse(element, parserContext);

        verify(parserContext.getReaderContext()).error(anyString(), any(), any());
    }
}
```