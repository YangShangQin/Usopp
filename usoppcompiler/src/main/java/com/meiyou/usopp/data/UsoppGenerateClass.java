package com.meiyou.usopp.data;

import com.meiyou.usopp.processor.UsoppProcessor;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class UsoppGenerateClass {
    public static String packageName = "com.meiyou.usopp.data";
    public static String className = "UsoppDataFile";
    public static String fullName = packageName + "." + className;
    public static String methodName ="getUsoppData";




    public UsoppGenerateClass() {

    }

/*
    public static String getClassNameForPackage(String simpleName) {
        return packageName + "." + simpleName;
    }


    public static String getValueFromClass(Class middleClass) {
        try {
            Field field = middleClass.getDeclaredField("value");
            return (String) field.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*
    public String generateMiddleClass(String middleClassName, String value) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from Summer. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");

        builder.append("import java.lang.String;\n");
        builder.append("public class ").append(middleClassName).append("{\n");

        builder.append("  public static String value=\"").append(value).append("\";\n");

        builder.append("}\n");
        return builder.toString();

    }*/


    public static String generateDataMap(String className,Map<String, UsoppProcessor.ElementHolder> map  ) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from Summer. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");

        builder.append("import java.lang.String;\n");

        builder.append("import java.util.ArrayList;\n");
        builder.append("import java.util.List;\n");

        builder.append("import java.util.Map;\n");
        builder.append("import java.util.HashMap;\n");



        builder.append("public class ").append(className).append("{\n");
        builder.append("static public Map<String, String> "+methodName+"(){\n");
        //builder.append(" List<String> list=new ArrayList<>();\n");
        builder.append(" Map<String, String> map=new HashMap<>();\n");

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            UsoppProcessor.ElementHolder info = ( UsoppProcessor.ElementHolder)entry.getValue();
            builder.append("map.put(\"").append(info.valueName).append("\"").
                    append(",\"").append(info.clazzName).append("\"")
                    .append(");\n");
        }
        /*
        for (UsoppProcessor.ElementHolder info : clazzInfoList) {

            *//*builder.append("if(map.containsKey(\"").append(info.valueName).append("\")){\n")
                    .append("throw new IllegalStateException(map.get(\""+info.valueName+"\")+\"已经使用了该名称:"+info.valueName+"\");\n")
                    .append("}");*//*
            builder.append("map.put(\"").append(info.valueName).append("\"").
                    append(",\"").append(info.clazzName).append("\"")
                    .append(");\n");
        }*/
        builder.append("return map;\n");
        builder.append("}\n");

        builder.append("}\n");
        return builder.toString();
    }

//    public Map<String, ClazzInfo> prepareData() {
//        Map<String, ClazzInfo> map = new HashMap<>();
//        for (ClazzInfo info : clazzInfoList) {
//            map.put(info.clazzName, info);
//        }
//        return map;
//    }

}
