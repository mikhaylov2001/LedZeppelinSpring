package com.javarush.lesson22;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApplicationUnderTestTest {

    public static final String loginPage ="http://localhost:8080/";
    public static final String loginField="//input[@id='login']";
    public static final String loginValue="Carl";
    public static final String passwordField="//input[@id='password']";
    public static final String passwordValue="456";
    public static final String buttonSubmit="//button[@id='loginUser']";
    public static final String textWithUserName="//div/span[text()='Carl']";
    public static final String userNameValue="Carl";

    @Test
    @DisplayName("When user input login and password Then show users list")
    void whenUserInputLoginAndPasswordThenShowUsersList() {
        Playwright playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
        launchOptions.setHeadless(false);
        Browser browser = playwright.chromium().launch(launchOptions);
        try (playwright; browser) {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions());
            Page page = context.newPage();
            page.navigate(loginPage);
            page.waitForLoadState();
            var timeout = new Page.WaitForSelectorOptions().setTimeout(5_000);
            page.waitForSelector(loginField, timeout).fill(loginValue);
            page.waitForSelector(passwordField).fill(passwordValue);
            Locator locator = page.locator(buttonSubmit);
            locator.focus();
            locator.dispatchEvent("click");
            locator.click();
            ElementHandle spanText = page.querySelector(textWithUserName);
            String textContent = spanText.textContent();
            Assertions.assertEquals(userNameValue, textContent);

        }
    }
}