package org.qafellas;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.*;

import java.util.ArrayList;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ElementActions {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    public void launchBrowser(){
        playwright = Playwright.create();
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("--start-maximized");
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(arguments).setSlowMo(3000));
    }

    @AfterClass
    public void closeBrowser(){
        playwright.close();
    }

    @BeforeMethod
    public void createContextAndPage(){
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = context.newPage();
    }

    @AfterMethod
    public void closeContext(){
        context.close();
    }

    @Test
    public void shouldClickAndType(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        // xpath --> //input[@id='name']
        // css --> input[id='name'], #name
        Locator textBox = page.locator("#name");
        textBox.fill("Florida");
        assertThat(textBox).hasAttribute("placeholder", "Enter Name");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("New Tab")).click();
    }

    @Test
    public void shouldDoubleClick(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Copy Text")).dblclick();
    }

    @Test
    public void shouldCheckBox(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        Locator dayCheckBox = page.locator("(//input[@type='checkbox'])[2]"); //Monday
        dayCheckBox.check();
        assertThat(dayCheckBox).isChecked();

        Locator genderRadioBtn = page.getByLabel("Male", new Page.GetByLabelOptions().setExact(true));
        genderRadioBtn.check();
        assertThat(genderRadioBtn).isChecked();
    }
}
