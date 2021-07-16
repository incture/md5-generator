package oneapp.incture.workbox.demo.test.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;
//import sap.spring.xsuaa.cf.model.Movie;
//import sap.spring.xsuaa.cf.repository.MovieRepository;

@RestController
@RequestMapping("employee")
public class MovieControllerEmployee {
//	
//	@Autowired
//	MovieRepository movieRepository;
	
	
	// Get Token info
//	@GetMapping("tokenInfo")
//	public Token getTokenInfo(@AuthenticationPrincipal Token token) {
//		return token;
//	}
//	
	
	// Get movie by ID
	@GetMapping("movie/{id}")
	public String getMovieById() {
		System.out.println("MovieControllerEmployee.getMovieById()");
		return "Get movie by id ";
	}
	
	@PostMapping("movie/post")
	public String postMovieById() {
		System.out.println("MovieControllerEmployee.postMovieById()");
		return "POst movie by id ";
	}
	
	// Get list of movies by date
	@GetMapping({"movie/list/{date}","movie/list"})
	public String getMovieList(@PathVariable(required=false) String date) throws ParseException{
		System.out.println("MovieControllerEmployee.getMovieList()");
		return "Movie list";
	}
	
//	// Get count of movies.
//	@GetMapping({"movie/count/{date}", "movie/count"})
//	public int getMovieCount(@PathVariable(required=false) String date) throws ParseException {
//		List<Movie> movies;
//		if(date !=null) {
//			movies = movieRepository.findAllByDate(date);
//			return movies.size();
//		}
//		else {
//			movies = movieRepository.findAll();
//			return movies.size();
//		}
//	}
//	
//	
//	
//	// Adds reservations
//	@PutMapping("reserve/{id}/{seats}")
//	public Optional<Movie> addReservations(@PathVariable ObjectId id, @PathVariable int seats) {
//		Optional<Movie>  movie = movieRepository.findById(id);
//		if(movie.isPresent() && movie.get().getSeats()-seats>=0) {
//			movie.get().setSeats(movie.get().getSeats() -seats);
//			movieRepository.save(movie.get());
//			return movie;
//		}
//		else {
//			return null;
//		}		
//	}
//	
//	
//	// Delete reservations
//	// 10 is the total number of seats
//	@DeleteMapping("reserve/{id}/{seats}")
//	public boolean deleteReservations(@PathVariable ObjectId id, @PathVariable int seats) {
//		Optional<Movie>  movie = movieRepository.findById(id);
//		if(movie.isPresent() && movie.get().getSeats() + seats <= 10 ) {
//			movie.get().setSeats(movie.get().getSeats()+seats);
//			movieRepository.save(movie.get());
//			return true;
//		}
//		else {
//			return false;
//		}		
//	}
}
