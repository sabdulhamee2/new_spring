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
        when(parserContext.getRegistry()).thenReturn(mock(org.springframework.beans.factory.support.BeanDefinitionRegistry.class));
    }

    @Test
    public void testParseWithTemplateRefAttributes() {
        when(element.getAttribute("template-ref")).thenReturn("sqlSessionTemplateBeanName");
        when(element.getAttribute("factory-ref")).thenReturn("sqlSessionFactoryBeanName");

        parser.parse(element, parserContext);

        verify(scanner).setSqlSessionTemplateBeanName("sqlSessionTemplateBeanName");
        verify(scanner).setSqlSessionFactoryBeanName("sqlSessionFactoryBeanName");
    }

    @Test
    public void testParseWithBasePackage() {
        when(element.getAttribute("base-package")).thenReturn("org.mybatis.spring.mapper");

        parser.parse(element, parserContext);

        verify(scanner).scan(new String[]{"org.mybatis.spring.mapper"});
    }

    @Test
    public void testParseWithAnnotation() throws ClassNotFoundException {
        when(element.getAttribute("annotation")).thenReturn("org.mybatis.spring.annotation.MyAnnotation");
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.loadClass("org.mybatis.spring.annotation.MyAnnotation")).thenReturn(MyAnnotation.class);
        when(scanner.getResourceLoader().getClassLoader()).thenReturn(classLoader);

        parser.parse(element, parserContext);

        verify(scanner).setAnnotationClass(MyAnnotation.class);
    }

    @Test
    public void testParseWithMarkerInterface() throws ClassNotFoundException {
        when(element.getAttribute("marker-interface")).thenReturn("org.mybatis.spring.marker.MyInterface");
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.loadClass("org.mybatis.spring.marker.MyInterface")).thenReturn(MyInterface.class);
        when(scanner.getResourceLoader().getClassLoader()).thenReturn(classLoader);

        parser.parse(element, parserContext);

        verify(scanner).setMarkerInterface(MyInterface.class);
    }

    @Test
    public void testParseWithNameGenerator() throws ClassNotFoundException {
        when(element.getAttribute("name-generator")).thenReturn("org.mybatis.spring.generator.MyNameGenerator");
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.loadClass("org.mybatis.spring.generator.MyNameGenerator")).thenReturn(MyNameGenerator.class);
        when(scanner.getResourceLoader().getClassLoader()).thenReturn(classLoader);

        parser.parse(element, parserContext);

        verify(scanner).setBeanNameGenerator(any(MyNameGenerator.class));
    }

    // Dummy classes for testing purposes
    public static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }
    }

    public interface MyInterface {}

    public static class MyNameGenerator implements BeanNameGenerator {
        @Override
        public String generateBeanName(BeanDefinition definition, org.springframework.beans.factory.support.BeanDefinitionRegistry registry) {
            return "myBeanName";
        }
    }
}
```