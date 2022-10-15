package se.iths.javatwentytwo.labTwo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static se.iths.javatwentytwo.labTwo.Category.values;

public class Store {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Product> productsList = new ArrayList<>(loadFromFile());
        List<ShoppingCart> shoppingCart = new ArrayList<>();

        startMenu(scanner, productsList, shoppingCart);

    }

    private static void startMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        try {
            while (true) {
                printStartMenu();
                switch (scanner.next()) {
                    case "1" -> productList.add(addProductInformation(scanner, productList));
                    case "2" -> searchMenu(scanner, productList, shoppingCart);
                    case "3" -> saveToFile(productList);
                    case "4" -> shoppingMenu(scanner, productList, shoppingCart);
                    case "5" -> printProducts(productList);
                    case "e", "E" -> System.exit(0);
                }
            }
        }catch (Exception e){
            System.out.println("Fel inmatning, börja om.");
            scanner.nextLine();
            startMenu(scanner,productList,shoppingCart);
        }
    }

    private static void printStartMenu(){
        System.out.println(
                """
                                Välkommen
                        -------------------------------
                        1. Lägg till produkt
                        2. Sök (ta bort, ändra produkt)
                        3. Spara
                        4. Handla
                        e. Avsluta""");
    }

    private static void searchMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        try {
            while (true) {
                printSearchMenu();
                switch (scanner.next()) {
                    case "1" -> searchOnName(scanner, productList);
                    case "2" -> searchOnBalanceRange(scanner, productList);
                    case "3" -> searchOnPriceRange(scanner, productList);
                    case "4" -> searchOnCategory(scanner, productList);
                    case "5" -> searchOnMaker(scanner, productList);
                    case "6" -> searchOnProductId(scanner, productList);
                    case "7" -> startMenu(scanner, productList, shoppingCart);
                }
                optionOnSearchResult(scanner, productList, shoppingCart);
            }
        }catch (Exception e){
            System.out.println("Fel inmatning, sök igen.");
            scanner.nextLine();
            searchMenu(scanner,productList, shoppingCart);
        }
    }

    private static void printSearchMenu(){
        System.out.println(
                """
                        ----------------
                        Sök på
                        1. Produkt namn
                        2. Saldo
                        3. Prisintervall
                        4. Kategori
                        5. Tillverkare
                        6. Produkt ID
                        7. Tillbaka""");
    }

    private static void optionOnSearchResult(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        printOptionMenu();
        switch (scanner.next()){
            case "1" -> changeMenu(scanner, productList, shoppingCart);
            case "2" -> productList.remove(getProductIndex(scanner, productList));
            case "3" -> searchMenu(scanner, productList, shoppingCart);
        }
    }

    private static void printOptionMenu(){
        System.out.println(
                """
                        -------------------------
                        Vad vill du göra?
                        1. Ändra
                        2. Ta bort
                        3. Tillbaka till sökmenyn""");
    }

    private static void changeMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        printChangeMenu();
        switch (scanner.next()) {
            case "1" -> productList.get(getProductIndex(scanner, productList)).setName(inputName(scanner));
            case "2" -> productList.get(getProductIndex(scanner, productList)).setProductBalance(inputProductBalance(scanner));
            case "3" -> productList.get(getProductIndex(scanner, productList)).setPrice(inputPrice(scanner));
            case "4" -> productList.get(getProductIndex(scanner, productList)).setCategory(inputCategory(scanner));
            case "5" -> productList.get(getProductIndex(scanner, productList)).setMaker(inputMaker(scanner));
            case "6" -> searchMenu(scanner, productList, shoppingCart);
        }
    }

    private static void printChangeMenu(){
        System.out.println(
                """
                        -------------------------
                        Vad vill du ändra?
                        1. Namn
                        2. Saldo
                        3. Pris
                        4. Kategori
                        5. Tillverkare
                        6. Tillbaka till sökmenyn""");
    }

    private static void shoppingMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        try{
            while(true) {
                printShoppingMenu();
                switch (scanner.next()) {
                    case "1" -> printAllProductsAndChoose(scanner, productList, shoppingCart);
                    case "2" -> printCategoryAndChoose(scanner, productList, shoppingCart);
                    case "3" -> printRecipe(shoppingCart);
                    case "4" -> registerAndGoToStartMenu(scanner, productList, shoppingCart);
                }
            }
        }catch (Exception e){
            System.out.println("Fel inmatning, välj vara igen");
            scanner.nextLine();
            shoppingMenu(scanner,productList, shoppingCart);
        }
    }

    private static void printShoppingMenu(){
        System.out.println(
                """ 
                        ------------------------------------------
                        Handla
                        1. Skriv ut alla produkter
                        2. Inom en kategori
                        3. Skriv ut kvitto
                        4. Registrera köpet och gå till huvudmenyn""");
    }

    private static void optionShoppingMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        printOptionShopping();
        switch (scanner.next()){
            case "1" -> addToShoppingCart(scanner, productList, shoppingCart);
            case "2" -> shoppingMenu(scanner, productList, shoppingCart);
        }
    }

    private static void printOptionShopping(){
        System.out.println("""
                        ---------------
                        1. Lägg till vara till kundvagnen
                        2. Tillbaka""");
    }

    private static void printAllProductsAndChoose(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        printProducts(productList);
        optionShoppingMenu(scanner, productList, shoppingCart);
    }

    private static void printCategoryAndChoose(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        searchOnCategory(scanner,productList);
        optionShoppingMenu(scanner, productList, shoppingCart);
    }

    private static void registerAndGoToStartMenu(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart){
        saveToRecipe(shoppingCart);
        shoppingCart.clear();
        startMenu(scanner, productList, shoppingCart);
    }

    private static void addToShoppingCart(Scanner scanner, List<Product> productList, List<ShoppingCart> shoppingCart) {
        int index = getProductIndex(scanner, productList);
        System.out.println("Hur många vill du ha: ");
        int amount = scanner.nextInt();
        if(productList.get(index).getProductBalance() - amount >= 0) {
            shoppingCart.add(new ShoppingCart(productList.get(index), amount));
            productList.get(index).setProductBalance(productList.get(index).getProductBalance() - amount);
        }else
            System.out.println("Inte tillräcklig i lagret");
    }

    private static void searchOnName(Scanner scanner, List<Product> productList){
        var name = inputName(scanner);
        productList.stream()
                .filter(product -> product.getName().equals(name))
                .forEach(System.out::println);
    }

    private static void searchOnBalanceRange(Scanner scanner, List<Product> productList){
        int balanceToSearch = inputProductBalance(scanner);
        productList.stream()
                .filter(product -> product.getProductBalance() == balanceToSearch)
                .forEach(System.out::println);
    }

    private static void searchOnPriceRange(Scanner scanner, List<Product> productList){
        System.out.println("Pris min: ");
        BigDecimal priceToSearchMin = scanner.nextBigDecimal();
        System.out.println("Pris max: ");
        BigDecimal priceToSearchMax = scanner.nextBigDecimal();
        productList.stream()
                .filter(product -> (product.getPrice().compareTo(priceToSearchMin) >= 0)
                        && (product.getPrice().compareTo(priceToSearchMax) <= 0))
                .forEach(System.out::println);
    }

    private static void searchOnCategory(Scanner scanner, List<Product> productList){
        Category categoryToSearch = inputCategory(scanner);
        productList.stream()
                .filter(product -> product.getCategory().equals(categoryToSearch))
                .forEach(System.out::println);
    }

    private static void searchOnMaker(Scanner scanner, List<Product> productList){
        String makerToSearch = String.valueOf(inputMaker(scanner));
        productList.stream()
                .filter(product -> product.getMaker().contains(makerToSearch))
                .forEach(System.out::println);
    }

    private static void searchOnProductId(Scanner scanner, List<Product> productList){
        System.out.println("Produkt Id: ");
        int productIdToSearch = scanner.nextInt();
        productList.stream()
                .filter(product -> product.getProductId() == productIdToSearch)
                .forEach(System.out::println);
    }

    private static int getProductIndex(Scanner scanner, List<Product> productList){
        System.out.println("Ange Id nummret på produkten: ");
        int idInput = scanner.nextInt();
        var matchingProduct = productList.stream()
                .filter(product -> product.getProductId() == idInput)
                .findFirst();
        return productList.indexOf(matchingProduct.get());
    }

    private static Product addProductInformation(Scanner scanner, List<Product> productList){
        return new Product(inputCategory(scanner), inputName(scanner), inputProductBalance(scanner), inputPrice(scanner),
                inputMaker(scanner), generateProductID(productList));
    }

    private static Category inputCategory(Scanner scanner){
        System.out.println("Välj kategori");
        System.out.println(Arrays.toString(values()));
        return Category.valueOf(scanner.next().toUpperCase());
    }

    private static String inputName(Scanner scanner){
        System.out.print("Namn: ");
        return scanner.next();
    }

    private static int inputProductBalance(Scanner scanner){
        System.out.print("Saldo: ");
        return scanner.nextInt();
    }

    private static BigDecimal inputPrice(Scanner scanner){
        System.out.print("Pris: ");
        return new BigDecimal(scanner.nextInt());
    }

    private static String inputMaker(Scanner scanner){
        System.out.print("Tillverkare: ");
        return scanner.next();
    }

    private static int generateProductID(List<Product> productList){
        var maxId =productList.stream()
                .mapToInt(Product::getProductId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    private static void printProducts(List<Product> productList){
        productList.forEach(System.out::println);
    }

    private static void printRecipe(List<ShoppingCart> shoppingCart){
        BigDecimal result;
        System.out.println("Kvitto");
        System.out.println("----------------------------");
        shoppingCart.forEach(s -> System.out.println("Antal: " + s.amountToBuy() + ", Nman: " +
                s.shoppingProduct().getName() + ", Pris: " + s.shoppingProduct().getPrice() + ":-"));

        BigDecimal total = shoppingCart.stream()
                .map(totalOrdinary -> totalOrdinary.shoppingProduct().getPrice().multiply(BigDecimal.valueOf(totalOrdinary.amountToBuy())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result = getSumAfterDiscount(total);

        System.out.println("----------------------------");
        System.out.println("Totalt att betala: " + total + ":-");
        System.out.println("Rabatt: " + total.subtract(result));
        System.out.println("Att betala efter rabatt: " + result + ":-");
    }

    private static BigDecimal getSumAfterDiscount(BigDecimal total) {
        BigDecimal result;
        if(total.compareTo(BigDecimal.valueOf(500)) >= 0){
            result = ShoppingCart.applyDiscount(Discount.overFiveHundred(), total);
        } else if (total.compareTo(BigDecimal.valueOf(300)) >= 0) {
            result = ShoppingCart.applyDiscount(Discount.overThreeHundred(), total);
        }else
            result = total;
        return result;
    }

    private static void saveToFile(List<Product> productList)  {
        Gson gson  = new Gson();
        String json = gson.toJson(productList);

        String homeFolder = System.getProperty("user.home");
        try {
            Files.writeString(Path.of(homeFolder, "productList.json"), json);
            System.out.println("Filen är sparad");
        } catch (IOException e) {
            System.out.println("Något gick fel vid sparandet");
        }
    }

    private static void saveToRecipe(List<ShoppingCart> shoppingCart)  {
        Gson gson  = new Gson();
        String json = gson.toJson(shoppingCart);

        String homeFolder = System.getProperty("user.home");
        try {
            Files.writeString(Path.of(homeFolder, "recipe.json"), json, StandardOpenOption.APPEND);
            System.out.println("Filen är sparad");
        } catch (IOException e) {
            System.out.println("Något gick fel vid sparandet");
        }
    }

    private static List<Product> loadFromFile(){
        Gson gson  = new Gson();

        String homeFolder = System.getProperty("user.home");
        Path filePath = Path.of(homeFolder, "productList.json");

        try {
            String json = Files.readString(filePath);
            return gson.fromJson(json, new TypeToken<ArrayList<Product>>() {
            }.getType());
        }catch (NoSuchFileException e){
            System.out.println("Inga produkter att hämta");
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Något gick fel vid inläsningen");
            System.out.println(e.getClass().getName() + " " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
}
