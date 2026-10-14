import os
import xml.etree.ElementTree as ET

# --- Configuration ---
TAX_NAME = "US"
TAX_RATE = "6.0"
OUTPUT_FILES = [
    'resources/floreantpos-menu-items.xml',
    'target/classes/floreantpos-menu-items.xml'
]

# --- Hardcoded Menu Data to keep the compiled .exe standalone ---
MENU_DATA = {
    "FOOD TRAYS": [
        ["Food Tray, Large. Serves 25 people.", 4000],
        ["Food Tray, Medium. Serves 15 people.", 3000],
        ["Food Tray, Mini. Serves 8 to 10 people.", 2000]
    ],
    "AFRICAN DISHES": [
        ["Krain Krain.", 125],
        ["Cassava Leaf.", 125],
        ["Potato Leaf.", 125],
        ["Jollof Rice with Meat Stew.", 225],
        ["Jollof Rice with Chicken or Fish Stew.", 215],
        ["Egg Fried Rice.", 155],
        ["Meat Fried Rice.", 215],
        ["Chicken Fried Rice.", 195],
        ["Seafood Fried Rice.", 245],
        ["Egusi Soup with Foo Foo, Eba, or Pounded Yam.", 195],
        ["Obiata/Sawa Soup with Foo Foo, Eba, or Pounded Yam.", 195],
        ["Acheke, Small.", 220],
        ["Acheke, Medium.", 250],
        ["Acheke, Large.", 350],
        ["Acheke with Whole Chicken.", 350],
        ["Acheke with Half Chicken.", 250]
    ],
    "BREAKFAST": [
        ["Shrimp Omelet.", 195],
        ["Sausage Omelet.", 145],
        ["English Breakfast.", 165],
        ["Cheese Pie.", 135],
        ["Pancake Platter.", 165]
    ],
    "NOODLES": [
        ["Chicken Noodles.", 195],
        ["Seafood Noodles.", 225],
        ["Meat Noodles.", 215],
        ["Vegetable Noodles.", 165],
        ["Shrimp Noodles.", 215],
        ["Supreme Noodles.", 225]
    ],
    "PASTA": [
        ["Spaghetti Bolognese.", 245],
        ["Marinara Pasta.", 205],
        ["Meat Pasta.", 245]
    ],
    "HAMBURGERS": [
        ["Chicken Burger.", 185],
        ["Meat Burger.", 205],
        ["Fish Burger.", 165],
        ["Egg Burger.", 175],
        ["Reness Special Burger.", 165]
    ],
    "WINGS": [
        ["Spicy Wings.", 115],
        ["Barbecue Wings.", 115],
        ["Crispy Wings.", 115],
        ["Garlic Wings.", 115],
        ["Grilled Wings.", 115]
    ],
    "SANDWICHES": [
        ["Shrimp Sandwich.", 195],
        ["Roast Meat Sandwich.", 215],
        ["Chicken Sandwich.", 175],
        ["Club Sandwich.", 185]
    ],
    "SHAWARMA": [
        ["Chicken Shawarma.", 120],
        ["Beef Shawarma.", 130],
        ["Mixed Shawarma.", 175],
        ["Vegetable Shawarma.", 105],
        ["Fish Shawarma.", 110]
    ],
    "SALADS": [
        ["African Chicken Salad.", 175],
        ["Seafood Salad.", 155],
        ["Lebanese Salad.", 155],
        ["Avocado Salad.", 165],
        ["Apple Salad.", 175]
    ],
    "MEAT STEAK": [
        ["Mushroom Steak.", 335],
        ["Pepper Steak.", 325],
        ["Blue Cheese Steak.", 315],
        ["Beef Cordon Bleu.", 325]
    ],
    "PIZZA": [
        ["Reness Delight Pizza.", 315],
        ["Shrimp Pizza.", 295],
        ["Seafood Pizza.", 305],
        ["Meat Pizza.", 305],
        ["Barbecue Pizza.", 295],
        ["Vegetable Pizza.", 265],
        ["Pepperoni Pizza.", 295],
        ["Cheese Pizza.", 255],
        ["Chicken Pizza.", 295]
    ],
    "APPETIZERS": [
        ["Fish Fingers.", 145],
        ["Kabbe, 4 Pieces.", 130],
        ["Vegetable Spring Rolls.", 105],
        ["Fried Meat Pie.", 145],
        ["French Fries.", 85],
        ["Breaded Shrimp, 4 Pieces.", 145],
        ["Breaded Calamari.", 125],
        ["Garlic Bread with Cheese.", 125],
        ["Seafood Soup.", 195],
        ["African Pepper Soup.", 185],
        ["Goat Pepper Soup.", 220]
    ],
    "MAIN DISHES, CHICKEN": [
        ["Chicken Cordon Bleu.", 345],
        ["Grilled Half Chicken.", 275],
        ["Whole Chicken.", 465],
        ["Fried Chicken Leg.", 215],
        ["Boneless Chicken.", 265],
        ["Chicken Breast.", 295],
        ["Family Mixed Grilled Platter.", 670]
    ],
    "SEAFOOD": [
        ["Breaded or Grilled Shrimp.", 265],
        ["Seafood Platter.", 670],
        ["Grilled Barracuda Fish.", 295],
        ["Grilled Snapper Fish.", 245],
        ["Grilled Lobster, Large.", 585],
        ["Grilled Lobster, Small.", 505],
        ["Reness Platter.", 505],
        ["Seafood Boil, Small.", 800],
        ["Seafood Boil, Medium.", 800],
        ["Seafood Boil, Large.", 1000]
    ]
}

