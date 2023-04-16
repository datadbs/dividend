package com.zb.dividend.scraper;

import com.zb.dividend.model.Company;
import com.zb.dividend.model.Dividend;
import com.zb.dividend.model.ScrapedResult;
import com.zb.dividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400;  //60*60*24
    @Override
    public ScrapedResult scrap(Company company){
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try{
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            WebDriver driver = new ChromeDriver(options);
            driver.get(url);

            // 로딩 대기 시간
            Thread.sleep(5000);

            // 배당금 데이터 추출
            WebElement tableEle = driver.findElement(By.cssSelector("[data-test='historical-prices']"));

            // 테이블 바디
            WebElement tbody = tableEle.findElement(By.tagName("tbody"));

            List<Dividend> dividends = new ArrayList<>();
            for(WebElement e : tbody.findElements(By.tagName("tr"))){
                List<WebElement> cells = e.findElements(By.tagName("td"));
                String txt = e.getText();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",",""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if(month<0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(new Dividend(LocalDateTime.of(year,month,day,0,0),dividend));
            }
            driver.quit();
            scrapResult.setDividends(dividends);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {

        String url = String.format(SUMMARY_URL, ticker, ticker);
        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[0].trim();

            return new Company(ticker, title);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
