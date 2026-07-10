// Interactive JavaScript for AI Mobile Web App

// Store Info Constants
const StoreInfo = {
    shopName: "AI Mobile Tachileik",
    address: "No. 77, Bogyoke Road, Near Tachileik Border Gate, Tachileik, Myanmar",
    phones: ["+95 9 777 666 555", "+95 9 123 456 789"],
    viberUrl: "viber://chat?number=%2B959777666555",
    messengerUrl: "https://m.me/aimobile7776"
};

// Full database matching StockData.kt
const products = [
    // iPhone New
    {
        id: "ip_15pro_new",
        name: "iPhone 15 Pro",
        priceMMK: 3850000,
        condition: "ပါကင်သစ်",
        category: "iPhone New",
        subcategory: "Phone",
        specs: "Screen: 6.1\" Super Retina XDR, Chipset: A17 Pro, Storage: 128GB/256GB, Color: Natural Titanium. 1 Year Store Warranty."
    },
    {
        id: "ip_15promax_new",
        name: "iPhone 15 Pro Max",
        priceMMK: 4300000,
        condition: "ပါကင်သစ်",
        category: "iPhone New",
        subcategory: "Phone",
        specs: "Screen: 6.7\" Super Retina XDR, Chipset: A17 Pro, Storage: 256GB, Color: Black Titanium. Brand New Sealed."
    },
    {
        id: "ip_15_new",
        name: "iPhone 15",
        priceMMK: 2950000,
        condition: "ပါကင်သစ်",
        category: "iPhone New",
        subcategory: "Phone",
        specs: "Screen: 6.1\" Dynamic Island, Chipset: A16 Bionic, Storage: 128GB, Color: Pastel Pink. 1 Year Warranty."
    },

    // iPhone Second
    {
        id: "ip_14pro_sec",
        name: "iPhone 14 Pro",
        priceMMK: 2900000,
        condition: "Second 99%",
        category: "iPhone Second",
        subcategory: "Phone",
        specs: "Screen: 6.1\" Dynamic Island, Storage: 256GB, Battery Health: 92%, Color: Deep Purple. No error, completely clean."
    },
    {
        id: "ip_13promax_sec",
        name: "iPhone 13 Pro Max",
        priceMMK: 2450000,
        condition: "Second 98%",
        category: "iPhone Second",
        subcategory: "Phone",
        specs: "Screen: 6.7\" 120Hz ProMotion, Storage: 128GB, Battery Health: 87%, Color: Sierra Blue. 100% original parts."
    },
    {
        id: "ip_12_sec",
        name: "iPhone 12",
        priceMMK: 1350000,
        condition: "Second 95%",
        category: "iPhone Second",
        subcategory: "Phone",
        specs: "Screen: 6.1\" Super Retina, Storage: 128GB, Battery Health: 81%, Color: White. Minor body scratches, fully functional.",
        hasDiscount: true,
        discountPriceMMK: 1290000,
        promoBadge: "Hot Sale"
    },

    // Android New
    {
        id: "s24_ultra_new",
        name: "Samsung Galaxy S24 Ultra",
        priceMMK: 4900000,
        condition: "ပါကင်သစ်",
        category: "Android New",
        subcategory: "Phone",
        specs: "Screen: 6.8\" Dynamic AMOLED 2X, Chipset: Snapdragon 8 Gen 3, Storage: 512GB, RAM: 12GB, Built-in S-Pen. 1 Year Warranty."
    },
    {
        id: "mi_14_new",
        name: "Xiaomi 14",
        priceMMK: 2650000,
        condition: "ပါကင်သစ်",
        category: "Android New",
        subcategory: "Phone",
        specs: "Screen: 6.36\" OLED 120Hz, Leica Professional Optics, Chipset: Snapdragon 8 Gen 3, Storage: 512GB, RAM: 12GB. Super Charger."
    },

    // Android Second
    {
        id: "s23_ultra_sec",
        name: "Samsung S23 Ultra",
        priceMMK: 3100000,
        condition: "Second 99%",
        category: "Android Second",
        subcategory: "Phone",
        specs: "Screen: 6.8\" Dynamic AMOLED, Storage: 256GB, Color: Green. Perfect condition, 99% like new. Original box included."
    },
    {
        id: "pixel_7pro_sec",
        name: "Google Pixel 7 Pro",
        priceMMK: 1650000,
        condition: "Second 97%",
        category: "Android Second",
        subcategory: "Phone",
        specs: "Screen: 6.7\" LTPO OLED, Camera: Dual Pixel 50MP, Storage: 128GB. Color: Hazel. Premium photography king.",
        hasDiscount: true,
        discountPriceMMK: 1580000,
        promoBadge: "Save 70k"
    },

    // Gadgets & Accessories
    {
        id: "pb_anker_20k",
        name: "Anker PowerCore 20,000mAh",
        priceMMK: 125000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Powerbank",
        specs: "Capacity: 20000mAh, Output: 22.5W High-Speed Charging, Dual USB-A & USB-C Ports. Perfect companion for iOS and Android.",
        hasDiscount: true,
        discountPriceMMK: 115000,
        promoBadge: "Free Cable"
    },
    {
        id: "pb_remax_10k",
        name: "Remax 10,000mAh Powerbank",
        priceMMK: 65000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Powerbank",
        specs: "Capacity: 10000mAh, Compact pocket design, Dual input, Built-in LED display battery indicator. Stylish and portable."
    },
    {
        id: "ch_apple_20w",
        name: "Apple 20W USB-C Adapter",
        priceMMK: 85000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Charger",
        specs: "Official Apple fast charger adapter, Output: 20W Power Delivery. Safely charge iPhones from 0 to 50% in 30 minutes."
    },
    {
        id: "ch_samsung_45w",
        name: "Samsung 45W Super Fast Charger",
        priceMMK: 95000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Charger",
        specs: "Samsung Super Fast Charging 2.0, includes 5A type-C to type-C cable. Highly reliable power output for flagships."
    },
    {
        id: "ear_airpods_pro",
        name: "Apple AirPods Pro (Gen 2)",
        priceMMK: 780000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Earphone",
        specs: "Active Noise Cancellation, Adaptive Audio, Conversation Awareness, up to 6 hours listening time. US Retail Box."
    },
    {
        id: "ear_redmi_buds",
        name: "Redmi Buds 5 Pro",
        priceMMK: 195000,
        condition: "ပါကင်သစ်",
        category: "Accessories",
        subcategory: "Accessory",
        accessoryType: "Earphone",
        specs: "52dB Active Noise Cancellation, Coaxial dual drivers, Hi-Res audio wireless, ultra low latency, dual connection."
    }
];

