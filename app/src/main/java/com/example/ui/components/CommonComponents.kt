package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import java.util.Locale

@Composable
fun EcoBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = contentColor
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Int = 14,
    showNumber: Boolean = true,
    reviewsCount: Int? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val roundedRating = rating.toInt()
        for (i in 1..maxStars) {
            val isFilled = i <= roundedRating
            Icon(
                imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                modifier = Modifier.size(starSize.dp),
                tint = if (isFilled) Color(0xFFFFB300) else MaterialTheme.colorScheme.outline
            )
        }
        if (showNumber) {
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = String.format(Locale.US, "%.1f", rating),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (reviewsCount != null) {
                Text(
                    text = " ($reviewsCount)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InteractiveStarRatingPicker(
    currentRating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (star in 1..5) {
            IconButton(
                onClick = { onRatingSelected(star) },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("star_rate_$star")
            ) {
                Icon(
                    imageVector = if (star <= currentRating) Icons.Default.Star else Icons.Outlined.StarBorder,
                    contentDescription = "$star stars",
                    tint = if (star <= currentRating) Color(0xFFFFB300) else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onActionClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onWishlistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartColor by animateColorAsState(
        targetValue = if (isWishlisted) Color(0xFFE53935) else MaterialTheme.colorScheme.outline,
        label = "heartColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Image / Visual Area with Eco Badge and Wishlist Heart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(product.placeholderColor).copy(alpha = 0.15f))
            ) {
                // Centered icon illustration
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color(product.placeholderColor),
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.Center)
                )

                // Top Left: Eco Score Badge
                EcoBadge(
                    text = "Score ${product.ecoScore}",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

                // Top Right: Wishlist Toggle Button
                IconButton(
                    onClick = onWishlistClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                        .testTag("wishlist_toggle_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Wishlist",
                        tint = heartColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Deal Tag if active
                if (product.isDeal && product.originalPrice != null) {
                    val discountPercent = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE53935)
                    ) {
                        Text(
                            text = "-$discountPercent%",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(38.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

                StarRatingBar(rating = product.rating, reviewsCount = product.reviewsCount)

                Spacer(modifier = Modifier.height(8.dp))

                // Price & Add to Cart Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format(Locale.US, "$%.2f", product.price),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (product.originalPrice != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format(Locale.US, "$%.2f", product.originalPrice),
                                    style = MaterialTheme.typography.bodySmall,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "🌱 -${product.carbonOffsetKg}kg CO₂",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        onClick = onAddToCartClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("add_to_cart_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
