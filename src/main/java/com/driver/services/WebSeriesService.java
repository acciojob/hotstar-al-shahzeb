package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //In case the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Don't forget to save the production and web series Repo

        //WebSeries webSerie = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null)
            throw new Exception("Series is already present");

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();


        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());


        double rate = productionHouse.getRatings();
        rate+=webSeries.getRating();
        if(productionHouse.getWebSeriesList().size()==1)
            productionHouse.setRatings(rate);
        else
            productionHouse.setRatings(rate/2.0);

        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);

        productionHouseRepository.save(productionHouse);
        WebSeries webSeries1 = productionHouse.getWebSeriesList().get(productionHouse.getWebSeriesList().size()-1);
        return webSeries1.getId();
    }

}
