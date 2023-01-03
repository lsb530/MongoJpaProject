package com.example.mongojpapractice.controller.mflix;

import com.example.mongojpalogic.mflix.document.Movies;
import com.example.mongojpalogic.mflix.service.MflixMovieService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/mflix")
@RestController
public class MflixMovieController {

    private final MflixMovieService mflixMovieService;

    @GetMapping(value = "/movies")
    public ResponseEntity<List<Movies>> getMflixMovies() {
        List<Movies> mflixMovies = mflixMovieService.getMflixMovies();
        return ResponseEntity.status(HttpStatus.OK).body(mflixMovies);
    }

    @GetMapping(value = "/movie/{id}")
    public ResponseEntity<Movies> getMflixMovie(@PathVariable ObjectId id, HttpServletRequest request) {
        Movies mflixMovie = mflixMovieService.getMflixMovie(id);
        return ResponseEntity.status(HttpStatus.OK).body(mflixMovie);
    }

}
