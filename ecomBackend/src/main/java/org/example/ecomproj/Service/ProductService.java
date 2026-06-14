package org.example.ecomproj.Service;

import org.example.ecomproj.Model.Product;
import org.example.ecomproj.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllproducts(){

        return repo.findAll();

    }


    public Product getProductbyId(int id) {

        return repo.findById(id).orElse(new Product());
    }

    public Product addProduct(Product product, MultipartFile imageFile)
            throws IOException {

        System.out.println("File Name = " + imageFile.getOriginalFilename());
        System.out.println("Content Type = " + imageFile.getContentType());
        System.out.println("File Size = " + imageFile.getSize());

        product.setImgName(imageFile.getOriginalFilename());
        product.setImgType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return repo.save(product);

    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

        product.setImageData(imageFile.getBytes());
        product.setImgName(imageFile.getOriginalFilename());
        product.setImgType(imageFile.getContentType());
        return repo.save(product);
    }

    public void deleteProduct(int id) {

        repo.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {

        return repo.searchProducts(keyword);
    }
}