// In-Memory state
let favourites = JSON.parse(localStorage.getItem("ai_mobile_favs")) || [];
let activePhonesFilter = "All";
let activeAccessoriesFilter = "All";
let homeSearchQuery = "";
let phonesSearchQuery = "";

// Initialize App
document.addEventListener("DOMContentLoaded", () => {
    renderHome();
    renderPhones();
    renderAccessories();
    renderOffers();
    updateFavBadge();
});

// Format Currency
function formatCurrency(val) {
    return new Intl.NumberFormat().format(val) + " MMK";
}

// TAB NAVIGATION SWITCHER
function switchTab(tabId) {
    // Hide all view content blocks
    document.querySelectorAll(".view-content").forEach(view => {
        view.classList.add("hidden");
    });
    
    // Show selected view
    document.getElementById(`view-${tabId}`).classList.remove("hidden");

    // Reset all bottom tab classes
    document.querySelectorAll(".nav-tab").forEach(tab => {
        tab.classList.remove("text-indigo-600");
        tab.classList.add("text-slate-400");
        tab.classList.remove("active");
        
        // Remove bg-indigo-50 from internal nav-pill
        const pill = tab.querySelector(".nav-pill");
        if (pill) {
            pill.classList.remove("bg-indigo-50");
        }
    });

    // Make target bottom tab active
    const activeTab = document.getElementById(`tab-${tabId}`);
    if (activeTab) {
        activeTab.classList.remove("text-slate-400");
        activeTab.classList.add("text-indigo-600");
        activeTab.classList.add("active");
        
        const pill = activeTab.querySelector(".nav-pill");
        if (pill) {
            pill.classList.add("bg-indigo-50");
        }
    }

    // Scroll back to top
    window.scrollTo({ top: 0, behavior: "smooth" });
}

