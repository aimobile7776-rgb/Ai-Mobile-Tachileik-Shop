package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.*
import com.example.ui.AuthModalBottomSheet
import com.example.ui.MainViewModel
import com.example.ui.UserProfileSheet
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppMainScreen()
            }
        }
    }
}

// Helper to format currency
fun formatTHB(amount: Long): String {
    val formatter = NumberFormat.getInstance(Locale.US)
    return "${formatter.format(amount)} THB"
}

// External communication handlers
fun callPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val cleanNumber = phoneNumber.replace("[^0-9+]".toRegex(), "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanNumber"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Cannot dial: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

fun openViberChat(context: Context, viberUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viberUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback: open in browser
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.viber.com"))
            context.startActivity(webIntent)
        } catch (webEx: Exception) {
            Toast.makeText(context, "Could not open Viber app.", Toast.LENGTH_SHORT).show()
        }
    }
}

fun openMessengerChat(context: Context, messengerUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(messengerUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback: open in web browser
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(messengerUrl))
            context.startActivity(webIntent)
        } catch (webEx: Exception) {
            Toast.makeText(context, "Could not open Messenger.", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScreen(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(0) }
    
    // Bottom Sheet Specs lookup trigger
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val favouriteIds by viewModel.favouriteIds.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isAuthDialogOpen by viewModel.isAuthDialogOpen.collectAsStateWithLifecycle()
    val isProfileSheetOpen by viewModel.isProfileSheetOpen.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val tabs = listOf(
                    Triple("Home", Icons.Default.Home, "home_tab"),
                    Triple("Phones", Icons.Default.PhoneAndroid, "phones_tab"),
                    Triple("Accessories", Icons.Default.Devices, "accessories_tab"),
                    Triple("Offers", Icons.Default.LocalOffer, "offers_tab"),
                    Triple("Contact", Icons.Default.ContactPhone, "contact_tab")
                )
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        icon = { Icon(tab.second, contentDescription = tab.first) },
                        label = { Text(tab.first, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag(tab.third)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "TabTransition"
            ) { targetTab ->
                when (targetTab) {
                    0 -> HomeScreen(viewModel = viewModel, onNavigateToPhones = { currentTab = 1 })
                    1 -> PhonesScreen(viewModel = viewModel)
                    2 -> AccessoriesScreen(viewModel = viewModel)
                    3 -> OffersScreen(viewModel = viewModel)
                    4 -> ContactScreen(viewModel = viewModel)
                }
            }

            // Specs Detail Modal Bottom Sheet
            selectedProduct?.let { product ->
                val isFav = favouriteIds.contains(product.id)
                ModalBottomSheet(
                    onDismissRequest = { viewModel.selectProduct(null) },
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    ProductDetailSheet(
                        product = product,
                        isFavourite = isFav,
                        onToggleFavourite = { viewModel.toggleFavourite(product) },
                        onInquireViber = { openViberChat(context, StoreInfo.viberUrl) },
                        onInquireMessenger = { openMessengerChat(context, StoreInfo.messengerUrl) }
                    )
                }
            }

            // Authentication Modal Bottom Sheet (Login / Sign Up)
            if (isAuthDialogOpen) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.closeAuthDialog() },
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    AuthModalBottomSheet(
                        onLogin = { identifier, pass, onResult ->
                            viewModel.login(identifier, pass, onResult)
                        },
                        onSignUp = { name, phone, email, pass, onResult ->
                            viewModel.signUp(name, phone, email, pass, onResult)
                        },
                        onDismiss = { viewModel.closeAuthDialog() }
                    )
                }
            }

            // User Profile Sheet
            if (isProfileSheetOpen && currentUser != null) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.closeProfileSheet() },
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    UserProfileSheet(
                        user = currentUser!!,
                        favouriteCount = favouriteIds.size,
                        onLogout = { viewModel.logout() },
                        onDismiss = { viewModel.closeProfileSheet() }
                    )
                }
            }
        }
    }
}

