package com.zb.dividend.scraper;

import com.zb.dividend.model.Company;
import com.zb.dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