// TOGGLE FAVOURITE persistence (Local Storage)
function toggleFavourite(prodId, event) {
    if (event) event.stopPropagation();
    
    const index = favourites.indexOf(prodId);
    if (index > -1) {
        favourites.splice(index, 1);
    } else {
        favourites.push(prodId);
    }
    
    localStorage.setItem("ai_mobile_favs", JSON.stringify(favourites));
    
    // Refresh UI elements
    updateFavBadge();
    renderHome();
    renderPhones();
    renderAccessories();
    renderOffers();

    // If bottom sheet open, update icon
    updateSheetFavBtn(prodId);
}

function updateFavBadge() {
    const badge = document.getElementById("fav-badge-count");
    if (favourites.length > 0) {
        badge.innerText = favourites.length;
        badge.classList.remove("hidden");
    } else {
        badge.classList.add("hidden");
    }
}

// RENDER HOME SCREEN
function renderHome() {
    const grid = document.getElementById("featured-grid");
    grid.innerHTML = "";

    // Show top 6 items matching search
    const filtered = products.filter(p => {
        if (!homeSearchQuery) return true;
        return p.name.toLowerCase().includes(homeSearchQuery.toLowerCase()) || 
               p.specs.toLowerCase().includes(homeSearchQuery.toLowerCase());
    }).slice(0, 6);

    if (filtered.length === 0) {
        grid.innerHTML = `<div class="col-span-2 text-center text-xs text-gray-500 py-8">No matching stock found.</div>`;
        return;
    }

    filtered.forEach(p => {
        grid.appendChild(createProductCard(p));
    });
}

// Search handling
function handleHomeSearch(val) {
    homeSearchQuery = val;
    const btn = document.getElementById("home-clear-btn");
    if (val) {
        btn.classList.remove("hidden");
    } else {
        btn.classList.add("hidden");
    }
    renderHome();
}

function handlePhonesSearch(val) {
    phonesSearchQuery = val;
    const btn = document.getElementById("phones-clear-btn");
    if (val) {
        btn.classList.remove("hidden");
    } else {
        btn.classList.add("hidden");
    }
    renderPhones();
}

function clearSearch(inputId) {
    document.getElementById(inputId).value = "";
    if (inputId === "home-search") {
        handleHomeSearch("");
    } else {
        handlePhonesSearch("");
    }
}

// RENDER PHONES SCREEN
function renderPhones() {
    const grid = document.getElementById("phones-grid");
    grid.innerHTML = "";

    const filtered = products.filter(p => {
        if (p.subcategory !== "Phone") return false;
        if (activePhonesFilter !== "All" && p.category !== activePhonesFilter) return false;
        if (!phonesSearchQuery) return true;
        return p.name.toLowerCase().includes(phonesSearchQuery.toLowerCase()) || 
               p.specs.toLowerCase().includes(phonesSearchQuery.toLowerCase());
    });

    if (filtered.length === 0) {
        grid.innerHTML = `<div class="col-span-2 text-center text-xs text-gray-500 py-12">No stock matching filters.</div>`;
        return;
    }

    filtered.forEach(p => {
        grid.appendChild(createProductCard(p));
    });
}

function filterPhones(category) {
    activePhonesFilter = category;
    
    // Toggle active filter button style
    document.querySelectorAll(".filter-btn").forEach(btn => {
        btn.classList.remove("bg-indigo-600", "text-white");
        btn.classList.add("bg-white", "text-gray-700");
    });

    const activeBtn = document.getElementById(`btn-filter-${category.replace(' ', '-')}`);
    if (activeBtn) {
        activeBtn.classList.remove("bg-white", "text-gray-700");
        activeBtn.classList.add("bg-indigo-600", "text-white");
    }

    renderPhones();
}

// Category links from Home
function filterPhonesTab(category) {
    switchTab("phones");
    filterPhones(category);
}

// RENDER ACCESSORIES
function renderAccessories() {
    const grid = document.getElementById("accessories-grid");
    grid.innerHTML = "";

    const filtered = products.filter(p => {
        if (p.subcategory !== "Accessory") return false;
        return activeAccessoriesFilter === "All" || p.accessoryType === activeAccessoriesFilter;
    });

    filtered.forEach(p => {
        grid.appendChild(createProductCard(p));
    });
}

function filterAccessories(type) {
    activeAccessoriesFilter = type;
    document.querySelectorAll(".acc-filter-btn").forEach(btn => {
        btn.classList.remove("bg-indigo-600", "text-white");
        btn.classList.add("bg-white", "text-gray-700");
    });

    const activeBtn = document.getElementById(`btn-acc-${type}`);
    if (activeBtn) {
        activeBtn.classList.remove("bg-white", "text-gray-700");
        activeBtn.classList.add("bg-indigo-600", "text-white");
    }

    renderAccessories();
}

