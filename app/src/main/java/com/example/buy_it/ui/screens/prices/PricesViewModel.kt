package com.example.buy_it.ui.screens.prices

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buy_it.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PricesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(PricesState())
    val uiState: StateFlow<PricesState> = _uiState

    private val productId: String? = savedStateHandle["productId"]

    init {
        loadPrices()
    }

    private fun loadPrices() {
        val id = productId ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = productRepository.getProductPrices(id)
            
            if (result.isSuccess) {
                val items = result.getOrDefault(emptyList())
                _uiState.update { it.copy(
                    pricedItems = items,
                    isLoading = false
                ) }
            } else {
                _uiState.update { it.copy(
                    isLoading = false,
                    pricedItems = emptyList()
                ) }
            }
        }
    }
}
