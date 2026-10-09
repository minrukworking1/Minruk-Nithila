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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.ui.AppScreen
import com.example.ui.ShoppingUiState
import java.util.Locale

@Composable
fun CartScreen(
    state: ShoppingUiState,
    onNavigate: (AppScreen) -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onClearCart: () -> Unit,
    onApplyPromo: (String) -> Unit,
    onToggleRedeemPoints: () -> Unit,
    onOpenCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promoInput by remember { mutableStateOf("") }
    val language = state.selectedLanguage

    val subtotal = state.cartItems.sumOf { it.product.price * it.quantity }
    val promoDiscount = subtotal * state.promoDiscountPercent
    val maxPointsAllowed = minOf(state.loyaltyProfile.points, (subtotal * 100).toInt())
    val pointsDiscount = if (state.redeemPointsChecked) maxPointsAllowed / 100.0 else 0.0
    val total = maxOf(0.0, subtotal - promoDiscount - pointsDiscount)
    val totalCarbonKg = state.cartItems.sumOf { it.product.carbonOffsetKg * it.quantity }

    if (state.cartItems.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
                .testTag("empty_cart_view"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your GreenCart is empty",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Explore our certified organic produce and zero-waste everyday goods.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onNavigate(AppScreen.SEARCH) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Explore Catalog", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("cart_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${AppStrings.get("cart", language)} (${state.cartItems.sumOf { it.quantity }})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Clear All",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFD32F2F),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onClearCart() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Cart Items List
        items(state.cartItems, key = { it.product.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("cart_item_${item.product.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Placeholder visual
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(item.product.placeholderColor).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = Color(item.product.placeholderColor),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format(Locale.US, "$%.2f", item.product.price),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Score ${item.product.ecoScore}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quantity Stepper
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onQuantityChange(item.product.id, -1) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(
                                        onClick = { onQuantityChange(item.product.id, 1) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            IconButton(
                                onClick = { onRemoveItem(item.product.id) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Loyalty Rewards Points Redemption
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.redeemPointsChecked,
                        onCheckedChange = { onToggleRedeemPoints() },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("redeem_points_checkbox")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Redeem GreenPoints Rewards",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Use $maxPointsAllowed pts for \$${String.format(Locale.US, "%.2f", maxPointsAllowed / 100.0)} discount (Balance: ${state.loyaltyProfile.points} pts)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Promo Code Input
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = promoInput,
                    onValueChange = { promoInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Promo Code (e.g. ECO20)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                Button(
                    onClick = {
                        if (promoInput.isNotBlank()) onApplyPromo(promoInput)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.height(54.dp)
                ) {
                    Text("Apply", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Eco-Impact Box
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE8F5E9)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌱", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Climate Positive Order",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "This purchase saves ~${String.format(Locale.US, "%.1f", totalCarbonKg)} kg CO₂ and uses 100% compostable packaging.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Order Summary Breakdown
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(String.format(Locale.US, "$%.2f", subtotal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }

                    if (promoDiscount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Promo Discount (${state.appliedPromoCode})", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E7D32))
                            Text(String.format(Locale.US, "-$%.2f", promoDiscount), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }

                    if (pointsDiscount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GreenPoints Redeemed", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2E7D32))
                            Text(String.format(Locale.US, "-$%.2f", pointsDiscount), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Eco Electric Shipping", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("FREE ($0.00)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        Text(
                            String.format(Locale.US, "$%.2f", total),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Checkout Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onOpenCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("proceed_to_checkout_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Proceed to Secure Checkout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