// RENDER OFFERS (Promotions & Favourites lists)
function renderOffers() {
    const promoList = document.getElementById("promotions-list");
    const favList = document.getElementById("favourites-list");

    promoList.innerHTML = "";
    favList.innerHTML = "";

    // Discount promotions list
    const promos = products.filter(p => p.hasDiscount);
    promos.forEach(p => {
        const card = document.createElement("div");
        card.className = "bg-white rounded-3xl p-4 border border-slate-100 shadow-sm flex items-center gap-4 cursor-pointer hover:border-indigo-100 transition-all";
        card.onclick = () => openBottomSheet(p.id);

        card.innerHTML = `
            <div class="w-16 h-16 rounded-2xl bg-orange-50 flex items-center justify-center text-orange-500">
                <i class="fa-solid fa-tag text-2xl"></i>
            </div>
            <div class="flex-grow">
                <span class="bg-[#FFE0B2] text-orange-700 text-[9px] font-extrabold px-2.5 py-0.5 rounded-full">${p.promoBadge}</span>
                <h4 class="text-xs font-bold text-slate-800 mt-1.5">${p.name}</h4>
                <div class="flex items-baseline gap-2 mt-0.5">
                    <span class="text-sm font-black text-indigo-600">${formatCurrency(p.discountPriceMMK)}</span>
                    <span class="text-[10px] text-slate-400 line-through font-bold">${formatCurrency(p.priceMMK)}</span>
                </div>
            </div>
            <i class="fa-solid fa-chevron-right text-indigo-500 text-xs"></i>
        `;
        promoList.appendChild(card);
    });

    // Favourites list
    if (favourites.length === 0) {
        favList.innerHTML = `
            <div class="bg-white rounded-3xl p-6 text-center border border-slate-100 shadow-sm text-xs text-slate-400">
                <i class="fa-solid fa-heart text-2xl mb-1 text-slate-200 block"></i>
                No saved stock yet. Tap heart icon to save.
            </div>
        `;
    } else {
        favourites.forEach(id => {
            const p = products.find(prod => prod.id === id);
            if (!p) return;

            const card = document.createElement("div");
            card.className = "bg-white rounded-3xl p-4 border border-slate-100 shadow-sm flex items-center gap-4 cursor-pointer hover:border-indigo-100 transition-all";
            card.onclick = () => openBottomSheet(p.id);

            const displayPrice = p.hasDiscount ? p.discountPriceMMK : p.priceMMK;

            card.innerHTML = `
                <div class="w-12 h-12 rounded-xl bg-purple-50 flex items-center justify-center text-purple-600">
                    <i class="fa-solid fa-mobile-screen text-lg"></i>
                </div>
                <div class="flex-grow">
                    <h4 class="text-xs font-bold text-slate-800">${p.name}</h4>
                    <p class="text-xs font-black text-indigo-600">${formatCurrency(displayPrice)}</p>
                    <span class="inline-block mt-1 bg-[#E1BEE7] text-purple-700 text-[8px] font-bold px-2 py-0.5 rounded-full">${p.condition}</span>
                </div>
                <button onclick="toggleFavourite('${p.id}', event)" class="p-2 text-rose-500 hover:text-slate-300 transition-colors">
                    <i class="fa-solid fa-trash-can text-sm"></i>
                </button>
            `;
            favList.appendChild(card);
        });
    }
}

