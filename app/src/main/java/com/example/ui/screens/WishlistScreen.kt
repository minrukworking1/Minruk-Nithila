package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.model.Product
import com.example.ui.AppScreen
import com.example.ui.ShoppingUiState
import com.example.ui.components.EcoBadge
import com.example.ui.components.StarRatingBar
import java.util.Locale

@Composable
fun WishlistScreen(
    state: ShoppingUiState,
    onNavigate: (AppScreen) -> Unit,
    onProductClick: (Product) -> Unit,
    onMoveToCart: (Product) -> Unit,
    onRemoveFromWishlist: (String) -> Unit,
    onShareWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wishlistProducts = state.products.filter { state.wishlistProductIds.contains(it.id) }
    val language = state.selectedLanguage

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("wishlist_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AppStrings.get("wishlist", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${wishlistProducts.size} saved sustainable items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (wishlistProducts.isNotEmpty()) {
                IconButton(onClick = onShareWishlist) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share Wishlist", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (wishlistProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFFFEBEE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Wishlist is Empty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap the heart icon on any product to save eco-friendly favorites for later.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { onNavigate(AppScreen.SEARCH) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Explore Organic Goods", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
            return
        }

        // Responsive Grid
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            val columns = if (maxWidth >= 840.dp) 4 else if (maxWidth >= 600.dp) 3 else 2

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(top = 4.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(wishlistProducts, key = { it.id }) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onProductClick(product) }
                            .testTag("wishlist_item_${product.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            // Top image placeholder & remove icon
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(Color(product.placeholderColor).copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color(product.placeholderColor),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .align(Alignment.Center)
                                )

                                EcoBadge(
                                    text = "Score ${product.ecoScore}",
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                )

                                IconButton(
                                    onClick = { onRemoveFromWishlist(product.id) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(34.dp)
                                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // Details
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    modifier = Modifier.height(38.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                StarRatingBar(rating = product.rating, reviewsCount = product.reviewsCount)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = String.format(Locale.US, "$%.2f", product.price),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { onMoveToCart(product) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("move_to_cart_${product.id}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Move to Cart", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
