package com.example.data

data class Product(
    val id: String,
    val name: String,
    val priceTHB: Long,
    val condition: String, // e.g., "ပါကင်သစ်" (Brand New), "Second 99%"
    val category: String, // "iPhone New", "iPhone Second", "Android New", "Android Second"
    val subcategory: String, // "Phone" or "Accessory"
    val accessoryType: String = "", // "Powerbank", "Charger", "Earphone"
    val specs: String, // e.g. "Battery: 100%, 128GB Storage, FaceID fully working"
    val hasDiscount: Boolean = false,
    val discountPriceTHB: Long = 0,
    val promoBadge: String = "" // e.g. "Gift: Screen Glass"
)

object StoreInfo {
    const val shopName = "AI Mobile Tachileik"
    const val address = "No. 77, Bogyoke Road, Near Tachileik Border Gate, Tachileik, Myanmar"
    const val phones = "+95 9 7776 7776"
    const val viberUrl = "viber://chat?number=%2B95977767776"
    const val messengerUrl = "https://m.me/aimobile7776"
}

object StockData {
    val products = listOf(
        // iPhone New
        Product(
            id = "ip_15pro_new",
            name = "iPhone 15 Pro",
            priceTHB = 38500,
            condition = "ပါကင်သစ်",
            category = "iPhone New",
            subcategory = "Phone",
            specs = "Screen: 6.1\" Super Retina XDR, Chipset: A17 Pro, Storage: 128GB/256GB, Color: Natural Titanium. 1 Year Store Warranty."
        ),
        Product(
            id = "ip_15promax_new",
            name = "iPhone 15 Pro Max",
            priceTHB = 43000,
            condition = "ပါကင်သစ်",
            category = "iPhone New",
            subcategory = "Phone",
            specs = "Screen: 6.7\" Super Retina XDR, Chipset: A17 Pro, Storage: 256GB, Color: Black Titanium. Brand New Sealed."
        ),
        Product(
            id = "ip_15_new",
            name = "iPhone 15",
            priceTHB = 29500,
            condition = "ပါကင်သစ်",
            category = "iPhone New",
            subcategory = "Phone",
            specs = "Screen: 6.1\" Dynamic Island, Chipset: A16 Bionic, Storage: 128GB, Color: Pastel Pink. 1 Year Warranty."
        ),

        // iPhone Second
        Product(
            id = "ip_14pro_sec",
            name = "iPhone 14 Pro",
            priceTHB = 29000,
            condition = "Second 99%",
            category = "iPhone Second",
            subcategory = "Phone",
            specs = "Screen: 6.1\" Dynamic Island, Storage: 256GB, Battery Health: 92%, Color: Deep Purple. No error, completely clean."
        ),
        Product(
            id = "ip_13promax_sec",
            name = "iPhone 13 Pro Max",
            priceTHB = 24500,
            condition = "Second 98%",
            category = "iPhone Second",
            subcategory = "Phone",
            specs = "Screen: 6.7\" 120Hz ProMotion, Storage: 128GB, Battery Health: 87%, Color: Sierra Blue. 100% original parts."
        ),
        Product(
            id = "ip_12_sec",
            name = "iPhone 12",
            priceTHB = 13500,
            condition = "Second 95%",
            category = "iPhone Second",
            subcategory = "Phone",
            specs = "Screen: 6.1\" Super Retina, Storage: 128GB, Battery Health: 81%, Color: White. Minor body scratches, fully functional.",
            hasDiscount = true,
            discountPriceTHB = 12900,
            promoBadge = "Hot Sale"
        ),

        // Android New
        Product(
            id = "s24_ultra_new",
            name = "Samsung Galaxy S24 Ultra",
            priceTHB = 49000,
            condition = "ပါကင်သစ်",
            category = "Android New",
            subcategory = "Phone",
            specs = "Screen: 6.8\" Dynamic AMOLED 2X, Chipset: Snapdragon 8 Gen 3, Storage: 512GB, RAM: 12GB, Built-in S-Pen. 1 Year Warranty."
        ),
        Product(
            id = "mi_14_new",
            name = "Xiaomi 14",
            priceTHB = 26500,
            condition = "ပါကင်သစ်",
            category = "Android New",
            subcategory = "Phone",
            specs = "Screen: 6.36\" OLED 120Hz, Leica Professional Optics, Chipset: Snapdragon 8 Gen 3, Storage: 512GB, RAM: 12GB. Super Charger."
        ),

        // Android Second
        Product(
            id = "s23_ultra_sec",
            name = "Samsung S23 Ultra",
            priceTHB = 31000,
            condition = "Second 99%",
            category = "Android Second",
            subcategory = "Phone",
            specs = "Screen: 6.8\" Dynamic AMOLED, Storage: 256GB, Color: Green. Perfect condition, 99% like new. Original box included."
        ),
        Product(
            id = "pixel_7pro_sec",
            name = "Google Pixel 7 Pro",
            priceTHB = 16500,
            condition = "Second 97%",
            category = "Android Second",
            subcategory = "Phone",
            specs = "Screen: 6.7\" LTPO OLED, Camera: Dual Pixel 50MP, Storage: 128GB. Color: Hazel. Premium photography king.",
            hasDiscount = true,
            discountPriceTHB = 15800,
            promoBadge = "Save 70k"
        ),

        // Gadgets & Accessories
        Product(
            id = "pb_anker_20k",
            name = "Anker PowerCore 20,000mAh",
            priceTHB = 1250,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Powerbank",
            specs = "Capacity: 20000mAh, Output: 22.5W High-Speed Charging, Dual USB-A & USB-C Ports. Perfect companion for iOS and Android.",
            hasDiscount = true,
            discountPriceTHB = 1150,
            promoBadge = "Free Cable"
        ),
        Product(
            id = "pb_remax_10k",
            name = "Remax 10,000mAh Powerbank",
            priceTHB = 650,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Powerbank",
            specs = "Capacity: 10000mAh, Compact pocket design, Dual input, Built-in LED display battery indicator. Stylish and portable."
        ),
        Product(
            id = "ch_apple_20w",
            name = "Apple 20W USB-C Adapter",
            priceTHB = 850,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Charger",
            specs = "Official Apple fast charger adapter, Output: 20W Power Delivery. Safely charge iPhones from 0 to 50% in 30 minutes."
        ),
        Product(
            id = "ch_samsung_45w",
            name = "Samsung 45W Super Fast Charger",
            priceTHB = 950,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Charger",
            specs = "Samsung Super Fast Charging 2.0, includes 5A type-C to type-C cable. Highly reliable power output for flagships."
        ),
        Product(
            id = "ear_airpods_pro",
            name = "Apple AirPods Pro (Gen 2)",
            priceTHB = 7800,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Earphone",
            specs = "Active Noise Cancellation, Adaptive Audio, Conversation Awareness, up to 6 hours listening time. US Retail Box."
        ),
        Product(
            id = "ear_redmi_buds",
            name = "Redmi Buds 5 Pro",
            priceTHB = 1950,
            condition = "ပါကင်သစ်",
            category = "Accessories",
            subcategory = "Accessory",
            accessoryType = "Earphone",
            specs = "52dB Active Noise Cancellation, Coaxial dual drivers, Hi-Res audio wireless, ultra low latency, dual connection."
        )
    )
}