// COMPONENT BUILDER: CARD RENDERER
function createProductCard(p) {
    const card = document.createElement("div");
    card.className = "bg-white p-3 rounded-3xl border border-slate-100 shadow-sm flex flex-col hover:border-indigo-100 transition-all cursor-pointer";
    card.onclick = () => openBottomSheet(p.id);

    const isFav = favourites.includes(p.id);
    const displayPrice = p.hasDiscount ? p.discountPriceMMK : p.priceMMK;
    
    // Select specific colors for the tags based on conditions
    const tagBg = p.condition === "ပါကင်သစ်" ? "bg-[#E1BEE7] text-purple-700" : "bg-[#FFE0B2] text-orange-700";

    card.innerHTML = `
        <div class="w-full aspect-square bg-slate-50 rounded-2xl mb-3 flex items-center justify-center relative overflow-hidden">
            <!-- Customized vector silhouette overlay representing phones or gadgets -->
            ${p.subcategory === "Phone" ? `
            <div class="w-8 h-14 bg-white/80 rounded-[8px] border border-slate-200 flex items-start justify-center pt-1.5 relative shadow-sm">
                <div class="w-3 h-0.5 bg-slate-300 rounded-full mb-1"></div>
                <div class="absolute bottom-1 w-2 h-2 rounded-full bg-slate-100"></div>
            </div>
            ` : `
            <div class="w-10 h-10 rounded-full bg-indigo-50 flex items-center justify-center text-indigo-500 text-lg shadow-sm">
                <i class="fa-solid ${p.accessoryType === 'Powerbank' ? 'fa-battery-full' : p.accessoryType === 'Charger' ? 'fa-bolt' : 'fa-headphones'}"></i>
            </div>
            `}

            <!-- Heart btn -->
            <button onclick="toggleFavourite('${p.id}', event)" class="absolute top-2 right-2 p-1.5 rounded-full bg-white/80 text-${isFav ? 'rose-500' : 'slate-400'} hover:scale-110 active:scale-95 transition-all shadow-sm">
                <i class="fa-solid fa-heart text-xs"></i>
            </button>

            <!-- Condition badge -->
            <span class="absolute top-2 left-2 text-[8px] font-bold px-2 py-0.5 rounded-full shadow-sm ${tagBg}">${p.condition}</span>
        </div>

        <div class="flex-grow flex flex-col justify-between">
            <div>
                <h4 class="text-[11px] font-bold text-slate-800 leading-tight mb-1 truncate">${p.name}</h4>
                <p class="text-indigo-600 text-xs font-black mt-1">${formatCurrency(displayPrice)}</p>
                ${p.hasDiscount ? `<p class="text-[9px] text-slate-400 line-through font-medium">${formatCurrency(p.priceMMK)}</p>` : ''}
            </div>

            ${p.promoBadge ? `
                <div class="mt-1.5 flex">
                    <span class="bg-orange-50 text-orange-600 text-[8px] font-extrabold px-2 py-0.5 rounded-full">${p.promoBadge}</span>
                </div>
            ` : ''}
        </div>
    `;
    return card;
}

// BOTTOM SHEET ACTIONS
function openBottomSheet(id) {
    const p = products.find(prod => prod.id === id);
    if (!p) return;

    // Fill Modal Data
    document.getElementById("sheet-product-name").innerText = p.name;
    
    const displayPrice = p.hasDiscount ? p.discountPriceMMK : p.priceMMK;
    document.getElementById("sheet-product-price").innerText = formatCurrency(displayPrice);
    
    const oldPriceEl = document.getElementById("sheet-product-old-price");
    if (p.hasDiscount) {
        oldPriceEl.innerText = formatCurrency(p.priceMMK);
        oldPriceEl.classList.remove("hidden");
    } else {
        oldPriceEl.classList.add("hidden");
    }

    document.getElementById("sheet-product-condition").innerText = "Condition: " + p.condition;
    document.getElementById("sheet-product-category").innerText = p.category;
    document.getElementById("sheet-product-specs").innerText = p.specs;

    // Update heart btn inside sheet
    const sheetFavBtn = document.getElementById("sheet-fav-btn");
    sheetFavBtn.onclick = () => toggleFavourite(p.id);
    updateSheetFavBtn(p.id);

    // Dynamic messaging link hooks
    document.getElementById("sheet-viber-link").href = StoreInfo.viberUrl;
    document.getElementById("sheet-messenger-link").href = StoreInfo.messengerUrl;

    // Slide up bottom sheet drawer
    const sheet = document.getElementById("specs-bottom-sheet");
    sheet.classList.remove("hidden");
}

function closeBottomSheet() {
    const sheet = document.getElementById("specs-bottom-sheet");
    sheet.classList.add("hidden");
}

function updateSheetFavBtn(prodId) {
    const btn = document.getElementById("sheet-fav-btn");
    if (!btn) return;

    const isFav = favourites.includes(prodId);
    if (isFav) {
        btn.innerHTML = `<i class="fa-solid fa-heart text-2xl text-rose-500"></i>`;
    } else {
        btn.innerHTML = `<i class="fa-solid fa-heart text-2xl text-gray-300"></i>`;
    }
}
