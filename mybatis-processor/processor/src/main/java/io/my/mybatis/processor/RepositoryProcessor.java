package io.my.mybatis.processor;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.my.mybatis.annotation.RepositoryMaker;
import io.my.mybatis.exception.ProcessingException;
import io.my.mybatis.generator.RepositoryGenerator;

@AutoService(Processor.class)
public class RepositoryProcessor extends AbstractProcessor {
    Logger log = LoggerFactory.getLogger(RepositoryProcessor.class);

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotationElement : roundEnv.getElementsAnnotatedWith(RepositoryMaker.class)) {
            if (annotationElement.getKind() != ElementKind.CLASS) {
                throw new ProcessingException(annotationElement, "only classes can be annotated with @%s",
                        RepositoryMaker.class.getSimpleName());
            }
            TypeElement typeElement = (TypeElement) annotationElement;

            checkValidClass(typeElement);
            String packageName = getPackageOf(annotationElement);
            
            try {
                RepositoryGenerator.generateCode(annotationElement, packageName, filer);
            } catch (IOException | ClassNotFoundException e) {
                error(null, e.getMessage());
            }
        }
        return true;
    }

    private void checkValidClass(TypeElement classElement) throws ProcessingException {
        // Cast to TypeElement, has more type specific methods
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(classElement, "The class %s is not public.",
                    classElement.getQualifiedName().toString());
        }

        // Check if it's an abstract class
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(), RepositoryMaker.class.getSimpleName());
        }

        // Check if an empty public constructor is given
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0
                        && constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
                    return;
                }
            }
        }

        // No empty constructor found
        throw new ProcessingException(classElement, "The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
    }

    private String getPackageOf(Element annotationElement) {
        StringBuffer buffer = new StringBuffer();
        String entityPackage = elementUtils.getPackageOf(annotationElement).toString();
        StringTokenizer tokenizer = new StringTokenizer(entityPackage, ".");
        
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.countTokens() == 1) {
                break;
            }
            buffer.append(tokenizer.nextToken()).append(".");
        }

        return buffer.append("repository").toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(RepositoryMaker.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String msg) {
        messager.printMessage(Kind.ERROR, msg, element);
    }
}
