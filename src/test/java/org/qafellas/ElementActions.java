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
        arguments.add("--enable-javascript-dialogs");
        arguments.add("--disable-popup-blocking");
        arguments.add("--start-maximized");
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(arguments).setChannel("chrome").setSlowMo(500));
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

    @Test
    public void shouldCheckAllBoxes(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        Locator checkboxes = page.locator("//label[text()='Days:']//..//input[@type='checkbox']");
        for (int i = 0; i < checkboxes.count() ; i++) {
            checkboxes.nth(i).check();
            assertThat(checkboxes.nth(i)).isChecked();
        }
    }

    @Test
    public void shouldSelectDropdownOptions(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        page.locator("#country").selectOption("china");//1 option
        page.locator("#colors").selectOption(new String[]{"Blue", "Yellow", "White"});
    }

    @Test
    public void shouldHoverOver(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        // <button class="dropbtn" fdprocessedid="xox2ns">Point Me</button>
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Point Me")).hover();
        page.locator(".dropdown-content > a:nth-child(1)").click();
    }

    @Test
    public void shouldHandleAlert(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        page.onceDialog(dialog -> {
            System.out.println("Alert text: " + dialog.message());
            dialog.accept();
        });
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirmation Alert")).click();
    }


}
