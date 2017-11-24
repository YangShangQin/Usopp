package com.meiyou.usopp.processor;

import com.meiyou.usopp.data.UsoppGenerateClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class UsoppProcessor extends AbstractProcessor {
    Elements elementUtils;
    Types typeUtils;
    Filer filer;
    String modulePackagerName;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processUsopp(roundEnv);
        return true;
    }


    private void processUsopp(RoundEnvironment roundEnv) {
        //int fileIndex = getFileIndex();

        Map<String, ElementHolder> map = collectClassInfo(roundEnv, com.meiyou.usopp.annotations.Usopp.class, ElementKind.CLASS);
        if (map.size() == 0) {
            System.out.println("find UsoppCompiler Annatotion size 0");
        }
        try {
            //把包名当成文件名，用来规避编译时类重复的问题
            JavaFileObject fileObject = filer.createSourceFile(UsoppGenerateClass.fullName+modulePackagerName, (Element[]) null);
            Writer writer = fileObject.openWriter();
            writer.write(UsoppGenerateClass.generateDataMap(UsoppGenerateClass.className+modulePackagerName,map));
            writer.flush();
            writer.close();
            //saveFileIndex(fileIndex);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private Map<String, ElementHolder> collectClassInfo(RoundEnvironment roundEnv,
                                                        Class<? extends Annotation> clazz, ElementKind kind) {
        Map<String, ElementHolder> map = new HashMap<>();
        int index=0;
        for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
            if (element.getKind() != kind) {
                throw new IllegalStateException(
                        String.format("@%s annotation must be on a  %s.", element.getSimpleName(), kind.name()));
            }


            TypeElement typeElement = (TypeElement) element;
            Annotation annotation = element.getAnnotation(clazz);
            Method annotationMethod = null;
            try {
                annotationMethod = clazz.getDeclaredMethod("value");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            String annotationName = null;
            try {
                annotationName = (String) annotationMethod.invoke(annotation);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            String clazzName = typeElement.getQualifiedName().toString();
            String simpleName = typeElement.getSimpleName().toString();
            /*if (map.get(annotationName) != null) {
                throw new IllegalStateException("注解名：" + annotationName + "已经被：" + map.get(annotationName).clazzName + "使用");
            }*/

            map.put(annotationName+index, new ElementHolder(typeElement, annotationName, clazzName, simpleName));
            System.out.println("UsoppCompiler get Annotation from Class :" + clazzName);
            modulePackagerName = annotationName;
            index++;

        }
        return map;
    }


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<String>();
        types.add(com.meiyou.usopp.annotations.Usopp.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
        //return SourceVersion.RELEASE_7;
    }

    public static class ElementHolder {
        public TypeElement typeElement;
        public String valueName;
        public String clazzName;
        public String simpleName;

        public ElementHolder(TypeElement typeElement, String valueName, String clazzName, String simpleName) {
            this.typeElement = typeElement;
            this.valueName = valueName;
            this.clazzName = clazzName;
            this.simpleName = simpleName;
        }
    }
}
