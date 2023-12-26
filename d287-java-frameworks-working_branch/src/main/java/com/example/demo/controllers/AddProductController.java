package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 *
 *
 *
 */
@Controller
public class AddProductController {
    @Autowired
    private ApplicationContext context;
    private final PartService partService;
    private List<Part> theParts;
    private static Product product1;
    private Product product;

    @Autowired
    private ProductService productService;

    @GetMapping("/showFormAddProduct")
    public String showFormAddPart(Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        product = new Product();
        product1=product;
        theModel.addAttribute("product", product);

        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!product.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        theModel.addAttribute("assparts",product.getParts());
        return "productForm";
    }

    @PostMapping("/showFormAddProduct")
    public String submitForm(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult, Model theModel) {
        theModel.addAttribute("product", product);

        if(bindingResult.hasErrors()){
            ProductService productService = context.getBean(ProductServiceImpl.class);
            Product product2=productService.findById((int)product.getId());
            theModel.addAttribute("parts", partService.findAll());
            List<Part>availParts=new ArrayList<>();
            for(Part p: partService.findAll()){
                if(!product2.getParts().contains(p))availParts.add(p);
            }
            theModel.addAttribute("availparts",availParts);
            theModel.addAttribute("assparts",product2.getParts());
            return "productForm";
        }
 //       theModel.addAttribute("assparts", assparts);
 //       this.product=product;
//        product.getParts().addAll(assparts);
          else {
            ProductService repo = context.getBean(ProductServiceImpl.class);
            if (product.getId() != 0) {
                Product product2 = repo.findById((int) product.getId());

                // Check for low part inventory when updating the product
                for (Part p : product2.getParts()) {
                    int remainingInventory = p.getInv() - (product.getInv() - product2.getInv());
                    if (remainingInventory < p.getMinInv()) {
                        // Reject the form submission and display an error message
                        bindingResult.rejectValue("inv", "inventory.invalid",
                                "Inventory Invalid. '" + p.getName() + "' to fall below the minimum inventory.");
                        theModel.addAttribute("parts", partService.findAll());
                        List<Part> availParts = new ArrayList<>();
                        for (Part availablePart : partService.findAll()) {
                            if (!product2.getParts().contains(availablePart)) {
                                availParts.add(availablePart);
                            }
                        }
                        theModel.addAttribute("availparts", availParts);
                        theModel.addAttribute("assparts", product2.getParts());
                        return "productForm";
                    }
                }
            }

            // Save or update the product
            repo.save(product);
            return "confirmationaddproduct";
        }
        }

    @PostMapping("/buyproduct")
    public String buyProduct(@RequestParam("productID") long productId, Model model) {
        Optional<Product> optionalProduct = Optional.ofNullable(productService.findById((int) productId));
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.buyNow()) {
                productService.save(product);
                model.addAttribute("purchaseResult", "Purchase successful!");
                return "confirmationbuyproduct"; // New view for successful purchase
            } else {
                model.addAttribute("purchaseResult", "Purchase failed. Product out of stock.");
                return "purchaseerror"; // New view for purchase failure
            }
        } else {
            model.addAttribute("purchaseResult", "Product not found.");  // Handle the case when the product is not found
            return "purchaseerror"; // New view for product not found
        }
    }

    @GetMapping("/showProductFormForUpdate")
    public String showProductFormForUpdate(@RequestParam("productID") int theId, Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        ProductService repo = context.getBean(ProductServiceImpl.class);
        Product theProduct = repo.findById(theId);
        product1=theProduct;
    //    this.product=product;
        //set the employ as a model attibute to prepopulate the form
        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("assparts",theProduct.getParts());
        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!theProduct.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        //send over to our form
        return "productForm";
    }

    @GetMapping("/deleteproduct")
    public String deleteProduct(@RequestParam("productID") int theId, Model theModel) {
        ProductService productService = context.getBean(ProductServiceImpl.class);
        Product product2=productService.findById(theId);
        for(Part part:product2.getParts()){
            part.getProducts().remove(product2);
            partService.save(part);
        }
        product2.getParts().removeAll(product2.getParts());
        productService.save(product2);
        productService.deleteById(theId);

        return "confirmationdeleteproduct";
    }

    public AddProductController(PartService partService) {
        this.partService = partService;
    }
// make the add and remove buttons work

    @GetMapping("/associatepart")
    public String associatePart(@Valid @RequestParam("partID") int theID, Model theModel){
    //    theModel.addAttribute("product", product);
    //    Product product1=new Product();
        if (product1.getName()==null) {
            return "saveproductscreen";
        }
        else{
        product1.getParts().add(partService.findById(theID));
        partService.findById(theID).getProducts().add(product1);
        ProductService productService = context.getBean(ProductServiceImpl.class);
        productService.save(product1);
        partService.save(partService.findById(theID));
        theModel.addAttribute("product", product1);
        theModel.addAttribute("assparts",product1.getParts());
        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!product1.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        return "productForm";}
 //        return "confirmationassocpart";
    }
    @GetMapping("/removepart")
    public String removePart(@RequestParam("partID") int theID, Model theModel){
        theModel.addAttribute("product", product);
      //  Product product1=new Product();
        product1.getParts().remove(partService.findById(theID));
        partService.findById(theID).getProducts().remove(product1);
        ProductService productService = context.getBean(ProductServiceImpl.class);
        productService.save(product1);
        partService.save(partService.findById(theID));
        theModel.addAttribute("product", product1);
        theModel.addAttribute("assparts",product1.getParts());
        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!product1.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        return "productForm";
    }
}