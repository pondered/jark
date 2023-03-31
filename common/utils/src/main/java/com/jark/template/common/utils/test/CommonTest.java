package com.jark.template.common.utils.test;

import java.awt.Desktop;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.aventstack.extentreports.ExtentReports;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试基类
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommonTest {
    @RegisterExtension
    private final DefaultTestWatcher testWatcher = new DefaultTestWatcher();

    /**
     * 所有测试代码之前执行
     */
    @BeforeAll
    public void init(final TestInfo testInfo) {
        ExtentReportsUtil.init(testInfo);
        log.info("[{}]类开始测试", testInfo.getDisplayName());
    }

    /**
     * 每个@Test方法之前执行
     */
    @BeforeEach
    public void setUp(final TestInfo testInfo) {
        ExtentReportsUtil.methodTest(method -> method.createNode(testInfo.getDisplayName()));
        ExtentReportsUtil.initNull();
        log.info("[{}]方法开始测试", testInfo.getDisplayName());
    }

    /**
     * 每个@Test之后执行
     */
    @AfterEach
    public void tearDown(final TestInfo testInfo) {
        log.info("[{}]方法结束测试", testInfo.getDisplayName());
    }

    /**
     * 所在类所有@Test之后执行
     */
    @SneakyThrows
    @AfterAll
    public void done(final TestInfo testInfo) {
        final String name = testInfo.getTags().stream().findFirst().orElseGet(() -> testInfo.getTestClass().hashCode() + "");
        log.info("[{}]类结束测试", testInfo.getDisplayName());
        ExtentReportsUtil.extentTest(ExtentReports::flush);
        ExtentReportsUtil.classTest(classTest -> classTest.createNode("测试结束"));
        final String path = "build/test-output/" + name + ".html";
        final File file = new File(path);
        log.info("\n日志路径:   {}", file.getAbsolutePath());
        final Desktop desktop = Desktop.getDesktop();
        if (file.exists() && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.open(file.getAbsoluteFile());
        }
    }
}


