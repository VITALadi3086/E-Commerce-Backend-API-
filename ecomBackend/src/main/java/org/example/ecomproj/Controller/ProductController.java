package org.example.ecomproj.Controller;

import org.apache.coyote.Response;
import org.example.ecomproj.Model.Product;
import org.example.ecomproj.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ser.std.DelegatingSerializer;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String greet() {
        return "hello world";
    }

    @GetMapping("/products")
    public List<Product> getAllproducts() {

        return service.getAllproducts();
    }

    @GetMapping("/product/{id}")
    public Product getProdcut(@PathVariable int id) {

        return service.getProductbyId(id);

    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {

        System.out.println("POST HIT");

        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {

        Product product = service.getProductbyId(productId);
        byte[] imagefile = product.getImageData();

        if(product.getImgType() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImgType()))
                .body(imagefile);
    }

    @PutMapping("/product/{id}")
        public ResponseEntity<String> updateProduct ( @PathVariable int id, @RequestPart Product product,
        @RequestPart MultipartFile imageFile){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (product1 != null)
            return new ResponseEntity<>("updated", HttpStatus.OK);
        else
            return new ResponseEntity<>("some issue", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductbyId(id);
        if (product != null){
        service.deleteProduct(id);
        return new ResponseEntity<>("deleted", HttpStatus.ACCEPTED);}
        else{
            return new ResponseEntity<>("product not found",HttpStatus.NOT_FOUND );
        }
    }

    @RequestMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(String keyword){

        System.out.println("searching with"+ keyword);

        List<Product> products = service.searchProduct(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }
}
