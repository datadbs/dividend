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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NaverFinanceScraper implements Scraper{
    private static final String STATISTICS_URL = "https://finance.naver.com/item/main.naver?code=005930";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;  //60*60*24
    @Override
    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try{
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test","historical-prices");
            Element tableEle = parsingDivs.get(0); // 테이블 전체

            Element tbody = tableEle.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for(Element e : tbody.children()){
                String txt = e.text();
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
            scrapResult.setDividends(dividends);

        } catch (IOException e){
            e.printStackTrace();
        }
        return scrapResult;
    }
    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

}
