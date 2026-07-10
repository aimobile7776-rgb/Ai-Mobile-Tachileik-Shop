package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val dao = database.favouriteDao()

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
                        price = product.priceMMK,
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
