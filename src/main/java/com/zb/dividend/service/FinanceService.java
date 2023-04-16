package com.zb.dividend.service;

import com.zb.dividend.exception.impl.NoCompanyException;
import com.zb.dividend.model.Company;
import com.zb.dividend.model.Dividend;
import com.zb.dividend.model.ScrapedResult;
import com.zb.dividend.model.constants.CacheKey;
import com.zb.dividend.persist.CompanyRepository;
import com.zb.dividend.persist.DividendRepository;
import com.zb.dividend.persist.entity.CompanyEntity;
import com.zb.dividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {

        //1. 회사 명을 기준으로 회사정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        //2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());


        //3. 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()),dividends);
    }

}
