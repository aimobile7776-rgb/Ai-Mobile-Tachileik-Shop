package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val dao = database.favouriteDao()
    private val userDao = database.userDao()
    private val prefs = application.getSharedPreferences("ai_mobile_auth_pref", Context.MODE_PRIVATE)

    // User session & auth state
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isAuthDialogOpen = MutableStateFlow(false)
    val isAuthDialogOpen = _isAuthDialogOpen.asStateFlow()

    private val _isProfileSheetOpen = MutableStateFlow(false)
    val isProfileSheetOpen = _isProfileSheetOpen.asStateFlow()

    init {
        // Restore session or seed default demo account
        viewModelScope.launch {
            val savedUserId = prefs.getLong("logged_in_user_id", -1L)
            if (savedUserId != -1L) {
                _currentUser.value = userDao.getUserById(savedUserId)
            }
            // Seed a demo user if empty
            val existingDemo = userDao.getUserByPhone("0977767776")
            if (existingDemo == null) {
                userDao.insertUser(
                    User(
                        fullName = "ကိုစိုင်းခမ်း (Sai Kham)",
                        phone = "0977767776",
                        email = "aimobile7776@gmail.com",
                        password = "password123",
                        memberTier = "VIP Member"
                    )
                )
            }
        }
    }

    // Search queries
    private val _homeSearchQuery = MutableStateFlow("")
    val homeSearchQuery = _homeSearchQuery.asStateFlow()

    private val _phonesSearchQuery = MutableStateFlow("")
    val phonesSearchQuery = _phonesSearchQuery.asStateFlow()

    // Selected products for details sheet
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    // Reactive flow of Favourites list
    val favourites: StateFlow<List<FavouriteProduct>> = dao.getAllFavourites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Set of favourited IDs for synchronous UI lookups
    val favouriteIds: StateFlow<Set<String>> = dao.getAllFavourites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun openAuthDialog() {
        _isAuthDialogOpen.value = true
    }

    fun closeAuthDialog() {
        _isAuthDialogOpen.value = false
    }

    fun openProfileSheet() {
        _isProfileSheetOpen.value = true
    }

    fun closeProfileSheet() {
        _isProfileSheetOpen.value = false
    }

    fun login(identifier: String, pass: String, onResult: (Boolean, String) -> Unit) {
        val cleanIdentifier = identifier.trim()
        val cleanPass = pass.trim()

        if (cleanIdentifier.isEmpty()) {
            onResult(false, "ကျေးဇူးပြု၍ ဖုန်းနံပါတ် သို့မဟုတ် အီးမေးလ် ထည့်ပါ")
            return
        }
        if (cleanPass.isEmpty()) {
            onResult(false, "ကျေးဇူးပြု၍ စကားဝှက် (Password) ထည့်ပါ")
            return
        }

        viewModelScope.launch {
            val user = userDao.getUserByIdentifier(cleanIdentifier)
            if (user == null) {
                onResult(false, "အကောင့်ရှာမတွေ့ပါ။ စာရင်းသွင်း (Sign Up) ပြုလုပ်ပေးပါ")
            } else if (user.password != cleanPass) {
                onResult(false, "စကားဝှက် မှားယွင်းနေပါသည်။ ပြန်လည်စစ်ဆေးပါ")
            } else {
                _currentUser.value = user
                prefs.edit().putLong("logged_in_user_id", user.id).apply()
                _isAuthDialogOpen.value = false
                onResult(true, "ကြိုဆိုပါသည် ${user.fullName}")
            }
        }
    }

    fun signUp(name: String, phone: String, email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        val cleanName = name.trim()
        val cleanPhone = phone.trim()
        val cleanEmail = email.trim()
        val cleanPass = pass.trim()

        if (cleanName.isEmpty()) {
            onResult(false, "ကျေးဇူးပြု၍ အမည်ထည့်သွင်းပါ")
            return
        }
        if (cleanPhone.length < 5) {
            onResult(false, "ကျေးဇူးပြု၍ မှန်ကန်သော ဖုန်းနံပါတ် ထည့်သွင်းပါ")
            return
        }
        if (cleanPass.length < 4) {
            onResult(false, "စကားဝှက်သည် အနည်းဆုံး ၄ လုံး ရှိရပါမည်")
            return
        }

        viewModelScope.launch {
            val existing = userDao.getUserByPhone(cleanPhone)
            if (existing != null) {
                onResult(false, "ဤဖုန်းနံပါတ်ဖြင့် အကောင့်ဖွင့်ပြီးသား ဖြစ်ပါသည်")
                return@launch
            }

            val newUser = User(
                fullName = cleanName,
                phone = cleanPhone,
                email = cleanEmail,
                password = cleanPass,
                memberTier = "VIP Member"
            )
            val newId = userDao.insertUser(newUser)
            val createdUser = newUser.copy(id = newId)
            _currentUser.value = createdUser
            prefs.edit().putLong("logged_in_user_id", newId).apply()
            _isAuthDialogOpen.value = false
            onResult(true, "အကောင့်ဖွင့်ခြင်း အောင်မြင်ပါသည်!")
        }
    }

    fun logout() {
        _currentUser.value = null
        prefs.edit().remove("logged_in_user_id").apply()
        _isProfileSheetOpen.value = false
    }

    fun setHomeSearchQuery(query: String) {
        _homeSearchQuery.value = query
    }

    fun setPhonesSearchQuery(query: String) {
        _phonesSearchQuery.value = query
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            if (favouriteIds.value.contains(product.id)) {
                dao.deleteFavouriteById(product.id)
            } else {
                dao.insertFavourite(
                    FavouriteProduct(
                        id = product.id,
                        name = product.name,
                        price = product.priceTHB,
                        condition = product.condition,
                        category = product.category,
                        specs = product.specs
                    )
                )
            }
        }
    }

    fun toggleFavourite(fav: FavouriteProduct) {
        viewModelScope.launch {
            dao.deleteFavouriteById(fav.id)
        }
    }
}
