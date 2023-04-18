package com.jark.template.common.utils.test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import cn.hutool.core.date.DatePattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.TestInfo;

/**
 * 报表生成工具类
 */
@Slf4j
@Data
public final class ExtentReportsUtil {
    private static Map<String, ExtentReports> extentMap = new HashMap<>();

    private static ExtentTest classTest;

    private static ExtentTest methodTest;

    private static ExtentReports extent;

    private static ExtentTest initTest;

    private ExtentReportsUtil() {
    }

    public static void methodNull() {
        methodTest = null;
    }

    public static void initNull(){
        initTest = null;
    }

    public static void extentTest(final Consumer<ExtentReports> extentTest) {
        extentTest.accept(ExtentReportsUtil.extent);
    }

    public static void initTest(final Consumer<ExtentTest> initTest) {
        initTest.accept(ExtentReportsUtil.initTest);
    }

    public static void classTest(final Consumer<ExtentTest> classTest) {
        classTest.accept(ExtentReportsUtil.classTest);
    }
    public static void methodTest(final Consumer<ExtentTest> methodTest) {
        methodTest.accept(ExtentReportsUtil.methodTest);
    }


    public static void init(final TestInfo testInfo) {
        final String displayName = testInfo.getDisplayName();
        final String name = testInfo.getTags().stream().findFirst().orElseGet(() -> testInfo.getTestClass().hashCode() + "");
        if (!extentMap.containsKey(name)) {
            final String path = "build/test-output/" + name + ".html";
            extent = new ExtentReports();
            ExtentSparkReporter spark =
                new ExtentSparkReporter(path)
                    .config(
                        ExtentSparkReporterConfig.builder()
                            .offlineMode(true)
                            .documentTitle(name)
                            .timeStampFormat(DatePattern.NORM_DATETIME_MS_PATTERN)
                            .build())
                    .viewConfigurer()
                    .viewOrder()
                    .as(
                        new ViewName[] {
                            ViewName.AUTHOR,
                            ViewName.CATEGORY,
                            ViewName.DASHBOARD,
                            ViewName.DEVICE,
                            ViewName.EXCEPTION,
                            ViewName.LOG,
                            ViewName.TEST
                        })
                    .apply();
            extent.attachReporter(spark);
            extentMap.put(name, extent);
            classTest = extent.createTest(displayName);
            initTest = classTest.createNode("测试初始化");
            methodTest = classTest.createNode(testInfo.getDisplayName());
        } else {
            extent = extentMap.get(name);
        }
    }
}


