package edu.ftn.iss.eventplanner.controllers;

import edu.ftn.iss.eventplanner.entities.ServiceAndProductProvider;
import edu.ftn.iss.eventplanner.services.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('PROVIDER')")
public class PriceListController {

    private final SolutionService solutionService;


    @GetMapping(path = {"/solutions/price-list", "/solutions/price-list.pdf", "/price-list", "/price-list.pdf"})
    Object exportPriceList2PDF(
            @AuthenticationPrincipal ServiceAndProductProvider principal
           )
    {
        var priceList = solutionService.getProviderPriceListPdf(principal);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline().filename("price-list.pdf").build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(priceList);
    }


}

