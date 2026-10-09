package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.ui.ShoppingUiState
import com.example.ui.SortOption
import com.example.ui.components.ProductCard

@Composable
fun SearchScreen(
    state: ShoppingUiState,
    onQueryChange: (String) -> Unit,
    onExecuteSearch: (String) -> Unit,
    onCategorySelect: (ProductCategory) -> Unit,
    onSortSelect: (SortOption) -> Unit,
    onToggleOrganic: () -> Unit,
    onToggleDeals: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleWishlist: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    // Filter products
    val filteredProducts = state.products.filter { product ->
        val matchesCategory = state.selectedCategory == ProductCategory.ALL || product.category == state.selectedCategory
        val matchesQuery = state.searchQuery.isBlank() ||
                product.name.contains(state.searchQuery, ignoreCase = true) ||
                product.description.contains(state.searchQuery, ignoreCase = true) ||
                product.brand.contains(state.searchQuery, ignoreCase = true)
        val matchesOrganic = !state.filterOrganicOnly || product.isOrganicCertified
        val matchesDeals = !state.filterDealsOnly || product.isDeal

        matchesCategory && matchesQuery && matchesOrganic && matchesDeals
    }.let { list ->
        when (state.sortOption) {
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.ECO_SCORE -> list.sortedBy { it.ecoScore }
            SortOption.POPULARITY -> list.sortedByDescending { it.reviewsCount }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("search_screen")
    ) {
        // User-Friendly Search Bar with Voice, Clear, and QR Barcode Scan
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_text_input"),
                placeholder = {
                    Text(
                        text = AppStrings.get("search_placeholder", state.selectedLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = { onExecuteSearch("Organic Avocados") }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice search simulation",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { onExecuteSearch("Bamboo Utensils") }) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scan barcode",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )

            // Search History / Quick Suggestion Chips
            if (state.searchHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(state.searchHistory) { tag ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onExecuteSearch(tag) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔍 $tag",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ProductCategory.values()) { cat ->
                val isSelected = state.selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(cat) },
                    label = { Text(cat.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_chip_${cat.name}")
                )
            }
        }

        // Toggles & Sort Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.filterOrganicOnly,
                    onClick = onToggleOrganic,
                    label = { Text("🌿 Organic") }
                )
                FilterChip(
                    selected = state.filterDealsOnly,
                    onClick = onToggleDeals,
                    label = { Text("🔥 Deals") }
                )
            }

            // Sort Menu Button
            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showSortMenu = true }
                        .testTag("sort_menu_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = state.sortOption.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                onSortSelect(option)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Results Header
        Text(
            text = "${filteredProducts.size} Sustainable Products Found",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Responsive Product Grid (2 columns on mobile, 3-4 columns on tablet)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            val columns = if (maxWidth >= 840.dp) 4 else if (maxWidth >= 600.dp) 3 else 2

            if (filteredProducts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🍃", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Try adjusting your search terms or filters.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 90.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredProducts, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            isWishlisted = state.wishlistProductIds.contains(product.id),
                            onProductClick = { onProductClick(product) },
                            onAddToCartClick = { onAddToCart(product) },
                            onWishlistClick = { onToggleWishlist(product.id) }
                        )
                    }
                }
            }
        }
    }
}
