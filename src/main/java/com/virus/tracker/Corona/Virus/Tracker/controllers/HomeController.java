package com.virus.tracker.Corona.Virus.Tracker.controllers;

import com.virus.tracker.Corona.Virus.Tracker.services.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    DataFetcher dataFetcher;
    @GetMapping("/")
    public String home(Model model){
        int totalReportedCases=dataFetcher.getAllStats().stream().mapToInt(stat->stat.getTotalCases()).sum();
        int totalNewCases=dataFetcher.getAllStats().stream().mapToInt(stat-> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("allStats",dataFetcher.getAllStats());
        model.addAttribute("totalReportedCases",totalReportedCases);
        model.addAttribute("totalNewCases",totalNewCases);

        return "home";
    }
}