// TAB 0: HOME SCREEN
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToPhones: () -> Unit
) {
    val searchQuery by viewModel.homeSearchQuery.collectAsStateWithLifecycle()
    val favouriteIds by viewModel.favouriteIds.collectAsStateWithLifecycle()

    // Filter featured list based on search and highlight premium
    val filteredFeatured = remember(searchQuery) {
        StockData.products.filter {
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.specs.contains(searchQuery, ignoreCase = true)
        }.take(6)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Space
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Shop Logo, App title and User Account Button
        item {
            val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Circular container with custom generated launcher image
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_store_logo),
                            contentDescription = "AI Mobile Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(
                            text = "AI Mobile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Tachileik Store Stock",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Auth Action / User Profile Chip
                if (currentUser != null) {
                    Surface(
                        onClick = { viewModel.openProfileSheet() },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 2.dp,
                        modifier = Modifier.testTag("home_user_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser!!.fullName.take(1).uppercase(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = currentUser!!.fullName.take(9),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                } else {
                    FilledTonalButton(
                        onClick = { viewModel.openAuthDialog() },
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("home_login_btn")
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Login",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Login",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Functional top Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setHomeSearchQuery(it) },
                placeholder = { Text("Search iPhone, Android, Specs...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setHomeSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_bar"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )
        }

        // Premium Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryColor, SecondaryColor)
                        )
                    )
            ) {
                // Background artistic pattern / glow
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = w * 0.4f,
                        center = androidx.compose.ui.geometry.Offset(w * 0.9f, h * 0.2f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        radius = w * 0.3f,
                        center = androidx.compose.ui.geometry.Offset(w * 0.1f, h * 0.8f)
                    )
                }

                // Banner Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "New Arrivals / Hot Stock",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "iPhone 15 Pro Max",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "ပါကင်သစ် - In Stock Now",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click to inquire price and full specifications",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Horizontal Category Tabs
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Browse Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val categories = listOf(
                        Pair("iPhone New", SoftPurpleBg to SoftPurpleText),
                        Pair("iPhone Second", SoftPurpleBg to SoftPurpleText),
                        Pair("Android New", SoftOrangeBg to SoftOrangeText),
                        Pair("Android Second", SoftOrangeBg to SoftOrangeText)
                    )
                    items(categories) { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(cat.second.first)
                                .clickable { onNavigateToPhones() }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = cat.first,
                                color = cat.second.second,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Featured Products Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Phone Stock",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavigateToPhones) {
                    Text("See All")
                }
            }
        }

        // Featured products list grid alternative for vertical LazyColumn
        if (filteredFeatured.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No stock found matching your search.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            items(filteredFeatured.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductItemCard(
                                product = product,
                                isFavourite = favouriteIds.contains(product.id),
                                onProductClick = { viewModel.selectProduct(product) },
                                onToggleFavourite = { viewModel.toggleFavourite(product) }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// TAB 1: PHONES SCREEN WITH FILTERS
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhonesScreen(viewModel: MainViewModel) {
    val searchQuery by viewModel.phonesSearchQuery.collectAsStateWithLifecycle()
    val favouriteIds by viewModel.favouriteIds.collectAsStateWithLifecycle()
    
    val phoneFilters = listOf("All", "iPhone New", "iPhone Second", "Android New", "Android Second")
    var selectedFilter by remember { mutableStateOf("All") }

    // Fully functional search & filter logic
    val filteredPhones = remember(searchQuery, selectedFilter) {
        StockData.products.filter { product ->
            product.subcategory == "Phone" &&
            (selectedFilter == "All" || product.category == selectedFilter) &&
            (product.name.contains(searchQuery, ignoreCase = true) || 
             product.specs.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "AI Phone Shop",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Current real-time phone stock list",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Product search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setPhonesSearchQuery(it) },
            placeholder = { Text("Search by model name or specs...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setPhonesSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("phones_search_bar"),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal tabs/filters
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(phoneFilters) { filter ->
                val isSelected = selectedFilter == filter
                val containerCol = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                val contentCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                val borderStroke = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                
                Surface(
                    onClick = { selectedFilter = filter },
                    color = containerCol,
                    contentColor = contentCol,
                    shape = RoundedCornerShape(16.dp),
                    border = borderStroke,
                    modifier = Modifier.testTag("filter_$filter")
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Products Stock List
        if (filteredPhones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.PhoneAndroid,
                        contentDescription = "No Products",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No phone stock matches this filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredPhones.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductItemCard(
                                    product = product,
                                    isFavourite = favouriteIds.contains(product.id),
                                    onProductClick = { viewModel.selectProduct(product) },
                                    onToggleFavourite = { viewModel.toggleFavourite(product) }
                                )
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// TAB 2: ACCESSORIES SCREEN
@Composable
fun AccessoriesScreen(viewModel: MainViewModel) {
    val favouriteIds by viewModel.favouriteIds.collectAsStateWithLifecycle()
    val categories = listOf("All", "Powerbank", "Charger", "Earphone")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredAccessories = remember(selectedCategory) {
        StockData.products.filter { product ->
            product.subcategory == "Accessory" &&
            (selectedCategory == "All" || product.accessoryType == selectedCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gadgets & Accessories",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Powerbanks, chargers, and earphones",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Category Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                val containerCol = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                val contentCol = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                val borderStroke = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

                Surface(
                    onClick = { selectedCategory = category },
                    color = containerCol,
                    contentColor = contentCol,
                    shape = RoundedCornerShape(16.dp),
                    border = borderStroke
                ) {
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid/List displays Accessories beautifully
        if (filteredAccessories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No accessories found in stock.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredAccessories.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductItemCard(
                                    product = product,
                                    isFavourite = favouriteIds.contains(product.id),
                                    onProductClick = { viewModel.selectProduct(product) },
                                    onToggleFavourite = { viewModel.toggleFavourite(product) }
                                )
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// TAB 3: PROMOTIONS & FAVOURITES SCREEN (24dp rounded white cards)
@Composable
fun OffersScreen(viewModel: MainViewModel) {
    val favList by viewModel.favourites.collectAsStateWithLifecycle()
    val offers = remember { StockData.products.filter { it.hasDiscount } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Text(
                text = "Promotions & Discounts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Exclusive deals and saved favorites",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Active Promos section with beautiful 24dp rounded white cards
        item {
            Text(
                text = "Hot Discount Offers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (offers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active discount promotions currently.")
                    }
                }
            }
        } else {
            items(offers) { product ->
                PromotionCard(
                    product = product,
                    onProductClick = { viewModel.selectProduct(product) }
                )
            }
        }

        // Saved Favorites section
        item {
            Text(
                text = "Your Saved Stock Items",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (favList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Empty",
                            tint = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No saved stock yet. Tap heart icon to save.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(favList) { fav ->
                FavouriteCard(
                    fav = fav,
                    onRemove = { viewModel.toggleFavourite(
                        Product(
                            id = fav.id,
                            name = fav.name,
                            priceTHB = fav.price,
                            condition = fav.condition,
                            category = fav.category,
                            subcategory = "Phone",
                            specs = fav.specs
                        )
                    ) },
                    onProductClick = {
                        viewModel.selectProduct(
                            Product(
                                id = fav.id,
                                name = fav.name,
                                priceTHB = fav.price,
                                condition = fav.condition,
                                category = fav.category,
                                subcategory = "Phone",
                                specs = fav.specs
                            )
                        )
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// TAB 4: CONTACT SCREEN
@Composable
fun ContactScreen(viewModel: MainViewModel? = null) {
    val context = LocalContext.current
    val currentUser = viewModel?.currentUser?.collectAsStateWithLifecycle()?.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Text(
                text = "Contact & Account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Drop by our physical store or manage your VIP account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Account / VIP Membership Section Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentUser != null) 
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else 
                        MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp, 
                    if (currentUser != null) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser.fullName.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = currentUser.fullName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "⭐ ${currentUser.memberTier} • ${currentUser.phone}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel?.openProfileSheet() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("အချက်အလက် (Profile)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel?.logout() },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ထွက်မည်", fontSize = 12.sp)
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Column {
                                Text(
                                    text = "AI Mobile VIP Member Account",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "၁၀% လျှော့ဈေးနှင့် အာမခံမှတ်တမ်းအတွက် အကောင့်ဖွင့်ပါ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel?.openAuthDialog() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Login / Sign Up (အကောင့်ဝင်/ဖွင့်ရန်)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Store Address Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Place, contentDescription = "Address", tint = MaterialTheme.colorScheme.primary)
                        Text(text = "Tachileik Store", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = StoreInfo.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Business Hours: 9:00 AM - 9:00 PM (Daily)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Store Phone Numbers Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Phone", tint = MaterialTheme.colorScheme.primary)
                        Text(text = "Call Phone Numbers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Tap on any number to make an instant direct call inquiries:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    val phoneList = StoreInfo.phones.split(",")
                    phoneList.forEach { phone ->
                        val trimmed = phone.trim()
                        Button(
                            onClick = { callPhoneNumber(context, trimmed) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = trimmed, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Click-To-Chat Integration Viber & Messenger
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Direct Social Messaging Channels",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Click to chat with our staff instantly on Viber or Facebook Messenger. We respond within minutes!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Viber Button
                    Button(
                        onClick = { openViberChat(context, StoreInfo.viberUrl) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7303C0)), // Viber Purple Brand color
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "Viber", modifier = Modifier.size(20.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Inquire on Viber Chat", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    // Messenger Button
                    Button(
                        onClick = { openMessengerChat(context, StoreInfo.messengerUrl) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0084FF)), // Messenger Blue Brand color
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Forum, contentDescription = "Messenger", modifier = Modifier.size(20.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Chat on Messenger", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// COMPOSABLE: REUSABLE PRODUCT ITEM CARD
@Composable
fun ProductItemCard(
    product: Product,
    isFavourite: Boolean,
    onProductClick: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            // Nested Aspect-Ratio/Container-like Header Thumbnail area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                // Customized artistic Device / Gadget Silhouette
                if (product.subcategory == "Phone") {
                    Canvas(modifier = Modifier.size(width = 44.dp, height = 76.dp)) {
                        val w = size.width
                        val h = size.height
                        // Device frame
                        drawRoundRect(
                            color = Color.White,
                            topLeft = androidx.compose.ui.geometry.Offset.Zero,
                            size = size,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
                        )
                        // Inner frame detail
                        drawRoundRect(
                            color = Color(0xFFE2E8F0),
                            topLeft = androidx.compose.ui.geometry.Offset(2f, 2f),
                            size = androidx.compose.ui.geometry.Size(w - 4f, h - 4f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                        )
                        // Dynamic island notch
                        drawRoundRect(
                            color = Color(0xFF0F172A),
                            topLeft = androidx.compose.ui.geometry.Offset(w * 0.3f, 4f),
                            size = androidx.compose.ui.geometry.Size(w * 0.4f, 4f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
                        )
                    }
                } else {
                    // Accessories icon indicator
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (product.accessoryType) {
                                "Powerbank" -> Icons.Default.BatteryFull
                                "Charger" -> Icons.Default.Bolt
                                else -> Icons.Default.Headphones
                            },
                            contentDescription = product.accessoryType,
                            tint = PrimaryColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Heart Favourite circular glass container
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.85f))
                        .clickable { onToggleFavourite() }
                        .testTag("fav_btn_${product.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = if (isFavourite) Color(0xFFEF4444) else Color(0xFF94A3B8),
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Condition Pill Tag on Top Left
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (product.condition == "ပါကင်သစ်") SoftPurpleBg else SoftOrangeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = product.condition,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (product.condition == "ပါကင်သစ်") SoftPurpleText else SoftOrangeText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name and Details block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF0F172A)
                )
                
                Spacer(modifier = Modifier.height(2.dp))

                val priceToShow = if (product.hasDiscount) product.discountPriceTHB else product.priceTHB
                Text(
                    text = formatTHB(priceToShow),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = PrimaryColor
                )

                if (product.hasDiscount) {
                    Text(
                        text = formatTHB(product.priceTHB),
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8),
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }

                if (product.promoBadge.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.promoBadge,
                            color = Color(0xFFD97706),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// COMPOSABLE: DISCOUNTS CARD (24dp rounded white cards)
@Composable
fun PromotionCard(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("promo_card_${product.id}"),
        shape = RoundedCornerShape(24.dp), // Beautiful 24dp rounded cards
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Promo Visual tag
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SoftOrangeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalOffer,
                    contentDescription = "Promo",
                    tint = SoftOrangeText,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text info
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftOrangeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = product.promoBadge,
                        color = SoftOrangeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatTHB(product.discountPriceTHB),
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = formatTHB(product.priceTHB),
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            // Arrow CTA
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "View Detail",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// COMPOSABLE: SAVED FAVORITE CARD
@Composable
fun FavouriteCard(
    fav: FavouriteProduct,
    onRemove: () -> Unit,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("fav_card_${fav.id}"),
        shape = RoundedCornerShape(24.dp), // 24dp rounded white card
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPurpleBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PhoneAndroid,
                    contentDescription = "Phone",
                    tint = SoftPurpleText,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fav.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = formatTHB(fav.price),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SoftPurpleBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = fav.condition,
                        color = SoftPurpleText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Delete / Remove Favorite button
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// COMPOSABLE: BOTTOM SHEET DETAIL CONTENT
@Composable
fun ProductDetailSheet(
    product: Product,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit,
    onInquireViber: () -> Unit,
    onInquireMessenger: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val currentPrice = if (product.hasDiscount) product.discountPriceTHB else product.priceTHB
                    Text(
                        text = formatTHB(currentPrice),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (product.hasDiscount) {
                        Text(
                            text = formatTHB(product.priceTHB),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            IconButton(onClick = onToggleFavourite) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Fav",
                    tint = if (isFavourite) Color.Red else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Tags Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoftPurpleBg)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Condition: ${product.condition}",
                    color = SoftPurpleText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoftOrangeBg)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = product.category,
                    color = SoftOrangeText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Specifications Section
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Product Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Text(
                    text = product.specs,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Inquire CTA buttons Viber & Messenger
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Have questions? Contact us directly:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onInquireViber,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7303C0)), // Viber brand purple
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Chat, contentDescription = "Viber")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Inquire via Viber", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Button(
                onClick = onInquireMessenger,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0084FF)), // Messenger brand blue
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Forum, contentDescription = "Messenger")
                Spacer(modifier = Modifier.width(12.dp))
                Text("Inquire via Messenger", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
