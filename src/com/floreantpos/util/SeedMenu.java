package com.floreantpos.util;

import com.floreantpos.model.*;
import com.floreantpos.model.dao.*;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class SeedMenu {
    public static void main(String[] args) {
        try {
            System.out.println("Initializing Database...");
            DatabaseUtil.initialize();
            seedMenu();
            System.out.println("Database Seeding Completed.");
            try {
                java.sql.DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (Exception ignored) {
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void seedMenu() {
        Tax defaultTax = null;
        List<Tax> taxes = TaxDAO.getInstance().findAll();
        if (taxes != null && !taxes.isEmpty()) {
            defaultTax = taxes.get(0);
        } else {
            defaultTax = new Tax();
            defaultTax.setName("Standard Tax");
            defaultTax.setRate(15.0);
            TaxDAO.getInstance().saveOrUpdate(defaultTax);
        }
        
        List<TaxGroup> taxGroups = TaxGroupDAO.getInstance().findAll();
        TaxGroup defaultTaxGroup = null;
        if (taxGroups != null && !taxGroups.isEmpty()) {
            defaultTaxGroup = taxGroups.get(0);
        } else {
            defaultTaxGroup = new TaxGroup();
            defaultTaxGroup.setName("Standard Tax Group");
            defaultTaxGroup.addTotaxes(defaultTax);
            TaxGroupDAO.getInstance().saveOrUpdate(defaultTaxGroup);
        }
        
        seed(defaultTaxGroup);
    }
    
    private static void seed(TaxGroup taxGroup) {
        addCategory("FOOD TRAYS", Arrays.asList(
            new ItemData("Food Tray, Large. Serves 25 people.", 4000),
            new ItemData("Food Tray, Medium. Serves 15 people.", 3000),
            new ItemData("Food Tray, Mini. Serves 8 to 10 people.", 2000)
        ), taxGroup);
        
        addCategory("AFRICAN DISHES", Arrays.asList(
            new ItemData("Krain Krain", 125),
            new ItemData("Cassava Leaf", 125),
            new ItemData("Potato Leaf", 125),
            new ItemData("Jollof Rice with Meat Stew", 225),
            new ItemData("Jollof Rice with Chicken or Fish Stew", 215),
            new ItemData("Egg Fried Rice", 155),
            new ItemData("Meat Fried Rice", 215),
            new ItemData("Chicken Fried Rice", 195),
            new ItemData("Seafood Fried Rice", 245),
            new ItemData("Egusi Soup with Foo Foo, Eba, or Pounded Yam", 195),
            new ItemData("Obiata/Sawa Soup with Foo Foo, Eba, or Pounded Yam", 195),
            new ItemData("Acheke, Small", 220),
            new ItemData("Acheke, Medium", 250),
            new ItemData("Acheke, Large", 350),
            new ItemData("Acheke with Whole Chicken", 350),
            new ItemData("Acheke with Half Chicken", 250)
        ), taxGroup);
        
        addCategory("BREAKFAST", Arrays.asList(
            new ItemData("Shrimp Omelet", 195),
            new ItemData("Sausage Omelet", 145),
            new ItemData("English Breakfast", 165),
            new ItemData("Cheese Pie", 135),
            new ItemData("Pancake Platter", 165)
        ), taxGroup);
        
        addCategory("NOODLES", Arrays.asList(
            new ItemData("Chicken Noodles", 195),
            new ItemData("Seafood Noodles", 225),
            new ItemData("Meat Noodles", 215),
            new ItemData("Vegetable Noodles", 165),
            new ItemData("Shrimp Noodles", 215),
            new ItemData("Supreme Noodles", 225)
        ), taxGroup);
        
        addCategory("PASTA", Arrays.asList(
            new ItemData("Spaghetti Bolognese", 245),
            new ItemData("Marinara Pasta", 205),
            new ItemData("Meat Pasta", 245)
        ), taxGroup);
        
        addCategory("HAMBURGERS", Arrays.asList(
            new ItemData("Chicken Burger", 185),
            new ItemData("Meat Burger", 205),
            new ItemData("Fish Burger", 165),
            new ItemData("Egg Burger", 175),
            new ItemData("Reness Special Burger", 165)
        ), taxGroup);
        
        addCategory("WINGS", Arrays.asList(
            new ItemData("Spicy Wings", 115),
            new ItemData("Barbecue Wings", 115),
            new ItemData("Crispy Wings", 115),
            new ItemData("Garlic Wings", 115),
            new ItemData("Grilled Wings", 115)
        ), taxGroup);
        
        addCategory("SANDWICHES", Arrays.asList(
            new ItemData("Shrimp Sandwich", 195),
            new ItemData("Roast Meat Sandwich", 215),
            new ItemData("Chicken Sandwich", 175),
            new ItemData("Club Sandwich", 185)
        ), taxGroup);
        
        addCategory("SHAWARMA", Arrays.asList(
            new ItemData("Chicken Shawarma", 120),
            new ItemData("Beef Shawarma", 130),
            new ItemData("Mixed Shawarma", 175),
            new ItemData("Vegetable Shawarma", 105),
            new ItemData("Fish Shawarma", 110)
        ), taxGroup);
        
        addCategory("SALADS", Arrays.asList(
            new ItemData("African Chicken Salad", 175),
            new ItemData("Seafood Salad", 155),
            new ItemData("Lebanese Salad", 155),
            new ItemData("Avocado Salad", 165),
            new ItemData("Apple Salad", 175)
        ), taxGroup);
        
        addCategory("MEAT STEAK", Arrays.asList(
            new ItemData("Mushroom Steak", 335),
            new ItemData("Pepper Steak", 325),
            new ItemData("Blue Cheese Steak", 315),
            new ItemData("Beef Cordon Bleu", 325)
        ), taxGroup);
        
        addCategory("PIZZA", Arrays.asList(
            new ItemData("Reness Delight Pizza", 315),
            new ItemData("Shrimp Pizza", 295),
            new ItemData("Seafood Pizza", 305),
            new ItemData("Meat Pizza", 305),
            new ItemData("Barbecue Pizza", 295),
            new ItemData("Vegetable Pizza", 265),
            new ItemData("Pepperoni Pizza", 295),
            new ItemData("Cheese Pizza", 255),
            new ItemData("Chicken Pizza", 295)
        ), taxGroup);
        
        addCategory("APPETIZERS", Arrays.asList(
            new ItemData("Fish Fingers", 145),
            new ItemData("Kabbe, 4 Pieces", 130),
            new ItemData("Vegetable Spring Rolls", 105),
            new ItemData("Fried Meat Pie", 145),
            new ItemData("French Fries", 85),
            new ItemData("Breaded Shrimp, 4 Pieces", 145),
            new ItemData("Breaded Calamari", 125),
            new ItemData("Garlic Bread with Cheese", 125),
            new ItemData("Seafood Soup", 195),
            new ItemData("African Pepper Soup", 185),
            new ItemData("Goat Pepper Soup", 220)
        ), taxGroup);
        
        addCategory("MAIN DISHES, CHICKEN", Arrays.asList(
            new ItemData("Chicken Cordon Bleu. Served with Chips, Mashed Potato, Fried Rice, or Vegetables.", 345),
            new ItemData("Grilled Half Chicken. Served with Fried Rice, Chips, or Steamed Vegetables.", 275),
            new ItemData("Whole Chicken. Served with Fried Rice, Chips, or Steamed Vegetables.", 465),
            new ItemData("Fried Chicken Leg. Served with Fried Rice, Chips, or Steamed Vegetables.", 215),
            new ItemData("Boneless Chicken. Served with Fried Rice, Chips, or Mashed Potato.", 265),
            new ItemData("Chicken Breast. Served with Fried Rice or Chips.", 295),
            new ItemData("Family Mixed Grilled Platter. Served with Fish, Meat, Chicken, Shrimp, Fried Rice, Steamed Vegetables, Salad, and Kafta.", 670)
        ), taxGroup);
        
        addCategory("SEAFOOD", Arrays.asList(
            new ItemData("Breaded or Grilled Shrimp. Served with Fried Rice, Chips, Steamed Vegetables, Mashed Potato, or Salad.", 265),
            new ItemData("Seafood Platter. Served with Fried Rice, Salad, Steamed Vegetables, or Mashed Potato.", 670),
            new ItemData("Grilled Barracuda Fish. Served with Fried Rice, Chips, Steamed Vegetables, or Coconut Rice.", 295),
            new ItemData("Grilled Snapper Fish. Served with Fried Rice, Chips, Steamed Vegetables, or Mashed Potato.", 245),
            new ItemData("Grilled Lobster, Large. Served with Fried Rice, French Fries, Steamed Vegetables, Mashed Potato, or Coconut Rice.", 585),
            new ItemData("Grilled Lobster, Small. Served with Fried Rice, French Fries, Steamed Vegetables, Mashed Potato, or Coconut Rice.", 505),
            new ItemData("Reness Platter. Served with Shrimp, Fish, Fried Rice, and Calamari.", 505),
            new ItemData("Seafood Boil, Small. Includes Lobster, Sausage, Egg, Potato, Shrimp, Crab, and Sweet and Sour Sauce.", 800),
            new ItemData("Seafood Boil, Medium. Includes Lobster, Sausage, Egg, Potato, Shrimp, Crab, and Sweet and Sour Sauce.", 800),
            new ItemData("Seafood Boil, Large. Includes Lobster, Sausage, Egg, Potato, Shrimp, Crab, and Sweet and Sour Sauce.", 1000)
        ), taxGroup);
    }
    
    private static void addCategory(String categoryName, List<ItemData> items, TaxGroup taxGroup) {
        List<MenuCategory> existingCategories = MenuCategoryDAO.getInstance().findAll();
        MenuCategory category = null;
        if (existingCategories != null) {
            for (MenuCategory c : existingCategories) {
                if (categoryName.equalsIgnoreCase(c.getName())) {
                    category = c;
                    break;
                }
            }
        }
        if (category == null) {
            category = new MenuCategory();
            category.setName(categoryName);
            category.setVisible(true);
            MenuCategoryDAO.getInstance().save(category);
        }
        
        List<MenuGroup> existingGroups = MenuGroupDAO.getInstance().findAll();
        MenuGroup group = null;
        if (existingGroups != null) {
            for (MenuGroup g : existingGroups) {
                if (categoryName.equalsIgnoreCase(g.getName())) {
                    group = g;
                    break;
                }
            }
        }
        if (group == null) {
            group = new MenuGroup();
            group.setName(categoryName);
            group.setParent(category);
            group.setVisible(true);
            MenuGroupDAO.getInstance().save(group);
        }
        
        List<MenuItem> existingItems = MenuItemDAO.getInstance().findAll();
        Set<String> itemNames = new HashSet<String>();
        if (existingItems != null) {
            for (MenuItem mi : existingItems) {
                if (mi.getName() != null) {
                    itemNames.add(mi.getName().toLowerCase());
                }
            }
        }
        
        for (ItemData itemData : items) {
            String itemName = itemData.name;
            if (itemName.length() > 115) {
                itemName = itemName.substring(0, 115);
            }
            if (itemNames.contains(itemName.toLowerCase())) {
                continue;
            }
            MenuItem item = new MenuItem();
            item.setName(itemName);
            item.setPrice(itemData.price);
            item.setBuyPrice(0.0);
            item.setDiscountRate(0.0);
            item.setParent(group);
            item.setTaxGroup(taxGroup);
            item.setVisible(true);
            MenuItemDAO.getInstance().save(item);
            itemNames.add(itemName.toLowerCase());
        }
    }
    
    private static class ItemData {
        String name;
        double price;
        
        public ItemData(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}
