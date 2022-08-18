package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 This class is responsible for initializing an in-memory Inventory, containing static ObservableLists for storing both Parts and Products, and static methods for various operations on these Lists.
 @author Sean
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static Part lookupPart(int partId) {
        Part foundPart = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                foundPart = part;
                break;
            }
        }
        return foundPart;
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String lowerCaseFilter = partName.toLowerCase();

        allParts.stream().filter(part -> (part.getName().toLowerCase().contains(lowerCaseFilter))).forEachOrdered(part -> {
            //add that part
            partsFound.add(part);
        }); //if the Part's name contains (any amount of) the input (partName)

        return partsFound;
    }

    public static void updatePart(int index, Part selectedPart) {
        //create a 2nd list to add Parts whose id == index
        ObservableList<Part> valuesToRemove = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getId() == index) valuesToRemove.add(part);
        }
        //valuesToRemove contains the (modified/now outdated) Part to remove
        allParts.removeAll(valuesToRemove);

        //add the newly created/modified Part to allParts
        allParts.add(selectedPart);
    }

    public static void deletePart(Part selectedPart) {
        if (selectedPart != null) allParts.remove(selectedPart);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Product lookupProduct(int productId) {
        Product foundProduct = null;

        for (Product product : allProducts) {
            if (product.getId() == productId) foundProduct = product;
        }
        return foundProduct;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String lowerCaseFilter = productName.toLowerCase();

        for (Product product : allProducts) {
            //if the Part's name contains (any amount of) the input (partName)
            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                //add that part
                productsFound.add(product);
            }
        }

        return productsFound;
    }

    public static void updateProduct(int index, Product selectedProduct) {
        //create a 2nd list to add Product whose id == index
        ObservableList<Product> valuesToRemove = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getId() == index) valuesToRemove.add(product);
        }
        //valuesToRemove contains the (modified/now outdated) Product to remove
        allProducts.removeAll(valuesToRemove);

        //add the newly created/modified Product to allProducts
        allProducts.add(selectedProduct);
    }

    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
