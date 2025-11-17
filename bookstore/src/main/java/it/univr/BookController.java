package it.univr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    /*@GetMapping("/book/{bookId}")
    public Optional<Book> readBook(@PathVariable("bookId") Long id) {
        return bookRepository.findById(id);
    }*/

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable("bookId") Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Book result = book.get();
            return ResponseEntity.ok(result);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@RequestParam("title") String title,
                                           @RequestParam("author") String author,
                                           @RequestParam("price") Float price){
        Book book = new Book(title, author, price);
        Book saved = bookRepository.save(book);
        URI location = URI.create("/book/" + saved.getId());

        return ResponseEntity.created(location).body(saved);
    }

    /*@PostMapping("/book")
    public Book createBook(@RequestParam("title") String title,
                           @RequestParam("author") String author,
                           @RequestParam("price") Float price) {
        Book book = new Book(title, author, price);
        bookRepository.save(book);
        return book;
    }*/
    
    @PutMapping("/book")
    public Book updateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/book")
    public ResponseEntity<?> deleteBook(@RequestParam("id") Long id) {
        boolean exists = bookRepository.existsById(id);
        if(exists) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*@DeleteMapping("/book")
    public void deleteBook(@RequestParam("id") Long id) {
        bookRepository.deleteById(id);
    }*/
}