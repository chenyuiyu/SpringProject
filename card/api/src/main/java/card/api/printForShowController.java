package card.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;

import card.data.printForShowRepository;
import card.domain.printForShow;
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
@RequestMapping(path = "/printForShows", produces = "application/json")
@CrossOrigin(origins="http://localhost:8080")
public class printForShowController {
    
    private printForShowRepository printForShowRepo;

    public printForShowController(printForShowRepository printForShowRepo) {
        this.printForShowRepo = printForShowRepo;
    }

    @GetMapping
    public Flux<printForShow> allprintForShows() {
        return printForShowRepo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<printForShow> getprintForShowById(@PathVariable String id) {
        return printForShowRepo.findById(id);
    }
    
    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> putprintForShowById(@PathVariable String id, @RequestBody printForShow entity) {
        if (!entity.getId().equals(id)) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Given printForShow's ID doesn't match the ID in the path!"));
        }
        return printForShowRepo.save(entity).map(res -> ResponseEntity.ok("printForShow updated successfully!"));
    }

    @PostMapping
    public Mono<ResponseEntity<printForShow>> postprintForShow(@RequestBody Mono<printForShow> entity, ServerRequest request) {
        return entity.flatMap(printForShowRepo::save)
        .map(
            p -> {
                HttpHeaders header = new HttpHeaders();
                header.setLocation(URI.create(request.uri() + "/" + p.getId()));
                return new ResponseEntity<printForShow>(p, header, HttpStatus.CREATED);
            }
        );
    }
    
    @DeleteMapping("/{id}")
    public void deleteprintForShow(@PathVariable  String id) {
        printForShowRepo.deleteById(id).subscribe();
    }
}
