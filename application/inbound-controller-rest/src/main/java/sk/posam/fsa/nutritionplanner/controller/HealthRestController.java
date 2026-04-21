package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.rest.api.DefaultApi;

@RestController
public class HealthRestController implements DefaultApi {

    @Override
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
