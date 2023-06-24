package com.example;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.XmlInclude;

import java.util.*;
import java.lang.reflect.Method;
import org.testng.annotations.Test;

public class AlterListener implements IAlterSuiteListener {
    private Map<String, Map<String, Map<String, List<String>>>> info = new HashMap<>() {
        {
            put("Suite1", new HashMap<>() {
                {
                    put("Test1", new HashMap<>() {
                        {
                            put("com.example.MyApiTest", new ArrayList<>() {
                                {
                                    add("testGet");
                                }
                            });
                            put("com.example.MyApiTest1", new ArrayList<>() {
                                {
                                    add("testGet");
                                }
                            });
                        }
                    });
                    put("Test2", new HashMap<>() {
                        {
                            put("com.example.MyApiTest", new ArrayList<>() {
                                {
                                    add("testGet");
                                    add("testPost");
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    @Override
    public void alter(List<XmlSuite> suites) {
        for (Map.Entry<String, Map<String, Map<String, List<String>>>> suiteEntry : info.entrySet()) {
            XmlSuite suite = new XmlSuite();
            suite.setName(suiteEntry.getKey());

            for (Map.Entry<String, Map<String, List<String>>> testEntry : suiteEntry.getValue().entrySet()) {
                XmlTest test = new XmlTest(suite);
                test.setName(testEntry.getKey());
                test.addParameter("param1", "value1");
                List<String> groups = new ArrayList<>();
                for (Map.Entry<String, List<String>> classEntry : testEntry.getValue().entrySet()) {
                    XmlClass clazz = new XmlClass(classEntry.getKey());
                    clazz.getIncludedMethods().addAll(buildXmlIncludeForXmlClass(clazz,classEntry.getValue()));
                    test.getXmlClasses().addAll(List.of(clazz));
                    groups.addAll(classEntry.getValue());
                }
                // Set dependencies between groups
                Map<String, String> dependencyGroups = new HashMap<>();
                dependencyGroups.put("testPost", "testGet");
                test.setXmlDependencyGroups(dependencyGroups);
                test.setIncludedGroups(groups);
            }
            suites.add(suite);
        }
    }

    private List<XmlInclude> buildXmlIncludeForXmlClass(XmlClass clazz, List<String> methodList) {
        List<XmlInclude> xmlIncludes = new ArrayList<>();
        Method[] methods = clazz.getSupportClass().getMethods();
        for (Method method : methods) {
            if (method.getDeclaredAnnotation(Test.class) != null) {
                if (methodList.contains(method.getName())) {
                    XmlInclude xmlInclude = new XmlInclude(method.getName());
                    xmlIncludes.add(xmlInclude);
                } else {
                    String[] dependsOnMethods = method.getDeclaredAnnotation(Test.class).dependsOnMethods();
                    if(dependsOnMethods.length != 0) {
                        for (String dependsOnMethod : dependsOnMethods) {
                            if (methodList.contains(dependsOnMethod)) {
                                XmlInclude xmlInclude = new XmlInclude(method.getName());
                                xmlIncludes.add(xmlInclude);
                            }
                        }
                    }
                }
            }
        }
        return xmlIncludes;
    }
}