def build_xml(menu_data):
    """Builds the ElementTree XML structure from menu data."""
    root = ET.Element("elements")

    # Create categories
    for cat_name in menu_data.keys():
        mc = ET.SubElement(root, "menuCategories")
        ET.SubElement(mc, "beverage").text = "false"
        ET.SubElement(mc, "name").text = cat_name
        ET.SubElement(mc, "visible").text = "true"

    # Create groups
    for cat_name in menu_data.keys():
        mg = ET.SubElement(root, "menuGroups")
        ET.SubElement(mg, "name").text = cat_name
        parent = ET.SubElement(mg, "parent")
        ET.SubElement(parent, "beverage").text = "false"
        ET.SubElement(parent, "name").text = cat_name
        ET.SubElement(parent, "visible").text = "true"
        ET.SubElement(mg, "visible").text = "true"

    # Create items
    for cat_name, items in menu_data.items():
        for item_name, price in items:
            mi = ET.SubElement(root, "menuItems")
            ET.SubElement(mi, "buyPrice").text = "0.0"
            ET.SubElement(mi, "discountRate").text = "0.0"
            ET.SubElement(mi, "name").text = item_name
            parent = ET.SubElement(mi, "parent")
            ET.SubElement(parent, "name").text = cat_name
            cat_parent = ET.SubElement(parent, "parent")
            ET.SubElement(cat_parent, "beverage").text = "false"
            ET.SubElement(cat_parent, "name").text = cat_name
            ET.SubElement(cat_parent, "visible").text = "true"
            ET.SubElement(parent, "visible").text = "true"
            ET.SubElement(mi, "price").text = str(float(price))
            ET.SubElement(mi, "showImageOnly").text = "false"
            tax = ET.SubElement(mi, "tax")
            ET.SubElement(tax, "name").text = TAX_NAME
            ET.SubElement(tax, "rate").text = TAX_RATE
            ET.SubElement(mi, "fractionalUnit").text = "false"
            ET.SubElement(mi, "visible").text = "true"

    return root

def save_xml_to_file(root, file_path):
    """Saves the XML tree to a formatted file, ensuring directories exist."""
    directory = os.path.dirname(file_path)
    if directory:
        os.makedirs(directory, exist_ok=True)
    
    try:
        # Indent the XML for pretty printing (Python 3.9+)
        if hasattr(ET, "indent"):
            ET.indent(root, space="    ", level=0)
            tree = ET.ElementTree(root)
            tree.write(file_path, encoding="utf-8", xml_declaration=True)
        else:
            # Fallback for older Python versions
            from xml.dom import minidom
            xml_str = ET.tostring(root, encoding='utf-8')
            parsed = minidom.parseString(xml_str)
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(parsed.toprettyxml(indent="    "))
                
        print(f"Successfully wrote {file_path}")
    except Exception as e:
        print(f"Error saving to {file_path}: {e}")

def main():
    print("Generating XML structure...")
    root = build_xml(MENU_DATA)

    print("Saving output files...")
    for file_path in OUTPUT_FILES:
        save_xml_to_file(root, file_path)
        
    print("Menu generation complete.")

if __name__ == "__main__":
    main()
