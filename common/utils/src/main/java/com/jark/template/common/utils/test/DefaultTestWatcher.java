package com.jark.template.common.utils.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.aventstack.extentreports.ExtentReports;

/**
 * 报表
 */
public final class DefaultTestWatcher implements TestWatcher {

    private ExtentReports extent;

    @Override
    public void testSuccessful(final ExtensionContext context) {
        ExtentReportsUtil.methodTest(method -> method.pass("测试成功"));
        ExtentReportsUtil.extentTest(ExtentReports::flush);
    }

    @Override
    public void testFailed(final ExtensionContext context, final Throwable e) {
        ExtentReportsUtil.methodTest(method -> method.fail(e));
        ExtentReportsUtil.extentTest(ExtentReports::flush);
        ExtentReportsUtil.methodNull();
    }

}



