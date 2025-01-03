package org.qafellas;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BrowserActions {
    @Test
    public void shouldNavigateWebsite(){
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch( new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(5000)); // launching the browser
        Page page = browser.newPage();
        page.navigate("http://playwright.dev"); // Navigate to website
        System.out.println(page.title());
        assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
        assertThat(page).hasURL(Pattern.compile("playwright"));
        Assert.assertTrue(page.title().contains("Playwright"));
        page.close();
        playwright.close();
    }

    @Test
    public void shouldMaximizeBrowser(){
        Playwright playwright = Playwright.create();

        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("--start-maximized");

        Browser browser = playwright.chromium().launch( new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(5000).setArgs(arguments)); // launching the browser
        BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        Page page = browserContext.newPage();
        page.navigate("http://playwright.dev"); // Navigate to website
        System.out.println(page.title());
        assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
        assertThat(page).hasURL(Pattern.compile("playwright"));
        Assert.assertTrue(page.title().contains("Playwright"));

        browserContext.close();
        playwright.close();
    }

    @Test
    public void shouldNavigateBackAndForward(){
        Playwright playwright = Playwright.create();
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("--start-maximized");
        Browser browser = playwright.chromium().launch( new BrowserType.LaunchOptions().setHeadless(false).setArgs(arguments).setChannel("chrome")); // launching the browser
        BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        Page page = browserContext.newPage();

        page.navigate("http://playwright.dev"); // Navigate to website
        System.out.println(page.title());
        assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");
        assertThat(page).hasURL(Pattern.compile("playwright"));
        Assert.assertTrue(page.title().contains("Playwright"));

        page.navigate("https://google.com");
        assertThat(page).hasTitle("Google");

        page.goBack();
        assertThat(page).hasTitle("Fast and reliable end-to-end testing for modern web apps | Playwright");

        page.goForward();
        assertThat(page).hasTitle("Google");

        browserContext.close();
        playwright.close();
    }
}
