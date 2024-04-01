package card.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;

import card.data.innerPrintRepository;
import card.data.domain.innerPrint;
import jakarta.jms.IllegalStateException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "/innerPrints", produces = "application/json")
@CrossOrigin(origins="http://localhost:8080")
public class innerPrintController {
    
    private innerPrintRepository innerPrintRepo;

    public innerPrintController(innerPrintRepository innerPrintRepo) {
        this.innerPrintRepo = innerPrintRepo;
    }

    @GetMapping
    public Flux<innerPrint> allInnerPrints() {
        return innerPrintRepo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<innerPrint> getInnerPrintById(@PathVariable Long id) {
        return innerPrintRepo.findById(id);
    }
    
    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> putInnerPrintById(@PathVariable Long id, @RequestBody innerPrint entity) {
        if (!entity.getId().equals(id)) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Given innerPrint's ID doesn't match the ID in the path!"));
        }
        return innerPrintRepo.save(entity).map(res -> ResponseEntity.ok("InnerPrint updated successfully!"));
    }

    @PostMapping
    public Mono<ResponseEntity<innerPrint>> postInnerPrint(@RequestBody Mono<innerPrint> entity, ServerRequest request) {
        return entity.flatMap(innerPrintRepo::save)
        .map(
            p -> {
                HttpHeaders header = new HttpHeaders();
                header.setLocation(URI.create(request.uri() + "/" + p.getId()));
                return new ResponseEntity<innerPrint>(p, header, HttpStatus.CREATED);
            }
        );
    }
    
    @DeleteMapping("/{id}")
    public void deleteInnerPrint(@PathVariable Long id) {
        innerPrintRepo.deleteById(id).subscribe();
    }
}
