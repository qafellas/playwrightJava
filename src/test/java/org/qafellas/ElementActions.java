package org.qafellas;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.MouseButton;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.HelperFunctions;

import java.nio.file.Path;
import java.nio.file.Paths;
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
        arguments.add("--start-maximized");
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(arguments).setChannel("chrome").setSlowMo(2000));
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
            Assert.assertEquals(dialog.message(), "Press a button!");
            dialog.accept();
        });
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirmation Alert")).click();
    }

    @Test
    public void shouldDragAndDrop(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        page.locator("//div[@id='draggable']").dragTo(page.locator("//div[@id='droppable']"));
        assertThat(page.locator("//div[@id='droppable']/p")).hasText("Dropped!");
    }

    @Test
    public void shouldUseKeyStrokes(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        Locator emailInputBox = page.locator("//label[text()='Email:']//following-sibling::input[1]");
        emailInputBox.fill("xyz@gmail.com");
        emailInputBox.press("Control+A");
        emailInputBox.press("Backspace");
    }

    @Test
    public void shouldDoRightClick(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        page.locator("#laptops > a:nth-child(2)").click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    @Test
    public void shouldUploadSingleFile(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        String file = "Kid.png";
        page.locator("#singleFileInput").setInputFiles(Paths.get("src/test/java/data/" + file));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Single File")).click();
        assertThat(page.locator("#singleFileStatus")).containsText("Single file selected: " + file);
    }

    @Test
    public void shouldUploadMultipleFiles(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        String file1 = "Kid.png";
        String file2 = "Qafellas.png";
        String dataFolderPath = "src/test/java/data/";
        page.locator("#multipleFilesInput").setInputFiles(new Path[]{Paths.get(dataFolderPath + file1), Paths.get(dataFolderPath + file2)});
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Multiple Files")).click();
        Locator multipleFileStatus = page.locator("#multipleFilesStatus");
        assertThat(multipleFileStatus).containsText("Multiple files selected: ");
        assertThat(multipleFileStatus).containsText(file1);
        assertThat(multipleFileStatus).containsText(file2);
    }


    @Test
    public void shouldDownloadFile(){
        page.navigate("https://www.selenium.dev/downloads/");
        Download download = page.waitForDownload(() -> {
            page.locator("//p[contains(text(), 'stable version')]/a").click();
        });
        download.saveAs(Paths.get("./src/test/java/downloads/selenium.jar"));
    }

    @Test
    public void shouldHandlePopUpWindows(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        Page page2 =  page.waitForPopup(() -> {
            page.locator("//button[text()='New Tab']").click();
        });
        assertThat(page2).hasTitle("Your Store");
        page2.locator("input[name='search']").fill("Florida is snowing!!!");
        page2.close();
        assertThat(page).hasTitle("Automation Testing Practice");
    }

    @Test
    public void shouldHandleIFrames(){
        page.setDefaultTimeout(90000);
        page.navigate("https://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_submit_get");

       // page.frameLocator("//iframe[@id='iframeResult']").locator("//button[text()='Try it']").click();
        page.frame("iframeResult").locator("//button[text()='Try it']").click();
    }

    @Test
    public void shouldHandleStaticTables(){
        page.navigate("https://testautomationpractice.blogspot.com/");
        Locator cols = page.locator("table[name='BookTable'] > tbody > tr > th");
        Locator rows = page.locator("table[name='BookTable'] > tbody > tr");
        int rowCount = rows.count();// Number of rows
        int colCount = cols.count(); // Number of columns

        // This is how we retrieve all cell values with nested loop
        for(int i = 2; i <= rowCount; i++){
            for (int j = 1; j<=colCount; j++){
                Locator cell = page.locator("table[name='BookTable'] > tbody > tr:nth-child(" + i + ") > td:nth-child(" + j + ")");
                System.out.println("row ==> " + i + ": column ==> " + j + ": cellValue ==> " + cell.textContent());
            }

            System.out.println("-------------------------------------------------------------------------");

        }

        //table[name='BookTable'] > tbody > tr:nth-child(7) > td:nth-child(4)

        HelperFunctions helperFunctions = new HelperFunctions(page);
        Assert.assertEquals(helperFunctions.cellData(3, 2), "Mukesh");
        Assert.assertEquals(helperFunctions.cellData(7, 4), "1000");

    }
}
