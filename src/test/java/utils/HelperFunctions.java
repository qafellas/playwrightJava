package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HelperFunctions {
    Page page;
    public HelperFunctions(Page page){
        this.page = page;
    }

    public String cellData(int rowNum, int colNum){
        Locator cell = page.locator("table[name='BookTable'] > tbody > tr:nth-child(" + rowNum + ") > td:nth-child(" + colNum + ")");
        return cell.textContent();
    }
}
