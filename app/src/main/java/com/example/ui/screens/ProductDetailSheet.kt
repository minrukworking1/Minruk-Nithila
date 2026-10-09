package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.model.Review
import com.example.ui.components.EcoBadge
import com.example.ui.components.StarRatingBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    reviews: List<Review>,
    isWishlisted: Boolean,
    onDismiss: () -> Unit,
    onAddToCart: (Product, Int) -> Unit,
    onToggleWishlist: (String) -> Unit,
    onOpenWriteReview: (Product) -> Unit,
    onMarkReviewHelpful: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var quantity by remember { mutableIntStateOf(1) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("product_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar with Close & Wishlist
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EcoBadge(text = "Eco-Score: ${product.ecoScore} Certified", containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)

                Row {
                    IconButton(onClick = { onToggleWishlist(product.id) }) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Wishlist",
                            tint = if (isWishlisted) Color(0xFFE53935) else MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hero Visual Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(product.placeholderColor).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color(product.placeholderColor),
                    modifier = Modifier.size(80.dp)
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = "🌱 Offsets ${product.carbonOffsetKg} kg CO₂",
                        color = Color(0xFF1B5E20),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Brand
            Text(
                text = product.brand,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRatingBar(rating = product.rating, reviewsCount = product.reviewsCount, starSize = 18)

                Text(
                    text = String.format(Locale.US, "$%.2f", product.price),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sustainability Certifications Badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (product.isOrganicCertified) {
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                        Text("USDA Organic", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                if (product.isFairTrade) {
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                        Text("Fair Trade Certified", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Text("Zero Plastic", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            Text("About This Product", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            // Highlights
            if (product.highlights.isNotEmpty()) {
                Text("Eco Highlights", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                product.highlights.forEach { highlight ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(highlight, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CUSTOMER REVIEWS SYSTEM
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Customer Reviews (${reviews.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                OutlinedButton(
                    onClick = { onOpenWriteReview(product) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Write Review", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rating Distribution Breakdown Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format(Locale.US, "%.1f", product.rating),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        StarRatingBar(rating = product.rating, showNumber = false, starSize = 14)
                        Text(
                            text = "${product.reviewsCount} ratings",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(Pair("5★", 0.88f), Pair("4★", 0.09f), Pair("3★", 0.02f), Pair("2★", 0.01f), Pair("1★", 0.00f)).forEach { (label, frac) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                LinearProgressIndicator(
                                    progress = { frac },
                                    modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = Color(0xFFFFB300),
                                    trackColor = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Customer Reviews List
            reviews.forEach { rev ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(28.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(rev.author.take(1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(rev.author, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.width(6.dp))
                                if (rev.verifiedPurchase) {
                                    Icon(Icons.Default.Verified, contentDescription = "Verified Purchase", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(rev.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        StarRatingBar(rating = rev.rating.toDouble(), showNumber = false, starSize = 14)
                        Spacer(modifier = Modifier.height(4.dp))

                        if (rev.title.isNotEmpty()) {
                            Text(rev.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        Text(rev.comment, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                rev.tags.forEach { tag ->
                                    Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                                        Text("#$tag", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onMarkReviewHelpful(product.id, rev.id) }
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ThumbUp, contentDescription = "Helpful", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Helpful (${rev.helpfulCount})", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Add-to-Cart Sticky Row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity Stepper
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(38.dp)) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text("$quantity", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp))
                            IconButton(onClick = { quantity++ }, modifier = Modifier.size(38.dp)) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }

                    // Add to Cart Button
                    Button(
                        onClick = {
                            onAddToCart(product, quantity)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(50.dp).testTag("dialog_add_to_cart_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Cart • \$${String.format(Locale.US, "%.2f", product.price * quantity)}", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
