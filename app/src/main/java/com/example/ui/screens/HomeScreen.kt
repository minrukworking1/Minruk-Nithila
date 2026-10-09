package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.model.AppLanguage
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.ui.AppScreen
import com.example.ui.ShoppingUiState
import com.example.ui.components.ProductCard
import com.example.ui.components.SectionHeader

@Composable
fun HomeScreen(
    state: ShoppingUiState,
    onNavigate: (AppScreen) -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = state.selectedLanguage

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // User-friendly Search Bar preview (Tapping navigates to Search)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigate(AppScreen.SEARCH) }
                        .testTag("home_search_bar_trigger"),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = AppStrings.get("search_placeholder", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Hero Promotional Green & White Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigate(AppScreen.SEARCH) }
                    .testTag("hero_banner"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF0F5132), Color(0xFF198754))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.72f)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD1E7DD).copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = "🌱 ZERO-EMISSION HARVEST SALE",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Up to 25% Off Organic Essentials",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Fresh regenerative produce, zero-waste pantry & eco goods delivered in 100% electric vans.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { onNavigate(AppScreen.SEARCH) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF0F5132)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Shop Deals", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }

                    // Decorative Eco Watermark
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }

        // Active Order Live Tracking Banner (if active)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigate(AppScreen.ORDER_TRACKING) }
                    .testTag("active_order_card_home"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Order ${state.activeOrder.orderNumber}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = state.activeOrder.status.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Courier: ${state.activeOrder.courierName} • ${state.activeOrder.estimatedDeliveryTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "View Tracking",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Eco-Impact Counter Strip
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "14.8 kg", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, color = Color(0xFF1B5E20))
                        Text(text = "CO₂ Saved", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(modifier = Modifier.height(28.dp).width(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${state.loyaltyProfile.treesPlanted} Trees", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, color = Color(0xFF1B5E20))
                        Text(text = "Planted to Date", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(modifier = Modifier.height(28.dp).width(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${state.loyaltyProfile.points} pts", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text(text = "GreenPoints", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Category Horizontal Selector
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = AppStrings.get("categories", language),
                actionText = "Explore All",
                onActionClick = { onNavigate(AppScreen.SEARCH) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ProductCategory.values().filter { it != ProductCategory.ALL }) { cat ->
                    val isSelected = state.selectedCategory == cat
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onCategoryClick(cat)
                                onNavigate(AppScreen.SEARCH)
                            }
                            .testTag("category_pill_${cat.name}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Personalized Product Recommendations ("Recommended For You")
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SectionHeader(
                title = AppStrings.get("recommended_for_you", language),
                subtitle = "Curated based on your eco-score preference & viewing history",
                actionText = "More",
                onActionClick = { onNavigate(AppScreen.SEARCH) }
            )
            val recommendedProducts = state.products.filter { it.isBestseller || it.rating >= 4.8 }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommendedProducts) { product ->
                    ProductCard(
                        product = product,
                        isWishlisted = state.wishlistProductIds.contains(product.id),
                        onProductClick = { onProductClick(product) },
                        onAddToCartClick = { onAddToCart(product) },
                        onWishlistClick = { onToggleWishlist(product.id) },
                        modifier = Modifier.width(190.dp)
                    )
                }
            }
        }

        // Flash Deals & Eco Discounts
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SectionHeader(
                title = AppStrings.get("flash_deals", language),
                subtitle = "Limited batch organic items up to 30% off",
                actionText = "See All",
                onActionClick = { onNavigate(AppScreen.SEARCH) }
            )
            val dealProducts = state.products.filter { it.isDeal }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dealProducts) { product ->
                    ProductCard(
                        product = product,
                        isWishlisted = state.wishlistProductIds.contains(product.id),
                        onProductClick = { onProductClick(product) },
                        onAddToCartClick = { onAddToCart(product) },
                        onWishlistClick = { onToggleWishlist(product.id) },
                        modifier = Modifier.width(190.dp)
                    )
                }
            }
        }

        // Recently Viewed Horizontal Strip
        if (state.recentlyViewedProductIds.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SectionHeader(title = "Recently Viewed Products")
                val recentProducts = state.products.filter { state.recentlyViewedProductIds.contains(it.id) }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentProducts) { product ->
                        ProductCard(
                            product = product,
                            isWishlisted = state.wishlistProductIds.contains(product.id),
                            onProductClick = { onProductClick(product) },
                            onAddToCartClick = { onAddToCart(product) },
                            onWishlistClick = { onToggleWishlist(product.id) },
                            modifier = Modifier.width(170.dp)
                        )
                    }
                }
            }
        }

        // Customer Support & Loyalty Quick Access Cards
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Loyalty Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigate(AppScreen.LOYALTY_REWARDS) }
                        .testTag("home_loyalty_shortcut"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "🌱 GreenPoints", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(text = "${state.loyaltyProfile.points} pts available", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Redeem at checkout >", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }

                // Support Chat Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigate(AppScreen.CUSTOMER_SUPPORT) }
                        .testTag("home_support_shortcut"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "💬 24/7 Live Chat", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(text = "Instant order help", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Chat with agent >", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

fun getCategoryIcon(category: ProductCategory): ImageVector {
    return when (category) {
        ProductCategory.PRODUCE -> Icons.Default.LocalFlorist
        ProductCategory.PANTRY -> Icons.Default.ShoppingBag
        ProductCategory.HOME -> Icons.Default.Home
        ProductCategory.FASHION -> Icons.Default.Checkroom
        ProductCategory.TECH -> Icons.Default.SolarPower
        ProductCategory.BEAUTY -> Icons.Default.Spa
        ProductCategory.ALL -> Icons.Default.Eco
    }
}
