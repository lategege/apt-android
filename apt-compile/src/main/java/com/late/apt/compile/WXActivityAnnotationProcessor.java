package com.late.apt.compile;

import com.google.auto.service.AutoService;
import com.late.apt.annotations.WXActvityGenerator;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;

/**
 * Created by xuhongliang on 2017/11/15.
 */

@AutoService(Processor.class)
public class WXActivityAnnotationProcessor extends AbstractProcessor {
    //可以理解为编译期的文档管理员
    private Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();
    }

    //这个函数式注解处理的核心函数,env对象是编译器环境，可获取整个包中的所有元素属性
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        generatorJavaCode(filer, env);
        return true;
    }

    private void generatorJavaCode(Filer filer, RoundEnvironment env) {
        //首先创建一个注解属性解析器
        final WXActvityAnnotationVisitor visitor = new WXActvityAnnotationVisitor(filer);
        //获取标注了WXActvityGenerator注解的所有元素集合
        final Set<? extends Element> elementsWithWXAnnotation = env.getElementsAnnotatedWith(WXActvityGenerator.class);
        //遍历
        for (Element element : elementsWithWXAnnotation) {
            //取得带有属性的注解元素
            final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            //遍历
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                //获取注解属性值
                final Map<? extends ExecutableElement, ? extends AnnotationValue> map = annotationMirror.getElementValues();
                for (AnnotationValue annotationValue : map.values()) {
                    //把属性值丢给注解解析器处理
                    annotationValue.accept(visitor, null);
                }
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //因为就一个注解,所以创建只存在一个注解的集合
        final Set<String> annotationNames = Collections.singleton(WXActvityGenerator.class.getCanonicalName());
        return annotationNames;
    }

    final static class WXActvityAnnotationVisitor extends SimpleAnnotationValueVisitor7<Void, Void> {
        private final Filer filer;
        private String packageName;

        public WXActvityAnnotationVisitor(Filer filer) {
            this.filer = filer;
        }
        @Override
        public Void visitString(String s, Void aVoid) {
            //解析得到包名
            this.packageName = s;
            return aVoid;
        }
        @Override
        public Void visitType(TypeMirror typeMirror, Void aVoid) {
            //生成java类也就是WXEntryActivity
            generatorJavaCode(typeMirror);
            return aVoid;
        }
        private void generatorJavaCode(TypeMirror typeMirror) {
            try {
                //创建类的描述
                final TypeSpec classType = TypeSpec.classBuilder("WXEntryActivity")
                        .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                        .superclass(TypeName.get(typeMirror)).build();
                //创建java类文件
                final JavaFile javaFile = JavaFile.builder(packageName + ".wxapi", classType)
                        .addFileComment("WXActivity")
                        .build();
                //写入编译文档文件
                javaFile.writeTo(filer);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}



